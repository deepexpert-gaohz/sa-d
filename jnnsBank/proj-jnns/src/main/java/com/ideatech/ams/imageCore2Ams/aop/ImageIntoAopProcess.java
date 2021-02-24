package com.ideatech.ams.imageCore2Ams.aop;


import com.ideatech.ams.imageCore2Ams.service.ImageImportAccess;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 使用切面切入存量导入方法结束时，触发影像存量导入执行器
 *
 */
@Slf4j
@Aspect
@Component
public class ImageIntoAopProcess {

      @Autowired
      ImageImportAccess imageImportAccess;

       //表达式匹配定位类中特定方法，进行强化联动
       @Pointcut("execution(public * com.ideatech.ams.account.service.core.DefaultCompanyImportAccess.mainAccess(..))")
       public  void  mainAccess(){}


       @Before("mainAccess()")
       public void doBefore(JoinPoint joinPoint) throws Throwable{

               log.info("拦截切面-核心存量处理完毕后进行影像存量处理----");


       }

       @AfterReturning(pointcut = "mainAccess()")
       public void doAfterReturning() throws Throwable{

               log.info("--------影像文件导入处理开始--------");
               imageImportAccess.imageProcess();
               log.info("--------影像文件导入处理结束---------");



       }



}
