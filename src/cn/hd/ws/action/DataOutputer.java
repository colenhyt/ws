package cn.hd.ws.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hd.util.MybatisSessionFactory;
import cn.hd.ws.dao.EcsRegion;
import cn.hd.ws.dao.EcsRegionExample;
import cn.hd.ws.dao.EcsRegionMapper;

public class DataOutputer {
	private static String CONFIG_PATH_JS = "static/js/";

	public void outputRegion(){
	    File fileDes = new File(CONFIG_PATH_JS+"/region.js");  
		try {
		    Object obj=MybatisSessionFactory.getSession().getMapper(EcsRegionMapper.class);
		    EcsRegionMapper mapper = (EcsRegionMapper)obj;

		Map<String,List<EcsRegion>> regonsMap = new HashMap<String,List<EcsRegion>>();
	    FileOutputStream out = null;
			if (!fileDes.exists())
			{
				fileDes.createNewFile();
			}
	        out = new FileOutputStream(fileDes);

			out.write(("var data_region=[\n").getBytes());
		    EcsRegionExample example = new EcsRegionExample();
		    EcsRegionExample.Criteria criteria = example.createCriteria();
		    criteria.andParentIdEqualTo((short)1);
			List<EcsRegion> items = mapper.selectByExample(example);
			for (int i=0;i<items.size();i++){
				EcsRegion r = items.get(i);
				String pname = r.getRegionName();
				if (pname.equals("澳门")||pname.equals("台湾")||pname.equals("香港"))
					continue;
				else if (pname.equals("北京")||pname.equals("上海")||pname.equals("天津")||pname.equals("重庆"))
					pname += "市";
				else
					pname += "省";
				out.write(("["+r.getRegionId()+",'"+pname+"'],").getBytes());
				System.out.println(pname);
				
			    EcsRegionExample example22 = new EcsRegionExample();
			    EcsRegionExample.Criteria criteria22 = example22.createCriteria();
			    criteria22.andParentIdEqualTo((short)r.getRegionId());			
			    List<EcsRegion> items22 = mapper.selectByExample(example22);
			    regonsMap.put(pname, items22);
			}
			out.write(("];\n\n").getBytes());
			
			out.write(("var data_subregion={\n").getBytes());
			Set<String> set = regonsMap.keySet();
			 for(Iterator<String> iterator = set.iterator();iterator.hasNext();){ 
				 String pname = iterator.next();
				out.write(("'"+(pname)+"':[").getBytes());
				List<EcsRegion> regons = regonsMap.get(pname);
				for (int i=0;i<regons.size();i++){
					EcsRegion reg = regons.get(i);
					out.write(("["+reg.getRegionId()+",'"+reg.getRegionName()+"市'],").getBytes());
				}
				
				out.write(("],\n").getBytes());
			}
			out.write(("};\n\n").getBytes());
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataOutputer aa = new DataOutputer();
		aa.outputRegion();
	}

}
