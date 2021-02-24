/**
 * Created by alven on 22/03/2018.
 */
layui.config({
    base: '/ui/js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form','murl','loading'], function () {
    var form= layui.form(),url=layui.murl,id = url.get("id"), name = decodeURI(url.get("name")), loading = layui.loading;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    $("#name").html(name);

    loading.show();

    //当前开户流水信息
    var currentItem;

    $.ajax({
        url: '/apply/openAccount/openAccountLog/' + id,
        type: 'get',
        dataType: "json"
    }).done(function (response) {
        if(response.rel){
            var data = response.result;
            //反显老数据
            for (var key in data) {
                $('#'+key).html(data[key]);
            }

            //按钮显示
            if(data.hasPreInfo){
                $('#preOpenAccount').removeClass('dn');
            }
            if(data.hasCusInfo){
                $('#openAccount').removeClass('dn');
            }

            currentItem = data;
        } else {
            layerTips.msg('获取数据失败');
        }
    }).always(function () {
        loading.hide();
    });

    //返回
    form.on('submit(cancel)', function(data){
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //预约信息
    form.on('submit(preOpenAccount)', function(data){
        if(!currentItem.sourceId){
            layerTips.msg('没有预约信息');
        } else {
            parent.tab.tabAdd({
                title: '预约信息-'+name,
                href: 'bank/view.html?id='+id+'&name='+encodeURI(name)
            });
        }
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //补录信息
    form.on('submit(openAccount)', function(data){
        var preOpenAcctId = currentItem.sourceId || '';
        var name = currentItem.acctName;
        var type = currentItem.acctType;
        var logId = currentItem.id;
        parent.tab.tabAdd({
            title: '补录记录-'+name,
            href: 'account/detail?preOpenAcctId='+preOpenAcctId+'&name='+encodeURI(name)+'&type='+type+'&logId='+logId
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

});