package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.data.CompareDataDto;

public interface DataTransformation<T> {

    void dataTransformation(CompareDataDto compareDataDto, T t);
}
