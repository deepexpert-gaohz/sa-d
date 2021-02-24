package com.ideatech.ams.account.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CORE_PUBLIC_ACCOUNT_FINISH",
        indexes = {@Index(name = "core_public_finish_an_idx", columnList = "acctNo")})
@Data
public class CorePublicAccountFinish extends CorePublicAccountBase implements Serializable {

	private static final long serialVersionUID = 4713307125881182573L;
	private String currencyType;
	private String currency0;
	private String currency1;
	private String nationality;
}
