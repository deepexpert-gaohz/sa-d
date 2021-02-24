package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.image.dto.ImageSessionDTO;
import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.service.FaceRecognitionService;
import com.ideatech.ams.image.service.ImageSessionService;
import com.ideatech.ams.image.service.ImageVideoService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author jzh
 * @date 2019-12-10.
 */

@RestController
@RequestMapping("/api/image")
public class ImageApiController {

    @Autowired
    private ImageSessionService imageSessionService;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Autowired
    private ImageVideoService imageVideoService;

    /**
     * 获取远程双录 会话信息
     * @param sessionId
     * @return
     */

    @CrossOrigin
    @GetMapping("/imageSession")
    public ResultDto getSession(Long sessionId){
        if (sessionId==null){
            return ResultDtoFactory.toNack("请输入sessionId");
        }
        ImageSessionDTO sessionDTO = imageSessionService.findOneById(sessionId);
        if (sessionDTO==null){
            return ResultDtoFactory.toNack("未查询到");
        }else {
            if ("-1".equals(sessionDTO.getFaceResult())){
                Map<String, Object> verifyResult = faceRecognitionService.getVerifyResult(sessionDTO.getIdpRequestId());
                sessionDTO.setFaceResult((String) verifyResult.get("statusCode"));
                imageSessionService.create(sessionDTO);
                return ResultDtoFactory.toAckData(sessionDTO);
            }
            //无需认证
            return ResultDtoFactory.toAckData(sessionDTO);
        }
    }

    /**
     * 双录编号查询接口
     * @param imageVideoDto
     * @return
     */
    @CrossOrigin
    @GetMapping("/imageVideo")
    public ResultDto getImageVideo(ImageVideoDto imageVideoDto){
        if (BeanValueUtils.checkObjAllFieldsIsNull(imageVideoDto)){
            return ResultDtoFactory.toNack("请输入查询条件");
        }
        Sort sort = new Sort(Direction.DESC, "lastUpdateDate");
        //利用现有方法实现
        TableResultResponse query = imageVideoService.query(imageVideoDto, new PageRequest(0, 1000, sort));
        return ResultDtoFactory.toAckData(query.getRows());
    }
}
