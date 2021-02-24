package com.ideatech.ams.pbc.spi;


import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncAcctType;

/**
 * 人行账管系统提供的服务接口，所有外部需调用的人行账管系统报备、查询接口
 *
 * @author zoulang
 */
public interface AmsMainService {

    /**
     * 账管系统登录
     *
     * @param amsUserAccouont 账管用户登录对象
     * @return LoginAuth
     */
    LoginAuth amsLogin(PbcUserAccount amsUserAccouont);

    /**
     * 校验人行账管用用户名、密码是否正确
     *
     * @param amsUserAccount
     * @throws Exception
     */
    void checkAmsUserName(PbcUserAccount amsUserAccount) throws  Exception;

    /**
     * 修改账管用户密码
     *
     * @param amsIp       账管Ip
     * @param amsUserName 账管用户名
     * @param amsOldPwd   账管原先密码
     * @param amsNewPwd   账管新密码
     * @throws Exception
     */
    void modifyAmsPwd(String amsIp, String amsUserName, String amsOldPwd, String amsNewPwd) throws  Exception;

    /**
     * 根据账号查询人行账管账户信息
     *
     * @param pbcUserAccount
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws  Exception;

    /**
     * 根据账号查询人行账管账户信息
     *
     * @param auth
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByAcctNo(LoginAuth auth, String acctNo) throws  Exception;

    /**
     * 销户时，根据账号查询人行账管系统账户信息
     *
     * @param pbcUserAccount
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws  Exception;

    /**
     * 销户时，根据账号查询人行账管系统账户信息
     *
     * @param auth
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByRevokeAcctNo(LoginAuth auth, String acctNo) throws  Exception;

    /**
     * 账户久悬时，根据账号查询人行账管系统账户信息
     *
     * @param pbcUserAccount
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(PbcUserAccount pbcUserAccount, String acctNo) throws  Exception;

    /**
     * 账户久悬时，根据账号查询人行账管系统账户信息
     *
     * @param auth
     * @param acctNo 账号
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoBySuspendAcctNo(LoginAuth auth, String acctNo) throws  Exception;

    /**
     * 根据基本户开户许可证、地区代码查询人行账管系统客户信息
     * @param pbcUserAccount 账管用户登录对象
     * @param accountKey     基本户开户许可证
     * @param regAreaCode    基本户注册地地区代码
     * @return 查询成功后，人行账管系统对象
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByAccountKey(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception;

    /**
     * 根据基本户开户许可证、地区代码查询人行账管系统客户信息
     *
     * @param auth        账管用户登录对象
     * @param accountKey  基本户开户许可证
     * @param regAreaCode 基本户注册地地区代码
     * @return 查询成功后，人行账管系统对象
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByAccountKey(LoginAuth auth, String accountKey, String regAreaCode) throws Exception;

    /**
     * 销户时，根据账号, 账户性质查询人行账管系统账户信息
     *
     * @param pbcUserAccount
     * @param acctNo 账号
     * @param acctType 账户性质
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(PbcUserAccount pbcUserAccount, String acctNo, SyncAcctType acctType) throws  Exception;

    /**
     * 销户时，根据账号, 账户性质查询人行账管系统账户信息
     *
     * @param auth
     * @param acctNo 账号
     * @param acctType 账户性质
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByRevokeAcctNoAndAcctType(LoginAuth auth, String acctNo,SyncAcctType acctType) throws  Exception;

    /**
     * 基本户变更根据变更上报第一步查询变更页面详细信息反显
     *
     * @param pbcUserAccount
     * @return
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoByAcctNoFromChangeHtml(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws  Exception;




    /**
     * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
     * @param pbcUserAccount 账管用户登录对象
     * @param accountKey     基本户开户许可证
     * @param regAreaCode    基本户注册地地区代码
     * @return 校验满足开后，校验结果
     * @throws Exception 接口异常
     */
    AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception;

    /**
     * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
     * (开一般户时使用：用于检测基本户和一般户是否开在统一机构下)
     * @param pbcUserAccount 账管用户登录对象
     * @param accountKey     基本户开户许可证
     * @param regAreaCode    基本户注册地地区代码
     * @return 校验满足开后，校验结果
     * @throws Exception 接口异常
     */
    AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(PbcUserAccount pbcUserAccount, String accountKey, String regAreaCode) throws Exception;

    /**
     * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
     *
     * @param auth        账管用户登录对象
     * @param accountKey  基本户开户许可证
     * @param regAreaCode 基本户注册地地区代码
     * @return 校验满足开后，校验结果
     * @throws Exception 接口异常
     */
    AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(LoginAuth auth, String accountKey, String regAreaCode) throws Exception;




    /**
     * 人行账管系统报备（开户、变更、久悬、销户<一般户\非预算专用户>）
     *
     * @param pbcUserAccount 账管用户登录对象
     * @param allAcct        报备对象
     * @throws Exception
     */
    void amsAccountSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception;

    /**
     * 核准类账户上报前删除核准列表
     * @param pbcUserAccount
     * @param allAcct
     * @throws Exception
     */
    void amsHeZhunAccountSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception;

    /**
     * 人行账管系统报备（开户、变更、久悬、销户<一般户\非预算专用户>）
     *
     * @param auth    账管用户登录对象
     * @param allAcct 报备对象
     * @throws Exception
     */
    void amsAccountSync(LoginAuth auth, AllAcct allAcct) throws Exception;

    /**
     * 下载人行账户列表
     *
     * @param pbcUserAccount 登录对象
     * @param task           任务对象
     * @throws Exception
     */
    public void downRHAccount(PbcUserAccount pbcUserAccount, AmsDownTask task) throws  Exception;

    /**
     * 下载人行账户列表
     *
     * @param auth 登录对象
     * @param task 任务对象
     * @throws Exception
     */
    public void downRHAccount(LoginAuth auth, AmsDownTask task) throws  Exception;

    /**
     * 下载人行账户列表,默认从2000年到下载截止日期
     *
     * @param pbcUserAccount
     * @param folderPath     下载的文件夹路径
     * @param bankId         要下载的人行机构号
     * @throws Exception
     */
    public void downRHAccount(PbcUserAccount pbcUserAccount, String folderPath, String bankId) throws  Exception;

    /**
     * 下载人行账户列表,默认从2000年到下载截止日期
     *
     * @param auth
     * @param folderPath 下载的文件夹路径
     * @param bankId     要下载的人行机构号
     * @throws Exception
     */
    public void downRHAccount(LoginAuth auth, String folderPath, String bankId) throws  Exception;

    /**
     * 核准列账户管理列表中是否存在要查询的账号
     *
     * @param pbcUserAccount
     * @param acctNo         待查询账号
     * @return
     * @throws Exception
     */
    boolean existAccountInCheckList(PbcUserAccount pbcUserAccount, String acctNo) throws  Exception;

    /**
     * 核准列账户管理列表中是否存在要查询的账号
     *
     * @param auth
     * @param acctNo 待查询账号
     * @return
     * @throws Exception
     */
    boolean existAccountInCheckList(LoginAuth auth, String acctNo) throws  Exception;

    /**
     * 删除在待核准信息管理列表中的账号
     *
     * @param pbcUserAccount
     * @param acctNo
     * @throws Exception
     */
    boolean deleteAcctNoInCheckList(PbcUserAccount pbcUserAccount, String acctNo) throws  Exception;

    /**
     * 删除在待核准信息管理列表中的账号
     *
     * @param auth
     * @param acctNo
     * @throws Exception
     */
    boolean deleteAcctNoInCheckList(LoginAuth auth, String acctNo) throws  Exception;

    /**
     * 查询存款人的所有账户信息
     * @param pbcUserAccount
     * @param allAcct
     * @throws Exception
     */
    void searchAllAccount(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception;
}
