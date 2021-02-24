$(function(){
    $.get('../allBillsPublic/findBillsCounts', function (data) {
        $('#addAcctCount').html(data.addAcctCount);
        $('#addBusinessCount').html(data.addBusinessCount);

        $('#preUnCompleteCount').html(data.preUnCompleteCount);
        $('#zhshCheckCount').html(data.zhshCheckCount);
        $('#dsbCheckCount').html(data.dsbCheckCount);
        $('#dbllbCount').html(data.dbllbCount);

        $('#preSuccessCount').html(data.preSuccessCount);
        $('#syncSuccsssCount').html(data.syncSuccsssCount);
        $('#hzSuccsssCount').html(data.hzSuccsssCount);

        if(data.addAcctCount != null) {
            $('#addAcctDiv').removeClass('dn')
        }
        if(data.addBusinessCount != null) {
            $('#addBusinessDiv').removeClass('dn')
        }


        if(data.preUnCompleteCount != null || data.zhshCheckCount != null || data.dsbCheckCount != null || data.dbllbCount != null) {
            $('#dealtRemindDiv').removeClass('dn')
        }
        if(data.preUnCompleteCount != null) {
            $('#preUnCompleteDiv').removeClass('dn')
        }
        if(data.zhshCheckCount != null) {
            $('#zhshCheckDiv').removeClass('dn')
        }
        if(data.dsbCheckCount != null) {
            $('#dsbCheckDiv').removeClass('dn')
        }
        if(data.dbllbCount != null) {
            $('#dbllbDiv').removeClass('dn')
        }

        if (data.preSuccessCount != null || data.syncSuccsssCount != null || data.hzSuccsssCount != null) {
            $('#passRemindDiv').removeClass('dn')
        }
        if(data.preSuccessCount != null) {
            $('#preSuccessDiv').removeClass('dn')
        }
        if(data.syncSuccsssCount != null) {
            $('#syncSuccsssDiv').removeClass('dn')
        }
        if(data.hzSuccsssCount != null) {
            $('#hzSuccsssDiv').removeClass('dn')
        }

    });

    $.get('../config/getVersionNumber', function (data) {
        if(!isEmpty(data)) {
            $('#versionSpan').html("version: " + data);
        }

    });

    //设置企业异动统计数据
    $.get('../customerAbnormal/findAbnormalCounts', function (data) {
        $('#abnormalAllCount').html(data.abnormalAllCount);
        $('#illegalCount').html(data.illegalCount);
        $('#changeMessCount').html(data.changeMessCount);
        $('#businessExpiresCount').html(data.businessExpiresCount);
        $('#abnormalStateCount').html(data.abnormalStateCount);
        $('#changedCount').html(data.changedCount);
        if (data.abnormalAllCount != null
            || data.illegalCount != null
            || data.changeMessCount != null
            || data.businessExpiresCount != null
            || data.abnormalStateCount != null
            || data.changedCount != null) {
            $('#abnormalStatisticsDiv').removeClass('dn')
            if (data.abnormalAllCount != null) {
                $('#abnormalAllDiv').removeClass('dn')
            }
            if (data.illegalCount != null) {
                $('#illegalDiv').removeClass('dn')
            }
            if (data.changeMessCount != null) {
                $('#changeMessDiv').removeClass('dn')
            }
            if (data.businessExpiresCount != null) {
                $('#businessExpiresDiv').removeClass('dn')
            }
            if (data.abnormalStateCount != null) {
                $('#abnormalStateDiv').removeClass('dn')
            }
            if (data.changedCount != null) {
                $('#changedDiv').removeClass('dn')
            }
        }
    });

    $('#noticeMangerBtn').click(function () {
        parent.tab.tabAdd({
            title: '公告管理',
            href: 'announcement/list.html'
        });
    });

    $.get('../permission/element/code?code=noticeManager_btn', function (data) {
        if(data) {
            $('#noticeMangerBtn').removeClass('dn');
        }
    })

});