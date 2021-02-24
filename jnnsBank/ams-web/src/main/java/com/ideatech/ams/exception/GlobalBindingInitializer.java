package com.ideatech.ams.exception;

import com.ideatech.common.util.XSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * @author wangqingan
 * @version 12/04/2018 2:32 PM
 */
@ControllerAdvice
@Component
@Slf4j
public class GlobalBindingInitializer {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if(text != null){
                    String cleanText = XSSUtil.stripXSS(text);
                    if(!cleanText.equals(text)){
                        log.info("xss clean, before[{}], after[{}]",text,cleanText);
                        text = cleanText;
                    }
                }
                setValue(text);
            }
            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
    }
}
