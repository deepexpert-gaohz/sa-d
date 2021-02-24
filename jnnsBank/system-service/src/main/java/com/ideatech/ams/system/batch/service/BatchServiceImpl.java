package com.ideatech.ams.system.batch.service;

import com.ideatech.ams.system.batch.dao.BatchDao;
import com.ideatech.ams.system.batch.dto.BatchDto;
import com.ideatech.ams.system.batch.dto.BatchSearchDto;
import com.ideatech.ams.system.batch.entity.BatchPo;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.ams.system.batch.spec.BatchSpec;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;

/**
 * @author fantao
 * @create 2018年10月17日15:36:08
 **/
@Service
public class BatchServiceImpl extends BaseServiceImpl<BatchDao, BatchPo, BatchDto> implements BatchService {

    private static final FastDateFormat ISO_DATETIME_TIME_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    @Override
    public String createBatch(String fileName, Long fileSize, BatchTypeEnum batchType) {
        BatchDto batchDto = new BatchDto();
        batchDto.setBatchNo(buildBatchNo(batchType));
        batchDto.setFileName(fileName);
        batchDto.setFileSize(fileSize);
        batchDto.setProcessTime(StringUtils.replace(DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis()), "T", " "));
        batchDto.setProcess(false);
        batchDto.setType(batchType);
        BatchPo batchPo = new BatchPo();
        BeanCopierUtils.copyProperties(batchDto, batchPo);
        batchDto.setId(getBaseDao().save(batchPo).getId());
        return batchDto.getBatchNo();
    }

    @Override
    public String createBatch(String fileName, Long fileSize, Long txCount, BatchTypeEnum batchType) {
        BatchDto batchDto = new BatchDto();
        batchDto.setBatchNo(buildBatchNo(batchType));
        batchDto.setFileName(fileName);
        batchDto.setFileSize(fileSize);
        batchDto.setTxCount(txCount);
        batchDto.setProcessTime(StringUtils.replace(DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis()), "T", " "));
        batchDto.setProcess(false);
        batchDto.setType(batchType);
        BatchPo batchPo = new BatchPo();
        BeanCopierUtils.copyProperties(batchDto, batchPo);
        batchDto.setId(getBaseDao().save(batchPo).getId());
        return batchDto.getBatchNo();
    }

    @Override
    public void finishBatch(String batchNo) {
        BatchPo batchPo = getBaseDao().findByBatchNo(batchNo);
        batchPo.setProcess(true);
        getBaseDao().save(batchPo);
    }

    @Override
    public TableResultResponse query(BatchSearchDto batchSearchDto, Pageable pageable) {
        //到期日期查询，包含终止日期
        if (StringUtils.isNotBlank(batchSearchDto.getProcessTimeEnd())) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.parse(batchSearchDto.getProcessTimeEnd(), DateFormatUtils.ISO_DATE_FORMAT.getPattern()));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                batchSearchDto.setProcessTimeEnd(DateFormatUtils.ISO_DATE_FORMAT.format(calendar));
            } catch (ParseException e) {
                //ignore
            }
        }
        Page<BatchPo> page = getBaseDao().findAll(new BatchSpec(batchSearchDto), pageable);
        TableResultResponse response = new TableResultResponse((int) page.getTotalElements(), page.getContent());
        return response;
    }

    @Override
    public void updateBatchCount(String batchNo, Long txCount) {
        BatchPo batchPo = getBaseDao().findByBatchNo(batchNo);
        batchPo.setTxCount(txCount);
        getBaseDao().save(batchPo);
    }

    /**
     * 生成批次号
     * <p>
     * 时间流水类型+yyyyMMddHHmmss+3位随机数
     *
     * @return
     */
    private String buildBatchNo(BatchTypeEnum batchType) {
        return batchType.name() + ISO_DATETIME_TIME_FORMAT.format(System.currentTimeMillis()) + RandomStringUtils.randomNumeric(3);
    }
}
