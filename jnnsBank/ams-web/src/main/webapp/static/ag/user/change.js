var user = {
    baseUrl: "../../user",
    entity: "user",
};

function encrypt(data) {
    var key  = CryptoJS.enc.Latin1.parse('ideatech20180223');
    var iv   = CryptoJS.enc.Latin1.parse('ideatech20180223');
    return CryptoJS.AES.encrypt(data, key,
        {iv:iv,mode:CryptoJS.mode.CBC,padding:CryptoJS.pad.ZeroPadding}).toString();
}
layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    form.on('submit', function (data) {
        var oldPassword = $("#oldPassword").val();
        var password1 = $("#password1").val();
        var password2 = $("#password2").val();
        var postLoginParams = {
            oldPassword : encrypt(oldPassword),
            password1 : encrypt(password1),
            password2 : encrypt(password2),
        };
        $.ajax({
            url: user.baseUrl + "/change",
            type: 'post',
            data: postLoginParams,
            dataType: "json",
            success: function (res) {
                if (res.code === 'ACK') {
                    layerTips.msg('更新成功');
                    location.reload();
                } else {
                    layerTips.msg(res.message);
                }
            }

        });
        return false;
    });

});