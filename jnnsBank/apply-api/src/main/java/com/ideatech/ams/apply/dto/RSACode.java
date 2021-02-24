package com.ideatech.ams.apply.dto;

import lombok.Data;

@Data
public class RSACode {
	private String privateKey;
	private String thirdPublicKey;
	private String organid;
}
