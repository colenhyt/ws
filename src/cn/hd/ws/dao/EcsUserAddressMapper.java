package cn.hd.ws.dao;

import cn.hd.ws.dao.EcsUserAddress;
import cn.hd.ws.dao.EcsUserAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EcsUserAddressMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int countByExample(EcsUserAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int deleteByExample(EcsUserAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int deleteByPrimaryKey(Integer addressId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int insert(EcsUserAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int insertSelective(EcsUserAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    List<EcsUserAddress> selectByExample(EcsUserAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    EcsUserAddress selectByPrimaryKey(Integer addressId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int updateByExampleSelective(@Param("record") EcsUserAddress record, @Param("example") EcsUserAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int updateByExample(@Param("record") EcsUserAddress record, @Param("example") EcsUserAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int updateByPrimaryKeySelective(EcsUserAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ecs_user_address
     *
     * @mbggenerated Wed Jul 15 09:06:14 CST 2015
     */
    int updateByPrimaryKey(EcsUserAddress record);
}