/**
 * 
 */
package com.ideatech.ams.annual.spi;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author zhailiang
 *
 */
public interface AnnualResultProcessor {

	String getName();

	Page<AnnualResultDto> query(AnnualResultDto annualResultInfo, Pageable pageable);

	List<AnnualResultDto> queryAll(AnnualResultDto annualResultInfo);

	long count(AnnualResultDto annualResultInfo);

}
