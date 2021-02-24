package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.dao.core.CoreFileBatchDao;
import com.ideatech.ams.account.dto.core.CoreFileBatchDto;
import com.ideatech.ams.account.entity.core.CoreFileBatchPo;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author vantoo
 */
@Service
public class CoreFileBatchServiceImpl extends BaseServiceImpl<CoreFileBatchDao, CoreFileBatchPo, CoreFileBatchDto> implements CoreFileBatchService {

    @Override
    public Long create(File file) {
        CoreFileBatchPo coreFileBatchPo = new CoreFileBatchPo();
        coreFileBatchPo.setFileName(file.getName());
        coreFileBatchPo.setFileSize(file.length());
        coreFileBatchPo.setProcessTime(StringUtils.replace(DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis()), "T", " "));
        coreFileBatchPo.setProcessStatus(false);
        return getBaseDao().save(coreFileBatchPo).getId();
    }
}
