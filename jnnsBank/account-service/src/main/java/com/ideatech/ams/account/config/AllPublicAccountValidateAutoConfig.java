package com.ideatech.ams.account.config;

import com.ideatech.ams.account.validate.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllPublicAccountValidateAutoConfig {

	@Bean
	@ConditionalOnMissingBean(name = "commonAllPublicAccountValidater")
	public AllPublicAccountValidate commonAllPublicAccountValidater() {
		return new CommonAllPublicAccountImportValidater();
	}

	@Bean
	@ConditionalOnMissingBean(name = "jiBenAmsOpenAllPublicAccountValidate")
	public AllPublicAccountValidate jibenAmsOpenAllAccountValidate() {
		JiBenAmsOpenValidate openValidate = new JiBenAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linShiOpenAllPublicAccountValidate")
	public AllPublicAccountValidate linshiAmsOpenAllAccountValidate() {
		LinShiAmsOpenValidate openValidate = new LinShiAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiLinShiOpenAllPublicAccountValidate")
	public AllPublicAccountValidate feilinshiAmsOpenAllAccountValidate() {
		FeiLinShiAmsOpenValidate openValidate = new FeiLinShiAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yuSuanOpenAllPublicAccountValidate")
	public AllPublicAccountValidate yusuanAmsOpenAllAccountValidate() {
		YuSuanAmsOpenValidate openValidate = new YuSuanAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiYuSuanOpenAllPublicAccountValidate")
	public AllPublicAccountValidate feiyusuanAmsOpenAllAccountValidate() {
		FeiYuSuanAmsOpenValidate openValidate = new FeiYuSuanAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yiBanOpenAllPublicAccountValidate")
	public AllPublicAccountValidate yibanAmsOpenAllAccountValidate() {
		YiBanAmsOpenValidate openValidate = new YiBanAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teShuOpenAllPublicAccountValidate")
	public AllPublicAccountValidate teshuAmsOpenAllAccountValidate() {
		TeShuAmsOpenValidate openValidate = new TeShuAmsOpenValidate();
		return commonFormProcessor(openValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanAmsChangeAllAccountValidate")
	public AllPublicAccountValidate yibanAmsChangeAllAccountValidate() {
		YiBanAmsChangeValidate chnageValidate = new YiBanAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenAmsChangeAllAccountValidate")
	public AllPublicAccountValidate jibenAmsChangeAllAccountValidate() {
		JiBenAmsChangeValidate chnageValidate = new JiBenAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiAmsChangeAllAccountValidate")
	public AllPublicAccountValidate linshiAmsChangeAllAccountValidate() {
		LinShiAmsChangeValidate chnageValidate = new LinShiAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiAmsChangeAllAccountValidate")
	public AllPublicAccountValidate feilinshiAmsChangeAllAccountValidate() {
		FeiLinShiAmsChangeValidate chnageValidate = new FeiLinShiAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanAmsChangeAllAccountValidate")
	public AllPublicAccountValidate yusuanAmsChangeAllAccountValidate() {
		YuSuanAmsChangeValidate chnageValidate = new YuSuanAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanAmsChangeAllAccountValidate")
	public AllPublicAccountValidate feiyusuanAmsChangeAllAccountValidate() {
		FeiYuSuanAmsChangeValidate chnageValidate = new FeiYuSuanAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuAmsChangeAllAccountValidate")
	public AllPublicAccountValidate teshuAmsChangeAllAccountValidate() {
		TeShuAmsChangeValidate chnageValidate = new TeShuAmsChangeValidate();
		return commonFormProcessor(chnageValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate jibenAmsSuspendAllAccountValidate() {
		JiBenAmsSuspendValidate suspendValidate = new JiBenAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate yibanAmsSuspendAllAccountValidate() {
		YiBanAmsSuspendValidate suspendValidate = new YiBanAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate yusuanAmsSuspendAllAccountValidate() {
		YuSuanAmsSuspendValidate suspendValidate = new YuSuanAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate feiyusuanAmsSuspendAllAccountValidate() {
		FeiYuSuanAmsSuspendValidate suspendValidate = new FeiYuSuanAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate linshiAmsSuspendAllAccountValidate() {
		LinShiAmsSuspendValidate suspendValidate = new LinShiAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate feilinshiAmsSuspendAllAccountValidate() {
		FeiLinShiAmsSuspendValidate suspendValidate = new FeiLinShiAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuAmsSuspendAllAccountValidate")
	public AllPublicAccountValidate teshuAmsSuspendAllAccountValidate() {
		TeShuAmsSuspendValidate suspendValidate = new TeShuAmsSuspendValidate();
		return commonFormProcessor(suspendValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "jibenAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate jibenAmsRevokeAllAccountValidate() {
		JiBenAmsRevokeValidate revokeValidate = new JiBenAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yibanAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate yibanAmsRevokeAllAccountValidate() {
		YiBanAmsRevokeValidate revokeValidate = new YiBanAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "yusuanAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate yusuanAmsRevokeAllAccountValidate() {
		YuSuanAmsRevokeValidate revokeValidate = new YuSuanAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feiyusuanAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate feiyusuanAmsRevokeAllAccountValidate() {
		FeiYuSuanAmsRevokeValidate revokeValidate = new FeiYuSuanAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "linshiAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate linshiAmsRevokeAllAccountValidate() {
		LinShiAmsRevokeValidate revokeValidate = new LinShiAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate feilinshiAmsRevokeAllAccountValidate() {
		FeiLinShiAmsRevokeValidate revokeValidate = new FeiLinShiAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "feilinshiAmsExtensionAllAccountValidate")
	public AllPublicAccountValidate feilinshiAmsExtensionAllAccountValidate() {
		FeiLinShiAmsExtensionValidate extensionValidate = new FeiLinShiAmsExtensionValidate();
		return commonFormProcessor(extensionValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "teshuAmsRevokeAllAccountValidate")
	public AllPublicAccountValidate teshuAmsRevokeAllAccountValidate() {
		TeShuAmsRevokeValidate revokeValidate = new TeShuAmsRevokeValidate();
		return commonFormProcessor(revokeValidate);
	}

	@Bean
	@ConditionalOnMissingBean(name = "amsCloseSuspendAllAccountValidate")
	public AllPublicAccountValidate amsCloseSuspendAllAccountValidate() {
		AmsCloseSuspendValidate extensionValidate = new AmsCloseSuspendValidate();
		return commonFormProcessor(extensionValidate);
	}

	public AllPublicAccountValidate commonFormProcessor(AbstractAllPublicAccountOpenValidate openValidate) {

		return openValidate;
	}
}
