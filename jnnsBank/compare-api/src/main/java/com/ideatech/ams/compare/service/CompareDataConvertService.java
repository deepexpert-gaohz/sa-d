package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.data.CompareDataDto;

/**
 * 人行（AMS）编号转换成中文处理接口
 * @author jzh
 * @date 2019-08-27.
 */
public interface CompareDataConvertService {

    void dataTransformation(CompareDataDto compareDataDto);

    void update();
}
