package com.ideatech.ams.controller.apply;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.apply.dto.ApplyOcrDto;
import com.ideatech.ams.apply.dto.ApplyOcrResultDto;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntSaicDto;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.enums.ApplyOcrDoccodeType;
import com.ideatech.ams.apply.service.ApplyOcrService;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.image.dto.ImageInfo;
import com.ideatech.ams.image.enums.IsUpload;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
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

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/apply")
@Slf4j
public class BankController {
	
	@Autowired
	private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

	@Autowired
	private ApplyOcrService applyOcrService;

	@Value("${apply.images.path}")
	private String applyImagesPath;

	/**
	 * 预约新接口模式是否启用：默认false不启用
	 */
	@Value("${apply.newRule.flag:false}")
	private Boolean applyNewRuleFlag;

	@Autowired
	private TemplateService templateService;

	@RequestMapping(value = "/saic",method = RequestMethod.GET)
	public String saic(){
		return "ui/account/saic";
	}

	/**
	 * 受理相关列表
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/bank/list", method = RequestMethod.GET)
	public TableResultResponse<CompanyPreOpenAccountEntDto> pre(CompanyPreOpenAccountEntDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
		TableResultResponse<CompanyPreOpenAccountEntDto> tableResultResponse =  companyPreOpenAccountEntService.query(dto, pageable);
		
		return tableResultResponse;
	}
	
	/**
	 * 获取受理列表数量
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/bank/count", method = RequestMethod.GET)
	public Object getCount(CompanyPreOpenAccountEntDto dto) {
		return companyPreOpenAccountEntService.getCount(dto);
		
	}

	/**
	 * 预约信息受理
	 * @param ent
	 * @return
	 */
	@RequestMapping(value = "/bank/edit", method = RequestMethod.POST)
	public ObjectRestResponse<CompanyPreOpenAccountEntDto> save(CompanyPreOpenAccountEntDto ent,CompanyPreOpenAccountEntSaicDto entSaic) {
		if (StringUtils.isBlank(ent.getApplyid())){
			return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(false);
		}

		companyPreOpenAccountEntService.edit(ent,entSaic);

		return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(true);
	}


	/**
	 * 状态修改
	 * @param applyid
	 * @return
	 */
	@RequestMapping(value = "/bank/state/success", method = RequestMethod.POST)
	public ObjectRestResponse<CompanyPreOpenAccountEntDto> state(String applyid,String status) {
		CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.findOne(applyid).getResult();
		if(result != null && ApplyEnum.SUCCESS.getValue().equals(result.getStatus())){
			if(ApplyEnum.REGISTER_SUCCESS.getValue().equals(status) ||ApplyEnum.REGISTER_FAIL.getValue().equals(status) ){
				companyPreOpenAccountEntService.stateUpdate(result,status);
				return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(true);
			}
		}
		return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(false).msg("预约状态不是受理成功");
	}

	/**
	 * 全数据同步按钮
	 * @return
	 */
	@RequestMapping(value = "/bank/all/sync", method = RequestMethod.POST)
	public ObjectRestResponse<Object> allSync(){
		String result = "";

		if(!applyNewRuleFlag) {
			result = companyPreOpenAccountEntService.getApplyRecordByPullId(0l);
		} else {  //预约接口新模式,根据日期区间取数
			result = companyPreOpenAccountEntService.getApplyRecordByPullTime("2000-01-01 00:00:00", "2099-01-01 00:00:00", "allSync");
		}

		if(result == null){
			return new ObjectRestResponse<Object>().rel(true);
		}else{
			return new ObjectRestResponse<Object>().rel(false).msg(result);
		}
	}


	@RequestMapping(value = "/bank/images/{id}", method = RequestMethod.POST)
	public ObjectRestResponse<Object> applyImages(@PathVariable("id")Long id,Integer reqPageNum,String transNoType){
		if(reqPageNum ==null || reqPageNum<1){
			reqPageNum = 1;
		}
		if(StringUtils.isBlank(transNoType)){
			transNoType = "ezhanghu";
		}
		try{
			CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = companyPreOpenAccountEntService.selectOne(id);
			String applyId = companyPreOpenAccountEntDto.getApplyid();
			if(companyPreOpenAccountEntDto != null && !StringUtils.equals("0",companyPreOpenAccountEntDto.getHasocr())){//确定有影像
				if(StringUtils.equals("1",companyPreOpenAccountEntDto.getHasocr())){//有影像，未同步或者未同步完全
					Integer curNum = companyPreOpenAccountEntDto.getCurNum();
					Integer totalNum = companyPreOpenAccountEntDto.getTotalNum();
					if((totalNum == null || totalNum ==0 ) && (curNum == null ||curNum ==0 )){//从未同步过影像
						if(reqPageNum == 1){//从首张开始同步
							companyPreOpenAccountEntService.getApplyImagesByApplyIdAndPageNum(applyId, reqPageNum, transNoType);
						}else{//不是从首张开始同步
							return new ObjectRestResponse<Object>().rel(false).msg("未同步过影像，请从首张影像开始同步");
						}
					}else if(reqPageNum > totalNum){//超过最大的影像数量
						return new ObjectRestResponse<Object>().rel(false).msg("超过最大影像数量");
					}else if(reqPageNum>curNum){//超过当前下载的数量
						companyPreOpenAccountEntService.getApplyImagesByApplyIdAndPageNum(applyId, reqPageNum, transNoType);
					}/*else{//未超过当前下载的数量，直接拿文件

					}*/
				}
				List<ApplyOcrDto> applyOcrDtos = applyOcrService.findByApplyidOrderByCurNumAsc(applyId);
				if(applyOcrDtos.size()>0){//已经有下载的影像
					ApplyOcrResultDto applyOcrResultDto = new ApplyOcrResultDto();
					CompanyPreOpenAccountEntDto companyPreOpenAccountEntDtoNew = companyPreOpenAccountEntService.selectOne(id);
					applyOcrResultDto.setTotalNum(companyPreOpenAccountEntDtoNew.getTotalNum());
					applyOcrResultDto.setCurNum(reqPageNum);
					ArrayList<String> fileNames = new ArrayList<>();
					for (ApplyOcrDto applyOcrDto:applyOcrDtos){
						applyOcrResultDto.setSyncNum(applyOcrDto.getCurNum());
						fileNames.add(ApplyOcrDoccodeType.getDisplayName(applyOcrDto.getDoccode()));
					}
					applyOcrResultDto.setFileNames(fileNames);
					if("1".equals(companyPreOpenAccountEntDtoNew.getHasocr())&& companyPreOpenAccountEntDtoNew.getCurNum() == companyPreOpenAccountEntDtoNew.getTotalNum()){//判断是否下完全
						companyPreOpenAccountEntService.updateHasocr(id);
					}
					return new ObjectRestResponse<Object>().rel(true).result(applyOcrResultDto);
				}else{//未下载
					return new ObjectRestResponse<Object>().rel(false).msg("影像同步未成功，请稍后再试");
				}
//				ApplyOcrDto applyOcrDto = applyOcrService.findByApplyidAndCurNum(applyId, reqPageNum);
			}else{
				return new ObjectRestResponse<Object>().rel(false).msg("没有预约影像数据");
			}
		}catch (Exception e){
			return new ObjectRestResponse<Object>().rel(false).msg(e.getMessage());
		}
	}


	@RequestMapping(value = "/bank/images/downLoadImage/{id}",method = RequestMethod.GET)
	public void downLoadImage(@PathVariable("id")Long id,Integer reqPageNum,String transNoType, HttpServletResponse response){
		InputStream inputStream = null;
		OutputStream outputStream = null;
		CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = companyPreOpenAccountEntService.selectOne(id);
		ApplyOcrDto applyOcrDto = applyOcrService.findByApplyidAndCurNum(companyPreOpenAccountEntDto.getApplyid(), reqPageNum);
		try {
			inputStream = new FileInputStream(applyImagesPath+ File.separator+companyPreOpenAccountEntDto.getApplyid()+File.separator+applyOcrDto.getFilename());
			outputStream = response.getOutputStream();
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=" + applyOcrDto.getFilename());
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ObjectRestResponse<CompanyPreOpenAccountEntDto> getDetails(Long id) {
		CompanyPreOpenAccountEntDto result = companyPreOpenAccountEntService.selectOne(id);
		return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().result(result).rel(true);
	}


	@RequestMapping(value = "/bank/view", method = RequestMethod.GET)
	public ObjectRestResponse<CompanyPreOpenAccountEntSaicDto> view(String applyId) {
		if(StringUtils.isBlank(applyId)){
	        log.info("预约号为空");
	        return new ObjectRestResponse<CompanyPreOpenAccountEntSaicDto>().rel(false).msg("预约号为空");
	      } else {
	    	  CompanyPreOpenAccountEntSaicDto saicDto = companyPreOpenAccountEntService.selectOneSaic(applyId);
	  		return new ObjectRestResponse<CompanyPreOpenAccountEntSaicDto>().result(saicDto).rel(true);
	      }
	}

	/**getCounts
	 * 打印预览
	 * @param billType
	 * @param depositorType
	 * @return
	 */
	@GetMapping("/getPrintPreview")
	public ResponseEntity<byte[]> getTemplateNameList(String applyId, BillType billType, DepositorType depositorType, String templateName) throws Exception {
		Map<String, Object> mapAll = new HashMap<>();

		CompanyPreOpenAccountEntDto preAccount = companyPreOpenAccountEntService.findByApplyid(applyId);
		CompanyPreOpenAccountEntSaicDto accountEntSaicDto = companyPreOpenAccountEntService.selectOneSaic(applyId);

		for (Field f : preAccount.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (f.getType() == String.class && f.get(preAccount) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
				f.set(preAccount, "");
			}
		}

		Map<String, String> map1 = BeanUtils.describe(preAccount);
		mapAll.putAll(map1);

		if(accountEntSaicDto != null) {
			companyPreOpenAccountEntService.conversion(accountEntSaicDto);

			for (Field f : accountEntSaicDto.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.getType() == String.class && f.get(accountEntSaicDto) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
					f.set(accountEntSaicDto, "");
				}
			}

			Map<String, String> map2 = BeanUtils.describe(accountEntSaicDto);
			mapAll.putAll(map2);
		}

		TemplateDto byId = templateService.findByBillTypeAndDepositorTypeAndTemplateName(billType, depositorType, templateName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));

		return new ResponseEntity<byte[]>(PdfGenerator.generate(byId.getTemplaeContent(), mapAll), headers, HttpStatus.OK);
	}

}
