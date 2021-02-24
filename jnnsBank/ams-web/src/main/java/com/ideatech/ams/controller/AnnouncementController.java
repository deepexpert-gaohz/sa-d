package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.system.announcement.dto.AnnouncementDto;
import com.ideatech.ams.system.announcement.dto.AttachmentDto;
import com.ideatech.ams.system.announcement.service.AnnouncementService;
import com.ideatech.ams.system.announcement.service.AttachmentService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.entity.id.IdWorker;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jzh
 * @date 2019/2/25.
 */

@RestController
@RequestMapping("/announcement")
@Slf4j
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private IdWorker idWorker;

    /**
     * 分页获取通告
     * @param title
     * @param pageable
     * @return
     */
    @GetMapping("/page")
    public TableResultResponse page(String title,Pageable pageable){
        //pageable = new PageRequest(0,10);
        TableResultResponse tableResultResponse;
        String organfullId = SecurityUtils.getCurrentOrgFullId();
        if (title==null||title.length()==0){
            tableResultResponse = announcementService.page(pageable,organfullId);
        }else {
            tableResultResponse = announcementService.page(pageable,title,organfullId);
        }
        return tableResultResponse;
    }

    /**
     * 获取最新一条公告
     * @return
     */
    @GetMapping("/getTop")
    public ResultDto getTop(){
        AnnouncementDto announcementDto = announcementService.getTop(SecurityUtils.getCurrentOrgFullId());
        if (announcementDto==null){
            return ResultDtoFactory.toAckData("欢迎来到账户管理平台，祝您使用愉快。");
        }
        return ResultDtoFactory.toAckData(announcementDto);
    }

    /**
     * 根据id获取公告
     * @param id
     * @return
     */
    @GetMapping("getOne")
    public ResultDto getOne(Long id){
        AnnouncementDto announcementDto = announcementService.findById(id);
        return ResultDtoFactory.toAckData(announcementDto);
    }

    /**
     * 新建公告
     * @param announcementDto
     * @param attachmentId
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNOUNCEMENT,operateType = OperateType.INSERT)
    @PostMapping("save")
    public ResultDto save(AnnouncementDto announcementDto,Long attachmentId){
        announcementDto.setOrganfullId(SecurityUtils.getCurrentOrgFullId());
        announcementDto.setUsername(SecurityUtils.getCurrentUsername());
        announcementDto.setOrganName(organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId()).getName());
        announcementDto.setNoticeDate(DateUtils.getNowDateShort());
        Long id = announcementService.saveAnnouncement(announcementDto);
        if (attachmentId!=null){
            AttachmentDto attachmentDto = attachmentService.findById(attachmentId);
            attachmentDto.setAnnouncementId(id);
            attachmentService.save(attachmentDto);
        }
        return ResultDtoFactory.toAck();
    }

    /**
     * 修改公告
     * @param announcementDto
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNOUNCEMENT,operateType = OperateType.UPDATE)
    @PutMapping("update")
    public ResultDto update(AnnouncementDto announcementDto,Long attachmentId){

        if (announcementDto.getId()==null){
            return ResultDtoFactory.toNack("修改失败！");
        }
        AnnouncementDto announcementDto1 = announcementService.findById(announcementDto.getId());

        //判断是否有修改权限（谁创建谁修改）
        if (announcementDto1.getUsername()!=null&&announcementDto1.getUsername().equals(SecurityUtils.getCurrentUsername())){

            //判空
            if (announcementDto.getTitle()==null){
                return ResultDtoFactory.toNack("标题为空");
            }else if (announcementDto.getContent()==null){
                return ResultDtoFactory.toNack("内容为空");
            }else {
                announcementDto1.setTitle(announcementDto.getTitle());
                announcementDto1.setContent(announcementDto.getContent());
                announcementService.save(announcementDto1);
                if (attachmentId!=null){
                    AttachmentDto attachmentDto = attachmentService.findById(attachmentId);
                    attachmentDto.setAnnouncementId(announcementDto.getId());
                    attachmentService.save(attachmentDto);
                }
                return ResultDtoFactory.toAck();
            }
        }else {
            return ResultDtoFactory.toNack("没有修改权限");
        }


    }

    /**
     * 根据id删除公告（软删除）
     * @param id
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNOUNCEMENT,operateType = OperateType.DELETE)
    @DeleteMapping("delete")
    public ResultDto delete(Long id){
        AnnouncementDto announcementDto = announcementService.findById(id);
        if (announcementDto.getUsername().equals(SecurityUtils.getCurrentUsername())){
            announcementService.delete(id);
            return ResultDtoFactory.toAck();
        }else {
            return ResultDtoFactory.toNack("没有删除权限！");
        }

    }

    /**
     * 上传附件
     * @param file
     */
    @OperateLog(operateModule = OperateModule.ANNOUNCEMENT,operateType = OperateType.IMPORT)
    @PostMapping(value = "/upload")
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        Long id = -1L;
        ResultDto dto = new ResultDto();

        try {
            long filePath = idWorker.nextId();
            String filename = file.getOriginalFilename();
            String suffixFilename = org.apache.commons.lang.StringUtils.substringAfterLast(filename,".");
            String path = announcementService.upload(file.getInputStream(),filePath+"."+suffixFilename);
            log.info(path);
            AttachmentDto attachmentDto = new AttachmentDto();
            attachmentDto.setFilename(filename);
            attachmentDto.setFullPath(path);
            attachmentDto.setSize(file.getSize());
            id = attachmentService.saveAttachment(attachmentDto);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        dto.setCode("ACK");
        dto.setData(id);
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(dto));
//        return ResultDtoFactory.toAckData(id);
    }

    /**
     * 下载附件
     * @param id
     * @param response
     */
    @OperateLog(operateModule = OperateModule.ANNOUNCEMENT,operateType = OperateType.EXPORT)
    @RequestMapping(value = "/download")
    public ResultDto download(Long id, HttpServletResponse response) {
        try {
            if (id==null){
                return ResultDtoFactory.toAckData("附件不存在");
            }
            announcementService.download(response,id);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResultDtoFactory.toAckData(id);
    }


    @GetMapping("/getAttachment")
    public ResultDto getAttachment(Long id){
        AttachmentDto dto = attachmentService.getAttachmentByAnnouncementId(id);
        if (dto == null){
            return ResultDtoFactory.toNack(id+"：公告不存在附件");
        }
        return ResultDtoFactory.toAckData(dto);
    }
}
