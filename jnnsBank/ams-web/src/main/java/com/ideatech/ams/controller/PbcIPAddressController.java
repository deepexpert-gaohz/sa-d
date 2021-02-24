package com.ideatech.ams.controller;

import com.ideatech.ams.system.pbc.dto.PbcIPAddressDto;
import com.ideatech.ams.system.pbc.service.PbcIPAddressService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pbc/address")
public class PbcIPAddressController {

    @Autowired
    private PbcIPAddressService pbcIPAddressService;

    @GetMapping("/list")
    public TableResultResponse<PbcIPAddressDto> list(PbcIPAddressDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return pbcIPAddressService.query(dto, pageable);
    }

    @OperateLog(operateModule = OperateModule.ORGANIZATION, operateType = OperateType.UPDATE,operateContent = "地方人行设置修改")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDto saveAndUpdate(PbcIPAddressDto info){
        pbcIPAddressService.save(info);
        return ResultDtoFactory.toAck();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultDto getPbcIPAddress(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(pbcIPAddressService.getPbcIPAddress(id));
    }

}
