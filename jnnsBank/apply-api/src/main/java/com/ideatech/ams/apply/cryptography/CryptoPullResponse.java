package com.ideatech.ams.apply.cryptography;

import lombok.Data;

import java.util.List;

@Data
public class CryptoPullResponse {
	private String message;
	private String status;
	private List<CryptoPullVo> data;
}
