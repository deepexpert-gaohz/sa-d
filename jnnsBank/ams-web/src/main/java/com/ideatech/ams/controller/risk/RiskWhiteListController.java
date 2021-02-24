package com.ideatech.ams.controller.risk;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.risk.whiteList.dto.WhiteListDto;
import com.ideatech.ams.risk.whiteList.dto.WhiteListSearchDto;
import com.ideatech.ams.risk.whiteList.entity.WhiteList;
import com.ideatech.ams.risk.whiteList.service.WhiteListService;
import com.ideatech.ams.risk.whiteList.vo.RiskWhiteListVo;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 10:28
 * @description
 * 白名单管理
 */

@RestController
@RequestMapping("whiteList")
public class RiskWhiteListController {

    @Autowired
    WhiteListService whiteListService;
    @Autowired
    OrganizationDao organizationDao;

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:11
     * @description:
     * 添加白名单
     */
    @PostMapping("saveWhiteList")
    public ResultDto saveWhiteList(WhiteListDto whiteListDto){
        WhiteListDto byAccountId = whiteListService.findByAccountId(whiteListDto.getAccountId());
        if (null != byAccountId){
            return ResultDtoFactory.toNack("账户号已存在");
        }
        WhiteListDto Social = whiteListService.findBySocialUnifiedCode(whiteListDto.getSocialUnifiedCode());
        if (null != Social){
            return ResultDtoFactory.toNack("社会统一编码已存在");
        }
       // whiteListDto.setCorporateBank( RiskUtil.getOrganizationCode());
        whiteListService.saveWhiteList(whiteListDto);
        return ResultDtoFactory.toAck();
    }

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:12
     * @description:
     * 查询白名单
     */
    @GetMapping("searchWhiteList")
    public ResultDto searchWhiteList(WhiteListSearchDto whiteListSearchDto){
//        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
//        //根据登陆人的信息获取当前登陆人的机构行Code
//        String currentCode="";
//        List<OrganizationPo> byFullIdLike = organizationDao.findByFullIdLike("%" + user.getOrgFullId ()+"%");
//        if (byFullIdLike!=null) {
//            currentCode = byFullIdLike.get(0).getCode();
//        }
//        whiteListSearchDto.setCorporateBank ( currentCode );
        return ResultDtoFactory.toAckData(whiteListService.searchWhiteListDto(whiteListSearchDto));
    }

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:12
     * @description:
     * 根据id获得详细信息
     */
    @GetMapping("/{id}")
    public ResultDto getById(@PathVariable("id") Long id){
        return ResultDtoFactory.toAckData(whiteListService.findWhiteListDtoById(id));
    }

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:13
     * @description:
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public ResultDto delById(@PathVariable("id") Long id){
        whiteListService.delWhiteListDto(id);
        return ResultDtoFactory.toAck();
    }

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:13
     * @description:
     * 根据id修改
     */
    @PutMapping("/{id}")
    public ResultDto updateWhiteList(@PathVariable("id") Long id, WhiteListDto whiteListDto){
        whiteListDto.setId(id);
        whiteListService.saveWhiteList(whiteListDto);
        return ResultDtoFactory.toAck();
    }

    /**
     * @author:yinjie
     * @date:2019/5/27
     * @time:16:13
     * @description:
     * accountId的重复验证
     */
    @GetMapping("getByAccountId/{accountId}")
    public ResultDto getByAccountId(@PathVariable("accountId") String accountId){
        WhiteListDto byAccountId = whiteListService.findByAccountId(accountId);
        return ResultDtoFactory.toAckData(byAccountId);
    }

    /**
     * 导入风险白名单
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping("/uploadRiskWhite")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();

        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 3) {
                dto.setCode( ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
                //return ResultDtoFactory.toNack("导入失败，错误的模板");
            }else{
                List<RiskWhiteListVo> dataList = importExcel.getDataList(RiskWhiteListVo.class);
                List<WhiteList> whiteListDtos = ConverterService.convertToList(dataList, WhiteList.class);
                for (WhiteList dtos : whiteListDtos) {
                    if (StringUtils.isBlank(dtos.getAccountId ())) {
                        continue;
                    }
                    WhiteListDto white = new WhiteListDto();
                    WhiteListDto byAccountId = whiteListService.findByAccountId(dtos.getAccountId ());
                    WhiteListDto Social = whiteListService.findBySocialUnifiedCode(dtos.getSocialUnifiedCode());
                    if(byAccountId==null && Social==null){
                        white.setAccountId ( dtos.getAccountId () );
                        white.setAccountName ( dtos.getAccountName () );
                        white.setSocialUnifiedCode ( dtos.getSocialUnifiedCode () );
                        whiteListService.saveWhiteList ( white );
                    }
                }
                dto.setCode( ResultCode.ACK);
                dto.setMessage("导入成功");
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write( JSON.toJSONString(dto));
        } catch (Exception e) {
            dto.setCode( ResultCode.NACK);
            dto.setMessage("导入白名单失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

}
