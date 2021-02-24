package com.ideatech.ams.image.service;

import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.dto.ImageVideoUpdateDto;
import com.ideatech.ams.image.dto.VideoFileDTO;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface ImageVideoService extends BaseService<ImageVideoDto> {

    /**
     * 查询双录视频
     * @param imageVideoDto
     * @param pageable
     * @return
     */
    TableResultResponse query(ImageVideoDto imageVideoDto,Pageable pageable);

    /**
     * 导出双录视频
     * @param imageVideoDto
     * @return
     */
    IExcelExport export(ImageVideoDto imageVideoDto);

    /**
     * 上传视频
     * @param multipartFile
     */
    String upload(MultipartFile multipartFile,String filename) throws Exception;

    /**
     * 处理压缩包
     * @param multipartFile
     * @param filename
     */
    void uploadZip(MultipartFile multipartFile,String filename,ImageVideoDto dto) throws Exception;
    /**
     * 上传视频到影像平台
     * @param billId 流水id
     * @return 返回失败数量,返回0代表上传成功
     */
    int syncToImage(Long billId);

    int syncToImage(String depositorName);
    /**
     * 定时任务同步
     */
    void schedule();

    /**
     * 删除视频
     * @param id
     * @return
     */
    ResultDto delete(Long id);

    /**
     * 下载视频
     * @param id
     * @param response
     */
    void downloadImageZip(Long id,HttpServletResponse response);

    /**
     * 根据条件查询视频
     * @param dto
     * @return
     */
    List<ImageVideoDto> searchByOut(ImageVideoDto dto);

    /**
     * 根据流水查找视频，并返回可播放的视频地址
     * @param billId
     * @return
     */
    ResultDto findByBillId(Long billId);

    /**
     * 根据预约编号查询视频
     * @param applyId
     * @return
     */
    ResultDto findByApplyId(String applyId);

    /**
     * 根据条件查询视频
     * @param condition
     * @return
     */
    ResultDto findByCondition(ImageVideoUpdateDto condition);
    /**

     */
    ResultDto findByOne(Long id);

    ResultDto findOneInfo(Long id);

    ResultDto findOne(Long id);

    /**
     * idp查询视频接口
     * @param applyid
     * @param billId
     */
    void getFromIdp(String applyid,Long billId);

    /**
     * 编辑视频信息
     * @param imageVideoDto
     * @return
     */
    ResultDto edit(ImageVideoDto imageVideoDto);

    /**
     * 根据会议id获取会议视频信息
     * @param callId
     * @return
     */
    VideoFileDTO getVideoFileByCallId(String callId);
}
