<?php

/**
 * ECSHOP 管理中心团购商品管理
 * ============================================================================
 * * 版权所有 2005-2012 上海商派网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.ecshop.com；
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和
 * 使用；不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: liubo $
 * $Id: group_buy.php 17217 2011-01-19 06:29:08Z liubo $
 */

define('IN_ECS', true);
require(dirname(__FILE__) . '/includes/init.php');
require_once(ROOT_PATH . 'includes/lib_goods.php');
require_once(ROOT_PATH . 'includes/lib_order.php');
include_once(ROOT_PATH . '/includes/cls_image.php');
require_once(ROOT_PATH . "includes/fckeditor/fckeditor.php");

/* 检查权限 */
admin_priv('group_by');

/* act操作项的初始化 */
if (empty($_REQUEST['act']))
{
    $_REQUEST['act'] = 'list';
}
else
{
    $_REQUEST['act'] = trim($_REQUEST['act']);
}

/*------------------------------------------------------ */
//-- 团购活动列表
/*------------------------------------------------------ */

if ($_REQUEST['act'] == 'list')
{
    /* 模板赋值 */
    $smarty->assign('full_page',    1);
    $smarty->assign('ur_here',      $_LANG['group_buy_list']);
    $smarty->assign('action_link',  array('href' => 'group_buy.php?act=add', 'text' => $_LANG['add_group_buy']));

    $list = group_buy_list();

    $smarty->assign('group_buy_list',   $list['item']);
    $smarty->assign('filter',           $list['filter']);
    $smarty->assign('record_count',     $list['record_count']);
    $smarty->assign('page_count',       $list['page_count']);

    $sort_flag  = sort_flag($list['filter']);
    $smarty->assign($sort_flag['tag'], $sort_flag['img']);

    /* 显示商品列表页面 */
    assign_query_info();
    $smarty->display('group_buy_list.htm');
}

elseif ($_REQUEST['act'] == 'query')
{
    $list = group_buy_list();

    $smarty->assign('group_buy_list', $list['item']);
    $smarty->assign('filter',         $list['filter']);
    $smarty->assign('record_count',   $list['record_count']);
    $smarty->assign('page_count',     $list['page_count']);

    $sort_flag  = sort_flag($list['filter']);
    $smarty->assign($sort_flag['tag'], $sort_flag['img']);

    make_json_result($smarty->fetch('group_buy_list.htm'), '',
        array('filter' => $list['filter'], 'page_count' => $list['page_count']));
}

/*------------------------------------------------------ */
//-- 添加/编辑团购活动
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] == 'add' || $_REQUEST['act'] == 'edit')
{
    /* 初始化/取得团购活动信息 */
    if ($_REQUEST['act'] == 'add')
    {
        $group_buy = array(
            'act_id'  => 0,
            'start_time'    => date('Y-m-d', time() + 86400),
            'end_time'      => date('Y-m-d', time() + 4 * 86400),
            'price_ladder'  => array(array('amount' => 0, 'price' => 0))
        );
    }
    else
    {
        $group_buy_id = intval($_REQUEST['id']);
        if ($group_buy_id <= 0)
        {
            die('invalid param');
        }
        $group_buy = group_buy_info($group_buy_id);
    }

	$cats = get_all_cats();
	$catids = explode(',',$group_buy['act_cats']);
	foreach ($catids as $catid)
	{
	 foreach ($cats as $key => $cat)
	 {
		if ($cat['cat_id']==$catid){
			$cats[$key]['hasCheck'] = 1;
			break;
		}
	 }
	}

	/* 创建 html editor */
    create_html_editor('act_desc', $group_buy['act_desc']);

    $smarty->assign('group_buy', $group_buy);

    $smarty->assign('group_cats', $cats);

    /* 模板赋值 */
    $smarty->assign('ur_here', $_LANG['add_group_buy']);
    $smarty->assign('action_link', list_link($_REQUEST['act'] == 'add'));
    $smarty->assign('cat_list', cat_list());
    $smarty->assign('brand_list', get_brand_list());

    /* 显示模板 */
    assign_query_info();
    $smarty->display('group_buy_info.htm');
}

/*------------------------------------------------------ */
//-- 添加/编辑团购活动的提交
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] =='insert_update')
{
    /* 取得团购活动id */
    $group_buy_id = intval($_POST['act_id']);
	$cats = $_POST['group_cats'];
	$catids = '';
	foreach ($cats as $catid){
		$catids = $catids.$catid.',';
	}

    if (isset($_POST['finish']) || isset($_POST['succeed']) || isset($_POST['fail']) || isset($_POST['mail']))
    {
        if ($group_buy_id <= 0)
        {
            sys_msg($_LANG['error_group_buy'], 1);
        }
        $group_buy = group_buy_info($group_buy_id);
        if (empty($group_buy))
        {
            sys_msg($_LANG['error_group_buy'], 1);
        }
    }

    if (isset($_POST['finish']))
    {
        /* 判断订单状态 */
        if ($group_buy['status'] != GBS_UNDER_WAY)
        {
            sys_msg($_LANG['error_status'], 1);
        }

        /* 结束团购活动，修改结束时间为当前时间 */
        $sql = "UPDATE " . $ecs->table('goods_activity') .
                " SET end_time = '" . gmtime() . "' " .
                "WHERE act_id = '$group_buy_id' LIMIT 1";
        $db->query($sql);

        /* 清除缓存 */
        clear_cache_files();

        /* 提示信息 */
        $links = array(
            array('href' => 'group_buy.php?act=list', 'text' => $_LANG['back_list'])
        );
        sys_msg($_LANG['edit_success'], 0, $links);
    }
    else
    {
        /* 保存团购信息 */
        $goods_id = 1;

        $goods_name = $db->getOne("SELECT goods_name FROM " . $ecs->table('goods') . " WHERE goods_id = '$goods_id'");

        $act_name = empty($_POST['act_name']) ? $goods_name : sub_str($_POST['act_name'], 0, 255, false);

        $restrict_amount = 1;

        /* 检查开始时间和结束时间是否合理 */
        $start_time = local_strtotime($_POST['start_time']);
        $end_time = local_strtotime($_POST['end_time']);
        if ($start_time >= $end_time)
        {
            sys_msg($_LANG['invalid_time']);
        }

        $group_buy = array(
            'act_name'   => $act_name,
            'act_desc'   => $_POST['act_desc'],
            'act_type'   => GAT_GROUP_BUY,
            'goods_id'   => $goods_id,
            'goods_name' => $goods_name,
            'start_time'    => $start_time,
            'end_time'      => $end_time,
            'ext_info'   => serialize(array(
                    'price_ladder'      => $price_ladder,
                    'restrict_amount'   => $restrict_amount,
                    'gift_integral'     => $gift_integral,
                    'deposit'           => $deposit
                    )),
			'act_cats'      => $catids,
        );

        /* 清除缓存 */
        clear_cache_files();

        /* 保存数据 */
        if ($group_buy_id > 0)
        {
            /* update */
            $db->autoExecute($ecs->table('goods_activity'), $group_buy, 'UPDATE', "act_id = '$group_buy_id'");

            /* log */
            admin_log(addslashes($goods_name) . '[' . $group_buy_id . ']', 'edit', 'group_buy');

            /* todo 更新活动表 */

            /* 提示信息 */
            $links = array(
                array('href' => 'group_buy.php?act=list&' . list_link_postfix(), 'text' => $_LANG['back_list'])
            );
            sys_msg($_LANG['edit_success'], 0, $links);
        }
        else
        {
            /* insert */
            $db->autoExecute($ecs->table('goods_activity'), $group_buy, 'INSERT');

            /* log */
            admin_log(addslashes($goods_name), 'add', 'group_buy');

            /* 提示信息 */
            $links = array(
                array('href' => 'group_buy.php?act=add', 'text' => $_LANG['continue_add']),
                array('href' => 'group_buy.php?act=list', 'text' => $_LANG['back_list'])
            );
            sys_msg($_LANG['add_success'], 0, $links);
        }
    }
}

/*------------------------------------------------------ */
//-- 批量删除团购活动
/*------------------------------------------------------ */
elseif ($_REQUEST['act'] == 'batch_drop')
{
    if (isset($_POST['checkboxes']))
    {
        $del_count = 0; //初始化删除数量
        foreach ($_POST['checkboxes'] AS $key => $id)
        {
            /* 取得团购活动信息 */
            $group_buy = group_buy_info($id);

            /* 如果团购活动已经有订单，不能删除 */
            if ($group_buy['valid_order'] <= 0)
            {
                /* 删除团购活动 */
                $sql = "DELETE FROM " . $GLOBALS['ecs']->table('goods_activity') .
                        " WHERE act_id = '$id' LIMIT 1";
                $GLOBALS['db']->query($sql, 'SILENT');

                admin_log(addslashes($group_buy['goods_name']) . '[' . $id . ']', 'remove', 'group_buy');
                $del_count++;
            }
        }

        /* 如果删除了团购活动，清除缓存 */
        if ($del_count > 0)
        {
            clear_cache_files();
        }

        $links[] = array('text' => $_LANG['back_list'], 'href'=>'group_buy.php?act=list');
        sys_msg(sprintf($_LANG['batch_drop_success'], $del_count), 0, $links);
    }
    else
    {
        $links[] = array('text' => $_LANG['back_list'], 'href'=>'group_buy.php?act=list');
        sys_msg($_LANG['no_select_group_buy'], 0, $links);
    }
}

/*------------------------------------------------------ */
//-- 搜索商品
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] == 'search_goods')
{
    check_authz_json('group_by');

    include_once(ROOT_PATH . 'includes/cls_json.php');

    $json   = new JSON;
    $filter = $json->decode($_GET['JSON']);
    $arr    = get_goods_list($filter);

    make_json_result($arr);
}

/*------------------------------------------------------ */
//-- 编辑保证金
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] == 'edit_deposit')
{
    check_authz_json('group_by');

    $id = intval($_POST['id']);
    $val = floatval($_POST['val']);

    $sql = "SELECT ext_info FROM " . $ecs->table('goods_activity') .
            " WHERE act_id = '$id' AND act_type = '" . GAT_GROUP_BUY . "'";
    $ext_info = unserialize($db->getOne($sql));
    $ext_info['deposit'] = $val;

    $sql = "UPDATE " . $ecs->table('goods_activity') .
            " SET ext_info = '" . serialize($ext_info) . "'" .
            " WHERE act_id = '$id'";
    $db->query($sql);

    clear_cache_files();

    make_json_result(number_format($val, 2));
}

/*------------------------------------------------------ */
//-- 编辑保证金
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] == 'edit_restrict_amount')
{
    check_authz_json('group_by');

    $id = intval($_POST['id']);
    $val = intval($_POST['val']);

    $sql = "SELECT ext_info FROM " . $ecs->table('goods_activity') .
            " WHERE act_id = '$id' AND act_type = '" . GAT_GROUP_BUY . "'";
    $ext_info = unserialize($db->getOne($sql));
    $ext_info['restrict_amount'] = $val;

    $sql = "UPDATE " . $ecs->table('goods_activity') .
            " SET ext_info = '" . serialize($ext_info) . "'" .
            " WHERE act_id = '$id'";
    $db->query($sql);

    clear_cache_files();

    make_json_result($val);
}

/*------------------------------------------------------ */
//-- 删除团购活动
/*------------------------------------------------------ */

elseif ($_REQUEST['act'] == 'remove')
{
    check_authz_json('group_by');

    $id = intval($_GET['id']);

    /* 取得团购活动信息 */
    $group_buy = group_buy_info($id);

    /* 如果团购活动已经有订单，不能删除 */
    if ($group_buy['valid_order'] > 0)
    {
        make_json_error($_LANG['error_exist_order']);
    }

    /* 删除团购活动 */
    $sql = "DELETE FROM " . $ecs->table('goods_activity') . " WHERE act_id = '$id' LIMIT 1";
    $db->query($sql);

    admin_log(addslashes($group_buy['goods_name']) . '[' . $id . ']', 'remove', 'group_buy');

    clear_cache_files();

    $url = 'group_buy.php?act=query&' . str_replace('act=remove', '', $_SERVER['QUERY_STRING']);

    ecs_header("Location: $url\n");
    exit;
}

/*
 * 取得团购活动列表
 * @return   array
 */
function group_buy_list()
{
    $result = get_filter();
    if ($result === false)
    {
        /* 过滤条件 */
        $filter['keyword']      = empty($_REQUEST['keyword']) ? '' : trim($_REQUEST['keyword']);
        if (isset($_REQUEST['is_ajax']) && $_REQUEST['is_ajax'] == 1)
        {
            $filter['keyword'] = json_str_iconv($filter['keyword']);
        }
        $filter['sort_by']      = empty($_REQUEST['sort_by']) ? 'act_id' : trim($_REQUEST['sort_by']);
        $filter['sort_order']   = empty($_REQUEST['sort_order']) ? 'DESC' : trim($_REQUEST['sort_order']);

        $where = (!empty($filter['keyword'])) ? " AND goods_name LIKE '%" . mysql_like_quote($filter['keyword']) . "%'" : '';

        $sql = "SELECT COUNT(*) FROM " . $GLOBALS['ecs']->table('goods_activity') .
                " WHERE act_type = '" . GAT_GROUP_BUY . "' $where";
        $filter['record_count'] = $GLOBALS['db']->getOne($sql);

        /* 分页大小 */
        $filter = page_and_size($filter);

        /* 查询 */
        $sql = "SELECT * ".
                "FROM " . $GLOBALS['ecs']->table('goods_activity') .
                " WHERE act_type = '" . GAT_GROUP_BUY . "' $where ".
                " ORDER BY $filter[sort_by] $filter[sort_order] ".
                " LIMIT ". $filter['start'] .", $filter[page_size]";

        $filter['keyword'] = stripslashes($filter['keyword']);
        set_filter($filter, $sql);
    }
    else
    {
        $sql    = $result['sql'];
        $filter = $result['filter'];
    }
    $res = $GLOBALS['db']->query($sql);

    $list = array();
    while ($row = $GLOBALS['db']->fetchRow($res))
    {
        $ext_info = unserialize($row['ext_info']);
        $stat = group_buy_stat($row['act_id'], $ext_info['deposit']);
        $arr = array_merge($row, $stat, $ext_info);

        /* 处理价格阶梯 */
        $price_ladder = $arr['price_ladder'];
        if (!is_array($price_ladder) || empty($price_ladder))
        {
            $price_ladder = array(array('amount' => 0, 'price' => 0));
        }
        else
        {
            foreach ($price_ladder AS $key => $amount_price)
            {
                $price_ladder[$key]['formated_price'] = price_format($amount_price['price']);
            }
        }

        /* 计算当前价 */
        $cur_price  = $price_ladder[0]['price'];    // 初始化
        $cur_amount = $stat['valid_goods'];         // 当前数量
        foreach ($price_ladder AS $amount_price)
        {
            if ($cur_amount >= $amount_price['amount'])
            {
                $cur_price = $amount_price['price'];
            }
            else
            {
                break;
            }
        }

        $arr['cur_price']   = $cur_price;

        $status = group_buy_status($arr);

        $arr['start_time']  = local_date($GLOBALS['_CFG']['date_format'], $arr['start_time']);
        $arr['end_time']    = local_date($GLOBALS['_CFG']['date_format'], $arr['end_time']);
        $arr['cur_status']  = $GLOBALS['_LANG']['gbs'][$status];

        $list[] = $arr;
    }
    $arr = array('item' => $list, 'filter' => $filter, 'page_count' => $filter['page_count'], 'record_count' => $filter['record_count']);

    return $arr;
}

function get_all_cats()
{
    $sql = "SELECT cat_id,cat_name FROM " .$GLOBALS['ecs']->table('category'). " WHERE parent_id=0 ";
    $res = $GLOBALS['db']->getAll($sql);
	$arr = array();
	foreach ($res AS $key => $val)
	{
		$arr[] = $val;

	}
	return $arr;
}
/**
 * 取得某商品的团购活动
 * @param   int     $goods_id   商品id
 * @return  array
 */
function goods_group_buy($goods_id)
{
    $sql = "SELECT * FROM " . $GLOBALS['ecs']->table('goods_activity') .
            " WHERE goods_id = '$goods_id' " .
            " AND act_type = '" . GAT_GROUP_BUY . "'" .
            " AND start_time <= " . gmtime() .
            " AND end_time >= " . gmtime();

    return $GLOBALS['db']->getRow($sql);
}

/**
 * 列表链接
 * @param   bool    $is_add         是否添加（插入）
 * @return  array('href' => $href, 'text' => $text)
 */
function list_link($is_add = true)
{
    $href = 'group_buy.php?act=list';
    if (!$is_add)
    {
        $href .= '&' . list_link_postfix();
    }

    return array('href' => $href, 'text' => $GLOBALS['_LANG']['group_buy_list']);
}

?>