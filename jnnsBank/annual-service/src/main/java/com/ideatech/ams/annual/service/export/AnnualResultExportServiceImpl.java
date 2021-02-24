package com.ideatech.ams.annual.service.export;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.CompareDataDto;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.dto.poi.AnnualResultPoi;
import com.ideatech.ams.annual.enums.AbnormalStatusEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.annual.poi.AnnualResultExport;
import com.ideatech.ams.annual.service.CompareFieldsService;
import com.ideatech.ams.annual.service.CompareService;
import com.ideatech.ams.annual.spi.AnnualResultProcessor;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.excel.util.AnnualExportExcel;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.ZipUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.util.*;

/**
 *
 *
 * @author van
 * @date 16:05 2018/8/31
 */
@Service
@Slf4j
public class AnnualResultExportServiceImpl implements AnnualResultExportService {

	@Autowired
	private Map<String, AnnualResultProcessor> annualResultProcessors;

	@Autowired
	private CompareService compareService;

	@Autowired
	private OrganizationService organizationService;

	@Value("${ams.annual.export-folder:/home/weblogic/idea/annual/export}")
	private String annualExportFolder;

	@Autowired
	private WebApplicationContext webApplicationConnect;

	@Autowired
	private CompareFieldsService compareFieldsService;

	@Override
	public void createAnnualResultExport(Long taskId) {
		File file = new File(annualExportFolder + File.separator + taskId);
		file.mkdirs();

		List<String> saicJsonList = new ArrayList<>(16);
		//获取年检比对字段
		List<CompareFieldsDto> compareFields = compareFieldsService.listCompareRulesByTakId(taskId);
        Set<String> annualFields = new HashSet<>();
        int active = 0;
        for(CompareFieldsDto c : compareFields){
            if(c.isActive()){
                annualFields.add(c.getCompareFieldEnum().getField());
                active++;
            }
        }
        Set poiFields = getPoiField(annualFields);
        String[] headers = new String[(annualFields.size() * 3) + 1];
        headers[0] = "";
		int index = 1;
		for(CompareFieldsDto compareFieldsDto : compareFields){
			if(StringUtils.equals("账号",compareFieldsDto.getCompareFieldEnum().getValue())){
				continue;
			}
			if(compareFieldsDto.isActive()){
				headers[index] = compareFieldsDto.getCompareFieldEnum().getValue();
				headers[index + active - 1] = compareFieldsDto.getCompareFieldEnum().getValue();
				headers[index + ((active - 1) * 2)] = compareFieldsDto.getCompareFieldEnum().getValue();
				index ++;
			}
		}

		AnnualResultExport[] annualResultExports = null;

		List<OrganizationDto> organizationDtos = new ArrayList<>(16);

		AnnualResultDto condition = new AnnualResultDto();
		condition.setTaskId(taskId);

		if (SecurityUtils.getCurrentUser() != null) {
			organizationDtos.addAll(organizationService.listDescendant(SecurityUtils.getCurrentUser().getOrgId()));
		} else {
			organizationDtos.addAll(organizationService.listAll());
		}

		//按机构导出
		for (OrganizationDto organizationDto : organizationDtos) {
			//下个机构直接清空数据
			annualResultExports = new AnnualResultExport[annualResultProcessors.values().size()];
			saicJsonList.clear();
			saicJsonList = new ArrayList<>(16);

			String organAnnualExportFilePtah = file.getAbsolutePath() + File.separator + organizationDto.getCode();

			//设置机构,此处使用organcode，避免数据包含下级机构
//			condition.setOrganFullId(organizationDto.getFullId());
			condition.setOrganCode(organizationDto.getCode());


			boolean hasData = false;

			int i = 0;
			for (AnnualResultProcessor annualResultProcessor : annualResultProcessors.values()) {

				AnnualResultDto queryCondition = new AnnualResultDto();
				BeanCopierUtils.copyProperties(condition, queryCondition);

				Page<AnnualResultDto> items = annualResultProcessor
						.query(queryCondition, new PageRequest(0, Integer.MAX_VALUE));

				if(CollectionUtils.isNotEmpty(items.getContent())){
					hasData = true;
				}
				//创建工商数据集合
				saicJsonList.addAll(createSaics(items.getContent()));

				AnnualResultExport annualResultExport = new AnnualResultExport();
				annualResultExport.setTitle(annualResultProcessor.getName());
				annualResultExport.setPoiList(annualResult2Poi(items.getContent()));
				annualResultExport.setHeaders3(headers);
				annualResultExport.setActive(active - 1);
				annualResultExports[i] = annualResultExport;
				i++;
			}

			if(hasData) {
				log.info("年检结果导出-开始生成EXCEL&WORD");
				//创建excel
				OutputStream os = null;
				try {
					File excelFile = new File(
							organAnnualExportFilePtah + File.separator + organizationDto.getCode() + "年检excel.xlsx");
					excelFile.getParentFile().mkdirs();
					os = new BufferedOutputStream(new FileOutputStream(excelFile));
					AnnualExportExcel.export(os, DateFormatUtils.ISO_DATE_FORMAT.getPattern(), poiFields, excelFile.getName(), annualResultExports);
					os.flush();
				} catch (Exception e) {
					log.error("导出年检excel数据异常", e);
				} finally {
					IOUtils.closeQuietly(os);
				}

				//创建word
				Writer out = null;
				FileOutputStream fos = null;
				try {
					Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
//				cfg.setServletContextForTemplateLoading(webApplicationConnect.getServletContext(), "/templates/ftl");
					StringWriter writer = new StringWriter();
					InputStream inputStream = new ClassPathResource("annualSaicInfo.ftl").getInputStream();
					IOUtils.copy(inputStream, writer,"UTF-8");

					StringTemplateLoader templateLoader = new StringTemplateLoader();
					templateLoader.putTemplate("annualSaicInfo.ftl", writer.toString());

//				cfg.setDirectoryForTemplateLoading(new ClassPathResource("annualSaicInfo.ftl").getFile().getParentFile());
					cfg.setTemplateLoader(templateLoader);

					cfg.setDefaultEncoding("UTF-8");
					cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
					Template template = cfg.getTemplate("annualSaicInfo.ftl");
                    JSONArray array = new JSONArray();
					for (String saicJson : saicJsonList) {
						if (StringUtils.isNotBlank(saicJson)) {
							//对于特殊字符的转义
							saicJson = saicJson.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
							JSONObject obj = JSONObject.parseObject(saicJson);
							if (obj == null) {
								continue;
							}
							obj.put("result", obj);
							array.add(obj);
						}
					}
					JSONObject results = new JSONObject();
					results.put("results", array);

					File wordFile = new File(
							organAnnualExportFilePtah + File.separator + organizationDto.getCode() + "年检word.doc");
					wordFile.getParentFile().mkdirs();
					fos = new FileOutputStream(wordFile);
					// 这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
					out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
					template.process(results, out);
				} catch (Exception e) {
					log.error("导出word异常", e);
				} finally {
					IOUtils.closeQuietly(out);
					IOUtils.closeQuietly(fos);
				}
			}

			File folder = new File(organAnnualExportFilePtah);
			if(folder.exists()){
				//压缩
				zipExportFile(folder);
			}

		}
	}

	/**
	 * 压缩文件
	 * @param file
	 */
	private void zipExportFile(File file) {
		File zipFile = new File(
				file.getParent() + File.separator + FilenameUtils.getBaseName(file.getName()) + ".zip");
		zipFile.getParentFile().mkdirs();
		//压缩机构数据
		try {
			if (ZipUtils.zipFile(file, zipFile, "")) {
				FileUtils.deleteQuietly(file);
			}
		} catch (IOException e) {
			//ignore
			log.warn("压缩机构数据失败！");
		}
	}

	List<String> createSaics(List<AnnualResultDto> annualResultDtos) {
		List<String> saicList = new ArrayList<>(16);
		for (AnnualResultDto annualResultDto : annualResultDtos) {
			saicList.add(annualResultDto.getSaicData());
		}
		return saicList;
	}


	List<AnnualResultPoi> annualResult2Poi(List<AnnualResultDto> annualResultDtos) {
		List<AnnualResultPoi> poiList = new ArrayList<>();
		for (AnnualResultDto annualResultDto : annualResultDtos) {
			poiList.add(annualResult2Poi(annualResultDto));
		}
		return poiList;
	}


	AnnualResultPoi annualResult2Poi(AnnualResultDto annualResultDto) {
		StringBuilder abnormalNames = new StringBuilder();
		String abnormals = annualResultDto.getAbnormal();
		if (StringUtils.isNotBlank(abnormals)) {
			if (abnormals.contains(AbnormalStatusEnum.ABNORMAL_SEPARATOR)) {
				for (String abnormal : StringUtils
						.splitByWholeSeparatorPreserveAllTokens(abnormals, AbnormalStatusEnum.ABNORMAL_SEPARATOR)) {
					abnormalNames.append(AbnormalStatusEnum.valueOf(abnormal).getName());
					abnormalNames.append(AbnormalStatusEnum.ABNORMAL_SEPARATOR);
				}
			}else{
				abnormalNames.append(AbnormalStatusEnum.valueOf(abnormals).getName());
			}
		}

		AnnualResultPoi annualResultPoi = new AnnualResultPoi();
		annualResultPoi.setAcctNo(annualResultDto.getAcctNo());
		annualResultPoi
				.setAbnormal(StringUtils.removeEnd(abnormalNames.toString(), AbnormalStatusEnum.ABNORMAL_SEPARATOR));
		annualResultPoi.setBlack(annualResultDto.getBlack() ? "是" : "否");
//		annualResultPoi.setForceStatus(
//				annualResultDto.getForceStatus() != null ? annualResultDto.getForceStatus().getName() : "");
		annualResultPoi.setMatch(annualResultDto.getMatch() ? "一致" : "不一致");
//		annualResultPoi.setOrganPbcCode(annualResultDto.getOrganPbcCode());
		annualResultPoi.setOrganCode(annualResultDto.getOrganCode());
		annualResultPoi.setPbcSubmitter(annualResultDto.getPbcSubmitter());
//		annualResultPoi.setPbcSubmitStatus(
//				annualResultDto.getPbcSubmitStatus() != null ? annualResultDto.getPbcSubmitStatus().getName() : "");
		annualResultPoi.setResult(annualResultDto.getResult() != null ? annualResultDto.getResult().getName() : "");
		annualResultPoi.setSaicStatus(
				annualResultDto.getSaicStatus() != null ? annualResultDto.getSaicStatus().getName() : "");
		annualResultPoi.setUnilateral(
				annualResultDto.getUnilateral() != null ? annualResultDto.getUnilateral().getName() : "");
		annualResultPoi.setAcctType(annualResultDto.getAcctType().getValue());

		Map<String, CompareDataDto> compareDatas = compareService.getCompareDatas(annualResultDto);
		for (Map.Entry<String, CompareDataDto> stringCompareDataDtoEntry : compareDatas.entrySet()) {
			String dataSource = stringCompareDataDtoEntry.getKey();
			CompareDataDto compareDataDto = stringCompareDataDtoEntry.getValue();

			if (DataSourceEnum.CORE.name().equals(dataSource)) {
				annualResultPoi.setCoreName(compareDataDto.getDepositorName());
				annualResultPoi.setCoreLegalName(compareDataDto.getLegalName());
				annualResultPoi.setCoreRegNo(compareDataDto.getRegNo());
				annualResultPoi.setCoreRegAddress(compareDataDto.getRegAddress());
				annualResultPoi.setCoreRegisteredCapital(compareDataDto.getRegisteredCapital());
                annualResultPoi.setCoreRegNo(compareDataDto.getRegNo());
                annualResultPoi.setCoreBusinessScope(compareDataDto.getBusinessScope());
                annualResultPoi.setCoreLegalIdcardNo(compareDataDto.getLegalIdcardNo());
                annualResultPoi.setCoreLegalIdcardType(compareDataDto.getLegalIdcardType());
                annualResultPoi.setCoreStateTaxRegNo(compareDataDto.getStateTaxRegNo());
                annualResultPoi.setCoreRegCurrencyType(compareDataDto.getRegCurrencyType());
                annualResultPoi.setCoreTaxRegNo(compareDataDto.getTaxRegNo());
                annualResultPoi.setCoreOrganCode(compareDataDto.getOrganCode());
				annualResultPoi.setCoreOrgCode(compareDataDto.getOrgCode());
			} else if (DataSourceEnum.SAIC.name().equals(dataSource)) {
				annualResultPoi.setSaicName(compareDataDto.getDepositorName());
				annualResultPoi.setSaicLegalName(compareDataDto.getLegalName());
				annualResultPoi.setSaicRegNo(compareDataDto.getRegNo());
				annualResultPoi.setSaicRegAddress(compareDataDto.getRegAddress());
				annualResultPoi.setSaicRegisteredCapital(compareDataDto.getRegisteredCapital());
                annualResultPoi.setSaicRegNo(compareDataDto.getRegNo());
                annualResultPoi.setSaicBusinessScope(compareDataDto.getBusinessScope());
                annualResultPoi.setSaicLegalIdcardNo(compareDataDto.getLegalIdcardNo());
                annualResultPoi.setSaicLegalIdcardType(compareDataDto.getLegalIdcardType());
                annualResultPoi.setSaicStateTaxRegNo(compareDataDto.getStateTaxRegNo());
                annualResultPoi.setSaicRegCurrencyType(compareDataDto.getRegCurrencyType());
                annualResultPoi.setSaicTaxRegNo(compareDataDto.getTaxRegNo());
                annualResultPoi.setSaicOrganCode(compareDataDto.getOrganCode());
				annualResultPoi.setSaicOrgCode(compareDataDto.getOrgCode());
			} else if (DataSourceEnum.PBC.name().equals(dataSource)) {
				annualResultPoi.setPbcName(compareDataDto.getDepositorName());
				annualResultPoi.setPbcLegalName(compareDataDto.getLegalName());
				annualResultPoi.setPbcRegNo(compareDataDto.getRegNo());
				annualResultPoi.setPbcRegAddress(compareDataDto.getRegAddress());
				annualResultPoi.setPbcRegisteredCapital(compareDataDto.getRegisteredCapital());
                annualResultPoi.setPbcRegNo(compareDataDto.getRegNo());
                annualResultPoi.setPbcBusinessScope(compareDataDto.getBusinessScope());
                annualResultPoi.setPbcLegalIdcardNo(compareDataDto.getLegalIdcardNo());
                annualResultPoi.setPbcLegalIdcardType(compareDataDto.getLegalIdcardType());
                annualResultPoi.setPbcStateTaxRegNo(compareDataDto.getStateTaxRegNo());
                annualResultPoi.setPbcRegCurrencyType(compareDataDto.getRegCurrencyType());
                annualResultPoi.setPbcTaxRegNo(compareDataDto.getTaxRegNo());
                annualResultPoi.setPbcOrganCode(compareDataDto.getOrganCode());
				annualResultPoi.setPbcOrgCode(compareDataDto.getOrgCode());
			}
		}
		return annualResultPoi;
	}

	public Set getPoiField(Set<String> annualField){
	    Set<String> poiField = new HashSet<>();
        poiField.add("acctNo");
	    for(String field : annualField){
	        if(StringUtils.equals("depositorName",field)){
	            poiField.add("coreName");
                poiField.add("saicName");
                poiField.add("pbcName");
            }
            if(StringUtils.equals("orgCode",field)){
                poiField.add("coreOrgCode");
                poiField.add("saicOrgCode");
                poiField.add("pbcOrgCode");
            }
            if(StringUtils.equals("legalName",field)){
                poiField.add("coreLegalName");
                poiField.add("saicLegalName");
                poiField.add("pbcLegalName");
            }
            if(StringUtils.equals("regNo",field)){
                poiField.add("coreRegNo");
                poiField.add("saicRegNo");
                poiField.add("pbcRegNo");
            }
            if(StringUtils.equals("businessScope",field)){
                poiField.add("coreBusinessScope");
                poiField.add("saicBusinessScope");
                poiField.add("pbcBusinessScope");
            }
            if(StringUtils.equals("regAddress",field)){
                poiField.add("coreRegAddress");
                poiField.add("saicRegAddress");
                poiField.add("pbcRegAddress");
            }
            if(StringUtils.equals("registeredCapital",field)){
                poiField.add("coreRegisteredCapital");
                poiField.add("saicRegisteredCapital");
                poiField.add("pbcRegisteredCapital");
            }
            if(StringUtils.equals("accountStatus",field)){
                poiField.add("coreAccountStatus");
                poiField.add("saicAccountStatus");
                poiField.add("pbcAccountStatus");
            }
            if(StringUtils.equals("legalIdcardType",field)){
                poiField.add("coreLegalIdcardType");
                poiField.add("saicLegalIdcardType");
                poiField.add("pbcLegalIdcardType");
            }
            if(StringUtils.equals("legalIdcardNo",field)){
                poiField.add("coreLegalIdcardNo");
                poiField.add("saicLegalIdcardNo");
                poiField.add("pbcLegalIdcardNo");
            }
            if(StringUtils.equals("regCurrencyType",field)){
                poiField.add("coreRegCurrencyType");
                poiField.add("saicRegCurrencyType");
                poiField.add("pbcRegCurrencyType");
            }
            if(StringUtils.equals("stateTaxRegNo",field)){
                poiField.add("coreStateTaxRegNo");
                poiField.add("saicStateTaxRegNo");
                poiField.add("pbcStateTaxRegNo");
            }
            if(StringUtils.equals("taxRegNo",field)){
                poiField.add("coreTaxRegNo");
                poiField.add("saicTaxRegNo");
                poiField.add("pbcTaxRegNo");
            }
        }
        return poiField;
    }
}
