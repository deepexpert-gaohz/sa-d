package com.ideatech.ams.controller.annual;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class HomeController {

    /**
     * 年检结果待处理
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/annualcheck/processdata",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<TableResultResponse<JSONObject>> processdata() throws IOException {
        List<JSONObject> item = new ArrayList<>();

        for(int i = 0; i<10;i++){
            JSONObject coreStatus = new JSONObject();
            coreStatus.put("id",i+1);
            coreStatus.put("account","21345678");
            coreStatus.put("name","21345678");
            coreStatus.put("code","21345678");
            coreStatus.put("saicStatus","未找到");
            coreStatus.put("anualStatus","待年检");
            coreStatus.put("status","正常");
            coreStatus.put("isSame","true");

            item.add(coreStatus);
        }
        TableResultResponse<JSONObject> resultList = new TableResultResponse<JSONObject>(
                10, item);
        return new ObjectRestResponse<TableResultResponse<JSONObject>>().rel(true).result(resultList);
    }
}
