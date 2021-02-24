
/**
 * 页面加载判断是否安装高拍仪插件
 */
var install="<br><font color='#FF00FF'>高拍仪控件未安装!点击这里<a href='../ag/image/install/ZZActiveX.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";

window.onload = function(){
        banqhs();
        //要执行的js代码段
        if(isHardWare==="true"){
            var isIE	 = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
            if(isIE){
                var res = checkIe();
                if(!res){
                    document.write(install);
                }
            }else{
                alert("高拍仪只支持IE浏览器");
            }
        }
}

function checkIe(){
    try{
        var ax = new ActiveXObject("ACTIVEX.ActiveXCtrl.1");

    }catch(e){

        return false;
    }
    return true;
}
/**
 * 向左转
 * show1是object的id名称
 * @constructor
 */
function rotateLeft() {
    //调取高拍仪接口

    show1.RotateLeft();
}

/**
 * 向右转
 * show1是object的id名称
 * @constructor
 */
function rotateRight() {
    //调取高拍仪接口

    show1.RotateRight();
}

/**
 * 打开拍摄视频
 */
function openScan()
{

    OpenDevice();
    OpenVideo();
    // return false;
}

/**
 * 关闭拍摄视频
 */
function closeScan()
{

    CloseVideo();
    CloseDevice();
    // return false;

}
function OpenDevice()
{

    var res = show1.OpenDevice(1);
    if((res == false)||(res=="false") ){
            alert("高拍仪控件开启失败！");
    }
    return false;

}

function CloseDevice()
{
    show1.CloseDevice();

}

function OpenVideo()
{
    show1.OpenVideo();
    return false;
}

function CloseVideo()
{
    show1.CloseVideo();
}

/**
 * 获取图片的base64字符串
 * @returns {*}
 */
function getBase64(){
    var name = show1.GetTempFileName(".jpg");
    show1.Capture(name);
    var base64 = show1.GetBase64(name);
    show1.DeleteFile(name);
    return base64;
}