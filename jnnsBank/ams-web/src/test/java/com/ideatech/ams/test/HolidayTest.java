package com.ideatech.ams.test;

import com.ideatech.ams.AmsApp;
import com.ideatech.ams.account.service.core.CompanyImportAccess;
import com.ideatech.ams.system.dict.service.DictionaryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = AmsApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class HolidayTest {


    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CompanyImportAccess access;

    @Test
    public void test() throws Exception {

        access.mainAccess();
    }
}
