package com.ideatech.ams.system.org.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对机构工具类
 * @author Administrator
 *
 */
public class OrganUtils {
	/**
	 * 根据fullId层次关系，拼凑出符合系统使用的IN查询条件参数
	 * @param fullId
	 * @return
	 */
	public static List<String> getFullIdsByFullId(String fullId){
		List<String> fullIds = new ArrayList<String>();
		fullIds.add(fullId);
		return splitFullId(fullIds,fullId);
	}
	/**
	 * 机构拼凑递归方法
	 * @param fullIds
	 * @param fullId
	 * @return
	 */
	public static List<String> splitFullId(List<String> fullIds,String fullId){
		int chIndex = fullId.lastIndexOf("-");
		if(chIndex>0){
			String tempFullId = fullId.substring(0, chIndex);
			fullIds.add(tempFullId);
			splitFullId(fullIds,tempFullId);
		}
		return fullIds;
	}
}
