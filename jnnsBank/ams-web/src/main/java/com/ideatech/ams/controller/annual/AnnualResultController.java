package com.ideatech.ams.controller.annual;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.dto.AnnualTaskDto;
import com.ideatech.ams.annual.dto.CompareDataDto;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import com.ideatech.ams.annual.service.*;
import com.ideatech.ams.controller.BaseController;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.FileExtraUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/annualResult")
@Slf4j
public class AnnualResultController extends BaseController<AnnualResultService, AnnualResultDto> {

    @Autowired
    private AnnualResultService annualResultService;

    @Autowired
    private CompareFieldsService compareFieldsService;

    @Autowired
    private CompareService compareService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AnnualTaskService annualTaskService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private AnnualStatisticsService annualStatisticsService;

	@Value("${ams.annual.export-folder:/home/weblogic/idea/annual/export}")
	private String annualExportFolder;

    /**
     * 年检结果相关列表展示
     * @param condition
     * @param code
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/annualResultByCode", method = RequestMethod.GET)
    public ObjectRestResponse<TableResultResponse<AnnualResultDto>> queryList(AnnualResultDto condition, String code, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (condition.getId() == null) {
            condition.setId(SecurityUtils.getCurrentUser().getOrgId());
        }

        TableResultResponse<AnnualResultDto> annualResultList = annualResultService.queryByCode(condition, code, pageable);

        return new ObjectRestResponse<TableResultResponse<AnnualResultDto>>().rel(true).result(annualResultList);
    }

	/**
	 * 年检结果相关列表数据 EXCEL导出
	 */
	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.EXPORT,operateContent = "结果导出")
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(AnnualResultDto condition, String code,HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (condition.getId() == null) {
			condition.setId(SecurityUtils.getCurrentUser().getOrgId());
		}
		List<IExcelExport> excelExportList = annualResultService.exportExcel(condition, code);
		StringBuilder fileName = new StringBuilder();
		fileName.append(System.currentTimeMillis());
		response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
		response.setContentType("application/octet-stream");
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",excelExportList);
		toClient.flush();

		response.getOutputStream().close();
	}

	/**
	 * 手工提交
	 * @param id
	 * @return
	 */
	@PutMapping("/submit/{id}")
	public ObjectRestResponse submit(@PathVariable("id") Long id) {
		//判断是否需要重新年检
		List<ConfigDto> cdList = configService.findByKey("annualAgainEnabled");
		String config = "false";
		if (cdList.size() > 0) {
			config = cdList.get(0).getConfigValue();
		}
		if ("true".equals(config)) {
			AnnualResultDto annualResultDto = annualResultService.annualAgain(id);
			return new ObjectRestResponse<>().rel(annualResultDto.getResult() == ResultStatusEnum.PASS).result(annualResultDto.getResult().getName());
		} else {
			ResultDto<AnnualResultDto> resultDto = annualResultService.submitAnnualAccount(id);
			if (resultDto.getData() == null) {
				return new ObjectRestResponse<>().rel(false).result(resultDto.getMessage());
			} else {
				AnnualResultDto annualResultDto = resultDto.getData();
				return new ObjectRestResponse<>().rel(annualResultDto.getResult() == ResultStatusEnum.PASS).result(annualResultDto.getPbcSubmitErrorMsg());
			}
		}
	}

	/**
	 * 手工批量提交
	 * 目前待处理、年检成功、手工成功、年检失败的（批量年检提交）和（单条年检提交）都使用该接口。
	 * 批量年检提交：table页选择单条或多条，点击年检提交。
	 * 单条年检提交：从比对结果中进入，点击年检提交。
	 * @param ids
	 * @return
	 */
	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.OTHER,operateContent = "年检提交/提交人行")
	@PostMapping("/submit")
	public ObjectRestResponse submit(@RequestParam("ids[]") Long[] ids) {
		//判断是否需要重新年检
		List<ConfigDto> cdList = configService.findByKey("annualAgainEnabled");
		String config = "false";
		if (cdList.size() > 0) {
			config = cdList.get(0).getConfigValue();
		}
		List<AnnualResultDto> ardList = new ArrayList<>();//年检结果
		List<AnnualResultDto> ardList_success = new ArrayList<>();//年检通过
		List<AnnualResultDto> ardList_failure = new ArrayList<>();//年检失败

		//重新年检（年检轮循开启）
		//TODO 系统年检成功、手动年检成功的数据进行重新年检
		if ("true".equals(config)) {
			log.info("annualAgainEnabled=true,重新年检");
			for (Long id : ids) {
				AnnualResultDto ard = annualResultService.annualAgain(id);
				if (ForceStatusEnum.AGAIN_SUCCESS.equals(ard.getForceStatus())) {
					ardList_success.add(ard);
				} else {
					ardList_failure.add(ard);
				}
			}
			ardList.addAll(ardList_failure);
			ardList.addAll(ardList_success);
			log.info("重新年检：年检成功={},年检失败={}",ardList_success.size(),ardList_failure.size());
			return new ObjectRestResponse<>().rel(true).result(ardList);
		} else {//直接提交（年检轮循禁用）
			int success = 0;
			int fail = 0;
			log.info("annualAgainEnabled=false or null,直接提交");
			for (Long id : ids) {
				ResultDto<AnnualResultDto> resultDto = annualResultService.submitAnnualAccount(id);
				if (resultDto.getData() == null) {
					fail++;
                    log.warn("id:{},年检提交异常:{}",id,resultDto.getMessage());
				} else {
					AnnualResultDto annualResultDto = resultDto.getData();
					if (annualResultDto.getResult() == ResultStatusEnum.PASS) {
						success++;
						ardList_success.add(annualResultDto);
					} else {
						fail++;
						ardList_failure.add(annualResultDto);
						annualStatisticsService.updateStatisticsSuccess(annualResultDto.getTaskId(), annualResultDto.getOrganCode(), "sub");
					}
				}
			}
			ardList.addAll(ardList_failure);
			ardList.addAll(ardList_success);
			log.info("直接提交：年检成功={},年检失败={}",success,fail);
			return new ObjectRestResponse<>().rel(true).result(ardList);
		}
	}

	/**
	 * 待年检数据全部手工提交
	 */
	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "重新年检")
	@PostMapping("/submitAll")
	public ObjectRestResponse submitAll(AnnualResultDto condition, String code) {
		annualResultService.annualAgainAll(condition, code);
		return new ObjectRestResponse<>().rel(true);
	}

	/**
	 * 数据处理
	 * @param ids
	 * @return
	 */
	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.UPDATE,operateContent = "年检处理")
	@PostMapping("/dataProcessSubmit")
	public ObjectRestResponse dataProcessSubmit(@RequestParam("ids[]") Long[] ids) {
		boolean result = false;
		for (Long id : ids) {
			result = annualResultService.submitAnnualDataProcess(id);
			if(result){
				break;
			}
		}
		return new ObjectRestResponse<>().rel(true).result(result);
	}

	/**
	 * 数据处理取消
	 * @param
	 * @return
	 */
	@PostMapping("/recall")
	public ObjectRestResponse dataProcessRecall(@RequestParam("id") Long id) {
		annualResultService.dataProcessRecall(id);
		return new ObjectRestResponse<>().rel(true).result("");
	}

	/**
	 * 数据处理取消
	 * @param
	 * @return
	 */
	@PostMapping("/checkSelf")
	public ObjectRestResponse checkSelf(@RequestParam("id") Long id) {
		boolean self = annualResultService.checkSelf(id);
		return new ObjectRestResponse<>().rel(true).result(self);
	}


	@GetMapping("/view/{id}/{dataSource}")
	public ObjectRestResponse viewData(@PathVariable("id") Long id, @PathVariable("dataSource") String dataSource) {
		DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf(dataSource);
		AnnualResultDto annualResultDto = annualResultService.findById(id);
		String data = "";
		switch (dataSourceEnum) {
			case CORE:
				data = annualResultDto.getCoreData();
				break;
			case PBC:
				data = annualResultDto.getPbcData();
				break;
			case SAIC:
				data = annualResultDto.getSaicData();
				break;
			default:
				break;
		}
		Object resultData = data;
		if(StringUtils.isNotBlank(data)){
			resultData = JSON.parse(data);
		}
		return new ObjectRestResponse<>().rel(true).result(resultData);
	}

	@GetMapping("/view/{id}")
	public ObjectRestResponse viewData(@PathVariable("id") Long id) {
		AnnualResultDto annualResultDto = annualResultService.findById(id);
		JSONObject json = new JSONObject();
		if(annualResultDto != null){
			json.put("core",JSON.parse(annualResultDto.getCoreData()));
			json.put("pbc",JSON.parse(annualResultDto.getPbcData()));
			json.put("saic",JSON.parse(annualResultDto.getSaicData()));
		}
		log.info(json.toJSONString());
		return new ObjectRestResponse<>().rel(true).result(json);
	}

	@GetMapping("/compareResult/{id}")
	public ObjectRestResponse getCompareResult(@PathVariable("id") Long id) {
		AnnualResultDto annualResultDto = annualResultService.findById(id);
		List<CompareFieldsDto> dtos = compareFieldsService.listCompareRulesByTakId(annualResultDto.getTaskId());
		Map<String, CompareDataDto> dataMap = compareService.getCompareDatas(annualResultDto);
		JSONArray array = new JSONArray(3);
		JSONObject jsonObject = null;
		for (CompareFieldsDto dto : dtos) {
			if (!dto.isActive()) {
				continue;
			}
			jsonObject = new JSONObject();
			jsonObject.put("name", dto.getCompareFieldEnum().getValue());
			for (DataSourceEnum dataSourceEnum : DataSourceEnum.values()) {
				String fieldName = dto.getCompareFieldEnum().getField();
				Object obj = BeanValueUtils.getValue(dataMap.get(dataSourceEnum.name()), dto.getCompareFieldEnum().getField());
				if (obj != null) {
					if (dataSourceEnum == DataSourceEnum.CORE) {
						if ("accountStatus".equals(fieldName)) {
							obj = com.ideatech.ams.account.enums.AccountStatus.str2enum(obj.toString()).getFullName();
						} else if ("acctType".equals(fieldName)) {
							obj = CompanyAcctType.str2enum(obj.toString()).getValue();
						}
					} else if (dataSourceEnum == DataSourceEnum.PBC) {
						if (obj instanceof AccountStatus) {
							obj = ((AccountStatus) obj).getFullName();
						} else if (obj instanceof AccountType) {
							obj = ((AccountType) obj).getFullName();
						} else if ("accountStatus".equals(fieldName)) {
							obj = com.ideatech.ams.account.enums.AccountStatus.str2enum(obj.toString()).getFullName();
						}
					}
				}
				jsonObject.put(dataSourceEnum.name(), obj);
			}
			if (StringUtils.isNotBlank(annualResultDto.getCompareResult())) {
				jsonObject.put("match", JSON.parseObject(annualResultDto.getCompareResult(), Map.class).get(dto.getCompareFieldEnum().name()));
			}

			jsonObject.put("annualResultId", id);
			array.add(jsonObject);
		}
		return new ObjectRestResponse<String>().rel(true).result(array);
	}


	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.EXPORT,operateContent = "年检统计导出年检结果")
	@GetMapping("/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request) throws Exception {
		Long organId = SecurityUtils.getCurrentUser().getOrgId();
		OrganizationDto organizationDto = organizationService.findById(organId);

		List<OrganizationDto> organizationDtos = organizationService.listDescendant(organId);
		Long taskId = annualTaskService.getAnnualCompareTaskId();

		List<File> fileList = new ArrayList<>(organizationDtos.size());
		for (OrganizationDto organization : organizationDtos) {
			File file = new File(
					annualExportFolder + File.separator + taskId + File.separator + organization.getCode() + ".zip");
//			File fileParent = file.getParentFile();
//			//判断是否存在
//			if (!fileParent.exists()) {
//				fileParent.mkdirs();
//			}
//			file.createNewFile();

			if (file.exists()) {
			    if (file.length()>0){
                    fileList.add(file);
                }
			}
		}
		File zipFile = new File(
				annualExportFolder + File.separator + "zip" + File.separator + organizationDto.getCode() + ".zip");
//		zipFile.getParentFile().mkdirs();
		File fileParent = zipFile.getParentFile();
		//判断是否存在
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		zipFile.createNewFile();
		//压缩所有机构数据
		ZipUtils.zipFiles(fileList, zipFile, "");
		HttpHeaders headers = new HttpHeaders();
		String fileName = null;
		try {
			fileName = FileExtraUtils.handleFileName(request, zipFile.getName());
		} catch (UnsupportedEncodingException e) {
			//ignore
			log.warn("文件名处理出错", e);
		}
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] fileByte = FileUtils.readFileToByteArray(zipFile);
		FileUtils.deleteQuietly(zipFile);
		return new ResponseEntity<>(fileByte, headers, HttpStatus.OK);
	}

	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.EXPORT,operateContent = "年检统计导出历史年检结果")
	@GetMapping("/downloadHis")
	public ResponseEntity<byte[]> downloadHis(HttpServletRequest request,Long hisTaskId) throws Exception {
		Long organId = SecurityUtils.getCurrentUser().getOrgId();
		OrganizationDto organizationDto = organizationService.findById(organId);

		List<OrganizationDto> organizationDtos = organizationService.listDescendant(organId);
//		Long taskId = annualTaskService.getAnnualCompareTaskId();
		Long taskId = hisTaskId;

		List<File> fileList = new ArrayList<>(organizationDtos.size());
		for (OrganizationDto organization : organizationDtos) {
			File file = new File(
					annualExportFolder + File.separator + taskId + File.separator + organization.getCode() + ".zip");
//			File fileParent = file.getParentFile();
//			//判断是否存在
//			if (!fileParent.exists()) {
//				fileParent.mkdirs();
//			}
//			file.createNewFile();

			if (file.exists()) {
				if (file.length()>0){
					fileList.add(file);
				}
			}
		}
		File zipFile = new File(
				annualExportFolder + File.separator + "zip" + File.separator + organizationDto.getCode() + ".zip");
//		zipFile.getParentFile().mkdirs();
		File fileParent = zipFile.getParentFile();
		//判断是否存在
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		zipFile.createNewFile();
		//压缩所有机构数据
		ZipUtils.zipFiles(fileList, zipFile, "");
		HttpHeaders headers = new HttpHeaders();
		String fileName = null;
		try {
			fileName = FileExtraUtils.handleFileName(request, zipFile.getName());
		} catch (UnsupportedEncodingException e) {
			//ignore
			log.warn("文件名处理出错", e);
		}
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] fileByte = FileUtils.readFileToByteArray(zipFile);
		FileUtils.deleteQuietly(zipFile);
		return new ResponseEntity<>(fileByte, headers, HttpStatus.OK);
	}

	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.DELETE,operateContent = "年检结果批量删除")
	@PostMapping("/batchDelete")
	public ResultDto batchDelete(@RequestParam("ids[]") Long[] ids) {
		annualResultService.batchDelete(ids);

		return ResultDtoFactory.toAck();
	}

	@OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.UPDATE,operateContent = "年检账户-无需年检")
	@PostMapping("/noCheckAnnual")
	public ResultDto noCheckAnnual(@RequestParam("ids[]") Long[] ids) {
		ResultDto resultDto = new ResultDto();
		String msg = annualResultService.noCheckAnnual(ids);
		resultDto.setMessage(msg);
		return resultDto;
	}

}
