/**
 * 
 */
package com.ideatech.ams.compare.dto.compareresultdetail;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 比对结果详细信息
 * 
 * @author zhailiang
 *
 */
public class CompareResultDetails {

//	public static final String DETAILS_TOKEN = "|!";
//	public static final String COLUMN_TOKEN = "%$";

	public static final String DETAILS_TOKEN = "|!";
	public static final String COLUMN_TOKEN = "%$";

	/**
	 * 所有的数据源的名字
	 */
	private List<String> categories = new ArrayList<String>();
	/**
	 * 每个字段的比对结果
	 */
	private List<CompareResultDetailsField> fields = new ArrayList<CompareResultDetailsField>();

	public void addCategory(String category) {
		if (!categories.contains(category)) {
			categories.add(category);
		}
	}

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public void addItem(CompareResultDetailsField fieldCompareResult) {
		fields.add(fieldCompareResult);
	}

	/**
	 * @return the fields
	 */
	public List<CompareResultDetailsField> getFields() {
		return fields;
	}

	/**
	 * 将比对结果转换为查询时表格里一行数据的格式
	 * 
	 * @return
	 */
	public String toRowContent() {
		StringBuffer content = new StringBuffer();
		for (CompareResultDetailsField field : fields) {
			Map<String, CompareResultDetailsCell> categoryData = field.getContent();
			String cellValue = "";
			for (String category : categories) {
				CompareResultDetailsCell cell = categoryData.get(category);
				if (cell != null && StringUtils.isNotBlank(cell.getValue())) {
					cellValue = cell.getValue();
					break;
				}
			}
			content.append(cellValue + COLUMN_TOKEN);
		}
		return StringUtils.removeEnd(content.toString(), COLUMN_TOKEN);
	}
	
	/**
	 * 判断数据的最终比对结果是否匹配
	 * 
	 * @return
	 */
	public boolean isMatch() {
		for (CompareResultDetailsField field : fields) {
			if (!field.isMatch()) {
				return false;
			}
		}
		return true;
	}

	public String toColumnColor() {
//		StringBuffer content = new StringBuffer();
		JSONObject json = new JSONObject();

		for (CompareResultDetailsField field : fields) {
			json.put(field.getFieldCode(), field.isMatch());
//			content.append(field.isMatch() + ",");
		}
//		return StringUtils.removeEnd(content.toString(), ",");
		return json.toString();
	}

	public String toRowDetails() {
		StringBuffer content = new StringBuffer();
		for (String category : categories) {
			content.append(getCategoryRowDetails(category) + DETAILS_TOKEN);
		}
		return StringUtils.removeEnd(content.toString(), DETAILS_TOKEN);
	}

	private String getCategoryRowDetails(String category) {
		StringBuffer content = new StringBuffer();
		for (CompareResultDetailsField field : fields) {
			String cellVal = "";
			CompareResultDetailsCell cell = field.getContent().get(category);
			if (cell != null) {
				cellVal = cell.getValue();
			}
			content.append(cellVal + COLUMN_TOKEN);
		}
		return StringUtils.removeEnd(content.toString(), COLUMN_TOKEN);
	}

	public JSONObject toRowDetail() {
		StringBuffer content = new StringBuffer();
		JSONObject json = new JSONObject(new LinkedHashMap<String, Object>());

		for (String category : categories) {
			content.append(json.put(category, getCategoryRowDetail(category)));
		}

		return json;
	}

	private JSONObject getCategoryRowDetail(String category) {
		JSONObject json = new JSONObject(new LinkedHashMap<String, Object>());

		for (CompareResultDetailsField field : fields) {
			String cellVal = "";
			CompareResultDetailsCell cell = field.getContent().get(category);
			if (cell != null) {
				cellVal = cell.getValue();
			}

			json.put(field.getFieldCode(), cellVal);
		}
		return json;
	}
	
	
	/**
	 * 获得企业名称字段值
	 * 
	 * @return
	 */
	public String getName() {
		StringBuffer content = new StringBuffer();

		for (String category : categories) {
			for (CompareResultDetailsField field : fields) {
				if(field.getFieldCode().equals("name")) {
					String cellVal = "";
					CompareResultDetailsCell cell = field.getContent().get(category);
					if (cell != null && StringUtils.isNotBlank(cell.getValue())) {
						cellVal = cell.getValue();
						content.append(cellVal + COLUMN_TOKEN);
					}
				}
			}
		}
		return StringUtils.removeEnd(content.toString(), COLUMN_TOKEN);
	}

}
