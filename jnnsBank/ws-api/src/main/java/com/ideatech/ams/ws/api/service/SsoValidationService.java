package com.ideatech.ams.ws.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SsoValidationService {
    String getValidUsername(HttpServletRequest request, HttpServletResponse response);
}
