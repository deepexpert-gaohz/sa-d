package com.ideatech.ams.pbc.spi;


import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 系统同步参数接口,用以拼接同步系统接口的参数
 * 
 * @author zoulang
 *
 */
public interface SyncParameter {

	String getParams(AllAcct allAcct, SyncSystem syncSystem) throws SyncException;

}
