package com.ideatech.ams.controller;

import com.ideatech.ams.dto.AcctNo;
import com.ideatech.ams.service.JnnsGetAcctNoService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/jnns")
@RestController
@Slf4j
public class JnnsGetAcctNoController {


    @Autowired
    private JnnsGetAcctNoService jnnsGetAcctNoService;


    @PostMapping("/getJnnsImageAll")
   public   ResultDto findByAcctNo(@RequestBody AcctNo acctNo, HttpServletResponse response){
        log.info("账号====="+acctNo.getAcctNo());
        ResultDto resultDto=new ResultDto();
        List<String> list = null;
        if (StringUtils.isEmpty(acctNo.getAcctNo())){
            log.info("账号为空");
            resultDto.setCode("0");
            resultDto.setMessage("账号不可为空======");
            resultDto.setData(null);
        }else {
            list = jnnsGetAcctNoService.findByAcctNo(acctNo.getAcctNo());
            if (!list.isEmpty()&&list!=null){
                resultDto.setCode("1");
                resultDto.setMessage("查询成功");
                resultDto.setData(list);
            } else{
                resultDto.setCode("0");
                resultDto.setMessage("查询数据为空");
                resultDto.setData(null);
            }
        }
       return resultDto;
    }
}
