package com.ideatech.ams.test;

import com.ideatech.ams.AmsApp;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.spi.AmsMainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author fantao
 * @date 2019-04-26 15:40
 */
@SpringBootTest(classes = AmsApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class WebTest {

    @Autowired
    private AmsMainService amsMainService;

    @Test
    public void pbc() throws Exception {
        LoginAuth auth = amsMainService.amsLogin(null);
        System.out.println(auth.getLoginStatus().getFullName());
    }

}
