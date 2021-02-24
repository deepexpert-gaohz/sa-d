package com.ideatech.ams.controller;

import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liangding
 * @create 2018-07-11 下午2:24
 **/
@RestController
public abstract class BaseController<SERVICE extends BaseService<DTO>, DTO> {
    @Autowired
    private SERVICE service;

    @GetMapping("/")
    public ResultDto<List<DTO>> list() {
        List<DTO> list = service.list();
        return ResultDtoFactory.toAckData(list);
    }

    @PostMapping("/")
    public ResultDto create(DTO dto) {
        service.save(dto);
        return ResultDtoFactory.toAck();
    }

    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, DTO dto) {
        service.save(dto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto<DTO> getById(@PathVariable("id") Long id) {
        DTO dto = service.findById(id);
        return ResultDtoFactory.toAckData(dto);
    }

    protected SERVICE getBaseService() {
        return service;
    }
}
