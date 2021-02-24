package com.ideatech.ams.apply.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ideatech.ams.apply.cryptography.*;
import com.ideatech.ams.apply.dao.CompanyPreOpenAccountEntDao;
import com.ideatech.ams.apply.dao.CompanyPreOpenAccountEntSaicDao;
import com.ideatech.ams.apply.dao.EzhMessageDao;
import com.ideatech.ams.apply.dao.spec.CompanyPreOpenAccountEntSpec;
import com.ideatech.ams.apply.dto.*;
import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEnt;
import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEntSaic;
import com.ideatech.ams.apply.entity.EzhMessage;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.util.IdUtil;
import com.ideatech.ams.apply.util.MsgFormat;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class CompanyPreOpenAccountEntServiceImpl implements CompanyPreOpenAccountEntService {

	/**
	 * 拉取预约信息-根据ID游标
	 * (获取批量预约数据接口地址)
	 */
    @Value("${apply.url.pullId}")
    private String applyPullId;

	/**
	 * 拉取预约信息-根据日期区间
	 * (获取批量预约数据新接口地址)
	 */
	@Value("${apply.url.pullTime}")
	private String applyPullTime;

	/**
	 * 预约新接口模式是否启用：默认false不启用
	 */
	@Value("${apply.newRule.flag:false}")
	private Boolean applyNewRuleFlag;

	/**
	 * 拉取预约信息-根据applyId
	 * (获取单条预约数据接口地址)
	 */
	@Value("${apply.url.pullApplyId}")
    private String pullApplyId;

    @Value("${apply.url.images}")
    private String applyImages;

    @Value("${apply.images.path}")
    private String applyImagesPath;

    @Value("${apply.url.modilyStatus}")
    private String applyModilyStatus;

	@Value("${apply.url.newModilyStatus}")
	private String newApplyModilyStatus;

	@Value("${apply.url.addApplyAcctUrl}")
	private String addApplyAcctUrl;

	@Value("${apply.url.editApplyAcctUrl}")
	private String editApplyAcctUrl;

	@Value("${apply.url.addAcctKeyUrl}")
	private String addAcctKeyUrl;

    @Value("${ams.preOpenAccount.message}")
	private String smsMessage;

	@Value("${ams.preOpenAccount.failMessage}")
	private String smsFailMessage;

	@Value("${ams.preOpenAccount.clerkMessage: 柜员短信模版}")
	private String clerkMessage;

	@Value("${apply.task.scheduletiming.flag}")
	private Boolean flag;

	@Value("${apply.task.regainApplyNum}")
	private int regainApplyNum;//重新获取预约数据的测试

	private Map<String, Integer> applyIdMap = new HashMap<>();//取预约数据时，没有影像的预约编号集合，在下一次定时去取预约数据时，重新获取数据，并进行数据更新

    @Autowired
    private HttpRequest httpRequest;
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;
    
	@Autowired
	private CompanyPreOpenAccountEntDao companyPreOpenAccountEntDao;

	@Autowired
	private CompanyPreOpenAccountEntSaicDao companyPreOpenAccountEntSaicDao;

	@Autowired
	private EzhMessageDao ezhMessageDao;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private SmsService smsService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private RSACode rsaCode;

	@Autowired
	private EzhMessageService ezhMessageService;

	@Autowired
	private ApplyOcrService applyOcrService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public String saveApply(CompanyPreOpenAccountEntDto dto) {
		String applyId = "";
		CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();

		dto.setCreatedDate(new Date());
		dto.setHasocr("0");
		dto.setStatus(ApplyEnum.UnComplete.getValue());
		//AuthUtil.bankOrg(entity);
		if(StringUtils.isBlank(dto.getApplyid())) {
			applyId = "APPLY" + new IdUtil(0, 0).nextId();
			// todo 可能会出现重复数据，需要检查数据库中applyid的唯一性
			dto.setApplyid(applyId);
		}

		BeanCopierUtils.copyProperties(dto, companyPreOpenAccountEnt);
		companyPreOpenAccountEntDao.save(companyPreOpenAccountEnt);

		return applyId;
	}

	@Override
	public TableResultResponse<CompanyPreOpenAccountEntDto> query(CompanyPreOpenAccountEntDto dto, Pageable pageable) {
		List<String> statuses = new ArrayList<>();

		if("BREAK_APPOINT".equalsIgnoreCase(dto.getStatus())) { //爽约时增加预约日期判断
			if (StringUtils.isBlank(dto.getBeginDate())) {
				dto.setBeginDate("00000000");
			}
			if(StringUtils.isBlank(dto.getEndDate())) {
				dto.setEndDate("99999999");
			}
		}
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());

		if(StringUtils.isNotBlank(dto.getStatus())) {
			if ("UnComplete".equals(dto.getStatus())) {   //待受理
				dto.setStatus(ApplyEnum.UnComplete.getValue());
			} else if ("Complete".equals(dto.getStatus())) {   //已受理
				statuses.add(ApplyEnum.SUCCESS.getValue());
				statuses.add(ApplyEnum.FAIL.getValue());
				dto.setStatuses(statuses);
				dto.setStatus("");
			} else if ("SUCCESS".equals(dto.getStatus())) {    //受理成功
				dto.setStatus(ApplyEnum.SUCCESS.getValue());
			} else if ("FAIL".equals(dto.getStatus())) {        //受理失败
				dto.setStatus(ApplyEnum.FAIL.getValue());
			}  else if ("REGISTER_SUCCESS".equals(dto.getStatus())) {        //开户成功
				dto.setStatus(ApplyEnum.REGISTER_SUCCESS.getValue());
			}  else if ("REGISTER_FAIL".equals(dto.getStatus())) {        //开户失败
				dto.setStatus(ApplyEnum.REGISTER_FAIL.getValue());
			}  else if ("BREAK_APPOINT".equals(dto.getStatus())) {        //爽约
				dto.setStatus(ApplyEnum.BREAK_APPOINT.getValue());
			}
		}

		Page<CompanyPreOpenAccountEnt> page = companyPreOpenAccountEntDao.findAll(new CompanyPreOpenAccountEntSpec(dto), pageable);
		List<CompanyPreOpenAccountEnt> openAccountList = page.getContent();
		long count = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		List<CompanyPreOpenAccountEntDto> openAccountDtoList = new ArrayList<>();
		CompanyPreOpenAccountEntDto openAccountEntDto = null;

		if(openAccountList != null && openAccountList.size() > 0) {
			for(CompanyPreOpenAccountEnt openAccount : openAccountList) {


				openAccountEntDto = new CompanyPreOpenAccountEntDto();
				BeanCopierUtils.copyProperties(openAccount, openAccountEntDto);
				openAccountDtoList.add(openAccountEntDto);
			}
		}

		return new TableResultResponse<>((int)count, openAccountDtoList);
	}

	@Override
	public Object getCount(CompanyPreOpenAccountEntDto dto) {
		List<String> statuses = new ArrayList<>();

		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.UnComplete.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long unCompleteCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		dto = new CompanyPreOpenAccountEntDto();
		statuses.add(ApplyEnum.SUCCESS.getValue());
		statuses.add(ApplyEnum.FAIL.getValue());
		dto.setStatuses(statuses);
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long count = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.SUCCESS.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long successCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.FAIL.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long failCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.REGISTER_SUCCESS.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long registerSuccessCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));


		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.REGISTER_FAIL.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long registerFailCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		dto = new CompanyPreOpenAccountEntDto();
		dto.setStatus(ApplyEnum.BREAK_APPOINT.getValue());
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		long breakAppointCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		return unCompleteCount + "," + count + "," + successCount + "," + failCount+ "," + registerSuccessCount+ "," + registerFailCount+ "," + breakAppointCount ;
	}

	@Override
	public List<CompanyPreOpenAccountEntDto> query(CompanyPreOpenAccountEntDto dto) {
		dto.setOrganfullid(SecurityUtils.getCurrentOrgFullId());
		List<CompanyPreOpenAccountEnt> list = companyPreOpenAccountEntDao.findAll(new CompanyPreOpenAccountEntSpec(dto));
		List<CompanyPreOpenAccountEntDto> openAccountDtoList = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (CompanyPreOpenAccountEnt openAccount : list) {
				CompanyPreOpenAccountEntDto openAccountEntDto = new CompanyPreOpenAccountEntDto();
				BeanCopierUtils.copyProperties(openAccount, openAccountEntDto);
				openAccountDtoList.add(openAccountEntDto);
			}
		}
		return openAccountDtoList;
	}

	@Override
	public ObjectRestResponse<CompanyPreOpenAccountEntDto> findOne(String applyId) {
		CompanyPreOpenAccountEnt companyPreOpenAccountEnt = companyPreOpenAccountEntDao.findByApplyid(applyId);
		CompanyPreOpenAccountEntDto dto = new CompanyPreOpenAccountEntDto();
		ConverterService.convert(companyPreOpenAccountEnt, dto);

		return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(true).result(dto);
	}

	@Override
	public CompanyPreOpenAccountEntDto findByApplyid(String applyId) {
		return ConverterService.convert(companyPreOpenAccountEntDao.findByApplyid(applyId),CompanyPreOpenAccountEntDto.class);
	}

	@Override
	public void edit(CompanyPreOpenAccountEntDto entDto) {
		CompanyPreOpenAccountEnt temp = new CompanyPreOpenAccountEnt();;
		temp.setApplyid(entDto.getApplyid());

		temp = companyPreOpenAccountEntDao.findByApplyid(temp.getApplyid());
		temp.setApplynote(entDto.getApplynote());
		temp.setBanktime(entDto.getBanktime());
		temp.setTimes(entDto.getTimes());
		temp.setStatus(entDto.getStatus());
		temp.setRegAreaCode(entDto.getRegAreaCode());
		temp.setAccountKey(entDto.getAccountKey());
		temp.setAcctOpenStatus(entDto.getAcctOpenStatus());
		temp.setAccepter(SecurityUtils.getCurrentUsername());
		temp.setAcceptTimes(DateUtils.getNowDateShort("yyyy-MM-dd HH:mm:ss"));

		companyPreOpenAccountEntDao.save(temp);

		BeanUtils.copyProperties(temp,entDto);
//		entDto.setId(temp.getId());
	}

    @Override
    public void update(CompanyPreOpenAccountEntDto entDto) {
		CompanyPreOpenAccountEnt temp = new CompanyPreOpenAccountEnt();;
		temp.setApplyid(entDto.getApplyid());

		temp = companyPreOpenAccountEntDao.findByApplyid(temp.getApplyid());
		BeanCopierUtils.copyProperties(entDto, temp);

        companyPreOpenAccountEntDao.save(temp);

    }

	@Override
	public CompanyPreOpenAccountEntDto selectOne(Long id) {
		CompanyPreOpenAccountEnt ent = companyPreOpenAccountEntDao.findOne(id);
		CompanyPreOpenAccountEntDto dto = new CompanyPreOpenAccountEntDto();
		BeanCopierUtils.copyProperties(ent, dto);

		return dto;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void saveMessage(CompanyPreOpenAccountEntDto temp) {
		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = transactionManager.getTransaction(definition);

		String applyMessage = "";
		String clerkMessageFormat = "";
		Boolean checkPass = true;
		String errorMessage = "";
		String returnValue = "";

		OrganizationPo organization = organizationDao.findByFullId(SecurityUtils.getCurrentOrgFullId());

		if (StringUtils.isNotBlank(temp.getPhone())
				&& temp.getStatus().equals(ApplyEnum.SUCCESS.getValue())) {
			String message =
					MsgFormat.formatSuccess(temp.getApplyid(), temp.getBanktime(), temp.getTimes(),
							temp.getBank() + temp.getBranch());
			// 预约人成功短信通知
			applyMessage = MsgFormat.formatMessage(smsMessage, temp);

			try {
				returnValue = smsService.sendMessage(temp.getPhone(), MsgFormat.formatMessage(smsMessage, temp));
			} catch (Exception e) {
				errorMessage = e.getMessage();
				checkPass = false;
				log.error("预约成功相关发送短信异常", errorMessage);
			}

			EzhMessage ezhMessage = new EzhMessage(temp.getPhone(), applyMessage,"1", checkPass, errorMessage, returnValue);
			ezhMessage.setApplyId(temp.getApplyid());
			ezhMessage.setCheckPass(checkPass);
			ezhMessageDao.save(ezhMessage);

		}

		if (StringUtils.isNotBlank(temp.getPhone())
				&& temp.getStatus().equals(ApplyEnum.FAIL.getValue())) {
			String message =
					MsgFormat.formatFail(temp.getApplyid());
			// 预约人退回短信通知
			applyMessage = MsgFormat.formatMessage(smsFailMessage, temp);

			try {
				returnValue = smsService.sendMessage(temp.getPhone(),MsgFormat.formatMessage(smsFailMessage,temp));
			} catch (Exception e) {
				errorMessage = e.getMessage();
				checkPass = false;
				log.error("预约失败相关发送短信异常", errorMessage);
			}

			EzhMessage ezhMessage = new EzhMessage(temp.getPhone(), applyMessage,"1", checkPass, errorMessage, returnValue);
			ezhMessage.setApplyId(temp.getApplyid());
			ezhMessage.setCheckPass(checkPass);
			ezhMessageDao.save(ezhMessage);

		}

		transactionManager.commit(transaction);
	}

	/**
	 * 根据预约号修改为指定开户状态
	 * @param applyId
	 * @param acctOpenStatus
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public void editModifyStatus(String applyId, String acctOpenStatus) {
		CompanyPreOpenAccountEnt preOpenAccount = companyPreOpenAccountEntDao.findByApplyid(applyId);

		if(preOpenAccount != null) {
            try {
				if(!applyNewRuleFlag) {
					sendModifyStatus(preOpenAccount.getId(), acctOpenStatus);
				} else {
					sendModifyStatus(preOpenAccount.getApplyid(), ApplyEnum.getEnum(acctOpenStatus), "", "");
				}

                preOpenAccount.setAcctOpenStatus(acctOpenStatus);
                companyPreOpenAccountEntDao.save(preOpenAccount);
            } catch (Exception e) {
                log.error("改变状态失败");
                e.printStackTrace();
            }
        } else {
            log.error("预约记录为空");
        }

	}

	/**
	 * 重新获取有上次定时任务中没有影像的预约数据
	 */
	@Override
	public void getApplyRecordByLastDubiousApplyIds() {
 		String applyIds = StringUtils.join(applyIdMap.keySet(),",");
		CryptoInput cryptoInput = new CryptoInput();
		cryptoInput.setRawBizContext("{\"applyids\":\"" + applyIds + "\"}");
//		cryptoInput.setRawBizContext("{\"applyids\":\"2018083009315513,2018092016257692,2019062813080038,2018071220193216,2018092610015050,2018071219208866,2018071310321805,2018100911206460\"}");
		cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		CryptoOutput cryptoOutput =new CryptoOutput();
		String code=null;
		try {
			code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput,pullApplyId);
		}catch(RuntimeException e) {
			log.error("调用状态接口，远程访问失败,"+e.getMessage()+","+applyIds);
		}
		if(StringUtils.isBlank(code) || !"000000".equals(code) || StringUtils.isEmpty(cryptoOutput.getRawBizContext())) {
			log.info("解密易账户调用的结果为空，无插入结果");
		}else {
			ObjectMapper mapper = new ObjectMapper();
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, CryptoPullVo.class);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CryptoPullVo> response;
			try {
				response = (List<CryptoPullVo>) mapper.readValue(cryptoOutput.getRawBizContext(), collectionType);
				Map<String, Integer> applyIdNewMap = new HashMap<>();
				for (CryptoPullVo cpv : response) {
					if (cpv.getHasocr() == null || cpv.getHasocr().equals("0")) {
						String applyid = cpv.getApplyid();
						int num = applyIdMap.get(applyid) + 1;
						if (num <= regainApplyNum) {//没有超过最大重新取值的次数时，次数+1
							applyIdNewMap.put(applyid, num);
						} else {
							log.info("预约编号为" + applyid + "超过最大重新取值的次数" + regainApplyNum + "，将不在下一次定时任务中再次取值");
						}
					}
				}
				applyIdMap = applyIdNewMap;
				insertBatch(response);
			} catch (IOException e) {
				log.error("插入预约数据有误,序列化结果出错,"+e.getMessage());
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String getApplyRecordByPullId(Long maxId) {
		CryptoPullIdMessage cryptoMessage = new CryptoPullIdMessage();
		cryptoMessage.setMinId(maxId);
		cryptoMessage.setOrganids(getPbcCodeList());

		CryptoInput cryptoInput = new CryptoInput();
        cryptoInput.setRawBizContext(JSON.toJSONString(cryptoMessage));
        cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
        CryptoOutput cryptoOutput =new CryptoOutput();
        String code=null;
        try {
            code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput, applyPullId);
        }catch(RuntimeException e) {
        	log.error("调用状态接口，远程访问失败,"+e.getMessage()+","+cryptoMessage);
        	return "远程调用接口失败";
        }

		return getApplyPullResponse(code, cryptoOutput);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String getApplyRecordByPullTime(String startDateTime, String endDateTime, String operateType) {
		if(StringUtils.isBlank(startDateTime) || StringUtils.isBlank(endDateTime)) {
			log.error("定时根据日期区间拉取预约信息的开始日期和结束日期不能为空");
			return "";
		}

		CryptoPullTimeMessage cryptoMessage = new CryptoPullTimeMessage();
		cryptoMessage.setOrganIds(getPbcCodeList());
		cryptoMessage.setStartDateTime(startDateTime);
		cryptoMessage.setEndDateTime(endDateTime);

		CryptoInput cryptoInput = new CryptoInput();
		cryptoInput.setRawBizContext(JSON.toJSONString(cryptoMessage));
		cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		CryptoOutput cryptoOutput =new CryptoOutput();
		String code=null;
		try {
			code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput, applyPullTime);
		}catch(RuntimeException e) {
			log.error("调用状态接口，远程访问失败,"+e.getMessage()+","+cryptoMessage);
			return "远程调用接口失败";
		}

		return getApplyPullTimeResponse(code, cryptoOutput, operateType);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Integer getApplyImagesByApplyIdAndPageNum(String openAcctTransNo, Integer reqPageNum,String transNoType) throws EacException {
		CryptoImagesMessage cryptoMessage = new CryptoImagesMessage();
		cryptoMessage.setOpenAcctTransNo(openAcctTransNo);
		cryptoMessage.setReqPageNum(reqPageNum);
		cryptoMessage.setTransNoType(transNoType);
        CryptoInput cryptoInput = new CryptoInput();
        cryptoInput.setRawBizContext(JSON.toJSONString(cryptoMessage));
        cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
        CryptoOutput cryptoOutput =new CryptoOutput();
        String code=null;
        Integer totalNum = 0;
        try {
            code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput,applyImages);
        }catch(RuntimeException e) {
        	log.error("调用影像接口，远程访问失败,"+e.getMessage()+","+cryptoMessage);
        	throw new EacException("远程调用接口失败");
        }
        if(StringUtils.isBlank(code) || !"000000".equals(code) || StringUtils.isEmpty(cryptoOutput.getRawBizContext())) {
            log.info("解密易账户调用的结果为空，无插入结果");
        }else {
            ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CryptoImagesVo response;
			try {
				response = (CryptoImagesVo)mapper.readValue(cryptoOutput.getRawBizContext(), CryptoImagesVo.class);
				if(response != null && response.getTotalNum() >0){
					doApplyImages(response);
					totalNum = response.getTotalNum();
				}else{
					log.info("预约影响资料转化结果或者影像为空");
					throw new EacException("预约影响资料转化结果或者影像为空");
				}
			} catch (Exception e) {
	            log.error("预约影像处理有误,序列化结果出错,"+e.getMessage());
				throw new EacException("预约数据同步失败");
			}
        }
        return totalNum;
	}


    @EnableSendCryptography
    public String send(CryptoInput cryptoInput, CryptoOutput cryptoOutput,String url){
    	RestTemplate restTemplate = httpRequest.getRestTemplate(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        CryptoVo cryptoVo = new CryptoVo();
        BeanUtils.copyProperties(cryptoInput, cryptoVo);

        log.info("发送请求参数[{}]",JSON.toJSONString(cryptoVo));
        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(cryptoVo), headers);
        httpRequest.checkRestTemplate(restTemplate);
        ObjectRestResponse<CryptoVo> response = null;
        try {
        	response = restTemplate.postForObject(url, request, ObjectRestResponse.class);
        }catch(RuntimeException e) {
            log.error("调用易账户的预约记录异常，"+e.getMessage());
            throw new EacException("调用易账户的预约记录异常,"+e.getMessage());
        }

        if (response == null) {
            log.error("发送消息异常,返回为NULL");
            throw new EacException("发送消息异常");
        }
        Object result = response.getResult();
		String code = response.getCode();
		if(result !=null && ("000000".equals(code) || "000407".equals(code))) {  //000407---创建预约信息成功
            ObjectMapper mapper = new ObjectMapper();
            CryptoVo cryptoResult = mapper.convertValue(result, CryptoVo.class);
            BeanUtils.copyProperties(cryptoResult, cryptoOutput);
        } else {
            log.info("返回的结果消息："+response.getMsg()+",返回的Code为"+response.getCode());
        }
        return code;
    }
    
	@Override
	public Long getMaxId() {
		String maxId = companyPreOpenAccountEntDao.getMaxId();
		if(maxId == null){
			return Long.valueOf(0L);
		}else{
			return Long.valueOf(maxId);
		}
	}


 	/**
	 * 预约的影像资料判断/比对/保存
	 * @param response
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void doApplyImages(CryptoImagesVo response) {
		String filename = response.getFilename();
		String imageBase64 = response.getImageBase64();
		String applyid = response.getApplyid();
		String doccode = response.getDoccode();
		Integer curNum = response.getCurNum();
		Long id = response.getId();
		if(StringUtils.isNotBlank(filename) && StringUtils.isNotBlank(doccode) && StringUtils.isNotBlank(imageBase64) && StringUtils.isNotBlank(applyid) && id != null && id >0){//有图片
			log.info("预约记录{}:收到对应的{}/{}的影像记录,进行比对和保存",response.getApplyid(),response.getCurNum(),response.getTotalNum());
			Long prefixFilename = id;
			String suffixFilename = StringUtils.substringAfterLast(filename,".");
			if(StringUtils.isNotBlank(suffixFilename)){//文件名判断
				response.setFilename(prefixFilename+"."+suffixFilename);
				ApplyOcrDto applyOcrDto = applyOcrService.findByApplyidAndCurNum(applyid,curNum);
//				ApplyOcrDto applyOcrDto = applyOcrService.findByFilenameAndApplyid(prefixFilename + "." + suffixFilename, applyid);
				if(applyOcrDto == null){//该图片未存在
					log.info("预约记录{}:收到对应的{}/{}的影像记录,未存在，进行保存",response.getApplyid(),response.getCurNum(),response.getTotalNum());
					insertImages(response);
				}else{//该图片已经存在
					log.info("预约记录{}:收到对应的{}/{}的影像记录,已经存在，不做处理",response.getApplyid(),response.getCurNum(),response.getTotalNum());
				}
			}else{
				log.info("预约记录{}:收到对应的{}/{}的影像记录,文件名为空，不做处理",response.getApplyid(),response.getCurNum(),response.getTotalNum());
			}
		}else{
			log.info("预约记录{}:未收到对应的{}/{}的影像记录:",response.getApplyid(),response.getCurNum(),response.getTotalNum(),response);
		}
	}


	/**
	 * 预约影像资料的保存
	 * @param response
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void insertImages(CryptoImagesVo response){
		ApplyOcrDto applyOcrDto = new ApplyOcrDto();
		BeanUtils.copyProperties(response,applyOcrDto);
		applyOcrDto.setId(null);

		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = null;
		transaction = transactionManager.getTransaction(definition);
		try{
			applyOcrService.saveApply(applyOcrDto);
			FileUtils.mkdirs(applyImagesPath+ File.separator+response.getApplyid());
			if(FileUtils.exists(applyImagesPath+ File.separator+response.getApplyid()+File.separator+response.getFilename())){//文件存在
				log.info("预约记录{}:对应的{}/{}的影像记录,影像名字:{}--已经存在，无需重新保存",response.getApplyid(),response.getCurNum(),response.getTotalNum(),response.getFilename());
			}else{
				Base64Utils.decoderBase64File(response.getImageBase64(),applyImagesPath+ File.separator+response.getApplyid()+File.separator+response.getFilename());
				log.info("预约记录{}:对应的{}/{}的影像记录,影像名字:{}--保存成功",response.getApplyid(),response.getCurNum(),response.getTotalNum(),response.getFilename());
			}
			CompanyPreOpenAccountEnt companyPreOpenAccountEnt = companyPreOpenAccountEntDao.findByApplyid(response.getApplyid());
			companyPreOpenAccountEnt.setCurNum(response.getCurNum());
			companyPreOpenAccountEnt.setTotalNum(response.getTotalNum());
			companyPreOpenAccountEntDao.save(companyPreOpenAccountEnt);
			transactionManager.commit(transaction);
		}catch (Exception e){
			if(!transaction.isCompleted()){
				try{
					log.error("预约记录{}:收到对应的{}/{}的影像记录,保存失败,进行回滚:{}",response.getApplyid(),response.getCurNum(),response.getTotalNum(),e.getMessage());
					transactionManager.rollback(transaction);
				}catch (Exception e1){
					log.error("预约记录{}:收到对应的{}/{}的影像记录,保存失败,回滚失败:{}",response.getApplyid(),response.getCurNum(),response.getTotalNum(),e1.getMessage());
				}
				throw new EacException("影像导入失败");
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void insertBatch(List<CryptoPullVo> list) {
		if(list != null && list.size()>0) {
			HashMap<String,OrganizationPo> map = new HashMap<String,OrganizationPo>();
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = null;
			List<CryptoPullVo> errorList = new ArrayList<CryptoPullVo>();
			for (CryptoPullVo cryptoPullResponse : list) {
				if(companyPreOpenAccountEntDao.findById(cryptoPullResponse.getUuid()) == null) {//预约表中没有对应的记录
					CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();
					BeanUtils.copyProperties(cryptoPullResponse, companyPreOpenAccountEnt);
					String organid = companyPreOpenAccountEnt.getOrganid();
					if (!map.containsKey(organid)) {
						List<OrganizationPo> organizationPos = organizationDao.findByPbcCode(organid);
						if (organizationPos.size() == 0) {
							log.error("组织机构对象为空:" + cryptoPullResponse.toString());
							continue;
						} else {
							map.put(organid, organizationPos.get(0));
						}
					}
					transaction = transactionManager.getTransaction(definition);
					try {
						companyPreOpenAccountEnt.setCreatedDate(DateUtils.getDateTime(Long.valueOf(cryptoPullResponse.getEzhCreateddate())));
						companyPreOpenAccountEnt.setApplytime(cryptoPullResponse.getApplytime());
						companyPreOpenAccountEnt.setId(cryptoPullResponse.getUuid());
						companyPreOpenAccountEnt.setOrganfullid(map.get(organid).getFullId());
						companyPreOpenAccountEnt.setOrganid(String.valueOf(map.get(organid).getId()));
						//2018-10-19：同步状态如果为以下的状态，更改为受理成功
						if (companyPreOpenAccountEnt.getStatus().equals("BuLuZhong") || companyPreOpenAccountEnt.getStatus().equals("Complete") || companyPreOpenAccountEnt.getStatus().equals("BANK_PROCESSING")
								|| companyPreOpenAccountEnt.getStatus().equals("BANK_SUCCESS") || companyPreOpenAccountEnt.getStatus().equals("BANK_FAIL")
								|| companyPreOpenAccountEnt.getStatus().equals("TRANSACTION_SUCCESS")) {
							companyPreOpenAccountEnt.setStatus("SUCCESS");
						}
						companyPreOpenAccountEntDao.save(companyPreOpenAccountEnt);
						transactionManager.commit(transaction);
//						transaction = transactionManager.getTransaction(definition);
						CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = new CompanyPreOpenAccountEntDto();
						BeanUtils.copyProperties(companyPreOpenAccountEnt, companyPreOpenAccountEntDto);

						sendMessage(map, organid, companyPreOpenAccountEntDto, cryptoPullResponse);
					} catch (RuntimeException e) {
						log.error("插入预约数据有误" + cryptoPullResponse.toString(), e);
						if (!transaction.isCompleted()) {
							try {
								transactionManager.rollback(transaction);
							} catch (Exception ex) {
								log.error("预约数据的事务回滚有误");
							}
						}
//						transaction = transactionManager.getTransaction(definition);
						errorList.add(cryptoPullResponse);
					}
				}else{//预约表中有对应的记录，比对状态
					CompanyPreOpenAccountEnt companyPreOpenAccountEntInDatabase = companyPreOpenAccountEntDao.findById(cryptoPullResponse.getUuid());
					CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();
					BeanUtils.copyProperties(cryptoPullResponse, companyPreOpenAccountEnt);
					if(companyPreOpenAccountEntInDatabase.getStatus().equals(companyPreOpenAccountEnt.getStatus())){//状态一致，无需插入
						log.info("状态一致，无需插入：{}",companyPreOpenAccountEntInDatabase);
					}else{//状态不一致，进行判断之后插入
						checkApplyStatus(companyPreOpenAccountEntInDatabase,companyPreOpenAccountEnt);
					}
				}
			}
			if(errorList.size()>0){
				ObjectMapper mapper = new ObjectMapper();
				String result ="";
				try {
					result = mapper.writeValueAsString(errorList);
				} catch (JsonProcessingException e) {
					log.error("错误的预约数据转成json有误",e);
				}
				throw new EacException("插入预约数据有误,列表如下："+result);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void insertBatchNew(List<CryptoPullNewVo> list, String operateType) {
		if(list != null && list.size()>0) {
			HashMap<String,OrganizationPo> map = new HashMap<String,OrganizationPo>();
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = null;
			List<CryptoPullNewVo> errorList = new ArrayList<CryptoPullNewVo>();
			for (CryptoPullNewVo cryptoPullResponse : list) {
				if(companyPreOpenAccountEntDao.findByApplyid(cryptoPullResponse.getApplyId()) == null) {//预约表中没有对应的记录
					CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();
					BeanUtils.copyProperties(cryptoPullResponse, companyPreOpenAccountEnt);
					String organid = cryptoPullResponse.getOrganId();
					if (!map.containsKey(organid)) {
						List<OrganizationPo> organizationPos = organizationDao.findByPbcCode(organid);
						if (organizationPos.size() == 0) {
							log.error("组织机构对象为空:" + cryptoPullResponse.toString());
							continue;
						} else {
							map.put(organid, organizationPos.get(0));
						}
					}
					transaction = transactionManager.getTransaction(definition);
					try {
						companyPreOpenAccountEnt.setApplyid(cryptoPullResponse.getApplyId());
						companyPreOpenAccountEnt.setCreatedDate(cryptoPullResponse.getCreateTime());
						companyPreOpenAccountEnt.setApplytime(cryptoPullResponse.getApplyDate());
						companyPreOpenAccountEnt.setApplynote(cryptoPullResponse.getNote());
						companyPreOpenAccountEnt.setBanktime(cryptoPullResponse.getBankDate());

						companyPreOpenAccountEnt.setOrganfullid(map.get(organid).getFullId());
						companyPreOpenAccountEnt.setOrganid(String.valueOf(map.get(organid).getId()));
						companyPreOpenAccountEnt.setApplyorganid(organid);

						//2018-10-19：同步状态如果为以下的状态，更改为受理成功
						if (companyPreOpenAccountEnt.getStatus().equals("BuLuZhong") || companyPreOpenAccountEnt.getStatus().equals("Complete") || companyPreOpenAccountEnt.getStatus().equals("BANK_PROCESSING")
								|| companyPreOpenAccountEnt.getStatus().equals("BANK_SUCCESS") || companyPreOpenAccountEnt.getStatus().equals("BANK_FAIL")
								|| companyPreOpenAccountEnt.getStatus().equals("TRANSACTION_SUCCESS")) {
							companyPreOpenAccountEnt.setStatus("SUCCESS");
						}
						companyPreOpenAccountEntDao.save(companyPreOpenAccountEnt);
						transactionManager.commit(transaction);
//						transaction = transactionManager.getTransaction(definition);
						CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto = new CompanyPreOpenAccountEntDto();
						BeanUtils.copyProperties(companyPreOpenAccountEnt, companyPreOpenAccountEntDto);

						sendMessage(map, organid, companyPreOpenAccountEntDto, cryptoPullResponse);
					} catch (RuntimeException e) {
						log.error("插入预约数据有误" + cryptoPullResponse.toString(), e);
						if (!transaction.isCompleted()) {
							try {
								transactionManager.rollback(transaction);
							} catch (Exception ex) {
								log.error("预约数据的事务回滚有误");
							}
						}
//						transaction = transactionManager.getTransaction(definition);
						errorList.add(cryptoPullResponse);
					}
				}else{//预约表中有对应的记录，比对状态
                     if("allSync".equalsIgnoreCase(operateType)) { //全数据同步更新状态模式
                        batchUpdateApplyStatus(cryptoPullResponse);
                    } else if(cryptoPullResponse.getCreateTime() != null
                            &&cryptoPullResponse.getCreateTime().after(new Date())) {
                     	//定时任务更新状态模式
                        batchUpdateApplyStatus(cryptoPullResponse);
                    }
				}
			}

			if(errorList.size()>0){
				ObjectMapper mapper = new ObjectMapper();
				String result ="";
				try {
					result = mapper.writeValueAsString(errorList);
				} catch (JsonProcessingException e) {
					log.error("错误的预约数据转成json有误",e);
				}
				throw new EacException("插入预约数据有误,列表如下："+result);
			}
		}
	}

	private void batchUpdateApplyStatus(CryptoPullNewVo cryptoPullResponse) {
        CompanyPreOpenAccountEnt companyPreOpenAccountEntInDatabase =
                companyPreOpenAccountEntDao.findByApplyid(cryptoPullResponse.getApplyId());
        CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();
        BeanUtils.copyProperties(cryptoPullResponse, companyPreOpenAccountEnt);
        if(companyPreOpenAccountEntInDatabase.getStatus().equals(companyPreOpenAccountEnt.getStatus())){//状态一致，无需插入
            log.info("状态一致，无需插入：{}",companyPreOpenAccountEntInDatabase);
        }else{//状态不一致，进行判断之后插入
            checkApplyStatus(companyPreOpenAccountEntInDatabase,companyPreOpenAccountEnt);
        }
    }

	/**
	 * 发送短信
	 * @param map
	 * @param organid
	 * @param companyPreOpenAccountEntDto
	 * @param obj
	 */
	private void sendMessage(Map<String,OrganizationPo> map, String organid, CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto,
				    Object obj) {
		CryptoPullVo cryptoPullResponse;
		CryptoPullNewVo cryptoPullNewResponse;

		try {
			if (StringUtils.isNotBlank(map.get(organid).getMobile())) {
				Boolean checkPass = true;
				String errorMessage = "";
				String returnValue = "";
				String[] mobiles = StringUtils.split(map.get(organid).getMobile(), ",");

				for (String mobile : mobiles) {
					try {
						log.info("开始发短信:" + mobile);
						returnValue = smsService.sendMessage(mobile, MsgFormat.formatMessage(clerkMessage, companyPreOpenAccountEntDto));
						log.info("结束发短信:" + mobile);
					} catch (Exception e) {
						errorMessage = e.getMessage();
						checkPass = false;
						log.error("发送短信失败", e);
					}


					EzhMessage ezhMessage = new EzhMessage(mobile, MsgFormat.formatMessage(clerkMessage,
							companyPreOpenAccountEntDto), "2", checkPass, errorMessage, returnValue);
					ezhMessage.setCheckPass(checkPass);
					if(obj instanceof CryptoPullNewVo) {
						cryptoPullNewResponse = (CryptoPullNewVo)obj;
						ezhMessage.setApplyId(cryptoPullNewResponse.getApplyId());
					} else if(obj instanceof CryptoPullVo) {
						cryptoPullResponse = (CryptoPullVo)obj;
						ezhMessage.setApplyId(cryptoPullResponse.getApplyid());
					}
					ezhMessageDao.save(ezhMessage);
				}
			} else {
				log.info("未发现该机构的手机号码：" + map.get(organid).getPbcCode());
			}
		} catch (Exception e) {
			log.error("发送短信异常", e);
		}
	}

	/**
	 * 判断预约状态
	 * @param companyPreOpenAccountEntInDatabase
	 * @param companyPreOpenAccountEnt
	 */
	private void checkApplyStatus(CompanyPreOpenAccountEnt companyPreOpenAccountEntInDatabase,CompanyPreOpenAccountEnt companyPreOpenAccountEnt){
		int levelInDatabase = applyStatusLevel(companyPreOpenAccountEntInDatabase);
		int level = applyStatusLevel(companyPreOpenAccountEnt);
		if(level > levelInDatabase){
			log.info("状态判断，需要更新：{}",companyPreOpenAccountEntInDatabase);
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = null;
			try{
				transaction = transactionManager.getTransaction(definition);
				//重新查询，防止乐观锁异常
				companyPreOpenAccountEntInDatabase =
						companyPreOpenAccountEntDao.findByApplyid(companyPreOpenAccountEntInDatabase.getApplyid());
				companyPreOpenAccountEntInDatabase.setStatus(companyPreOpenAccountEnt.getStatus());
				companyPreOpenAccountEntDao.save(companyPreOpenAccountEntInDatabase);
				log.info("companyPreOpenAccountEntInDatabase:" + JSON.toJSONString(companyPreOpenAccountEntInDatabase));
				transactionManager.commit(transaction);
			} catch (Exception e){
				log.error("保存失败，进行回滚：{}",companyPreOpenAccountEntInDatabase);
				if(!transaction.isCompleted()){
					try{
						transactionManager.rollback(transaction);
					}catch (Exception e1){
						log.error("回滚失败");
					}
				}
			}
		}else{
			log.info("状态判断，不需要更新：{}",companyPreOpenAccountEntInDatabase);
		}
	}

	/**
	 * @Description TODO 同步预约信息及预约状态
	 * @param companyPreOpenAccountEntInDatabase
	 * @param companyPreOpenAccountEnt
	 */
	private void updateApplyInfo(CompanyPreOpenAccountEnt companyPreOpenAccountEntInDatabase,CompanyPreOpenAccountEnt companyPreOpenAccountEnt){
		int levelInDatabase = applyStatusLevel(companyPreOpenAccountEntInDatabase);
		int level = applyStatusLevel(companyPreOpenAccountEnt);

		if(level >= levelInDatabase) {
			log.info("状态判断，需要更新：{}", companyPreOpenAccountEntInDatabase);
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = null;
			try {
				transaction = transactionManager.getTransaction(definition);
				//重新查询，防止乐观锁异常
				companyPreOpenAccountEntInDatabase =
						companyPreOpenAccountEntDao.findByApplyid(companyPreOpenAccountEntInDatabase.getApplyid());
				BeanValueUtils.copyProperties(companyPreOpenAccountEnt, companyPreOpenAccountEntInDatabase);
				//			companyPreOpenAccountEntInDatabase.setStatus(companyPreOpenAccountEnt.getStatus());
				companyPreOpenAccountEntDao.save(companyPreOpenAccountEntInDatabase);
				log.info("companyPreOpenAccountEntInDatabase:" + JSON.toJSONString(companyPreOpenAccountEntInDatabase));
				transactionManager.commit(transaction);
			} catch (Exception e) {
				log.error("保存失败，进行回滚：{}", companyPreOpenAccountEntInDatabase);
				if (!transaction.isCompleted()) {
					try {
						transactionManager.rollback(transaction);
					} catch (Exception e1) {
						log.error("回滚失败");
					}
				}
			}
		} else{
			log.info("状态判断，不需要更新：{}",companyPreOpenAccountEntInDatabase);
		}
	}

	/**
	 * 状态进行排序
	 * @param companyPreOpenAccountEntInDatabase
	 * @return
	 */
	private int applyStatusLevel(CompanyPreOpenAccountEnt companyPreOpenAccountEntInDatabase){
		if(companyPreOpenAccountEntInDatabase.getStatus().equals("UnComplete")){
			return 0;
		}else if(companyPreOpenAccountEntInDatabase.getStatus().equals("SUCCESS") || companyPreOpenAccountEntInDatabase.getStatus().equals("FAIL")
				|| companyPreOpenAccountEntInDatabase.getStatus().equals("BuLuZhong") || companyPreOpenAccountEntInDatabase.getStatus().equals("Complete")
				|| companyPreOpenAccountEntInDatabase.getStatus().equals("BANK_PROCESSING") || companyPreOpenAccountEntInDatabase.getStatus().equals("BANK_SUCCESS")
				|| companyPreOpenAccountEntInDatabase.getStatus().equals("BANK_FAIL") || companyPreOpenAccountEntInDatabase.getStatus().equals("TRANSACTION_SUCCESS")){
			return 1;
		}else if(companyPreOpenAccountEntInDatabase.getStatus().equals("REGISTER_SUCCESS") || companyPreOpenAccountEntInDatabase.getStatus().equals("REGISTER_FAIL")){
			return 2;
		}
		return 0;
	}

	@Override
	public void sendModifyStatus(Long uuid, String status)  throws Exception{
		CryptoModifyStatusMessage cryptoModifyStatusMessage = new CryptoModifyStatusMessage();
		cryptoModifyStatusMessage.setStatus(status);
		cryptoModifyStatusMessage.setUuid(uuid);
        CryptoInput cryptoInput = new CryptoInput();
        cryptoInput.setRawBizContext(JSON.toJSONString(cryptoModifyStatusMessage));
        try {
            cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
        }catch(RuntimeException e) {
            log.error(e.getMessage()+","+cryptoModifyStatusMessage.toString());
            return;
    	}

		getApplyModilyStatus(cryptoInput, JSON.toJSONString(cryptoModifyStatusMessage), "old");
	}

	@Override
	public void sendModifyStatus(String applyId, ApplyEnum status, String pbcCode, String note) throws Exception {
		CryptoModifyStatusMessageNew cryptoModifyStatusMessage = new CryptoModifyStatusMessageNew();
		cryptoModifyStatusMessage.setApplyId(applyId);
		cryptoModifyStatusMessage.setStatus(status);
		cryptoModifyStatusMessage.setNote(note);

		CryptoInput cryptoInput = new CryptoInput();
		try {
			cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		} catch (RuntimeException e) {
			log.error(e.getMessage() + "," + cryptoModifyStatusMessage.toString());
			return;
		}

		if(StringUtils.isBlank(pbcCode)) {
			CompanyPreOpenAccountEnt openAccount = companyPreOpenAccountEntDao.findByApplyid(applyId);
			if(openAccount != null) {
				cryptoModifyStatusMessage.setOrganId(openAccount.getApplyorganid());
			}
		} else {
			cryptoModifyStatusMessage.setOrganId(pbcCode);
		}
		cryptoInput.setRawBizContext(JSON.toJSONString(cryptoModifyStatusMessage));

		getApplyModilyStatus(cryptoInput, JSON.toJSONString(cryptoModifyStatusMessage), "new");
	}

	@Override
	public String pushApplyAddAcct(CryptoAddApplyAcctVo applyAcctVo) throws Exception {
		CryptoInput cryptoInput = new CryptoInput();
		cryptoInput.setRawBizContext(JSON.toJSONString(applyAcctVo));
		try {
			cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		}catch(RuntimeException e) {
			log.error(e.getMessage() + "," + applyAcctVo.toString());
			return "";
		}

		return applyAcctPush(cryptoInput, JSON.toJSONString(applyAcctVo), "add");
	}

	@Override
	public void pushApplyEditAcct(CryptoEditApplyAcctVo applyAcctVo) throws Exception {
		CryptoInput cryptoInput = new CryptoInput();
		cryptoInput.setRawBizContext(JSON.toJSONString(applyAcctVo));
		try {
			cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		}catch(RuntimeException e) {
			log.error(e.getMessage() + "," + applyAcctVo.toString());
			return;
		}

		applyAcctPush(cryptoInput, JSON.toJSONString(applyAcctVo), "edit");
	}

	@Override
	public void pushApplyAcctKey(CryptoModifyAcctKeyMessage acctKeyMessage) throws Exception {
		CryptoInput cryptoInput = new CryptoInput();
		cryptoInput.setRawBizContext(JSON.toJSONString(acctKeyMessage));
		try {
			cryptoInput.setThirdPartyOrgCode(getRSACodeOrganId(rsaCode));
		}catch(RuntimeException e) {
			log.error(e.getMessage() + "," + acctKeyMessage.toString());
			return;
		}

		applyAcctKeyPush(cryptoInput, JSON.toJSONString(acctKeyMessage));
	}

	private void getApplyModilyStatus(CryptoInput cryptoInput, String messageJsonStr, String type) {
		CryptoOutput cryptoOutput = new CryptoOutput();
		String code = null;
		int i = 1;
		String applyModilyStatusUrl = "";

		if("old".equalsIgnoreCase(type)) {
			applyModilyStatusUrl = applyModilyStatus;
		} else if("new".equalsIgnoreCase(type)) {  //新接口---预约状态修改
			applyModilyStatusUrl = newApplyModilyStatus;
		}
		while (i <= 3) {
			log.info("第" + i + "次调用状态接口," + messageJsonStr);
			try {
				code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput, applyModilyStatusUrl);
			} catch (RuntimeException e) {
				log.error("第" + i + "次调用状态接口,远程访问失败," + e.getMessage() + "," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
				continue;
			}
			if (StringUtils.isBlank(code)) {
				log.error("第" + i + "次调用状态接口,无数据结果," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
			} else {
				break;
			}
		}
		if (StringUtils.isBlank(code) || !"000405".equals(code)) {
			log.error("调用状态接口返回的结果为空,状态值为" + code);
			throw new EacException("调用状态接口返回失败，状态值为" + code + "," + messageJsonStr);
		} else {
			log.info("调用状态接口修改状态成功");
		}
	}

	/**
	 * 预约补录信息推送
	 * @param cryptoInput
	 * @param messageJsonStr
	 * @param type
	 */
	private String applyAcctPush(CryptoInput cryptoInput, String messageJsonStr, String type) {
		CryptoOutput cryptoOutput = new CryptoOutput();
		String code = "";
		int i = 1;
		String applyAcctUrl = "";
		String successStatusCode = "";

		if("add".equalsIgnoreCase(type)) {
			applyAcctUrl = addApplyAcctUrl;
			log.info("预约单新增接口调用-----");
			successStatusCode = "000407";
		} else if("edit".equalsIgnoreCase(type)) {
			applyAcctUrl = editApplyAcctUrl;
			log.info("预约单修改接口调用-----");
			successStatusCode = "000410";
		}

		while(i <= 3) {
			log.info("第" + i + "次调用预约单补录接口," + messageJsonStr);
			try {
				code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput, applyAcctUrl);
			} catch(RuntimeException e) {
				log.error("第" + i + "次调用预约单补录接口,远程访问失败," + e.getMessage() + "," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
				continue;
			}
			if (StringUtils.isBlank(code)) {
				log.error("第" + i + "次调用状态接口,无数据结果," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
			} else {
				break;
			}
		}

		if (StringUtils.isBlank(code) || !successStatusCode.equals(code)) {
			log.error("调用预约补录接口返回的结果为空,状态值为" + code);
			throw new EacException("调用预约补录接口返回失败，状态值为" + code + "," + messageJsonStr);
		} else {
			log.info("调用预约补录接口成功", cryptoOutput);
			return cryptoOutput.getRawBizContext();
		}
	}

	private void applyAcctKeyPush(CryptoInput cryptoInput, String messageJsonStr) {
		CryptoOutput cryptoOutput = new CryptoOutput();
		String code = null;
		int i = 1;

		while (i <= 3) {
			log.info("第" + i + "次调用状态接口," + messageJsonStr);
			try {
				code = companyPreOpenAccountEntService.send(cryptoInput, cryptoOutput, addAcctKeyUrl);
			} catch (RuntimeException e) {
				log.error("第" + i + "次调用录入开户许可证接口,远程访问失败," + e.getMessage() + "," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
				continue;
			}
			if (StringUtils.isBlank(code)) {
				log.error("第" + i + "次调用录入开户许可证接口,无数据结果," + messageJsonStr);
				cryptoOutput = new CryptoOutput();
				i++;
			} else {
				break;
			}
		}
		if (StringUtils.isBlank(code) || !"000412".equals(code)) {
			log.error("调用录入开户许可证接口返回的结果为空,状态值为" + code);
			throw new EacException("调用录入开户许可证接口返回失败，状态值为" + code + "," + messageJsonStr);
		} else {
			log.info("调用录入开户许可证接口成功");
		}
	}

	private String getPbcCodeList(){
		List<String> list = organizationDao.findDistinctPbcCode();
        if(list.isEmpty()) {
        	return "";
        }else {
        	StringBuffer sb =new StringBuffer();
        	for (String str : list) {
				if(str != null && !"".equals(str)) {
					if("".equals(sb.toString())) {
						sb.append(str);
					}else {
						sb.append(","+str);
					}
				}
			}
    		return sb.toString();
        }
	}

	@Override
	@Transactional(noRollbackFor=RuntimeException.class)
	public String getRSACodeOrganId(RSACode rsaCode) {
 		if(rsaCode.getOrganid()!=null && !"".equals(rsaCode.getOrganid())) {
			return rsaCode.getOrganid();
		}else {
			List<ConfigDto> organids = configService.findByKey("organid");
			if(organids.size()>0 && !"".equals(organids.get(0).getConfigValue())) {
				rsaCode.setOrganid(organids.get(0).getConfigValue());
				return rsaCode.getOrganid();
			}else {
	            throw new EacException("加密前获取组织机构号出错!");
			}
		}
	}


	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void stateUpdate(CompanyPreOpenAccountEntDto entDto,String status){
		CryptoEditApplyAcctVo applyAcctVo = new CryptoEditApplyAcctVo();
		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = transactionManager.getTransaction(definition);

		CompanyPreOpenAccountEnt byId = companyPreOpenAccountEntDao.findById(entDto.getId());
		byId.setStatus(status);
		companyPreOpenAccountEntDao.save(byId);
		transactionManager.commit(transaction);
		try{
			if(!applyNewRuleFlag) {
				sendModifyStatus(entDto.getId(), status);
			} else {
				BeanValueUtils.copyProperties(entDto, applyAcctVo);
				setCryptoEditApplyAcctVo(applyAcctVo, entDto);

				pushApplyEditAcct(applyAcctVo);
				sendModifyStatus(entDto.getApplyid(), ApplyEnum.getEnum(status), "", "");
			}
		}catch (Exception e){
			log.error("发送预约状态异常",e.getMessage());
		}
	}


	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void edit(CompanyPreOpenAccountEntDto entDto, CompanyPreOpenAccountEntSaicDto saicDto) {

		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = transactionManager.getTransaction(definition);
		edit(entDto);
		CompanyPreOpenAccountEntSaic cpoaes = new CompanyPreOpenAccountEntSaic();

		BeanCopierUtils.copyProperties(saicDto,cpoaes);
		cpoaes.setCompanyPreId(entDto.getId());
		companyPreOpenAccountEntSaicDao.save(cpoaes);
		transactionManager.commit(transaction);
		if (flag) {
			try {
				if(!applyNewRuleFlag) {
					sendModifyStatus(entDto.getId(), entDto.getStatus());
				} else {  //预约新接口模式
					sendModifyStatus(entDto.getApplyid(), ApplyEnum.getEnum(entDto.getStatus()), "", entDto.getApplynote());
				}
			} catch (Exception e) {
				log.error("发送预约状态异常", e.getMessage());
			}
		}

		companyPreOpenAccountEntService.saveMessage(entDto);
	}

	@Override
	public CompanyPreOpenAccountEntSaicDto selectOneSaic(String applyId) {
		CompanyPreOpenAccountEnt companyPreOpenAccountEnt = companyPreOpenAccountEntDao.findByApplyid(applyId);
		CompanyPreOpenAccountEntSaic entSaic = companyPreOpenAccountEntSaicDao.findByCompanyPreId(companyPreOpenAccountEnt.getId());
		CompanyPreOpenAccountEntSaicDto dto = new CompanyPreOpenAccountEntSaicDto();
		BeanCopierUtils.copyProperties(entSaic, dto);

		return dto;
	}

	@Override
	public Boolean sendMessage(Long id) {
		EzhMessageDto dto = ezhMessageService.getOne(id);

		try {
			smsService.sendMessage(dto.getPhone(), dto.getMessage());
			dto.setCheckPass(true);
			ezhMessageService.edit(dto);
		} catch (Exception e) {
			dto.setCheckPass(false);
			ezhMessageService.edit(dto);
			log.error("预约成功相关发送短信异常", e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 获取违约客户信息
	 * @return
	 */
	@Override
	public List<CompanyPreOpenAccountEntDto> getBreakAppointInfo() {
		String breakAppointCustomTime = "";
		List<ConfigDto> list = configService.findByKey("breakAppointCustomTime");

		if(CollectionUtils.isNotEmpty(list)){
			ConfigDto dto = list.get(0);
			breakAppointCustomTime = dto.getConfigValue();
		}

		if(StringUtils.isBlank(breakAppointCustomTime)) {
			breakAppointCustomTime = "30";
		}

		Date endDate = DateUtil.subDays(new Date(), Integer.parseInt(breakAppointCustomTime));
		String endDateStr = DateUtils.DateToStr(endDate, "yyyy-MM-dd HH:mm:ss");

		List<CompanyPreOpenAccountEnt> preOpenAccountEntList = companyPreOpenAccountEntDao.findByStatusAndAcceptTimesLessThan(ApplyEnum.SUCCESS.getValue(), endDateStr);
		if(CollectionUtils.isNotEmpty(preOpenAccountEntList)) {
			return ConverterService.convertToList(preOpenAccountEntList, CompanyPreOpenAccountEntDto.class);
		}

		return null;
	}

	@Override
	public List<CompanyPreOpenAccountEntDto> findTop10ByHasocr(String hasocr) {
		List<CompanyPreOpenAccountEnt> top10ByHasocr = companyPreOpenAccountEntDao.findTop10ByHasocr(hasocr);
		return ConverterService.convertToList(top10ByHasocr, CompanyPreOpenAccountEntDto.class);
	}

	@Override
	public void updateHasocr(Long id) {
		CompanyPreOpenAccountEnt one = companyPreOpenAccountEntDao.findOne(id);
		if("1".equals(one.getHasocr())){
			one.setHasocr("2");
		}
		companyPreOpenAccountEntDao.save(one);
	}

    @Override
    public Long countUnprocessedCountBefore(Date createdBefore, String organFullId) {
		return companyPreOpenAccountEntDao.countByStatusAndCreatedDateLessThanAndOrganfullidLike(ApplyEnum.UnComplete.getValue(), createdBefore, organFullId+"%");
    }

    @Override
	public PagingDto<CompanyPreOpenAccountEntDto> listUnprocessedCountBefore(final CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto,final Date createdBefore,final String organFullId, PagingDto pagingDto) {
		Pageable pageable = new PageRequest(pagingDto.getOffset(), pagingDto.getLimit());
//        Page<CompanyPreOpenAccountEnt> page = companyPreOpenAccountEntDao.findByStatusAndCreatedDateLessThanAndOrganfullidLike(ApplyEnum.UnComplete.getValue(),
//                createdBefore, organFullId + "%", pageable);
        Page<CompanyPreOpenAccountEnt> page = companyPreOpenAccountEntDao.findAll(new CompanyPreOpenAccountEntSpec(companyPreOpenAccountEntDto),pageable);

        List<CompanyPreOpenAccountEntDto> dtos = ConverterService.convertToList(page.getContent(), CompanyPreOpenAccountEntDto.class);
        pagingDto.setList(dtos);
        pagingDto.setTotalRecord(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        return pagingDto;
    }

	@Override
	public void saveApply(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, String bankCode) {
		companyPreOpenAccountEntDto.validate();
		if (StringUtils.isBlank(bankCode)) {
			throw new RuntimeException("[核心机构代码]不能为空");
		}
		OrganizationDto organizationDto = organizationService.findByCode(bankCode);
		//验证核心机构代码
		if (organizationDto == null) {
			throw new EacException("无效的核心机构代码，bankCode:" + bankCode);
		}
		companyPreOpenAccountEntService.saveApplyImmediately(companyPreOpenAccountEntDto,organizationDto);

		//给柜员发送短信通知
		String mobile = organizationDto.getMobile();
		String returnValue = "";
		Boolean checkPass = true;
		String errorMessage = "";
		try {
			log.info("开始发短信:" + mobile);
			returnValue = smsService.sendMessage(mobile, MsgFormat.formatMessage(clerkMessage, companyPreOpenAccountEntDto));
			log.info("结束发短信:" + mobile);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			checkPass = false;
			log.error("发送短信失败", e);
		}

		EzhMessage ezhMessage = new EzhMessage(mobile, MsgFormat.formatMessage(clerkMessage, companyPreOpenAccountEntDto), "2", checkPass, errorMessage, returnValue);
		ezhMessage.setCheckPass(checkPass);
		ezhMessage.setApplyId(companyPreOpenAccountEntDto.getApplyid());
		ezhMessageDao.save(ezhMessage);
	}

	@Override
	@Transactional(isolation= Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
	public void saveApplyImmediately(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, OrganizationDto organizationDto){
		int loopTimes =3 ;
		//验证状态
		if (!companyPreOpenAccountEntDto.getStatus().equals(ApplyEnum.UnComplete.getValue())) {
			throw new EacException("插入预约数据状态错误，status:" + companyPreOpenAccountEntDto.getStatus());
		}

		//重新生产预约编号
		companyPreOpenAccountEntDto.setApplyid(DateUtils.currentLongTimeToStr(3));

		//3次循环进行判断数据是否存在
		while(loopTimes>0){
			if (companyPreOpenAccountEntService.findByApplyid(companyPreOpenAccountEntDto.getApplyid()) != null) {//已存在
				log.error("插入的预约编号已经已存在,applyid:{}",companyPreOpenAccountEntDto.getApplyid());
				companyPreOpenAccountEntDto.setApplyid(DateUtils.currentLongTimeToStr(3));
				log.error("重新生产预约编号，applyid:{}",companyPreOpenAccountEntDto.getApplyid());
				loopTimes--;
			}else{
				break;
			}
		}

		if(loopTimes<0){
			throw new EacException("已经自动生产了多次预约编号，都重复，请稍微再插入预约信息");
		}

		companyPreOpenAccountEntDto.setOrganfullid(organizationDto.getFullId());
		companyPreOpenAccountEntDto.setOrganid(organizationDto.getId() + "");
		companyPreOpenAccountEntDto.setCreatedDate(new Date());
		companyPreOpenAccountEntDto.setStatus(ApplyEnum.UnComplete.getValue());
		CompanyPreOpenAccountEnt companyPreOpenAccountEnt = new CompanyPreOpenAccountEnt();
		BeanCopierUtils.copyProperties(companyPreOpenAccountEntDto, companyPreOpenAccountEnt);
		companyPreOpenAccountEntDao.save(companyPreOpenAccountEnt);
	}

	/**
	 * 根据applyid获取预约状态
	 */
	@Override
	public String getStatusByApplyid(String applyid) {
		CompanyPreOpenAccountEnt cpoae = companyPreOpenAccountEntDao.findByApplyid(applyid);
		return cpoae == null ? null : cpoae.getStatus();
	}

	@Override
	public long getCompanyPreCount(CompanyPreOpenAccountEntDto dto) {
		long successCount = companyPreOpenAccountEntDao.count(new CompanyPreOpenAccountEntSpec(dto));

		return successCount;
	}

	@Override
	public ResultDto deletePreOpenAccount(String applyid, String name) {
		CompanyPreOpenAccountEnt preOpenAccount = companyPreOpenAccountEntDao.findByApplyidAndName(applyid, name);

		if(preOpenAccount == null) {
			return ResultDtoFactory.toNack("对应的预约数据不存在");
		}

		companyPreOpenAccountEntDao.delete(preOpenAccount.getId());

		return ResultDtoFactory.toApiSuccess("");
	}

	@Override
	public CompanyPreOpenAccountEntSaicDto conversion(CompanyPreOpenAccountEntSaicDto info1) {
		if (StringUtils.isNotBlank(info1.getDepositorType())) {
			if ("50".equals(info1.getDepositorType()) || "51".equals(info1.getDepositorType())
					|| "52".equals(info1.getDepositorType())) {
				info1.setDepositorType(
						dictionaryService.transalte("depositorTypeTeShuValueItem", info1.getDepositorType()));
			} else {
				info1.setDepositorType(
						dictionaryService.transalte("depositorTypeValue2Item", info1.getDepositorType()));
			}
		}
		if (StringUtils.isNotBlank(info1.getLegalType())) {
			info1.setLegalType(dictionaryService.transalte("legalTypeValue2Item", info1.getLegalType()));
		}
		if (StringUtils.isNotBlank(info1.getIsIdentification())) {
			info1.setIsIdentification(
					dictionaryService.transalte("isIdentificationValue2Item", info1.getIsIdentification()));
		}
		if (StringUtils.isNotBlank(info1.getLegalIdcardType())) {
			info1.setLegalIdcardType(
					dictionaryService.transalte("legalIdcardTypeValue2Item", info1.getLegalIdcardType()));
		}
		if (StringUtils.isNotBlank(info1.getFileType())) {
				info1.setFileType(
						dictionaryService.transalte("fileTypejiBenValueItem", info1.getFileType()));
		}
		if (StringUtils.isNotBlank(info1.getFileType2())) {
			info1.setFileType2(
					dictionaryService.transalte("fileTypejiBenValue2Item", info1.getFileType2()));

		}
		if (StringUtils.isNotBlank(info1.getIndustryCode())) {
			info1.setIndustryCode(
					dictionaryService.transalte("industryCodeValueItem", info1.getIndustryCode()));
		}
		if (StringUtils.isNotBlank(info1.getRegCurrencyType())) {
			info1.setRegCurrencyType(
					dictionaryService.transalte("regCurrencyType2Item", info1.getRegCurrencyType()));
		}

		if (StringUtils.isNotBlank(info1.getRegOffice())) {
			info1.setRegOffice(
					dictionaryService.transalte("regOfficeValue2Item", info1.getRegOffice()));
		}
		if (StringUtils.isNotBlank(info1.getRegType())) {
			info1.setRegType(
					dictionaryService.transalte("regTypeValue2Item", info1.getRegType()));
		}

		return info1;
	}

	@Override
	public CompanyPreOpenAccountEntDto findByApplyIdAndName(String applyId, String name) {
		return ConverterService.convert(companyPreOpenAccountEntDao.findByApplyidAndName(applyId,name),CompanyPreOpenAccountEntDto.class);
	}

	@Override
	public void applyOpenValidater(CryptoAddApplyAcctVo applyAcctVo) throws Exception {
		if(StringUtils.isBlank(applyAcctVo.getName())) {
			throw new EacException("预约客户企业名称不能为空");
		}
		if(applyAcctVo.getBillType() == null) {
			throw new EacException("预约业务操作类型不能为空");
		}
		if(applyAcctVo.getType() == null) {
			throw new EacException("账户性质不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getBank())) {
			throw new EacException("开户行银行名称不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getBranch())) {
			throw new EacException("开户行网点名称不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getApplyDate())) {
			throw new EacException("客户预约办理时间不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getOperator())) {
			throw new EacException("预约客户姓名不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getPhone())) {
			throw new EacException("预约客户手机号不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getOrganId())) {
			throw new EacException("预约开户行网点银行人行联行号不能为空");
		}

	}

	@Override
	public void applyEditValidater(CryptoEditApplyAcctVo applyAcctVo) throws Exception {
		if(StringUtils.isBlank(applyAcctVo.getApplyId())) {
			throw new EacException("预约编号不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getOrganId())) {
			throw new EacException("预约开户行网点银行人行联行号不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getFirstSupplyOperator())) {
			throw new EacException("预约创建人不能为空");
		}
		if(StringUtils.isBlank(applyAcctVo.getBankApplyTime())) {
			throw new EacException("银行通知客户临柜时间不能为空");
		}

	}

	private String getApplyPullResponse(String code, CryptoOutput cryptoOutput) {
		String errorMsg = null;
		if(StringUtils.isBlank(code) || !"000000".equals(code) || StringUtils.isEmpty(cryptoOutput.getRawBizContext())) {
			log.info("解密易账户调用的结果为空，无插入结果");
		}else {
			ObjectMapper mapper = new ObjectMapper();
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, CryptoPullVo.class);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			List<CryptoPullVo> response;

			try {
				response = (List<CryptoPullVo>) mapper.readValue(cryptoOutput.getRawBizContext(), collectionType);
				for (CryptoPullVo cpv : response) {
					if (cpv.getHasocr() == null || cpv.getHasocr().equals("0")) {
						String applyid = cpv.getApplyid();
						if (!applyIdMap.containsKey(applyid)) {//没有记录过的（没有影像信息的预约数据）
							applyIdMap.put(applyid, 1);
						}
					}
				}
				insertBatch(response);
			} catch (IOException e) {
				log.error("插入预约数据有误,序列化结果出错,"+e.getMessage());
				errorMsg = "预约数据同步失败";
			}
		}

		return errorMsg;
	}

	private String getApplyPullTimeResponse(String code, CryptoOutput cryptoOutput, String operateType) {
		String errorMsg = null;
		if(StringUtils.isBlank(code) || !"000000".equals(code) || StringUtils.isEmpty(cryptoOutput.getRawBizContext())) {
			log.info("解密易账户调用的结果为空，无插入结果");
		}else {
			ObjectMapper mapper = new ObjectMapper();
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, CryptoPullNewVo.class);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			List<CryptoPullNewVo> response;

			try {
				response = (List<CryptoPullNewVo>) mapper.readValue(cryptoOutput.getRawBizContext(), collectionType);
				for (CryptoPullNewVo cpv : response) {
					if (cpv.getHasocr() == null || cpv.getHasocr().equals("0")) {
						String applyid = cpv.getApplyId();
						if (!applyIdMap.containsKey(applyid)) {//没有记录过的（没有影像信息的预约数据）
							applyIdMap.put(applyid, 1);
						}
					}
				}
				insertBatchNew(response, operateType);
			} catch (IOException e) {
				log.error("插入预约数据有误,序列化结果出错,"+e.getMessage());
				errorMsg = "预约数据同步失败";
			}
		}

		return errorMsg;
	}

	private void setCryptoEditApplyAcctVo(CryptoEditApplyAcctVo applyAcctVo, CompanyPreOpenAccountEntDto entDto) {
		applyAcctVo.setDepositorName(entDto.getName());
		applyAcctVo.setAcctName(entDto.getName());
		applyAcctVo.setAcctType(CompanyAcctType.str2enum(entDto.getType()));
		applyAcctVo.setBankName(entDto.getBranch());
		applyAcctVo.setBankCode(entDto.getApplyorganid());
		applyAcctVo.setApplyId(entDto.getApplyid());
		applyAcctVo.setOrganId(entDto.getApplyorganid());
		applyAcctVo.setFirstSupplyOperator(entDto.getAccepter());
		applyAcctVo.setBankApplyTime(entDto.getBanktime() + " " + entDto.getTimes());
	}

}

