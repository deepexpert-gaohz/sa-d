package com.ideatech.ams.initializer;

import com.ideatech.ams.system.permission.dao.PermissionDao;
import com.ideatech.ams.system.permission.dao.RolePermissionDao;
import com.ideatech.ams.system.permission.entity.PermissionPo;
import com.ideatech.ams.system.permission.entity.RolePermissionPo;
import com.ideatech.ams.system.role.dao.RoleDao;
import com.ideatech.ams.system.role.entity.RolePo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("jnnsRolePermissionInitializer")
public class JnnsRolePermissionInitializer extends AbstractDataInitializer {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleDao roleDao;

    private List<PermissionPo> permissionList = new ArrayList<>();

    @Override
    protected void doInit() throws Exception {
        PermissionPo indexPermission = new PermissionPo("indexHtml", "首页", -1L, "../ui/about.html", "fa-book", "menu", -1L, "GET", "首页", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(indexPermission);

//        PermissionPo applyPermission = new PermissionPo("apply_manager", "账户预约管理", -1L, "", "fa-calendar", "menu", 10000L, "GET", "账户预约管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applyPermission);
//        PermissionPo applyRecordPermission = new PermissionPo("apply_record", "预约记录", applyPermission.getId(), "../ui/bank/list.html", "fa-list", "menu", 100L, "GET", "预约记录", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applyRecordPermission);
//        PermissionPo applyingPermission = new PermissionPo("applying", "待受理", applyPermission.getId(), "../ui/bank/pre.html", "fa-calendar-minus-o", "menu", 200L, "GET", "待受理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applyingPermission);
//        PermissionPo applyedPermission = new PermissionPo("applyed", "已受理", applyPermission.getId(), "../ui/bank/after.html", "fa-calendar-o", "menu", 300L, "GET", "已受理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applyedPermission);
//        PermissionPo applySuccessPermission = new PermissionPo("apply_success", "受理成功", applyPermission.getId(), "../ui/bank/success.html", "fa-calendar-check-o", "menu", 400L, "GET", "受理成功", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applySuccessPermission);
//        PermissionPo applyFailPermission = new PermissionPo("apply_fail", "受理失败", applyPermission.getId(), "../ui/bank/fail.html", "fa-calendar-times-o", "menu", 500L, "GET", "受理失败", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(applyFailPermission);

        PermissionPo customerSearchPermission = new PermissionPo("customerManager_search", "客户综合尽调", -1L, "", "fa-search-plus", "menu", 20000L, "GET", "客户综合尽调", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerSearchPermission);
//        PermissionPo jibenUniqueValidatePermission = new PermissionPo("jibenUnique_search", "基本户唯一性校验", customerSearchPermission.getId(), "../ui/validate/unique.html?type=menu", "fa-adjust", "menu", 100L, "GET", "基本户唯一性校验", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(jibenUniqueValidatePermission);
//
//        PermissionPo jibenCancelValidatePermission = new PermissionPo("jibenCancel_search", "基本户销户校验", customerSearchPermission.getId(), "../ui/search/revokeValidate.html", "fa fa-ban", "menu", 100L, "GET", "基本户销户校验", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(jibenCancelValidatePermission);
//
        PermissionPo pbcResultByAcctNoPermission = new PermissionPo("pbc_result_byAcctNo", "账号查人行信息", customerSearchPermission.getId(), "../ui/search/pbcResultByAcctNo.html", "fa-search", "menu", 200L, "GET", "账号查人行信息", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(pbcResultByAcctNoPermission);
        PermissionPo pbcResultByAccountKeyPermission = new PermissionPo("pbc_result_byAccountKey", "开户许可证查人行", customerSearchPermission.getId(), "../ui/search/pbcResultByAccountKey.html", "fa-newspaper-o", "menu", 300L, "GET", "开户许可证查人行信息", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(pbcResultByAccountKeyPermission);
//        PermissionPo eccsResultSearchPermission = new PermissionPo("eccs_result_search", "查询代码证系统", customerSearchPermission.getId(), "../ui/search/eccsResultSearch.html", "fa-eraser", "menu", 400L, "GET", "查询代码证系统信息", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(eccsResultSearchPermission);
//        PermissionPo saicInfoByNamePermission = new PermissionPo("saicInfo_byName", "工商基本信息查询", customerSearchPermission.getId(), "../ui/search/saicInfoByName.html", "fa-desktop", "menu", 500L, "GET", "工商基本信息查询", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(saicInfoByNamePermission);


        PermissionPo kycMgtPermission = new PermissionPo("kyc_manager", "客户尽调", -1L, "../ui/kyc/list.html", "fa-search", "menu", 30000L, "GET", "客户尽调", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(kycMgtPermission);

//        PermissionPo customerManagerPermission = new PermissionPo("customer_manager", "客户智能管理", -1L, "../ui/customer/list.html", "fa-users", "menu", 40000L, "GET", "客户智能管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(customerManagerPermission);

        PermissionPo acctPermission = new PermissionPo("acc_manager", "账户智能管理", -1L, "", "fa-address-book-o", "menu", 50000L, "GET", "账户智能管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(acctPermission);
        // PermissionPo companyAccountsWhiteListPermission = new PermissionPo("companyAccounts_manager_whiteList", "白名单业务管理", acctPermission.getId(), "../ui/company/companyAccounts.html?whiteList=1", "fa-clone", "menu", 100L, "GET", "白名单业务管理", Boolean.TRUE, Boolean.FALSE);
        //saveAndAddtoList(companyAccountsWhiteListPermission);
        PermissionPo daibuluPermission = new PermissionPo("daibulu_manager", "待补录业务", acctPermission.getId(), "../ui/company/daibulu.html", "fa-plus-square-o", "menu", 200L, "GET", "待补录业务", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(daibuluPermission);
        PermissionPo companyAccountsPermission = new PermissionPo("companyAccounts_manager", "对公业务管理", acctPermission.getId(), "../ui/company/companyAccounts.html", "fa-address-card-o", "menu", 300L, "GET", "对公报备管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(companyAccountsPermission);
//        PermissionPo resetPwdPermission = new PermissionPo("reset_pwd", "存款人密码重置", acctPermission.getId(), "../ui/selectpwd/reset.html", "fa-refresh", "menu", 400L, "GET", "存款人密码重置", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(resetPwdPermission);
//        PermissionPo jibenResetPrintPermission = new PermissionPo("jibenResetPrint", "基本户补打", acctPermission.getId(), "../ui/jibenResetPrint/jibenReset.html", "fa-refresh", "menu", 400L, "GET", "基本户补打", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(jibenResetPrintPermission);

        PermissionPo todayPbcDataPermission = new PermissionPo("todayPbcData", "当日开变销", acctPermission.getId(), "../ui/jnns/list.html", "fa-balance-scale", "menu", 500L, "GET", "当日报送数据", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(todayPbcDataPermission);

        PermissionPo customerReportPermission = new PermissionPo("statistics_report", "账户报表统计", -1L, "", "fa-area-chart", "menu", 60000L, "GET", "账户报表统计", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerReportPermission);
        PermissionPo statisticsInfoPermission = new PermissionPo("statistics_infoManager", "账户信息统计", customerReportPermission.getId(), "../ui/company/businessStatisticsInfo.html", "fa-bar-chart", "menu", 100L, "GET", "账户信息统计", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(statisticsInfoPermission);
        PermissionPo statisticsPermission = new PermissionPo("statistics_manager", "账户开变销统计", customerReportPermission.getId(), "../ui/company/businessStatistics.html", "fa-line-chart", "menu", 200L, "GET", "开变销统计", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(statisticsPermission);
        PermissionPo statisticsOpenAccountPermission = new PermissionPo("statistics_open_account", "账户开户报送统计", customerReportPermission.getId(), "../ui/company/businessStatisticsOpen.html", "fa-signal", "menu", 300L, "GET", "账户开户报送统计", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(statisticsOpenAccountPermission);

        PermissionPo imagePermission = new PermissionPo("image_manager", "影像管理", -1L, "", "fa-image", "menu", 70000L, "GET", "影像管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(imagePermission);

        PermissionPo imageBillPermission = new PermissionPo("image_bill", "业务影像信息", imagePermission.getId(), "../ui/jnns/imageList.html", "fa-book", "menu", 300L, "GET", "业务影像信息", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(imageBillPermission);

//        PermissionPo comparePermission = new PermissionPo("compare_manager_menu", "比对管理", -1L, "../ui/comparison/taskManager.html", "fa-balance-scale", "menu", 80000L, "GET", "比对管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(comparePermission);

        PermissionPo annualPermission = new PermissionPo("annual_manager_menu", "年检管理", -1L, "", "fa-gavel", "menu", 90000L, "GET", "年检管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualPermission);
        PermissionPo annualTaskPermission = new PermissionPo("annual_manager", "年检任务", annualPermission.getId(), "../ui/annualcheck/page.html", "fa-tasks", "menu", 100L, "GET", "年检任务", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualTaskPermission);
        PermissionPo annualHistoryPermission = new PermissionPo("annualHistory_manager", "历史年检", annualPermission.getId(), "../ui/annualcheck/history.html", "fa-hourglass", "menu", 200L, "GET", "历史年检", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualHistoryPermission);

//        PermissionPo noticePermission = new PermissionPo("noticeManager", "证件到期提醒", -1L, "", "fa-bell-o", "menu", 100000L, "GET", "证件到期提醒", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(noticePermission);
//        PermissionPo legalDuePermission = new PermissionPo("legalDueNoticeCode", "法人证件到期提醒", noticePermission.getId(), "../ui/notice/legalDueList.html?noticeType=legalDueNotice", "fa-address-card-o", "menu", 100L, "GET", "法人证件到期提醒", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(legalDuePermission);
//        PermissionPo fileDuePermission = new PermissionPo("fileDueNoticeCode", "证明文件到期提醒", noticePermission.getId(), "../ui/notice/legalDueList.html?noticeType=fileDueNotice", "fa-file-image-o", "menu", 200L, "GET", "证明文件到期提醒", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(fileDuePermission);
//        PermissionPo operateDuePermission = new PermissionPo("operatorIdcardDueCode", "经办人证件到期提醒", noticePermission.getId(), "../ui/notice/operatorIdcardDueList.html?noticeType=operatorDueNotice", "fa-id-card-o", "menu", 300L, "GET", "经办人证件到期提醒", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(operateDuePermission);

        //修改
//        PermissionPo batchComparisonPermission = new PermissionPo("beneficiary_manager", "批量校验任务", -1L, "", "fa-hourglass-half", "menu", 110000L, "GET", "批量校验任务", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(batchComparisonPermission);
//        //新增
//        PermissionPo shareholderComparisonPermission = new PermissionPo("shareholderComparison", "批量股东信息比对", batchComparisonPermission.getId(), "../ui/beneficiary/stockHolderList.html",
//                "fa-gavel", "menu", 100L, "GET", "批量股东信息比对", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(shareholderComparisonPermission);
//        PermissionPo beneficiaryNameComparisonPermission = new PermissionPo("beneficiaryNameComparison", "批量受益人名称比对", batchComparisonPermission.getId(), "../ui/beneficiary/beneficiaryNameList.html",
//                "fa-window-restore", "menu", 200L, "GET", "批量受益人名称比对", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(beneficiaryNameComparisonPermission);
//        PermissionPo beneficiaryComparisonPermission = new PermissionPo("beneficiaryComparison", "批量受益人比对", batchComparisonPermission.getId(), "../ui/beneficiary/beneficiaryList.html",
//                "fa-user", "menu", 300L, "GET", "批量受益人比对", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(beneficiaryComparisonPermission);
//        PermissionPo operatorComparisonPermission = new PermissionPo("operatorComparison", "批量电信运营商校验", batchComparisonPermission.getId(), "../ui/beneficiary/telecomList.html",
//                "fa-desktop", "menu", 400L, "GET", "批量电信运营商校验", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(operatorComparisonPermission);

        //新增
//        PermissionPo batchCollectPermission = new PermissionPo("batchCollectTask", "批量采集任务", -1L, "", "fa-inbox", "menu", 120000L, "GET", "批量采集任务", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(batchCollectPermission);
//        //修改 url路径
//        PermissionPo illegalPermission = new PermissionPo("illegal_manager", "批量违法采集", batchCollectPermission.getId(), "../ui/illegal/list.html", "fa-search-plus", "menu",
//                100L, "GET", "批量违法采集", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(illegalPermission);
//        //新增
//        PermissionPo batchBeneficiaryCollectPermission = new PermissionPo("batchBeneficiaryCollect", "批量受益人采集", batchCollectPermission.getId(), "../ui/beneficiary/beneficiaryCollectList.html", "fa-user", "menu",
//                200L, "GET", "批量受益人采集", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(batchBeneficiaryCollectPermission);
//        PermissionPo batchKycPermission = new PermissionPo("batchKyc", "批量客户尽调", batchCollectPermission.getId(), "../ui/beneficiary/customerTuneList.html", "fa-users", "menu",
//                300L, "GET", "批量客户尽调", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(batchKycPermission);

//        PermissionPo freshCompanyPermission = new PermissionPo("fresh_company_manager", "新注册企业", -1L, "../ui/newCompany/list.html", "fa-building-o", "menu", 130000L, "GET", "新注册企业", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(freshCompanyPermission);

//        PermissionPo systemMonitorPermission = new PermissionPo("system_interface_monitor", "接口统计", -1L, "", "fa-pie-chart", "menu", 140000L, "GET", "接口统计", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(systemMonitorPermission);
//        PermissionPo statisticePermission = new PermissionPo("statistice_manager", "人行报送统计", systemMonitorPermission.getId(), "../ui/interfaceMonitor/list.html", "fa-exchange", "menu", 100L, "GET", "人行报送统计", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(statisticePermission);
//        PermissionPo saicMonitorPermission = new PermissionPo("system_saic_monitor", "工商查询统计", systemMonitorPermission.getId(), "../ui/saicInterfaceMonitor/list.html", "fa-usb", "menu", 200L, "GET", "工商查询统计", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(saicMonitorPermission);

        PermissionPo sysMgtPermission = new PermissionPo("sys_manager", "系统配置", -1L, "", "fa-cogs", "menu", 200000L, "GET", "系统配置", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysMgtPermission);
        PermissionPo sysRolePermission = new PermissionPo("sys_role_manager", "角色管理", sysMgtPermission.getId(), "../ui/group/list.html", "fa-address-book-o", "menu", 100L, "GET", "角色管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysRolePermission);
        PermissionPo sysUserPermission = new PermissionPo("sys_user_manager", "用户管理", sysMgtPermission.getId(), "../ui/user/list.html", "fa-user", "menu", 200L, "GET", "用户管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysUserPermission);
        PermissionPo sysMenuPermission = new PermissionPo("sys_menu_manager", "菜单管理", sysMgtPermission.getId(), "../ui/menu/list.html", "fa-align-justify", "menu", 300L, "GET", "菜单管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysMenuPermission);
        PermissionPo sysOrgPermission = new PermissionPo("sys_org_manager", "组织机构管理", sysMgtPermission.getId(), "../ui/organization/list.html", "fa-university", "menu", 400L, "GET", "组织机构管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysOrgPermission);
        PermissionPo sysConfigPermission = new PermissionPo("sys_config_manager", "系统配置管理", sysMgtPermission.getId(), "../ui/config/detail.html", "fa-server", "menu", 500L, "GET", "系统配置管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysConfigPermission);
        PermissionPo sysDictPermission = new PermissionPo("sys_dict_manager", "数据字典管理", sysMgtPermission.getId(), "../ui/dict/list.html", "fa-book", "menu", 600L, "GET", "数据字典管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysDictPermission);
        /*PermissionPo sysImagePermission = new PermissionPo("sys_image_manager", "影像配置管理", sysMgtPermission.getId(), "../ui/image/imageTypeManger.html", "fa-list", "menu", 0L, "GET", "影像配置管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysImagePermission);*/
//        PermissionPo sysMetaPermission = new PermissionPo("sys_meta_manager", "元数据管理", sysMgtPermission.getId(), "../ui/meta/list.html", "fa-folder-open-o", "menu", 800L, "GET", "元数据管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysMetaPermission);
        PermissionPo sysLogPermission = new PermissionPo("sys_log_manager", "日志管理", sysMgtPermission.getId(), "../ui/log/list.html", "fa-file-text-o", "menu", 900L, "GET", "日志管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(sysLogPermission);
//        PermissionPo sysBlackListPermission = new PermissionPo("sys_blacklist_manager", "黑名单管理", sysMgtPermission.getId(), "../ui/blacklist/list.html", "fa-ban", "menu", 1000L, "GET", "黑名单管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackListPermission);
//        PermissionPo sysWhiteListPermission = new PermissionPo("sys_whitelist_manager", "白名单管理", sysMgtPermission.getId(), "../ui/whiteList/list.html", "fa-bookmark", "menu", 1100L, "GET", "白名单管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteListPermission);
//        PermissionPo holidayPermission = new PermissionPo("holiday_manager", "节假日管理", sysMgtPermission.getId(), "../ui/holiday/list.html", "fa-calendar", "menu", 1200L, "GET", "节假日管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(holidayPermission);
//        PermissionPo sysTemplatePermission = new PermissionPo("sys_template_manager", "打印管理", sysMgtPermission.getId(), "../ui/template/list.html", "fa-print", "menu", 1300L, "GET", "打印管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysTemplatePermission);
//        PermissionPo announcementPermission = new PermissionPo("sys_announcement_manager", "公告管理", sysMgtPermission.getId(), "../ui/announcement/list.html", "fa-bullhorn", "menu", 1400L, "GET", "公告管理", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(announcementPermission);

        //按钮权限

        //首页区块权限
        PermissionPo addAcctDivPermission = new PermissionPo("addAcct_div", "新增账户", indexPermission.getId(), "../ui/about.html", "", "button", 41L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(addAcctDivPermission);
        PermissionPo addBusinessDivPermission = new PermissionPo("addBusiness_div", "新增业务", indexPermission.getId(), "../ui/about.html", "", "button", 42L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(addBusinessDivPermission);
        PermissionPo dealtRemindDivPermission = new PermissionPo("dealtRemind_div", "待办提醒", indexPermission.getId(), "../ui/about.html", "", "button", 43L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dealtRemindDivPermission);
        PermissionPo preUnCompleteDivPermission = new PermissionPo("preUnComplete_div", "预约待受理", indexPermission.getId(), "../ui/about.html", "", "button", 44L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(preUnCompleteDivPermission);
        PermissionPo zhshCheckDivPermission = new PermissionPo("zhshCheck_div", "申请待审核", indexPermission.getId(), "../ui/about.html", "", "button", 45L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(zhshCheckDivPermission);
        PermissionPo dsbCheckDivPermission = new PermissionPo("dsbCheck_div", "审核待上报", indexPermission.getId(), "../ui/about.html", "", "button", 46L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dsbCheckDivPermission);
        PermissionPo dbllbDivPermission = new PermissionPo("dbllb_div", "失败待补录", indexPermission.getId(), "../ui/about.html", "", "button", 47L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dbllbDivPermission);
        PermissionPo passRemindDivPermission = new PermissionPo("passRemind_div", "通过提醒", indexPermission.getId(), "../ui/about.html", "", "button", 48L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(passRemindDivPermission);
        PermissionPo preSuccessDivPermission = new PermissionPo("preSuccess_div", "预约受理成功", indexPermission.getId(), "../ui/about.html", "", "button", 49L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(preSuccessDivPermission);
        PermissionPo syncSuccsssDivPermission = new PermissionPo("syncSuccsss_div", "上报成功", indexPermission.getId(), "../ui/about.html", "", "button", 50L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(syncSuccsssDivPermission);
        PermissionPo hzSuccsssDivPermission = new PermissionPo("hzSuccsss_div", "核准成功", indexPermission.getId(), "../ui/about.html", "", "button", 51L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(hzSuccsssDivPermission);


//        PermissionPo listViewBtnPermission = new PermissionPo("apply:view_list", "查看", applyRecordPermission.getId(), "../ui/bank/list", "", "button", 52L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(listViewBtnPermission);
//        PermissionPo listKycBtnPermission = new PermissionPo("apply:kyc_list", "客户尽调", applyRecordPermission.getId(), "../ui/bank/list", "", "button", 53L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(listKycBtnPermission);
//        PermissionPo listSyncBtnPermission = new PermissionPo("apply:sync_list", "全数据同步", applyRecordPermission.getId(), "../ui/bank/list", "", "button", 54L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(listSyncBtnPermission);
//        PermissionPo preAddBtnPermission = new PermissionPo("apply:add_pre", "接洽", applyingPermission.getId(), "../ui/bank/pre", "", "button", 55L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(preAddBtnPermission);
//        PermissionPo preKhjdBtnPermission = new PermissionPo("apply:kyc_pre", "客户尽调", applyingPermission.getId(), "../ui/bank/pre", "", "button", 56L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(preKhjdBtnPermission);
//        PermissionPo preSyncBtnPermission = new PermissionPo("apply:sync_pre", "全数据同步", applyingPermission.getId(), "../ui/bank/pre", "", "button", 57L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(preSyncBtnPermission);
//        PermissionPo afterViewBtnPermission = new PermissionPo("apply:view_after", "查看", applyedPermission.getId(), "../ui/bank/after", "", "button", 58L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(afterViewBtnPermission);
//        PermissionPo afterAddBtnPermission = new PermissionPo("apply:kyc_after", "客户尽调", applyedPermission.getId(), "../ui/bank/after", "", "button", 60L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(afterAddBtnPermission);
//        PermissionPo afterSyncBtnPermission = new PermissionPo("apply:sync_after", "全数据同步", applyedPermission.getId(), "../ui/bank/after", "", "button", 61L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(afterSyncBtnPermission);
//        PermissionPo sendHistoryBtnPermission = new PermissionPo("apply:send_history", "短信发送历史", applyedPermission.getId(), "../ui/bank/after", "", "button", 62L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sendHistoryBtnPermission);
//
//        PermissionPo successViewBtnPermission = new PermissionPo("apply:view_success", "查看", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 63L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successViewBtnPermission);
//        PermissionPo successAccountBtnPermission = new PermissionPo("apply:account_success", "账户开户", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 64L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successAccountBtnPermission);
//        PermissionPo successKycBtnPermission = new PermissionPo("apply:kyc_success", "客户尽调", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 65L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successKycBtnPermission);
//        PermissionPo successSyncBtnPermission = new PermissionPo("apply:sync_success", "全数据同步", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 66L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successSyncBtnPermission);
//        PermissionPo successSuccessBtnPermission = new PermissionPo("apply:register_success", "开户成功", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 67L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successSuccessBtnPermission);
//        PermissionPo successFailBtnPermission = new PermissionPo("apply:register_fail", "开户失败", applySuccessPermission.getId(), "../ui/bank/success", "", "button", 68L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(successFailBtnPermission);
//
//        PermissionPo failAddBtnPermission = new PermissionPo("apply:kyc_fail", "客户尽调", applyFailPermission.getId(), "../ui/bank/fail", "", "button", 69L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(failAddBtnPermission);
//        PermissionPo failViewBtnPermission = new PermissionPo("apply:view_fail", "查看", applyFailPermission.getId(), "../ui/bank/fail", "", "button", 70L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(failViewBtnPermission);
//        PermissionPo failSyncBtnPermission = new PermissionPo("apply:sync_fail", "全数据同步", applyFailPermission.getId(), "../ui/bank/fail", "", "button", 71L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(failSyncBtnPermission);

        PermissionPo buluBtnPermission = new PermissionPo("apply:bulu_btn", "补录", daibuluPermission.getId(), "../ui/company/daibulu.html", "", "button", 72L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(buluBtnPermission);

        PermissionPo bbcgBtnPermission = new PermissionPo("account:btn_bbcg", "报备成功", daibuluPermission.getId(), "../ui/company/daibulu.html", "", "button", 73L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(bbcgBtnPermission);
        PermissionPo deleteBtnPermission = new PermissionPo("account:btn_delete", "删除", daibuluPermission.getId(), "../ui/company/daibulu.html", "", "button", 73L, "GET", "删除", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(deleteBtnPermission);

        PermissionPo accountOpenBtnPermission = new PermissionPo("account:open", "开户", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 74L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountOpenBtnPermission);
        PermissionPo accountViewBtnPermission = new PermissionPo("account:view", "查看", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 75L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountViewBtnPermission);

        //比对管理按钮
//        PermissionPo compareDataSourceBtnPermission = new PermissionPo("compare:datasource", "数据源配置", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareDataSourceBtnPermission);
//        PermissionPo compareDataSourceAddBtnPermission = new PermissionPo("compare:datasource-add", "数据源-新建", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareDataSourceAddBtnPermission);
//        PermissionPo compareDataSourceModifyBtnPermission = new PermissionPo("compare:datasource-modify", "数据源-修改", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareDataSourceModifyBtnPermission);
//        PermissionPo compareDataSourceDelBtnPermission = new PermissionPo("compare:datasource-del", "数据源-删除", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareDataSourceDelBtnPermission);
//        PermissionPo compareRuleBtnPermission = new PermissionPo("compare:rule", "比对规则配置", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareRuleBtnPermission);
//        PermissionPo compareRuleAddBtnPermission = new PermissionPo("compare:rule-add", "规则-新建", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareRuleAddBtnPermission);
//        PermissionPo compareRuleModifyBtnPermission = new PermissionPo("compare:rule-modify", "规则-修改", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareRuleModifyBtnPermission);
//        PermissionPo compareRuleDelBtnPermission = new PermissionPo("compare:rule-del", "规则-删除", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareRuleDelBtnPermission);
//        PermissionPo compareTaskBtnPermission = new PermissionPo("compare:task", "比对任务配置", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskBtnPermission);
//        PermissionPo compareTaskAddBtnPermission = new PermissionPo("compare:task-add", "比对任务配置-新建", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskAddBtnPermission);
//        PermissionPo compareTaskModifyBtnPermission = new PermissionPo("compare:task-modify", "比对任务配置-修改", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskModifyBtnPermission);
//        PermissionPo compareTaskDelBtnPermission = new PermissionPo("compare:task-del", "比对任务配置-删除", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskDelBtnPermission);
//        PermissionPo compareTaskStartBtnPermission = new PermissionPo("compare:task-start", "比对-开始", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskStartBtnPermission);
//        PermissionPo compareTaskResetBtnPermission = new PermissionPo("compare:task-reset", "比对-重置", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskResetBtnPermission);
//        PermissionPo compareTaskCancelBtnPermission = new PermissionPo("compare:task-cancel", "比对-取消", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareTaskCancelBtnPermission);
//        PermissionPo compareResultBtnPermission = new PermissionPo("compare:result", "比对结果", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareResultBtnPermission);
//        PermissionPo compareResultExcelBtnPermission = new PermissionPo("compare:result-excel", "比对结果-excel", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareResultExcelBtnPermission);
//        PermissionPo compareResultWordBtnPermission = new PermissionPo("compare:result-word", "比对结果-word", comparePermission.getId(), "../ui/comparison/taskManager.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(compareResultWordBtnPermission);

        PermissionPo roleAddBtnPermission = new PermissionPo("apply:role_add", "新增", sysRolePermission.getId(), "../ui/group/list.html", "", "button", 76L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(roleAddBtnPermission);
        PermissionPo roleEditBtnPermission = new PermissionPo("apply:role_edit", "修改", sysRolePermission.getId(), "../ui/group/list.html", "", "button", 77L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(roleEditBtnPermission);
        PermissionPo roleDelBtnPermission = new PermissionPo("apply:role_del", "修改", sysRolePermission.getId(), "../ui/group/list.html", "", "button", 78L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(roleDelBtnPermission);
        PermissionPo roleManagerBtnPermission = new PermissionPo("apply:role_Manager", "权限管理", sysRolePermission.getId(), "../ui/group/list.html", "", "button", 79L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(roleManagerBtnPermission);


        PermissionPo userAddBtnPermission = new PermissionPo("apply:user_Add", "新增", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 80L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userAddBtnPermission);
        PermissionPo userEditBtnPermission = new PermissionPo("apply:user_Edit", "修改", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 81L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userEditBtnPermission);
        PermissionPo userDelBtnPermission = new PermissionPo("apply:user_Del", "删除", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 82L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userDelBtnPermission);
        PermissionPo userResetBtnPermission = new PermissionPo("apply:user_reset", "重置密码", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 82L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userResetBtnPermission);
        PermissionPo userUploadBtUnPermission = new PermissionPo("apply:user_Upload", "导入", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 83L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userUploadBtUnPermission);
        PermissionPo userDownBtUnPermission = new PermissionPo("apply:user_Down", "下载模板", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 84L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userDownBtUnPermission);
        PermissionPo userOnlineBtUnPermission = new PermissionPo("apply:user_Online", "在线用户列表", sysUserPermission.getId(), "../ui/user/list.html", "", "button", 85L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(userOnlineBtUnPermission);

        PermissionPo menuAddBtnPermission = new PermissionPo("apply:menu_Add", "新增", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 86L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(menuAddBtnPermission);
        PermissionPo menuEditBtnPermission = new PermissionPo("apply:menu_Edit", "修改", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 87L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(menuEditBtnPermission);
        PermissionPo menuDelBtnPermission = new PermissionPo("apply:menu_Del", "删除", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 88L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(menuDelBtnPermission);
        PermissionPo functionAddBtnPermission = new PermissionPo("apply:function_Add", "添加", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 89L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(functionAddBtnPermission);
        PermissionPo functionEditBtnPermission = new PermissionPo("apply:function_Edit", "修改", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 90L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(functionEditBtnPermission);
        PermissionPo functionDelBtnPermission = new PermissionPo("apply:function_Del", "移除", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 91L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(functionDelBtnPermission);
        PermissionPo functionUpBtnPermission = new PermissionPo("apply:menu_Up", "上移", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 92L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(functionUpBtnPermission);
        PermissionPo functionDownBtnPermission = new PermissionPo("apply:menu_Down", "下移", sysMenuPermission.getId(), "../ui/menu/list.html", "", "button", 93L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(functionDownBtnPermission);

        PermissionPo organAddBtnPermission = new PermissionPo("apply:organ_Add", "新建", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 94L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organAddBtnPermission);
        PermissionPo organEditBtnPermission = new PermissionPo("apply:organ_Edit", "修改", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 95L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organEditBtnPermission);
        PermissionPo organDelBtnPermission = new PermissionPo("apply:organ_Del", "删除", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 96L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organDelBtnPermission);
        PermissionPo organRefeshBtnPermission = new PermissionPo("apply:organ_Refesh", "刷新", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 97L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organRefeshBtnPermission);
        PermissionPo organPbcAccountBtnPermission = new PermissionPo("apply:organ_PbcAccount", "人行账号", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 98L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organPbcAccountBtnPermission);
        PermissionPo organImportBtnPermission = new PermissionPo("apply:organ_Import", "导入", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 99L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organImportBtnPermission);
        PermissionPo organDownBtnPermission = new PermissionPo("apply:organ_Down", "下载模版", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 100L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organDownBtnPermission);
        PermissionPo organAccountImportBtnPermission = new PermissionPo("apply:organ_account_Import", "导入账号", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 99L, "GET", "导入账号", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organAccountImportBtnPermission);
        PermissionPo organAccountDownBtnPermission = new PermissionPo("apply:organ_account_Down", "下载账号模版", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 100L, "GET", "下载账号模版", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organAccountDownBtnPermission);

        PermissionPo organQianBingPermission = new PermissionPo("apply:organ_qianbing", "机构迁并", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 101L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organQianBingPermission);

        PermissionPo organSyncPermission = new PermissionPo("apply:organ_syncRecord", "机构同步记录", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 101L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organSyncPermission);

        PermissionPo organAddUserBtnPermission = new PermissionPo("organ:add_user", "添加用户", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 102L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organAddUserBtnPermission);
        PermissionPo organEditUserBtnPermission = new PermissionPo("organ:edit_user", "修改用户", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 103L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organEditUserBtnPermission);
        PermissionPo organRemoveUserBtnPermission = new PermissionPo("organ:remove_user", "用户移除", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 104L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organRemoveUserBtnPermission);
        PermissionPo organEnableUserBtnPermission = new PermissionPo("organ:enable_user", "用户启用", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 105L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organEnableUserBtnPermission);
        PermissionPo organDisableUserBtnPermission = new PermissionPo("organ:disable_user", "用户禁用", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 106L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organDisableUserBtnPermission);

        PermissionPo dictAddBtnPermission = new PermissionPo("apply:dict_Add", "新建", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 107L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictAddBtnPermission);
        PermissionPo dictEditBtnPermission = new PermissionPo("apply:dict_Edit", "修改", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 108L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictEditBtnPermission);
        PermissionPo dictDelBtnPermission = new PermissionPo("apply:dict_Del", "删除", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 109L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictDelBtnPermission);
        PermissionPo dictManagerAddBtnPermission = new PermissionPo("apply:dictManager_Add", "添加", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 110L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictManagerAddBtnPermission);
        PermissionPo dictManagerEditBtnPermission = new PermissionPo("apply:dictManager_Edit", "修改", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 111L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictManagerEditBtnPermission);
        PermissionPo dictManagerDelBtnPermission = new PermissionPo("apply:dictManager_Del", "移除", sysDictPermission.getId(), "../ui/dict/list.html", "", "button", 112L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dictManagerDelBtnPermission);

        /*PermissionPo imageAddBtnPermission = new PermissionPo("apply:image_Add", "新建", sysImagePermission.getId(), "../ui/image/imageTypeManger.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(imageAddBtnPermission);
        PermissionPo imageEditBtnPermission = new PermissionPo("apply:image_Edit", "修改", sysImagePermission.getId(), "../ui/image/imageTypeManger.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(imageEditBtnPermission);
        PermissionPo imageDelBtnPermission = new PermissionPo("apply:image_Del", "删除", sysImagePermission.getId(), "../ui/image/imageTypeManger.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(imageDelBtnPermission);*/

//        PermissionPo metaAddBtnPermission = new PermissionPo("apply:meta_Add", "新建", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 113L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaAddBtnPermission);
//        PermissionPo metaEditBtnPermission = new PermissionPo("apply:meta_Edit", "修改", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 114L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaEditBtnPermission);
//        PermissionPo metaDelBtnPermission = new PermissionPo("apply:meta_Del", "删除", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 115L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaDelBtnPermission);
//        PermissionPo metaProAddBtnPermission = new PermissionPo("apply:metaPro_Add", "添加", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 116L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaProAddBtnPermission);
//        PermissionPo metaProEditBtnPermission = new PermissionPo("apply:metaPro_Edit", "修改", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 117L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaProEditBtnPermission);
//        PermissionPo metaProDelBtnPermission = new PermissionPo("apply:metaPro_Del", "移除", sysMetaPermission.getId(), "../ui/meta/list.html", "", "button", 118L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(metaProDelBtnPermission);

        PermissionPo logDownBtnPermission = new PermissionPo("apply:log_Down", "打包下载", sysLogPermission.getId(), "../ui/log/list.html", "", "button", 119L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(logDownBtnPermission);

//        PermissionPo sysBlackAddBtnPermission = new PermissionPo("apply:black_Add", "新建", sysBlackListPermission.getId(), "../ui/blacklist/list.html", "", "button", 120L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackAddBtnPermission);
//        PermissionPo sysBlackEditBtnPermission = new PermissionPo("apply:black_Edit", "修改", sysBlackListPermission.getId(), "../ui/blacklist/list.html", "", "button", 121L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackEditBtnPermission);
//        PermissionPo sysBlackDelBtnPermission = new PermissionPo("apply:black_Del", "删除", sysBlackListPermission.getId(), "../ui/blacklist/list.html", "", "button", 122L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackDelBtnPermission);
//        PermissionPo sysBlackImportBtnPermission = new PermissionPo("apply:black_Import", "导入", sysBlackListPermission.getId(), "../ui/blacklist/list.html", "", "button", 123L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackImportBtnPermission);
//        PermissionPo sysBlackDownBtnPermission = new PermissionPo("apply:black_Down", "下载模版", sysBlackListPermission.getId(), "../ui/blacklist/list.html", "", "button", 124L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysBlackDownBtnPermission);

//        PermissionPo sysWhiteAddBtnPermission = new PermissionPo("apply:white_Add", "新建", sysWhiteListPermission.getId(), "../ui/whiteList/list.html", "", "button", 125L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteAddBtnPermission);
//        PermissionPo sysWhiteEditBtnPermission = new PermissionPo("apply:white_Edit", "修改", sysWhiteListPermission.getId(), "../ui/whiteList/list.html", "", "button", 126L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteEditBtnPermission);
//        PermissionPo sysWhiteDelBtnPermission = new PermissionPo("apply:white_Del", "删除", sysWhiteListPermission.getId(), "../ui/whiteList/list.html", "", "button", 127L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteDelBtnPermission);
//        PermissionPo sysWhiteImportBtnPermission = new PermissionPo("apply:white_Import", "导入", sysWhiteListPermission.getId(), "../ui/whiteList/list.html", "", "button", 128L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteImportBtnPermission);
//        PermissionPo sysWhiteDownBtnPermission = new PermissionPo("apply:white_Down", "下载模版", sysWhiteListPermission.getId(), "../ui/whiteList/list.html", "", "button", 129L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(sysWhiteDownBtnPermission);

        PermissionPo batchSuspendBtnPermission = new PermissionPo("account:batch_suspend", "批量久悬", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 130L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(batchSuspendBtnPermission);

        PermissionPo notActiveDeleteBtnPermission = new PermissionPo("account:notActive_delete", "未激活删除", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 131L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(notActiveDeleteBtnPermission);

        PermissionPo batchSuspendSubmitBtnPermission = new PermissionPo("account:batch_suspend_submit", "久悬提交", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 132L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(batchSuspendSubmitBtnPermission);

        PermissionPo updateAcctTypeBtnPermission = new PermissionPo("account:update_accttype", "账户性质校验", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 133L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(updateAcctTypeBtnPermission);
        PermissionPo openPbcIgnoreBtnPermission = new PermissionPo("account:open_pbcIgnore", "跳过", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 134L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(openPbcIgnoreBtnPermission);

        //工商校验跳过
        PermissionPo openSaicIgnoreBtnPermission = new PermissionPo("account:open_saicIgnore", "工商跳过", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 135L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(openSaicIgnoreBtnPermission);

        //基本户人行唯一性校验
        PermissionPo amsJibenOpenIgnoreBtnPermission = new PermissionPo("account:open_amsJibenIgnore", "基本户唯一性校验跳过", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 135L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(amsJibenOpenIgnoreBtnPermission);

        PermissionPo buluVerifyPermission = new PermissionPo("account:bulu_verify", "审核并上报", daibuluPermission.getId(), "../ui/company/daibulu.html", "", "button", 136L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(buluVerifyPermission);

        //审核通过
        PermissionPo verifyPassPermission = new PermissionPo("account:verifyPass", "审核通过", companyAccountsPermission.getId(), "../ui/accttype/jibenOpen.html", "", "button", 136L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(verifyPassPermission);
        //审核不通过
        PermissionPo verifyNotPassPermission = new PermissionPo("account:verifyNotPass", "审核不通过", companyAccountsPermission.getId(), "../ui/accttype/jibenOpen.html", "", "button", 136L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(verifyNotPassPermission);

//        PermissionPo illegalDownPermission = new PermissionPo("illegal:Batch_Down", "导入模版下载", illegalPermission.getId(), "../ui/illegal/list.html", "", "button", 137L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(illegalDownPermission);
//        PermissionPo illegalUploadPermission = new PermissionPo("illegal:Batch_Upload", "批量上传排查", illegalPermission.getId(), "../ui/illegal/list.html", "", "button", 138L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(illegalUploadPermission);
////        PermissionPo illegalEditPermission = new PermissionPo("illegal:Batch_Edit", "存量排查", illegalPermission.getId(), "../ui/illegal/list.html", "", "button", 0L, "GET", "", Boolean.TRUE, Boolean.FALSE);
////        saveAndAddtoList(illegalEditPermission);
//        PermissionPo illegalDetailDownPermission = new PermissionPo("illegal_Detail:Batch_Down", "明细导出", illegalPermission.getId(), "../ui/illegal/detail.html", "", "button", 139L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(illegalDetailDownPermission);

        //节假日管理按钮
        //holidayPermission
//        PermissionPo addHolidayBtnPermission = new PermissionPo("holiday:add", "新建", holidayPermission.getId(), "../ui/holiday/list.html", "", "button", 140L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(addHolidayBtnPermission);
//        PermissionPo editHolidayBtnPermission = new PermissionPo("holiday:edit", "修改", holidayPermission.getId(), "../ui/holiday/list.html", "", "button", 141L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(editHolidayBtnPermission);
//        PermissionPo delHolidayBtnPermission = new PermissionPo("holiday:del", "删除", holidayPermission.getId(), "../ui/holiday/list.html", "", "button", 142L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(delHolidayBtnPermission);
//        PermissionPo syncHolidayBtnPermission = new PermissionPo("holiday:sync", "同步", holidayPermission.getId(), "../ui/holiday/list.html", "", "button", 143L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(syncHolidayBtnPermission);

        //年检管理按钮
        PermissionPo annualCoreDataBtnPermission = new PermissionPo("annual:core_data", "核心数据", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 144L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualCoreDataBtnPermission);
        PermissionPo annualPbcDataBtnPermission = new PermissionPo("annual:pbc_data", "人行数据", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 145L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualPbcDataBtnPermission);
        PermissionPo annualSaicDataBtnPermission = new PermissionPo("annual:saic_data", "工商数据", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 146L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualSaicDataBtnPermission);
        PermissionPo annualStartCheckBtnPermission = new PermissionPo("annual:start_check", "开始年检比对", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 147L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualStartCheckBtnPermission);
        PermissionPo annualResetBtnPermission = new PermissionPo("annual:reset", "重置", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 148L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualResetBtnPermission);
        PermissionPo annualDataConfigBtnPermission = new PermissionPo("annual:data_config", "采集配置", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 149L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualDataConfigBtnPermission);
        PermissionPo annualFailRecollectPbcBtnPermission = new PermissionPo("annual:fail_recollectPbc", "人行失败记录重新采集", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 150L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualFailRecollectPbcBtnPermission);
        PermissionPo annualFailRecollectSaicBtnPermission = new PermissionPo("annual:fail_recollectSaic", "工商失败记录重新采集", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 151L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualFailRecollectSaicBtnPermission);
        PermissionPo annualResultExportBtnPermission = new PermissionPo("annual:result_export", "导出年检统计", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 152L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualResultExportBtnPermission);
        PermissionPo annualResultExportResultBtnPermission = new PermissionPo("annual:result_exportResult", "导出年检结果", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 153L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualResultExportResultBtnPermission);
        PermissionPo annualBatchSubmitBtnPermission = new PermissionPo("annual:batch_submit", "批量年检提交", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 154L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualBatchSubmitBtnPermission);
        PermissionPo annualSubmitBtnPermission = new PermissionPo("annual:submit", "年检提交", annualTaskPermission.getId(), "../ui/annualcheck/page.html", "", "button", 155L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualSubmitBtnPermission);

//        PermissionPo zhsqPermission = new PermissionPo("companyAccount_zhsq", "账户申请", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 156L, "GET", "账户申请", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(zhsqPermission);
        PermissionPo dblPermission = new PermissionPo("companyAccount_dbl", "待补录", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 157L, "GET", "待补录", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dblPermission);
        PermissionPo dshPermission = new PermissionPo("companyAccount_dsh", "待审核", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 158L, "GET", "待审核", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dshPermission);
//        PermissionPo dsbPermission = new PermissionPo("companyAccount_dsb", "待上报", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 159L, "GET", "待上报", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(dsbPermission);
        PermissionPo bbcgPermission = new PermissionPo("companyAccount_cgsb", "报备成功", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 160L, "GET", "报备成功", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(bbcgPermission);
        PermissionPo hzcgPermission = new PermissionPo("companyAccount_hztg", "核准成功", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "fa-bus", "button", 161L, "GET", "核准成功", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(hzcgPermission);
        PermissionPo clzhPermission = new PermissionPo("companyAccount_clzh", "存量账户", companyAccountsPermission.getId(), "../ui/company/clzhAccount.html", "fa-bus", "button", 162L, "GET", "存量账户", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(clzhPermission);

//        PermissionPo freshCompanyCollectBtnPermission = new PermissionPo("company:fresh_config", "定时任务采集配置", freshCompanyPermission.getId(), "../ui/newCompany/list.html", "", "button", 163L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(freshCompanyCollectBtnPermission);

//        PermissionPo verifyUploadBtnPermission = new PermissionPo("verify:upload", "上传文件开始批量任务", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 164L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyUploadBtnPermission);
//        PermissionPo verifyUploadBtnPermissionBeneficiary = new PermissionPo("verify:upload_beneficiary", "受益人上传", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 165L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyUploadBtnPermissionBeneficiary);
//        PermissionPo verifyUploadBtnPermissionStockHolder = new PermissionPo("verify:upload_stockHolder", "控股股东上传", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 166L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyUploadBtnPermissionStockHolder);
//        PermissionPo verifyUploadBtnPermissionTelecom = new PermissionPo("verify:upload_telecom", "电信运营商上传", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 167L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyUploadBtnPermissionTelecom);
//        PermissionPo verifyExportBtnPermissionBeneficiary = new PermissionPo("verify:export_beneficiary", "受益人比对下载", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 168L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyExportBtnPermissionBeneficiary);
//        PermissionPo verifyExportBtnPermissionStockHolder = new PermissionPo("verify:export_stockHolder", "控股股东比对下载", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 169L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyExportBtnPermissionStockHolder);
//        PermissionPo verifyExportBtnPermissionTelecom = new PermissionPo("verify:export_telecom", "电信三要素校验结果下载", batchComparisonPermission.getId(), "../ui/beneficiary/list.html", "", "button", 170L, "GET", "", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(verifyExportBtnPermissionTelecom);

        PermissionPo uploadCustomerPermission = new PermissionPo("kyc:uploadCustomers", "批量尽调模板导入", kycMgtPermission.getId(), "../ui/kyc/list.html", "", "button", 171L, "GET", "批量尽调模板导入", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(uploadCustomerPermission);
        PermissionPo downloadTemplatePermission = new PermissionPo("kyc:downloadTemplate", "批量尽调模板导出", kycMgtPermission.getId(), "../ui/kyc/list.html", "", "button", 172L, "GET", "批量尽调模板导出", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(downloadTemplatePermission);

//        PermissionPo customerManagerCreatePermission = new PermissionPo("customerManager:create", "客户开立", customerManagerPermission.getId(), "../ui/customer/list.html", "", "button", 173L, "GET", "客户开立", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(customerManagerCreatePermission);
//        PermissionPo customerManagerChangePermission = new PermissionPo("customerManager:change", "变更", customerManagerPermission.getId(), "../ui/customer/list.html", "", "button", 174L, "GET", "变更", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(customerManagerChangePermission);

        //历史年检按钮
        PermissionPo annualHistoryResultExportBtnPermission = new PermissionPo("annualHistory:result_export", "历史年检-导出年检统计", annualHistoryPermission.getId(), "../ui/annualcheck/history.html", "", "button", 175L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualHistoryResultExportBtnPermission);
        PermissionPo annualHistoryResultExportResultBtnPermission = new PermissionPo("annualHistory:result_exportResult", "历史年检-导出年检结果", annualHistoryPermission.getId(), "../ui/annualcheck/history.html", "", "button", 176L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(annualHistoryResultExportResultBtnPermission);


        PermissionPo organCancelAuditPermission = new PermissionPo("apply:organ_cancelAudit", "取消核准设置", sysOrgPermission.getId(), "../ui/organization/list.html", "", "button", 177L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(organCancelAuditPermission);

        PermissionPo cgsbAccountBtnPermission = new PermissionPo("cgsbAccount:btn_delete", "报备成功列表删除", companyAccountsPermission.getId(), "../ui/company/companyAccounts.html", "", "button", 178L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(cgsbAccountBtnPermission);

        //所有打印按钮增加权限
        PermissionPo kycPrintPermission = new PermissionPo("apply:kyc_print", "客户尽调打印", kycMgtPermission.getId(), "../ui/kyc/detail.html", "", "button", 0L, "GET", "客户尽调打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(kycPrintPermission);

//        PermissionPo bankEditPrintPermission = new PermissionPo("apply:bank_edit_print", "预约接洽打印", applyingPermission.getId(), "../ui/bank/edit.html", "", "button", 0L, "GET", "预约接洽打印", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(bankEditPrintPermission);
//
//        PermissionPo bankViewPrintPermission = new PermissionPo("apply:bank_view_print", "预约受理打印", applyedPermission.getId(), "../ui/bank/view.html", "", "button", 0L, "GET", "预约受理打印", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(bankViewPrintPermission);

        PermissionPo accountFeilinshiPrintPermission = new PermissionPo("apply:account_feilinshi_print", "非临时账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/feilinshiOpen.html", "", "button", 0L, "GET", "非临时账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountFeilinshiPrintPermission);

        PermissionPo accountJibenPrintPermission = new PermissionPo("apply:account_jiben_print", "基本账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/jibenOpen.html", "", "button", 0L, "GET", "基本账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountJibenPrintPermission);

        PermissionPo accountYibanPrintPermission = new PermissionPo("apply:account_yiban_print", "一般账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/yibanOpen.html", "", "button", 0L, "GET", "一般账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountYibanPrintPermission);

        PermissionPo accountLinshiPrintPermission = new PermissionPo("apply:account_linshi_print", "临时账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/linshiOpen.html", "", "button", 0L, "GET", "临时账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountLinshiPrintPermission);

        PermissionPo accountYusuanPrintPermission = new PermissionPo("apply:account_yusuan_print", "预算账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/yusuanOpen.html", "", "button", 0L, "GET", "预算账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountYusuanPrintPermission);

        PermissionPo accountFeiyusuanPrintPermission = new PermissionPo("apply:account_feiyusuan_print", "非预算账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/feiyusuanOpen.html", "", "button", 0L, "GET", "非预算账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountFeiyusuanPrintPermission);

        PermissionPo accountTeshuPrintPermission = new PermissionPo("apply:account_teshu_print", "特殊账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/teshuOpen.html", "", "button", 0L, "GET", "特殊账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountTeshuPrintPermission);

        PermissionPo accountYanziPrintPermission = new PermissionPo("apply:account_yanzi_print", "验资账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/yanziOpen.html", "", "button", 0L, "GET", "验资账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountYanziPrintPermission);

        PermissionPo accountZengziPrintPermission = new PermissionPo("apply:account_zengzi_print", "增资账户详情打印", companyAccountsPermission.getId(), "../ui/accttype/zengziOpen.html", "", "button", 0L, "GET", "增资账户详情打印", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(accountZengziPrintPermission);

        // 证件到期提醒 信息导出按钮
//        PermissionPo legalDueExportPermission = new PermissionPo("notice:legalDue-export", "法人证件到期信息导出", legalDuePermission.getId(), "../ui/notice/legalDueList.html", "", "button", 0L, "GET", "法人证件到期信息导出", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(legalDueExportPermission);
//
//        PermissionPo fileDueExportPermission = new PermissionPo("notice:fileDue-export", "证明文件到期信息导出", fileDuePermission.getId(), "../ui/notice/legalDueList.html", "", "button", 0L, "GET", "证明文件到期信息导出", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(fileDueExportPermission);
//
//        PermissionPo OperatorDueExportPermission = new PermissionPo("notice:operatorDue-export", "经办人证件到期信息导出", operateDuePermission.getId(), "../ui/notice/operatorIdcardDueList.html", "", "button", 0L, "GET", "经办人证件到期信息导出", Boolean.TRUE, Boolean.FALSE);
//        saveAndAddtoList(OperatorDueExportPermission);

        //risk模块

        //模型管理
        PermissionPo modelManagerPermisssion = new PermissionPo("risk_model_manager","模型及规则管理",-1L,"","fa-bullhorn","menu",1600L,"GET","模型管理",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(modelManagerPermisssion);
        PermissionPo modelConfigPermisssion = new PermissionPo("sys_model_sheji","模型设计",modelManagerPermisssion.getId(),"../ui/model/list.html","fa-list","menu",300L,"GET","模型设计",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(modelConfigPermisssion);
        PermissionPo  riskLevelPermisssion = new PermissionPo("risk_level","风险等级",modelManagerPermisssion.getId(),"../ui/level/list.html","fa-list","menu",200L,"GET","风险等级",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(riskLevelPermisssion);
        PermissionPo  riskTypePermisssion = new PermissionPo("risk_type","风险类型",modelManagerPermisssion.getId(),"../ui/type/list.html","fa-list","menu",100L,"GET","风险类型",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(riskTypePermisssion);
        PermissionPo ruleConfigurePermisssion = new PermissionPo("risk_configure","规则配置",modelManagerPermisssion.getId(),"../ui/modelRule/list.html","fa fa-list","menu",400L,"GET","规则配置",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(ruleConfigurePermisssion);


        PermissionPo transactionRiskPermisssion = new PermissionPo("transaction_risk","账户开户行为监测",-1L,"../ui/riskdata/staticList.html","fa-bullhorn","menu",100L,"GET","账户开户行为监测",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(transactionRiskPermisssion);
        PermissionPo openRiskBtnPermisssion = new PermissionPo("apply:menu_Add", "导出", transactionRiskPermisssion.getId(), "../ui/riskdata/staticList.html", "", "button", 179L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(openRiskBtnPermisssion);
        PermissionPo tradeRiskPermisssion = new PermissionPo("trade_risk","账户交易行为监测",-1L,"../ui/riskdata/tradeList.html","fa-bullhorn","menu",100L,"GET","账户交易行为监测",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(tradeRiskPermisssion);
        PermissionPo tadeRiskBtnPermisssion = new PermissionPo("apply:menu_Add", "导出", tradeRiskPermisssion.getId(), "../ui/riskdata/tradeList.html", "", "button", 180L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(tadeRiskBtnPermisssion);
        PermissionPo riskHandlePermisssion = new PermissionPo("handle_risk","账户风险处置",-1L,"../ui/riskdata/todoList.html","fa-bullhorn","menu",100L,"GET","账户风险处置",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(riskHandlePermisssion);

        PermissionPo dZRiskPermisssion = new PermissionPo("dz_risk", "账户对账行为监测", -1L, "../ui/riskdata/dzList.html", "fa-bullhorn", "menu", 100L, "GET", "账户对账行为监测", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(dZRiskPermisssion);
        PermissionPo openDzBtnPermisssion = new PermissionPo("apply:menu_Add", "导出", dZRiskPermisssion.getId(), "../ui/riskdata/dzList.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(openDzBtnPermisssion);

        //白名单菜单
        PermissionPo riskWhitePermisssion = new PermissionPo("handle_risk","白名单账户管理",-1L,"../ui/riskWhiteList/list.html","fa-bullhorn","menu",100L,"GET","白名单账户管理",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(riskWhitePermisssion);
        //白名单按钮
        PermissionPo addWhitePermission = new PermissionPo("apply:whiteList_add", "新建", riskWhitePermisssion.getId(), "../ui/riskWhiteList/list.html", "", "button", 179L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(addWhitePermission);
        PermissionPo editWhitePermission = new PermissionPo("apply:whiteList_edit", "修改", riskWhitePermisssion.getId(), "../ui/riskWhiteList/list.html", "", "button", 180L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(editWhitePermission);
        PermissionPo delWhitePermission = new PermissionPo("apply:whiteList_del", "删除", riskWhitePermisssion.getId(), "../ui/riskWhiteList/list.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(delWhitePermission);
        PermissionPo exWhitePermission = new PermissionPo("apply:whiteList_ex", "导入", riskWhitePermisssion.getId(), "../ui/riskWhiteList/list.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(exWhitePermission);

        //风险数据补录菜单
        PermissionPo riskClearPermisssion = new PermissionPo("risk_clear","风险数据补录",-1L,"../ui/riskdata/riskClear.html","fa-bullhorn","menu",100L,"GET","风险数据补录",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(riskClearPermisssion);


        PermissionPo customerAbnormalManagerPermission = new PermissionPo("customerAbnormal_manager", "客户异动管理", -1L, "../ui/customer/abnormal.html", "fa-exclamation-triangle", "menu", 200L, "GET", "客户异动管理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerPermission);

        PermissionPo customerAbnormalManagerExportPermission = new PermissionPo("customerAbnormal_manager:export", "导出", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 175L, "GET", "导出", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerExportPermission);
        PermissionPo customerAbnormalManagerProcessPermission = new PermissionPo("customerAbnormal_manager:process", "异动处理", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 176L, "GET", "异动处理", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerProcessPermission);
        PermissionPo customerAbnormalManagerProcessFinishPermission = new PermissionPo("customerAbnormal_manager:processFinish", "异动处理完成", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 177L, "GET", "异动处理完成", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerProcessFinishPermission);
        PermissionPo customerAbnormalManagerNotePermission = new PermissionPo("customerAbnormal_manager:note", "短信提醒", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 178L, "GET", "短信提醒", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerNotePermission);
        PermissionPo customerAbnormalManagerHistoryPermission = new PermissionPo("customerAbnormal_manager:history", "短信发送历史", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 179L, "GET", "短信发送历史", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManagerHistoryPermission);
        PermissionPo customerAbnormalManualComparisonPermission = new PermissionPo("customerAbnormal_manager:comparison", "实时对比", customerAbnormalManagerPermission.getId(), "../ui/customer/abnormal.html", "", "button", 180L, "GET", "实时对比", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(customerAbnormalManualComparisonPermission);
        //高风险
        PermissionPo highRiskPermisssion = new PermissionPo("account_risk","高风险管理",-1L,"","fa-bullhorn","menu",1900L,"GET","高风险管理",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(highRiskPermisssion);
        PermissionPo highRiskApiPermisssion = new PermissionPo("high_risk_api","外部数据接口配置",highRiskPermisssion.getId(),"../ui/highRisk/riskApi.html","fa-list","menu",100L,"GET","外部数据接口配置",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(highRiskApiPermisssion);
        PermissionPo highRiskDataPermisssion = new PermissionPo("high_risk_data","高风险展示列表",highRiskPermisssion.getId(),"../ui/highRisk/highRiskData.html","fa-list","menu",100L,"GET","高风险展示列表",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(highRiskDataPermisssion);
        PermissionPo highRiskRulePermisssion = new PermissionPo("high_risk_rule","高风险规则配置",highRiskPermisssion.getId(),"../ui/highRisk/highRiskRule.html","fa-list","menu",300L,"GET","高风险规则配置",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(highRiskRulePermisssion);
        //高风险规则配置按钮
        PermissionPo highRiskRuleCorePermisssion = new PermissionPo("annual:core_data", "行内数据", highRiskRulePermisssion.getId(), "../ui/highRisk/highRiskRule.html", "", "button", 179L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(highRiskRuleCorePermisssion);
        PermissionPo highRiskRuleBankPermisssion = new PermissionPo("annual:pbc_data", "模型规则", highRiskRulePermisssion.getId(), "../ui/highRisk/highRiskRule.html", "", "button", 180L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(highRiskRuleBankPermisssion);
        PermissionPo highRiskRuleSaicPermisssion = new PermissionPo("annual:saic_data", "外部数据", highRiskRulePermisssion.getId(), "../ui/highRisk/highRiskRule.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(highRiskRuleSaicPermisssion);
        PermissionPo highRiskRuleStartPermisssion = new PermissionPo("annual:start_check", "开始跑批", highRiskRulePermisssion.getId(), "../ui/highRisk/highRiskRule.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(highRiskRuleStartPermisssion);
        PermissionPo highRiskRuleResetPermisssion = new PermissionPo("annual:reset", "规则重置", highRiskRulePermisssion.getId(), "../ui/highRisk/highRiskRule.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(highRiskRuleResetPermisssion);


        //报表和统计
        PermissionPo reportPermisssion = new PermissionPo("risk_report","统计和报表",-1L,"","fa-bullhorn","menu",2400L,"GET","统计和报表",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(reportPermisssion);
        PermissionPo accountSizeReportPermisssion = new PermissionPo("risk_account_size_report","企业银行账户数量监测分析表",reportPermisssion.getId(),"../ui/reportForm/list.html","fa-bullhorn","menu",2400L,"GET","企业银行账户数量监测分析表",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(accountSizeReportPermisssion);
        PermissionPo cfrReportPermisssion = new PermissionPo("risk_account_size_report","账户资金流动情况统计表",reportPermisssion.getId(),"../ui/cfrReport/list.html","fa-bullhorn","menu",2400L,"GET","账户资金流动情况统计表",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(cfrReportPermisssion);
        PermissionPo acctNumChangeReportPermisssion = new PermissionPo("acct_num_change_report","账户数量变动表",reportPermisssion.getId(),"../ui/reportForm/acctNumChangeReport.html","fa-bullhorn","menu",2400L,"GET","账户数量变动表",Boolean.TRUE,Boolean.FALSE);
        saveAndAddtoList(acctNumChangeReportPermisssion);
        //risk模块
        //模型管理
        PermissionPo addModelPermission = new PermissionPo("apply:model_add", "新建", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 179L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(addModelPermission);
        PermissionPo editModelPermission = new PermissionPo("apply:model_edit", "修改", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 180L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(editModelPermission);
        PermissionPo delModelPermission = new PermissionPo("apply:model_del", "删除", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 181L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(delModelPermission);
        PermissionPo relyModelPermission = new PermissionPo("apply:model_rely", "依赖", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 182L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(relyModelPermission);
        PermissionPo fieldModelPermission = new PermissionPo("apply:model_field", "字段", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 183L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(fieldModelPermission);
        PermissionPo relyAddModelPermission = new PermissionPo("apply:modelRely_add", "新建", modelConfigPermisssion.getId(), "../ui/model/modelRely.html", "", "button", 184L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(relyAddModelPermission);
        PermissionPo relyModelFieldInitPermission = new PermissionPo("apply:modelField_init", "模型字段初始化", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 184L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(relyModelFieldInitPermission);
        PermissionPo relyModelInitPermission = new PermissionPo("apply:model_init", "模型初始化", modelConfigPermisssion.getId(), "../ui/model/list.html", "", "button", 184L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(relyModelInitPermission);
        PermissionPo relyModelRuleInitPermission = new PermissionPo("apply:model_init", "规则初始化", ruleConfigurePermisssion.getId(), "../ui/modelRule/list.html", "", "button", 184L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(relyModelRuleInitPermission);

        //模型属性
        PermissionPo levelAddPermission = new PermissionPo("apply:level_add", "新增", riskLevelPermisssion.getId(), "../ui/level/list.html", "", "button", 199L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(levelAddPermission);
        PermissionPo levelEditPermission = new PermissionPo("apply:level_edit", "修改", riskLevelPermisssion.getId(), "../ui/level/list.html", "", "button", 200L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(levelEditPermission);
        PermissionPo levelDelPermission = new PermissionPo("apply:level_del", "删除", riskLevelPermisssion.getId(), "../ui/level/list.html", "", "button", 201L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(levelDelPermission);

        PermissionPo typeAddPermission = new PermissionPo("apply:type_add", "新增", riskTypePermisssion.getId(), "../ui/type/list.html", "", "button", 202L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(typeAddPermission);
        PermissionPo typeEditPermission = new PermissionPo("apply:type_edit", "修改", riskTypePermisssion.getId(), "../ui/type/list.html", "", "button", 203L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(typeEditPermission);
        PermissionPo typeDelPermission = new PermissionPo("apply:type_del", "删除", riskTypePermisssion.getId(), "../ui/type/list.html", "", "button", 204L, "GET", "", Boolean.TRUE, Boolean.FALSE);
        saveAndAddtoList(typeDelPermission);

        //管理员权限
        RolePo suRole = roleDao.findByCode("admin");

        for (PermissionPo permissionPo : permissionList) {
            RolePermissionPo rolePermission = new RolePermissionPo(suRole.getId(), permissionPo.getId());
            rolePermissionDao.save(rolePermission);
        }
    }

    private PermissionPo saveAndAddtoList(PermissionPo permissionPo) {
        PermissionPo permission = permissionDao.save(permissionPo);
        permissionList.add(permission);
        return permission;
    }

    @Override
    protected boolean isNeedInit() {
        return permissionDao.findAll().size() == 0 && rolePermissionDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.AUTH_INITIALIZER_INDEX + 1;
    }

}
