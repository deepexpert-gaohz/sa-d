package com.ideatech.ams.controller.apply;

import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.dto.EzhMessageDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.apply.service.EzhMessageService;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ezhMessage")
@Slf4j
public class EzhMessageController {

    @Autowired
    private EzhMessageService ezhMessageService;

    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    /**
     * 短信发送历史列表
     * @param dto
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableResultResponse<EzhMessageDto> list(EzhMessageDto dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<EzhMessageDto> tableResultResponse =  ezhMessageService.query(dto, pageable);

        return tableResultResponse;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public ObjectRestResponse<EzhMessageDto> sendMessage(Long id) {
        Boolean flag = companyPreOpenAccountEntService.sendMessage(id);

        if(!flag) {
            return new ObjectRestResponse<EzhMessageDto>().rel(false).msg("短信重新发送失败");
        }

        return new ObjectRestResponse<EzhMessageDto>().rel(true).msg("短信重新发送成功");
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ObjectRestResponse<EzhMessageDto> getOne(Long id) {
        return new ObjectRestResponse<EzhMessageDto>().rel(true).result(ezhMessageService.getOne(id));

    }

}


