package cn.hd.ws.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.hd.util.MybatisSessionFactory;
import cn.hd.ws.dao.EcsRegion;
import cn.hd.ws.dao.EcsRegionExample;
import cn.hd.ws.dao.EcsRegionMapper;
import cn.hd.ws.dao.EcsUsersExample.Criteria;

public class DataOutputer {
	private static String CONFIG_PATH_JS = "static/js/";

	public void outputRegion(){
	    File fileDes = new File(CONFIG_PATH_JS+"/region.js");  
		try {
		    Object obj=MybatisSessionFactory.getSession().getMapper(EcsRegionMapper.class);
		    EcsRegionMapper mapper = (EcsRegionMapper)obj;

		
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
				out.write(("'"+r.getRegionName()+"'").getBytes());
				System.out.println(r.getRegionName());
			}
			out.write(("];\n\n").getBytes());
			
			out.write(("var data_subregion={\n").getBytes());
		    EcsRegionExample example2 = new EcsRegionExample();
		    EcsRegionExample.Criteria criteria2 = example2.createCriteria();
		    criteria2.andParentIdGreaterThan((short)1);
			List<EcsRegion> items2 = mapper.selectByExample(example2);
			for (int i=0;i<items2.size();i++){
				EcsRegion r = items2.get(i);
				out.write(("["+r.getParentId()+"]=").getBytes());
				out.write(("'"+r.getRegionName()+"'").getBytes());
				System.out.println(r.getRegionName());
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
