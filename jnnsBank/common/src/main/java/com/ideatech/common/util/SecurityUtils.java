package com.ideatech.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {


    private static final ObjectMapper mapper = new ObjectMapper();


    public static void sendError(HttpServletResponse response, Exception exception, int status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        ResultDto resultDto = ResultDtoFactory.toUnauthorized(message);
        writer.write(mapper.writeValueAsString(resultDto));
        writer.flush();
        writer.close();
    }


    public static void sendResponse(HttpServletResponse response, int status, Object object) throws IOException {

        sendResponseBase(response,status,object,"application/json;charset=UTF-8");
    }

    public static void sendResponseJsonFile(HttpServletResponse response, int status, Object object) throws
            IOException {
        sendResponseBase(response,status,object,"text/json;charset=UTF-8");
    }

    private static void sendResponseBase(HttpServletResponse response, int status, Object object, String contentType)
            throws
            IOException {
        response.setContentType(contentType);
        PrintWriter writer = response.getWriter();
        ResultDto resultDto = ResultDtoFactory.toAckData(object);
        writer.write(mapper.writeValueAsString(resultDto));
        response.setStatus(status);
        writer.flush();
        writer.close();
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfo) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user.getUsername();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfo) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }

    public static UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfo) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user;
        }
        return null;
    }

    public static String getCurrentOrgFullId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfo) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user.getOrgFullId();
        }
        return null;
    }

    @Data
    public static class UserInfo extends User{
        private Long id;
        private Long orgId;
        private String orgFullId;
        private Long roleId;

        public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }

        public UserInfo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        }
    }

}
