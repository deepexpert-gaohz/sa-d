package com.ideatech.ams.readData.controller;


import com.ideatech.ams.readData.AlteritemMointor;
import com.ideatech.ams.readData.service.JnnsAlteritemService;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jnns")
public class JnnsAlteritemController {

    @Autowired
    private JnnsAlteritemService jnnsAlteritemService;

   @GetMapping("/queryAmteritemList")
   public TableResultResponse<AlteritemMointor> queryList(AlteritemMointor dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable){
       TableResultResponse<AlteritemMointor> tableResultResponse = jnnsAlteritemService.query(dto, pageable);
       return tableResultResponse;
   }
}
