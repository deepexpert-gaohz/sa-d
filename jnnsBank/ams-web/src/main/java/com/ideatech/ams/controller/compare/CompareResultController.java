package com.ideatech.ams.controller.compare;

import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.service.CompareExportService;
import com.ideatech.ams.compare.service.CompareResultService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.FileExtraUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compareResult")
@Slf4j
public class CompareResultController {

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private CompareExportService compareExportService;
    /**
     * 比对结果列表名获取
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/struct/{taskId}", method = RequestMethod.GET)
    public Map<String, Object> getCompareResultGridStruct(@PathVariable Long taskId) {
        return compareResultService.getCompareResultFieldStruct(taskId);
    }

    /**
     * 比对结果列表
     * @param dto
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public TableResultResponse list(CompareResultDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return compareResultService.query(dto, pageable);

    }

    /**
     * 比对结果详情
     * @param dto
     * @return
     */
    @GetMapping("/detail")
    public CompareResultDetailDto detail(CompareResultDto dto) throws Exception {
        return compareResultService.detail(dto);

    }

    /**
     * 比对结果excel导出
     * @param taskId
     * @param matchType
     * @param organId
     * @param request
     * @param response
     */
    @GetMapping("/exportXLS/{taskId}")
    public void export(@PathVariable Long taskId, String matchType, Long organId, HttpServletRequest request, HttpServletResponse response) {
        if (organId == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }

        String filePath = compareExportService.exportXlsResult(taskId, matchType, organId);
        downloadFile(request, response, filePath, "compareResult.xls");
    }

    /**
     * 比对结果txt导出
     * @param taskId
     * @param matchType
     * @param organId
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportTxt/{taskId}", method = RequestMethod.GET)
    public void exportTxt(@PathVariable Long taskId, String matchType, Long organId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (organId == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }

        String filePath = compareExportService.exportTxtResult(taskId, matchType, organId);
        downloadFile(request, response, filePath, "compareResult.txt");
    }

    /**
     * @param request
     * @param response
     * @param filePath
     * @param fileName
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + FileExtraUtils.handleFileName(request, fileName));
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

}
