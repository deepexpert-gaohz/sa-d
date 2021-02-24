/**
 * 创建开始时间和结束时间，并进行日期约束
 * @param laydate   日期控件对象（laydate2.0版本）
 * @param startId   开始日期id
 * @param endId     结束日期id
 * @param dateFormat    日期格式化（默认YYYY-MM-DD）
 * @param istime    是否开启时间选择(默认true)
 */
function beginEndDateInit2(laydate, startId, endId, dateFormat, istime) {

    var startTime = laydate.render({
        elem: '#' + startId,
        format: dateFormat === undefined ? 'yyyy-MM-dd HH:mm:ss' : dateFormat,
        done: function (value, date, endDate) {
            setEndTimeMin(endTime, date, startId, endId);
        }
    });
    var endTime = laydate.render({
        elem: '#' + endId,
        format: dateFormat === undefined ? 'yyyy-MM-dd HH:mm:ss' : dateFormat,
        done: function (value, date, endDate) {
            setStartTimeMax(startTime, date, startId, endId);
        }
    });
    if (istime === undefined || istime) {
        startTime.config.type = 'datetime';
        endTime.config.type = 'datetime';
    }

    $('#' + startId).change(function () {
        var thisDate = Date.parse(this.value.replace(/-/g, "/"));
        if (isNaN(thisDate)) {
            layer.msg("时间格式错误");
            $(this).val("");
            setEndTimeMin(endTime, {}, startId, endId);
        } else {
            var thisDateJson = {
                year: new Date(thisDate).getFullYear(),
                month: new Date(thisDate).getMonth() + 1,
                date: new Date(thisDate).getDate(),
                hours: new Date(thisDate).getHours(),
                minutes: new Date(thisDate).getMinutes(),
                seconds: new Date(thisDate).getSeconds()
            };
            setEndTimeMin(endTime, thisDateJson, startId, endId);
        }
    });

    $('#' + endId).change(function () {
        var thisDate = Date.parse(this.value.replace(/-/g, "/"));
        if (isNaN(thisDate)) {
            layer.msg("时间格式错误");
            $(this).val("");
            setStartTimeMax(startTime, {}, startId, endId);
        } else {
            var thisDateJson = {
                year: new Date(thisDate).getFullYear(),
                month: new Date(thisDate).getMonth() + 1,
                date: new Date(thisDate).getDate(),
                hours: new Date(thisDate).getHours(),
                minutes: new Date(thisDate).getMinutes(),
                seconds: new Date(thisDate).getSeconds()
            };
            setStartTimeMax(startTime, thisDateJson, startId, endId);
        }
    });
    return [startTime, endTime];
}

/**
 * 设置开始时间最小值
 * @param startTime 开始时间日期控件对象
 * @param endDate 结束时间（json格式）
 * @param startId   开始日期id
 * @param endId 结束日期id
 */
function setStartTimeMax(startTime, endDate, startId, endId) {
    if ($.isEmptyObject(endDate)) {//结束值为空时，去除开始日期限制
        startTime.config.max = {
            date: 31,
            hours: 0,
            minutes: 0,
            month: 11,
            seconds: 0,
            year: 2099
        };
    } else {
        if ($('#' + startId).val() === '' || $('#' + endId).val() === '' ||
            new Date($('#' + startId).val().replace(/-/g, '/')).getTime() <= new Date($('#' + endId).val().replace(/-/g, '/')).getTime()) {
            var startTimeMax = endDate;
            startTimeMax.month = startTimeMax.month - 1;
            startTime.config.max = startTimeMax;
        } else {
            layer.msg("开始时间大于结束时间");
            $('#' + endId).val("");
        }
    }
}

/**
 * 设置结束时间最小值
 * @param endTime 结束时间日期控件对象
 * @param startDate 开始时间（json格式）
 * @param startId   开始日期id
 * @param endId 结束日期id
 */
function setEndTimeMin(endTime, startDate, startId, endId) {
    if ($.isEmptyObject(startDate)) {//开始值为空时，去除结束日期限制
        endTime.config.min = {
            date: 1,
            hours: 0,
            minutes: 0,
            month: 0,
            seconds: 0,
            year: 1900
        };
    } else {
        if ($('#' + startId).val() === '' || $('#' + endId).val() === '' ||
            new Date($('#' + startId).val().replace(/-/g, '/')).getTime() <= new Date($('#' + endId).val().replace(/-/g, '/')).getTime()) {
            var endTimeMin = startDate;
            endTimeMin.month = endTimeMin.month - 1;
            endTime.config.min = endTimeMin;
        } else {
            layer.msg("开始时间大于结束时间");
            $('#' + startId).val("");
        }
    }
}

