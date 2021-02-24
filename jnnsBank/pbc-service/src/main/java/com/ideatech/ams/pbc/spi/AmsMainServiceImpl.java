package com.ideatech.ams.pbc.spi;

import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.*;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.*;
import com.ideatech.ams.pbc.service.ams.cancel.AmsOpenAccSearchService;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.PbcBeanRefUtil;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 人行账管系统提供的服务接口实现类
 *
 * @auther zoulang
 * @create 2018-05-17 下午4:54
 **/
@Component
public class AmsMainServiceImpl implements AmsMainService {
    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AmsLoginService amsLoginService;

    @Autowired
    AmsSearchService amsSearchService;

    @Autowired
    Map<String, SyncValidater> syncAccountValidaterMap;

    @Autowired
    Map<String, Synchronizer> accountSynchronizerMap;

    @Autowired
    AmsDownExcelService amsDownExcelService;

    @Autowired
    AmsSyncOperateService amsSyncOperateService;

    @Autowired
    AmsMainService amsManService;

    @Autowired
    PbcMockService pbcMockService;
    @Autowired
    AmsOpenAccSearchService amsOpenAccSearchService;

    public void abc() {
        PbcUserAccount account = new PbcUserAccount();
        LoginAuth loginAuth = amsManService.amsLogin(account);
    }

    @Override
    public LoginAuth amsLogin(PbcUserAccount amsUserAccouont) {
        try {
            logger.info("访问人行账管的用户名：{}",amsUserAccouont.getLoginUserName());
        }catch (Exception e){
            logger.error("打印人行账管用户名异常：{}",e);
        }


        String amsUserPwd = amsUserAccouont.getLoginPassWord();
        // 截取密码长度 密码长度最大为8位
        if (StringUtils.isNotEmpty(amsUserPwd) && StringUtils.length(amsUserPwd) > 8) {
            amsUserPwd = new String(amsUserPwd.substring(0, 8));
            amsUserAccouont.setLoginPassWord(amsUserPwd);
        }
        LoginAuth auth = new LoginAuth(amsUserAccouont.getLoginIp(), amsUserAccouont.getLoginUserName(), amsUserPwd);
        //2018年7月19日 如果挡板打开，此处返回登录成功
        if (pbcMockService.isLoginMockOpen()) {
            logger.info("人行挡板开启，默认返回登录成功");
            auth.setLoginStatus(LoginStatus.Success);
            auth.setCookie(new BasicCookieStore());
            return auth;
        }
        // 校验账户信息是否正确
        LoginStatus validatStatus = amsLoginService.validateAmsLogin(amsUserAccouont);
        if (validatStatus != LoginStatus.Success) {
            auth.setLoginStatus(validatStatus);
            return auth;
        }
        // 登录
        auth = amsLoginService.amsLogin(amsUserAccouont.getLoginIp(), amsUserAccouont.getLoginUserName(), amsUserPwd);
        if (auth.getLoginStatus() == LoginStatus.PasswordExpire) {
            logger.info("[" + amsUserAccouont.getLoginUserName() + ":" + amsUserPwd + "]》》密码过期,开始进行密码修改");
            String temPassWord = "Idea0506";
            if (temPassWord.equals(amsUserPwd)) {
                temPassWord = "0506Idea";
            }
            PwdModifyStatus pwdStatus = amsLoginService.modifyPassWord(auth, amsUserPwd, temPassWord);
            if (pwdStatus == PwdModifyStatus.Success) {
                logger.info("[old-->" + amsUserPwd + ":new-->" + temPassWord + "]》》密码修改成功");
                pwdStatus = amsLoginService.modifyPassWord(auth, temPassWord, amsUserPwd);
            }
            if (pwdStatus != PwdModifyStatus.Success) {
                auth.setLoginStatus(LoginStatus.PasswordExpire);
                logger.info("[old-->" + temPassWord + ":new-->" + amsUserPwd + "]》》密码修改失败");
            } else {
                logger.info("[old-->" + temPassWord + ":new-->" + amsUserPwd + "]》》密码修改成功");
                auth = amsLoginService.amsLogin(amsUserAccouont.getLoginIp(), amsUserAccouont.getLoginUserName(), amsUserPwd);
                logger.info("账号{}密码修改后账户登录状态：{}",amsUserAccouont.getLoginUserName(),auth.getLoginStatus().getFullName());
            }
        }
        return auth;
    }

    @Override
    public void checkAmsUserName(PbcUserAccount amsUserAccount) throws Exception {
        LoginAuth auth = amsLogin(amsUserAccount);
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
    }

    @Override
    public void modifyAmsPwd(String amsIp, String amsUserName, String amsOldPwd, String amsNewPwd) throws Exception {
        PbcUserAccount amsUser = new PbcUserAccount();
        amsUser.setLoginIp(amsIp);
        amsUser.setLoginPassWord(amsOldPwd);
        amsUser.setLoginUserName(amsUserName);
        // 用原先用户名、密码进行登录
        LoginAuth auth = amsLogin(amsUser);
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        if (StringUtils.isBlank(amsNewPwd)) {
            throw new SyncException("新密码不能为空");
        } else if (StringUtils.equals(amsOldPwd, amsNewPwd)) {
            throw new SyncException("新密码不能与原密码相同");
        }
        PwdModifyStatus status = amsLoginService.modifyPassWord(auth, amsUserName, amsNewPwd);
        if (status != PwdModifyStatus.Success) {
            throw new SyncException(status.getFullName());
        }
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return getAmsAccountInfoByAcctNo(auth, acctNo);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNo(LoginAuth auth, String acctNo) throws Exception {
        if (pbcMockService.isLoginMockOpen()) {
            logger.info("人行挡板开启，默认返回空内容");
            logger.info("方法：getAmsAccountInfoByAcctNo统计人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
            return null;
        }
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoByAcctNo(auth, acctNo);
        PbcBussUtils.printObjectColumn(info);
        return info;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return getAmsAccountInfoByRevokeAcctNo(auth, acctNo);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(PbcUserAccount pbcUserAccount, String acctNo, SyncAcctType acctType) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return getAmsAccountInfoByRevokeAcctNoAndAcctType(auth, acctNo,acctType);
    }
    @Override
    public AmsAccountInfo getAmsAccountInfoByAcctNoFromChangeHtml(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoByAcctNoFromBeiAnChange(auth, allAcct);
        PbcBussUtils.printObjectColumn(info);
        return info;
    }
    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(LoginAuth auth, String acctNo) throws Exception {

        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoByRevokeAcctNo(auth, acctNo);
        PbcBussUtils.printObjectColumn(info);
        return info;
    }
    @Override
    public AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(LoginAuth auth, String acctNo, SyncAcctType acctType) throws Exception {
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoByRevokeAcctNoAndAcctType(auth, acctNo,acctType);
        PbcBussUtils.printObjectColumn(info);
        return info;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return getAmsAccountInfoBySuspendAcctNo(auth, acctNo);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(LoginAuth auth, String acctNo) throws Exception {
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoBySuspendAcctNo(auth, acctNo);
        PbcBussUtils.printObjectColumn(info);
        return info;
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAccountKey(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return getAmsAccountInfoByAccountKey(auth, accountKey, regAreaCode);
    }

    @Override
    public AmsAccountInfo getAmsAccountInfoByAccountKey(LoginAuth auth, String accountKey, String regAreaCode) throws Exception {
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        if (pbcMockService.isLoginMockOpen()) {
            logger.info("人行挡板开启，默认返回空内容");
            return null;
        }
        AmsAccountInfo info = amsSearchService.getAmsAccountInfoByAccountKey(auth, accountKey, regAreaCode);
        info.setAccountKey(StringUtils.trim(info.getAccountKey()));
        PbcBussUtils.printObjectColumn(info);
        return info;
    }

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return checkPbcByAccountKeyAndRegAreaCode(auth, accountKey, regAreaCode);
    }

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        AmsCheckResultInfo resultInfo = new AmsCheckResultInfo();
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        if (pbcMockService.isLoginMockOpen()) {
            logger.info("人行挡板开启，默认返回空内容");
            return null;
        }
        AmsAccountInfo info;
        try {
            info = amsSearchService.getAmsAccountInfoByAccountKeyForYiBanOpen(auth, accountKey, regAreaCode);
            info.setAccountKey(StringUtils.trim(info.getAccountKey()));
            PbcBussUtils.printObjectColumn(info);
            resultInfo.setCheckPass(true);
        } catch (Exception e) {
            throw e;
        }
        resultInfo.setAmsAccountInfo(info);
        return resultInfo;
    }

    @Override
    public AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(LoginAuth auth, String accountKey, String regAreaCode) throws Exception {
        AmsCheckResultInfo resultInfo = new AmsCheckResultInfo();
        if (auth == null || auth.getLoginStatus() == null) {
            throw new SyncException("人行登录信息为空");
        }
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsAccountInfo info = new AmsAccountInfo();
        try {
            info = getAmsAccountInfoByAccountKey(auth, accountKey, regAreaCode);
            resultInfo.setCheckPass(true);
        } catch (Exception e) {
            if(e instanceof NullPointerException){
                throw  e;
            }
            if (e.getMessage().contains("存款人有其他久悬") || e.getMessage().contains("已撤销")) {
                resultInfo.setCheckPass(false);
                resultInfo.setNotPassMessage(e.getMessage());
            } /*else if (e.getMessage().contains("通过接口无法显示基本户详细信息")) {
                resultInfo.setCheckPass(true);
            } */else {
                throw e;
            }
        }
        resultInfo.setAmsAccountInfo(info);
        return resultInfo;
    }

    @Override
    public void amsAccountSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception {
        // 登录
        LoginAuth auth = amsLogin(pbcUserAccount);
        amsAccountSync(auth, allAcct);
    }

    @Override
    public void amsHeZhunAccountSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception {
        // 登录
        LoginAuth auth = amsLogin(pbcUserAccount);
        validateSync(auth, allAcct);
        //核准类开户或上报删除待核准列表,直接执行删除操作，如列表中没有此账号也不会抛异常，只是返回false
        if (PbcBussUtils.isHeZhunAccount(allAcct.getAcctType()) && (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN || allAcct.getOperateType() == SyncOperateType.ACCT_CHANGE)) {
            //如果开户需要查询是否已开户
            if (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
                AmsAccountInfo info = getAmsAccountInfoByAcctNo(auth, allAcct.getAcctNo());
                if (info != null && StringUtils.isNotBlank(info.getAccountKey()) && StringUtils.isNotBlank(info.getRegAreaCode())) {
                    throw new SyncException("核准类账户已开户，请勿重复提交");
                }
            }
            deleteAcctNoInCheckList(auth, allAcct.getAcctNo());
        }
        // 报备
        String syncName = allAcct.getAccountSynchronizerName(SyncSystem.ams.toString());
        Synchronizer accountSynchronizer = accountSynchronizerMap.get(syncName);
        if (accountSynchronizer == null) {
            accountSynchronizer = accountSynchronizerMap.get("defaultSynchronizer");
            throw new SyncException("未找到" + syncName + "报备器,请联系系统管理员");
        }
        accountSynchronizer.synchronizer(auth, SyncSystem.ams, allAcct);
    }

    @Override
    public void amsAccountSync(LoginAuth auth, AllAcct allAcct) throws Exception {
        validateSync(auth, allAcct);
        // 报备
        String syncName = allAcct.getAccountSynchronizerName(SyncSystem.ams.toString());
        Synchronizer accountSynchronizer = accountSynchronizerMap.get(syncName);
        if (accountSynchronizer == null) {
            accountSynchronizer = accountSynchronizerMap.get("defaultSynchronizer");
            throw new SyncException("未找到" + syncName + "报备器,请联系系统管理员");
        }
        accountSynchronizer.synchronizer(auth, SyncSystem.ams, allAcct);
    }

    private void validateSync(LoginAuth auth, AllAcct allAcct) throws Exception {
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        PbcBeanRefUtil.setFieldEmpty(allAcct);
        // 校验
        String validaterName = allAcct.getAccountSyncValidater(SyncSystem.ams.toString());
        SyncValidater validater = syncAccountValidaterMap.get(validaterName);
        if (validater == null) {
            validater = syncAccountValidaterMap.get("defaultSyncValidater");
            if (validater == null) {
                throw new SyncException("未找到" + validaterName + "报备人行校验器,请联系系统管理员");
            }
        }
        validater.validater(allAcct, SyncSystem.ams);
    }

    @Override
    public void downRHAccount(PbcUserAccount pbcUserAccount, AmsDownTask task) throws Exception {
        // 登录
        LoginAuth auth = amsLogin(pbcUserAccount);
        downRHAccount(auth, task);
    }

    @Override
    public void downRHAccount(LoginAuth auth, AmsDownTask task) throws Exception {
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        // 校验
        validDownExcel(task);
        // 下载
        amsDownExcelService.downRHAccount(auth, task);
    }

    @Override
    public void downRHAccount(PbcUserAccount pbcUserAccount, String folderPath, String bankId) throws Exception {
        AmsDownTask task = new AmsDownTask();
        task.setBankId(bankId);
        task.setFolderPath(folderPath);
        task.setCurrencyType("1");
        task.setCruArray("1");
        downRHAccount(pbcUserAccount, task);
    }

    @Override
    public void downRHAccount(LoginAuth auth, String folderPath, String bankId) throws Exception {
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        AmsDownTask task = new AmsDownTask();
        task.setBankId(bankId);
        task.setFolderPath(folderPath);
        downRHAccount(auth, task);
    }

    @Override
    public boolean existAccountInCheckList(PbcUserAccount pbcUserAccount, String acctNo) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return existAccountInCheckList(auth, acctNo);
    }

    @Override
    public boolean existAccountInCheckList(LoginAuth auth, String acctNo) throws Exception {
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        boolean result = amsSearchService.existAcctInCheckListByAcctNo(auth, acctNo);
        logger.info("账号根据核准类列表查询结果" + result);
        return result;
    }

    @Override
    public boolean deleteAcctNoInCheckList(PbcUserAccount pbcUserAccount, String acctNo) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        return deleteAcctNoInCheckList(auth, acctNo);
    }

    @Override
    public boolean deleteAcctNoInCheckList(LoginAuth auth, String acctNo) throws Exception {
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        return amsSyncOperateService.deleteAccountInCheckList(auth, acctNo);
    }

    @Override
    public void searchAllAccount(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception {
        LoginAuth auth = amsLogin(pbcUserAccount);
        if (auth.getLoginStatus() != LoginStatus.Success) {
            throw new SyncException(auth.getLoginStatus().getFullName());
        }
        amsOpenAccSearchService.openAccSearch(auth,allAcct);
        amsOpenAccSearchService.checkSdepPassword(auth,allAcct);
    }

    private void validDownExcel(AmsDownTask amsDownTask) throws SyncException {
        if (amsDownTask == null) {
            throw new SyncException("下载任务对象不能为空");
        }
        if (StringUtils.isBlank(amsDownTask.getBankId())) {
            throw new SyncException("要下在的银行机构代码不能为空");
        }
        if (StringUtils.isBlank(amsDownTask.getFolderPath())) {
            throw new SyncException("文件要下载的文件路径不能为空");
        }
        if (StringUtils.isEmpty(amsDownTask.getBeginDay())) {
            amsDownTask.setBeginDay("1900-01-01");
        }
        if (StringUtils.isEmpty(amsDownTask.getEndDay())) {
            amsDownTask.setEndDay(DateUtils.getNowDateShort());
        }
    }

}
