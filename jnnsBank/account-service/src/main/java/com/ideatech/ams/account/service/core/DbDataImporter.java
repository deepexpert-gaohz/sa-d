/**
 * 
 */
package com.ideatech.ams.account.service.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ideatech.ams.account.dao.core.CorePublicAccountDao;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhailiang
 *
 */
@Component
@Transactional
@Slf4j
public class DbDataImporter extends AbstractDataImporter {

	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CorePublicAccountDao corePublicAccountDao;
	
	@Autowired
	private TransactionUtils transactionUtils;

	@Autowired
	private AmsCoreAccountService amsCoreAccountService;

	private int threshold;
	
	private Map<String, String> accountStatusCore2PbcMap;


	/*@Override
	protected void doProcess() throws Exception {
		//t+1操作使用虚拟用户
		UserDto userDto = userService.findVirtualUser();
		if (userDto == null) {
			throw new EacException("虚拟用户未创建");
		}
		
		accountStatusCore2PbcMap = getAccountStatusCore2PbcMap();
		Collection<String> values = accountStatusCore2PbcMap.values();
		for (String value : values) {
			if (StringUtils.isBlank(value)) {
				throw new EacException("core2pbc字典未做映射，无法继续处理");
			}
		}
		
		Pageable pageable = new PageRequest(0, threshold);
		long count = corePublicAccountDao.countByHandleStatus(CompanyIfType.No);
		List<CorePublicAccount> list = null;
		boolean hasNext = true;
		while (hasNext && count > 0) {
			try {
				list = corePublicAccountDao.findByHandleStatusOrderByCreatedDateDesc(CompanyIfType.No, pageable);
				hasNext = pageable.getPageSize() * pageable.getPageNumber() + 1 < count;
				pageable = pageable.next();
				
				for (int i = 0; i < list.size(); i++) {
					CorePublicAccountBase corePublicAccount = list.get(i);
					try {
						saveCorePublicAccount(corePublicAccount, userDto);
					} catch (Exception e) {
						log.error("处理增量账户信息异常,账号为:" + corePublicAccount.getAcctNo(), e);
					}
				}
			} catch (Exception e) {
				log.error("分页处理核心数据异常", e);
			}

		}
	}
	
	
	public void saveCorePublicAccount(final CorePublicAccountBase corePublicAccount, final UserDto userInfo) throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			@Override
			public void execute() throws Exception {
				try {
					// 赋值给DTO
					CorePublicAccountDto corePublicAccountDto = new CorePublicAccountDto();
					BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountDto);
					corePublicAccountDto.setId(null);
					// 赋值给Error Bean
					CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
					BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountError);
					corePublicAccountError.setId(null);

					CorePublicAccountOuterInfo corePublicAccountOuterInfo = null;

					String acctNo = corePublicAccount.getAcctNo();
					if (StringUtils.isBlank(acctNo)) {
						saveErrorAccount(corePublicAccountError, "账户账号为空，无法继续处理");
						return;
					}

					// 必要字段的校验，不满足则无法继续处理
					// 必须有账户状态
					String coreAccountStatus = corePublicAccount.getAccountStatus();
					if (StringUtils.isBlank(coreAccountStatus)) {
						saveErrorAccount(corePublicAccountError, "账户状态为空，无法继续处理");
						return;
					}

					// 根据账号去库中查询
					CorePublicAccountBase oldCorePublicAccount = corePublicAccountDao.findFirstByAcctNoAndIdNotOrderByCreatedDateDesc(acctNo, corePublicAccount.getId());
					// 状态为正常
					// 数据库不存在
					if (oldCorePublicAccount == null) {
						// 如果是正常类型
						if (coreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.normal.name()))) {
							corePublicAccountDto.setBillType("ACCT_OPEN");
							corePublicAccountOuterInfo = corePublicAccountService.save(userInfo.getId(), corePublicAccountDto);
						} else {
							// 销户与久悬插入错误表
							saveErrorAccount(corePublicAccountError, "存量数据不存在，但账户状态为" + coreAccountStatus + "，不做处理");
							return;
						}

					} else {
						String oldCoreAccountStatus = oldCorePublicAccount.getAccountStatus();
						if (coreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.normal.name()))) {
							// 与oldCorePublicAccount比对是否有区别
							String[] notMatchFields = corePublicAccountService.compare(oldCorePublicAccount, corePublicAccount);
							corePublicAccountDto.setChangeFields(notMatchFields);
							if (notMatchFields.length > 0) {
								corePublicAccountDto.setBillType("ACCT_CHANGE");
								corePublicAccountOuterInfo = corePublicAccountService.save(userInfo.getId(), corePublicAccountDto);
								//保存变更记录
								corePublicAccountService.saveChangeItem(corePublicAccount, oldCorePublicAccount,notMatchFields);
							} else {
								// 不处理、插入错误表
								saveErrorAccount(corePublicAccountError, "账户数据未变更，不做变更处理");
								return;
							}
						} else if (coreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.revoke.name()))) {
							// 存量数据为销户，则不处理
							if (oldCoreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.revoke.name()))) {
								saveErrorAccount(corePublicAccountError, "存量账户数据为销户，不做销户处理");
								return;
							}
							corePublicAccountDto.setBillType("ACCT_REVOKE");
							corePublicAccountOuterInfo = corePublicAccountService.save(userInfo.getId(), corePublicAccountDto);
						} else if (coreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.suspend.name()))) {
							if (oldCoreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.suspend.name()))) {
								saveErrorAccount(corePublicAccountError, "存量账户数据为久悬，不做久悬处理");
								return;
							}
							if (oldCoreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.revoke.name()))) {
								saveErrorAccount(corePublicAccountError, "存量账户数据为销户，不做久悬处理");
								return;
							}
							corePublicAccountDto.setBillType("ACCT_SUSPEND");
							corePublicAccountOuterInfo = corePublicAccountService.save(userInfo.getId(), corePublicAccountDto);

						}
					}

					// 保存处理失败的数据到错误表
					if (corePublicAccountOuterInfo != null) {
						if ("1".equals(corePublicAccountOuterInfo.getCode())) {
							// 失败则插入错误表
							saveErrorAccount(corePublicAccountError, "账户数据保存并上报处理异常");
							return;
						}
					}

				} finally {
					// 更新处理状态
					updateHandleStatus(corePublicAccount);
				}
			}
		});
	}
	
	private Map<String, String> getAccountStatusCore2PbcMap() {
		List<OptionDto> optionDtoList = dictionaryService.listOptionByDictName("core2pbc-accountStatus");
		Map<String, String> accountStatusCore2PbcMap = new HashMap<>(16);
		for (OptionDto optionDto : optionDtoList) {
			String value = optionDto.getValue();
			if (value.equals(AccountStatus.normal.name()) || value.equals(AccountStatus.revoke.name()) || value.equals(AccountStatus.suspend.name())) {
				accountStatusCore2PbcMap.put(optionDto.getValue(), optionDto.getName());
			}
		}
		return accountStatusCore2PbcMap;
	}
	
	private void updateHandleStatus(CorePublicAccountBase corePublicAccount) {
		corePublicAccount.setHandleStatus(CompanyIfType.Yes);
		corePublicAccountRepository.save(corePublicAccount);
	}
	
	private void saveErrorAccount(CorePublicAccountError corePublicAccountError, String errorMsg) {
		corePublicAccountError.setErrorReason(errorMsg);
		corePublicAccountErrorRepository.save(corePublicAccountError);
	}
	
	public TransactionUtils getTransactionUtils() {
		return transactionUtils;
	}

	public void setTransactionUtils(TransactionUtils transactionUtils) {
		this.transactionUtils = transactionUtils;
	}*/

	@Override
	protected void firstProcess() throws Exception {
		Long startTime = System.currentTimeMillis();
		amsCoreAccountService.initTableData();
		Long endTime = System.currentTimeMillis();
		log.info("初次导入存量数据总耗时" + (endTime - startTime) / 1000 + "秒");

		/*File file = new File("c:/home/weblogic/idea/apprott/20180116.txt");
		if (file.exists()) {
			FileUtils.moveFileToDirectory(file, new File("c:/home/weblogic/idea/apprott/dgbb"), true);
		}*/
	}


	@Override
	protected void doProcess() throws Exception {
		
	}

}
