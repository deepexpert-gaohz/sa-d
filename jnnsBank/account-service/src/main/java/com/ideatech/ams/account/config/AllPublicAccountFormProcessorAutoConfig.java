/**
 * 
 */
package com.ideatech.ams.account.config;

import com.ideatech.ams.account.spi.AllPublicAccountFormProcessor;
import com.ideatech.ams.account.spi.processor.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhailiang
 *
 */
@Configuration
public class AllPublicAccountFormProcessorAutoConfig {
	
	@Bean
	@ConditionalOnMissingBean(name = "jibenOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor jibenOpenFormProcessor() {
		JibenOpenFormProcessor formProcessor = new JibenOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor jibenRevokeFormProcessor() {
		JibenRevokeFormProcessor formProcessor = new JibenRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor jibenChangeFormProcessor() {
		JibenChangeFormProcessor formProcessor = new JibenChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor jibenSuspendFormProcessor() {
		JibenSuspendFormProcessor formProcessor = new JibenSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yibanOpenFormProcessor() {
		YibanOpenFormProcessor formProcessor = new YibanOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yibanChangeFormProcessor() {
		YibanChangeFormProcessor formProcessor = new YibanChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yibanRevokeFormProcessor() {
		YibanRevokeFormProcessor formProcessor = new YibanRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yibanSuspendFormProcessor() {
		YibanSuspendFormProcessor formProcessor = new YibanSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yusuanOpenFormProcessor() {
		YusuanOpenFormProcessor formProcessor = new YusuanOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yusuanChangeFormProcessor() {
		YusuanChangeFormProcessor formProcessor = new YusuanChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yusuanSuspendFormProcessor() {
		YusuanSuspendFormProcessor formProcessor = new YusuanSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yusuanRevokeFormProcessor() {
		YusuanRevokeFormProcessor formProcessor = new YusuanRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feiyusuanOpenFormProcessor() {
		FeiyusuanOpenFormProcessor formProcessor = new FeiyusuanOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feiyusuanChangeFormProcessor() {
		FeiyusuanChangeFormProcessor formProcessor = new FeiyusuanChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feiyusuanSuspendFormProcessor() {
		FeiyusuanSuspendFormProcessor formProcessor = new FeiyusuanSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feiyusuanRevokeFormProcessor() {
		FeiyusuanRevokeFormProcessor formProcessor = new FeiyusuanRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor linshiOpenFormProcessor() {
		LinshiOpenFormProcessor formProcessor = new LinshiOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor linshiChangeFormProcessor() {
		LinshiChangeFormProcessor formProcessor = new LinshiChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor linshiRevokeFormProcessor() {
		LinshiRevokeFormProcessor formProcessor = new LinshiRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor linshiSuspendFormProcessor() {
		LinshiSuspendFormProcessor formProcessor = new LinshiSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feilinshiOpenFormProcessor() {
		FeilinshiOpenFormProcessor formProcessor = new FeilinshiOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feilinshiChangeFormProcessor() {
		FeilinshiChangeFormProcessor formProcessor = new FeilinshiChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feilinshiRevokeFormProcessor() {
		FeilinshiRevokeFormProcessor formProcessor = new FeilinshiRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiExtensionAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feilinshiExtensionFormProcessor() {
		FeilinshiExtensionFormProcessor formProcessor = new FeilinshiExtensionFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor feilinshiSuspendFormProcessor() {
		FeilinshiSuspendFormProcessor formProcessor = new FeilinshiSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor teshuOpenFormProcessor() {
		TeshuOpenFormProcessor formProcessor = new TeshuOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuChangeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor teshuChangeFormProcessor() {
		TeshuChangeFormProcessor formProcessor = new TeshuChangeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuSuspendAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor teshuSuspendFormProcessor() {
		TeshuSuspendFormProcessor formProcessor = new TeshuSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuRevokeAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor teshuRevokeFormProcessor() {
		TeshuRevokeFormProcessor formProcessor = new TeshuRevokeFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yanziOpenAllPublicAccountFormProcessor")
	public AllPublicAccountFormProcessor yanziOpenFormProcessor() {
		YanziOpenFormProcessor formProcessor = new YanziOpenFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	@Bean
	@ConditionalOnMissingBean(name = "amsCloseSuspendFormProcessor")
	public AllPublicAccountFormProcessor amsCloseSuspendFormProcessor() {
		AmsCloseSuspendFormProcessor formProcessor = new AmsCloseSuspendFormProcessor();
		return commonFormProcessor(formProcessor);
	}

	public AllPublicAccountFormProcessor commonFormProcessor(BasicAllPublicAccountFormDataProcessor formProcessor) {
		return formProcessor;
	}

}
