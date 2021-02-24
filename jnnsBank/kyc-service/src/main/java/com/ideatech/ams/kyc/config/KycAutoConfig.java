/**
 * 
 */
package com.ideatech.ams.kyc.config;

import com.ideatech.ams.kyc.service.CarrierOperatorServiceImpl;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicInfoServiceImpl;
import com.ideatech.ams.kyc.service.SaicRequestServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wanghongjie
 *
 */
@Configuration
public class KycAutoConfig {
	@Bean
	@ConditionalOnMissingBean(name = "carrierOperatorService")
	public CarrierOperatorServiceImpl carrierOperatorService() {
		return new CarrierOperatorServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(name = "saicRequestService")
	public SaicRequestServiceImpl saicRequestService() {
		return new SaicRequestServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(name = "saicInfoService")
	public SaicInfoService saicInfoService(){
		return new SaicInfoServiceImpl();
	}

}
