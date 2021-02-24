package com.ideatech.ams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ideatech.ams.account.service.core.CompanyImportAccess;

@Controller
@RequestMapping("/grid")
public class GridController {
	@Autowired
	CompanyImportAccess access;

	@RequestMapping(value = "/testAccess")
	public void test() {
		try {
			access.mainAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
