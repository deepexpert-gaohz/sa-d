package com.ideatech.ams.test.mock;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenOpenBeiAnService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import org.apache.http.impl.client.BasicCookieStore;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * morck人行方法，没人行环境测试时使用
 *
 * @author fantao
 * @date 2019-04-26 14:16
 */
@ActiveProfiles("mock")
@Configuration
public class PbcMocker {

    @Bean
    public AmsMainService amsMainService() {
        AmsMainService amsMainService = Mockito.mock(AmsMainService.class);

        LoginAuth mockLoginAuth = new LoginAuth("1.1.1.1", "u", "p");
        mockLoginAuth.setCookie(new BasicCookieStore());
        mockLoginAuth.setLoginStatus(LoginStatus.Success);

        Mockito.when(amsMainService.amsLogin(Mockito.any(PbcUserAccount.class))).thenReturn(mockLoginAuth);

        return amsMainService;
    }

    @Bean
    public AmsJibenOpenBeiAnService amsJibenOpenBeiAnService() {
        AmsJibenOpenBeiAnService amsJibenOpenBeiAnService = Mockito.mock(AmsJibenOpenBeiAnService.class);
        try {
            Mockito.doNothing().when(amsJibenOpenBeiAnService).openAccountFirstStep(Mockito.any(LoginAuth.class), Mockito.any(AllAcct.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amsJibenOpenBeiAnService;
    }
}
