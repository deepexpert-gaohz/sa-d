package com.ideatech.ams.account.service.core;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface Map2DomainService {

	Object converter(Map<String, String> formMap, Class<?> clazz) throws Exception;
}
