/**
 * 创建开始时间和结束时间，并进行日期约束
 * @param laydate   日期控件对象（laydate1.0.9版本）
 * @param startId   开始日期id
 * @param endId     结束日期id
 * @param dateFormat    日期格式化（默认YYYY-MM-DD）
 * @param istime    是否开启时间选择(默认false)
 */
function beginEndDateInit(laydate, startId, endId, dateFormat, istime) {
    var startTime = {
        format: dateFormat === undefined ? 'YYYY-MM-DD' : dateFormat,
        istime: istime === undefined ? false : istime,
        choose: function (datas) {
            setEndTimeMin(endTime, datas, startId, endId);
        }
    };
    var endTime = {
        format: dateFormat === undefined ? 'YYYY-MM-DD' : dateFormat,
        istime: istime === undefined ? false : istime,
        choose: function (datas) {
            setStartTimeMax(startTime, datas, startId, endId);
        }
    };

    $('#' + startId).click(function () {
        startTime.elem = this;
        laydate(startTime);
        if (!endTime.min) {
            endTime.min = this.value; //点击文本框会自动选择当前日期，重置结束日的最小日期
        }
    });
    $('#' + endId).click(function () {
        endTime.elem = this;
        laydate(endTime);
        if (!startTime.max) {
            startTime.max = this.value; //点击文本框会自动选择当前日期，重置开始日的最大日期
        }
    });

    $('#' + startId).change(function () {
        $(this).val($.trim(this.value));
        if ($.trim(this.value) === "") {
            delete endTime.min;
        } else {
            if (isNaN(Date.parse(this.value.replace(/-/g, "/")))) {
                layer.msg("时间格式错误");
                $(this).val("");
            } else {
                setEndTimeMin(endTime, this.value, startId, endId);
            }
        }
    });

    $('#' + endId).change(function () {
        $(this).val($.trim(this.value));
        if ($.trim(this.value) === "") {
            delete startTime.max;
        } else {
            if (isNaN(Date.parse(this.value.replace(/-/g, "/")))) {
                layer.msg("时间格式错误");
                $(this).val("");
            } else {
                setStartTimeMax(startTime, this.value, startId, endId);
            }
        }
    });

    return [startTime, endTime];
}


/**
 *
 * 设置开始时间最大值
 * @param startTime 开始日期控件对象
 * @param datas 结束时间（string格式）
 * @param startId   开始日期id
 * @param endId 结束日期id
 */
function setStartTimeMax(startTime, datas, startId, endId) {
    if (!datas) {
        delete startTime.max;
        return;
    }
    if ($.trim($('#' + startId).val()) === "" || $.trim($('#' + endId).val()) === "" ||
        new Date($('#' + startId).val().replace(/-/g, '/')).getTime() <= new Date($('#' + endId).val().replace(/-/g, '/')).getTime()) {
        startTime.max = datas; //结束日选好后，重置开始日的最大日期
    } else {
        layer.msg("开始时间大于结束时间");
        $('#' + endId).val("");
    }
}

/**
 * 设置结束时间最小值
 * @param endTime 结束日期控件对象
 * @param datas 结束时间（string格式）
 * @param startId   开始日期id
 * @param endId 结束日期id
 */
function setEndTimeMin(endTime, datas, startId, endId) {
    if (!datas) {
        delete endTime.min;
        return;
    }
    if ($.trim($('#' + startId).val()) === "" || $.trim($('#' + endId).val()) === "" ||
        new Date($('#' + startId).val().replace(/-/g, '/')).getTime() <= new Date($('#' + endId).val().replace(/-/g, '/')).getTime()) {
        endTime.min = datas; //开始日选好后，重置结束日的最小日期
    } else {
        layer.msg("开始时间大于结束时间");
        $('#' + startId).val("");
    }
}
