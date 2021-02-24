/**
 * 联网核查调取身份证读卡器接口：将读卡器读取的相应数据赋值给对应变量。
 * 现场需重写js
 */
function setData() {
        /**
         * 身份证姓名
         */
        var idCardName ="";
        /**
         * 身份证号码
         * @type {string}
         */
        var idCardNo = "";
        /**
         * 民族
         * @type {string}
         */
        var idCardNation ="";
        /**
         * 出生年月
         * 格式：yyyy-MM-dd
         * @type {string}
         */
        var idCardBirthday = "";
        /**
         * 有效期限开始日期
         * @type {string}
         */
        var idCardValidityBeginData = "";
            /**
             * 有效期截止日期
             * @type {string}
             */

        var idCardValidityEndData = ""
        /**
         * 发证机关
         * @type {string}
         */
        var idCardOrgan = "";
        /**
         * 住址
         * @type {string}
         */
        var idCardAddress = "";
        /**
        *性别
        * @type {string}
        */
        var idCardSex ="";
        /**
         * 身份证头像的base64字符串
         * @type {string}
         */
        var idCardLocalImageByte = "";





        /**
         * 新增代码只可在上面添加，以下内容不可修改
         */
        $("#idCardName").val(idCardName);
        $("#idCardNo").val(idCardNo);
        $("#idCardNation").val(idCardNation);
        $("#idCardBirthday").val(idCardBirthday);
        $("#idCardValidityBeginData").val(idCardValidityBeginData);
       $("#idCardValidityEndData").val(idCardValidityEndData);
        $("#idCardOrgan").val(idCardOrgan);
        $("#idCardAddress").val(idCardAddress);
        $("#idCardSex").val(idCardSex);
        $("#idCardLocalImageByte").val(idCardLocalImageByte);
        $("#img").attr("src","data:image/jpeg;base64,"+idCardLocalImageByte);
}