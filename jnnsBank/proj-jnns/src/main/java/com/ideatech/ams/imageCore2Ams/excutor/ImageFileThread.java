package com.ideatech.ams.imageCore2Ams.excutor;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 执行存量影像信息插入进表的线程
 */
@Slf4j
@Data
public class ImageFileThread implements  Runnable{

    private  CoreImageFileIntoExcutor coreImageFileIntoExcutor;

    private  Integer integer;

    public ImageFileThread(CoreImageFileIntoExcutor coreImageFileIntoExcutor,Integer integer){

        this.coreImageFileIntoExcutor = coreImageFileIntoExcutor;

        this.integer = integer;

    }

    @Override
    public void run() {

        try{
            Thread.currentThread().setName("第"+integer+"线程");
            log.info(Thread.currentThread().getName()+"处理影像信息入表开始！");
            coreImageFileIntoExcutor.saveImageInfoInit();
            log.info(Thread.currentThread().getName()+"处理影像信息入表结束！");

        }catch (Exception e ){

            log.error(Thread.currentThread().getName()+"执行存量影像信息入表出现异常：");
            e.printStackTrace();

        }


    }


}

