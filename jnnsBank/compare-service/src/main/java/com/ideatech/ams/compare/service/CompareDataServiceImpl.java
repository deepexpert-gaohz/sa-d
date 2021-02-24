package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.data.CompareDataRepository;
import com.ideatech.ams.compare.dao.jpa.CompareRepositoryFinder;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.data.CompareData;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author vantoo
 * @date 2019-01-22 19:09
 */
@Service
public class CompareDataServiceImpl implements CompareDataService {

    @Autowired
    private CompareRepositoryFinder compareRepositoryFinder;

    @Autowired
    private CompareDataConvertService compareDataConvertService;

    @Override
    public void saveCompareData(CompareDataDto compareDataDto, DataSourceDto dataSourceDto) {

        //pbc字典编码转中文处理
        compareDataConvertService.dataTransformation(compareDataDto);

        CompareData compareData = getCompareData(dataSourceDto);
        BeanCopierUtils.copyProperties(compareDataDto, compareData);
        CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
        repository.save(compareData);
    }

    @Override
    public List<CompareDataDto> getCompareData(Long taskId, DataSourceDto dataSourceDto) {
        CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
        List<CompareData> compareDatas = repository.findByTaskId(taskId);
        return ConverterService.convertToList(compareDatas, CompareDataDto.class);
    }

    @Override
    public List<CompareDataDto> getCompareData(Long taskId, DataSourceDto dataSourceDto, Pageable pageable) {
        CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
        Page<CompareData> compareDatas = repository.findByTaskId(taskId,pageable);
        List<CompareData> list = compareDatas.getContent();
        return ConverterService.convertToList(list, CompareDataDto.class);
    }

    @Override
    public List<String> getCompareDataAcctNo(Long taskId, DataSourceDto dataSourceDto) {
        return null;
    }

    @Override
    public void delCompareData(Long taskId, DataSourceDto dataSourceDto) {
        CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
        List<CompareData> list = repository.findByTaskId(taskId);
        repository.delete(list);
    }


    private CompareData getCompareData(DataSourceDto dataSourceDto) {
        String domainPackage = dataSourceDto.getDomainPackage();
        if (StringUtils.isBlank(domainPackage)) {
            domainPackage = CompareData.domainPackage;
        }
        try {
            return (CompareData) Class.forName(domainPackage + dataSourceDto.getDomain()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
