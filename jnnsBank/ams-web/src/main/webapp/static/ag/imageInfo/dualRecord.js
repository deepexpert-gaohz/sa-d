$(function(){
    var userInfoWid=$(".userInfo").width(),cMWid=$(".contMargin").width(),
        userVideoWid=$(".userVideo").width(),userMessage=$(".userMessage").width();
    var uWid=userInfoWid+userVideoWid+userMessage+cMWid*3+42;
    interfaceSettings();
    $(window).resize(function(){
        interfaceSettings();
    });

    $(".resolution").val('1');

    // 点击事件
    $(".hybridTit").click(function(){
        $(this).hide();
        $(".currencyTit").hide();
        $(".retrun").show();
        $(".hybrid").stop().slideDown();
    });
    $(".currencyTit").click(function(){
        $(this).hide();
        $(".hybridTit").hide();
        $(".retrun").show();
        $(".currency").stop().slideDown();
    });
    $(".pcard").click(function(){
        $(this).addClass("active").next().removeClass("active");
        $(".positive").show();
        $(".reverse").hide();
    });
    $(".rcard").click(function(){
        $(this).addClass("active").prev().removeClass("active");
        $(".positive").hide();
        $(".reverse").show();
    });
    $(".cardBox").find("img").dblclick(function(){
        var src=$(this).attr("src");
        $(".bigImg").find("img").attr("src",src);
        $(".bigImg").show();
    });
    $(".close").click(function(){
        $(".bigImg").hide();
        $(".bigImg").find("img").attr("src","");
    });
    $(".folderItem i").dblclick(function(){
        $(this).parent().parent().hide();
        $(".files").show();
    });
    $(".fileItem .hybridFile").dblclick(function(){
        $(this).parent().parent().hide();
        $('.riskCont').hide();
        $(".riskWarning").show();
        $(".hybrid").show();
    });
    $(".fileItem .currencyFile").dblclick(function(){
        $(this).parent().parent().hide();
        $('.riskCont').hide();
        $(".riskWarning").show();
        $(".currency").show();
    });
    $(".retrunfolder").click(function(){
        $(".files").hide();
        $(".folders").show();
    });
    $(".retrunfile").click(function(){
        $(".riskWarning").hide();
        $(".files").show();
    });
    $(".panel").click(function(){
        $(".panelBox").show();
    });
    $(".panelClose").click(function(){
        $(".panelBox").hide();
    });
    $(".buttons .bimg").click(function(){
        $(".buttons .bimg").removeClass("active");
        $(this).addClass("active");
    });
    $(".setup").click(function(){
        // $(".videoSet").show();
    });
    $(".vsBtns button").click(function(){
        // $(".videoSet").hide();
    });


    // 方法
    // 界面样式设置
    function interfaceSettings(){
        var height=$(window).height(),width=$(window).width();
        if(height<648){
            height=648;
        }else if(height>1000){
            height=1000;
        }
        if(width>1920){
            width=1920;
        }else if(width<1106){
            width=1106;
        }
        var wiWidth=width-uWid-20;
        if(wiWidth<122){
            wiWidth=122;
        }
        $("html,body").css("height",height);
        $(".all").height(height);
        var contHeight=height-90,riskHeight=contHeight-41;
        var infoHeight=(contHeight-161)>401?(contHeight-161):401;
        var wdHeight=contHeight*0.49,imHeight=contHeight-wdHeight-15;
        $(".content").height(contHeight);
        $(".userInfo").height(contHeight);
        $(".userVideo").height(contHeight);
        $(".userMessage").height(contHeight);
        $(".contMargin").height(contHeight);
        $(".wordsInfo").height(contHeight);
        $(".information").height(infoHeight);
        $(".video").height(infoHeight);
        $("#server").height(infoHeight);
        $("#server").width($(".video").width());
        $(".wordsInfo").width(wiWidth);
        $(".wordsMsg").height(wdHeight);
        $(".infoMsg").height(imHeight);
        $(".loginPage").height(height);

        $(".remarksInfo").height(contHeight*0.65);
    }

});