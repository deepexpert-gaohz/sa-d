package com.ideatech.ams.system.pbc.service;

import com.ideatech.ams.system.pbc.dto.PbcIPAddressDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

public interface PbcIPAddressService {

    void save(PbcIPAddressDto pbcIPAddressDto);

    TableResultResponse<PbcIPAddressDto> query(PbcIPAddressDto dto, Pageable pageable);

    PbcIPAddressDto getPbcIPAddress(Long id);

    PbcIPAddressDto getByPbcIPAddress(String ip);
}
