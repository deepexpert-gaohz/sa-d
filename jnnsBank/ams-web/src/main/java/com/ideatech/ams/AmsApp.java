package com.ideatech.ams;

import com.ideatech.ams.config.AmsWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liangding
 * @create 2018-05-03 上午11:26
 **/
@SpringBootApplication
@ComponentScan("com.ideatech")
@Controller
@EnableTransactionManagement(proxyTargetClass = true)
//public class AmsApp extends SpringBootServletInitializer implements WebApplicationInitializer {
public class AmsApp {

    @Autowired
    private AmsWebProperties amsWebProperties;

    public static void main(String[] args) {
        SpringApplication.run(AmsApp.class, args);
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + amsWebProperties.getIndex());
        } catch (IOException e) {
            //ignore
        }
    }


}
