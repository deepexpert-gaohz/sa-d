package com.ideatech.ams.permissions;

import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alven on 31/05/2017.
 */
@Component
@Slf4j
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.error(exception.getMessage(),exception);
        if(exception instanceof SessionAuthenticationException){
            SecurityUtils.sendError(response, exception, HttpServletResponse.SC_OK, "用户重复登陆");
        }else{
            SecurityUtils.sendError(response, exception, HttpServletResponse.SC_OK, exception.getMessage());
        }

    }
}
