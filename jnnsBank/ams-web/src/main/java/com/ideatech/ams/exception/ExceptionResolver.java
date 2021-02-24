package com.ideatech.ams.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.exception.DisplayableError;
import com.ideatech.common.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Class Name: ExceptionResolver
 * <p>
 * Description: the <code>ValidateException</code> handler<br>
 * the validation from service will be wrapped into <code>ValidateException</code>, then the handler will catch the
 * exception and return the errors into view
 *
 * @author SC
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    @Autowired
    BindingResultExceptionHandler bindingResultExceptionHandler;

    @Autowired
    BeanValidatorExceptionHandler beanValidatorExceptionHandler;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
                                         final Object handler, final Exception ex) {
        try {
            String formId = request.getHeader(IdeaConstant.X_FORM_ID);
            Locale locale = request.getLocale();
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json;charset=UTF-8");
            ResultDto<?> error = getErrorDto(ex, handler, formId, locale);
            if (error.isNonBizError()) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else {
                response.setStatus(HttpStatus.OK.value());
            }
            PrintWriter writer = response.getWriter();
            objectMapper.writeValue(response.getWriter(), error);
            writer.flush();
        } catch (IOException ie) {
            LOGGER.error("Failed to serialize the object to json for exception handling.", ie);
        }
        return new ModelAndView();
    }

    /**
     * Description: return the error message to front-end
     *
     * @param ex
     * @param handler
     * @param formId
     * @param locale
     * @return
     */
    private ResultDto<?> getErrorDto(final Exception ex, final Object handler, final String formId, final Locale locale) {
        ResultDto<?> error = null;
        if (ex instanceof ValidateException) {
            return bindingResultExceptionHandler.buildErrorDto(ex, handler, formId);
        } else if (ex instanceof ConstraintViolationException) {
            return beanValidatorExceptionHandler.buildErrorDto(ex, handler, formId);
        } else if (ex instanceof BizServiceException) {
            BizServiceException bizEx = (BizServiceException) ex;
            DisplayableError errorCode = bizEx.getError();
            String msg = null;
            if (errorCode != null) {
                msg = bizEx.getMessage();
                if (StringUtils.isEmpty(msg)) {
                    msg = MessageUtil.getMessage(errorCode.getDisplayMsg(), locale, errorCode.getArgs());
                }
                if (StringUtils.isEmpty(msg)) {
                    msg = errorCode.getDisplayMsg();
                }
                if (errorCode.isBizError()) {
                    error = ResultDtoFactory.toBizError(msg, ex);
                } else {
                    error = ResultDtoFactory.toCommonError(bizEx);
                }
            } else {
                error = ResultDtoFactory.toCommonError(bizEx);
            }
            LOGGER.warn("exception message: {} \n error code message：{}", ex.getMessage(), msg);
        } else if (ex instanceof MaxUploadSizeExceededException) {
            error = ResultDtoFactory.toNack("文件大小必须小于2M，请重新上传");
        } else {
            error = ResultDtoFactory.toCommonError(ex);
            LOGGER.error("Unknown exception handled:", ex);
        }
        return error;
    }
}
