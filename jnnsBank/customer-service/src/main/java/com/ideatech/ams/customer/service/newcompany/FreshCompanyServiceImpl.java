package com.ideatech.ams.customer.service.newcompany;

import com.ideatech.ams.customer.dao.newcompany.FreshCompanyDao;
import com.ideatech.ams.customer.dao.spec.FreshCompanySpec;
import com.ideatech.ams.customer.dto.neecompany.FreshCompanyDto;
import com.ideatech.ams.customer.dto.poi.FreshCompanyPoi;
import com.ideatech.ams.customer.entity.newcompany.FreshCompany;
import com.ideatech.ams.customer.service.poi.FreshCompanyQueryExport;
import com.ideatech.ams.kyc.dto.newcompany.FreshCompanyQueriesDto;
import com.ideatech.ams.kyc.dto.newcompany.OutFreshCompanyQueryDto;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.system.area.dto.AreaDto;
import com.ideatech.ams.system.area.service.AreaService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FreshCompanyServiceImpl implements FreshCompanyService {

    @Autowired
    private FreshCompanyDao freshCompanyDao;

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public TableResultResponse<FreshCompanyDto> query(FreshCompanyDto dto, Pageable pageable) {
        if (StringUtils.isBlank(dto.getBeginDate())) {
            dto.setBeginDate("00000000");
        }
        if(StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate("99999999");
        }

        Page<FreshCompany> page = freshCompanyDao.findAll(new FreshCompanySpec(dto), pageable);
        long count = freshCompanyDao.count(new FreshCompanySpec(dto));

        List<FreshCompany> freshCompanyList = page.getContent();
        List<FreshCompanyDto> listDto = ConverterService.convertToList(freshCompanyList, FreshCompanyDto.class);

        return new TableResultResponse<FreshCompanyDto>((int)count, listDto);
    }

    @Override
    public void add(String provinceCode, String startDate, String endDate) {
        System.out.println(startDate + " " + endDate);

        FreshCompany freshCompany = null;

        log.info("新增企业接口开始调用------");
        OutFreshCompanyQueryDto freshCompanyList = saicRequestService.getFreshCompanyList(provinceCode, startDate, endDate, 1, 20);

        int total = freshCompanyList.getTotal();  //接口返回总条目数
//        int total = 500;  //接口返回总条目数
        int count = 1;  //接口访问次数

        List<FreshCompanyQueriesDto> items = freshCompanyList.getItems();
        if(CollectionUtils.isNotEmpty(items)) {
            for(FreshCompanyQueriesDto dto : items) {
                if(dto.getOpenDate().indexOf(" ") != -1) {
                    dto.setOpenDate(dto.getOpenDate().split(" ")[0]);
                }

                List<FreshCompany> list = freshCompanyDao.findByName(dto.getName());
                if(CollectionUtils.isNotEmpty(list)) {
                    continue;
                } else {
                    TransactionDefinition definition = new DefaultTransactionDefinition(
                            TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transaction = transactionManager.getTransaction(definition);

                    try {
                        freshCompany = new FreshCompany();
                        BeanCopierUtils.copyProperties(dto, freshCompany);
                        saveAddress(freshCompany);
                        freshCompanyDao.save(freshCompany);

                        transactionManager.commit(transaction);
                        log.info("新增企业接口调用中:" + freshCompany.getName() + "存储成功");
                    } catch (Exception e){
                        log.error("新增企业保存失败", e);
                        transactionManager.rollback(transaction);
                    }
                }

            }

            if(total % 10 == 0) {
                count = total / 10;
            } else {
                count = total / 10 + 1;
            }

            if(count >= 1) {
                for(int i = 2; i <= count; i++) {
                    save(provinceCode, startDate, endDate, i, 20);
                }
            }

        }

        log.info("新增企业接口调用结束------");
    }

    @Override
    public void delete(String time) {
        freshCompanyDao.deleteAllByOpenDateLessThan(time);
    }

    @Override
    public FreshCompanyDto detail(Long id) {
        FreshCompany freshCompany = freshCompanyDao.findOne(id);

        if(freshCompany == null) {
            return null;
        }
        return ConverterService.convert(freshCompany, FreshCompanyDto.class);

    }

    @Override
    public IExcelExport exportExcel() {
        FreshCompanyPoi freshCompanyPoi = null;
        List<FreshCompany> freshCompanyList = freshCompanyDao.findAll();
        List<FreshCompanyDto> freshCompanyDtoList = ConverterService.convertToList(freshCompanyList, FreshCompanyDto.class);

        IExcelExport illegalQueryExport = new FreshCompanyQueryExport();
        List<FreshCompanyPoi> freshCompanyPoiList = new ArrayList<>();

        for(FreshCompanyDto freshCompanyDto : freshCompanyDtoList) {
            freshCompanyPoi = new FreshCompanyPoi();
            BeanCopierUtils.copyProperties(freshCompanyDto, freshCompanyPoi);
            freshCompanyPoiList.add(freshCompanyPoi);
        }

        illegalQueryExport.setPoiList(freshCompanyPoiList);
        return illegalQueryExport;
    }

    /**
     * @param provinceCode
     * @param startDate
     * @param endDate
     * @param pageIndex 当前页数
     * @param pageSize  每页条目数
     */
    private void save(String provinceCode, String startDate, String endDate, Integer pageIndex, Integer pageSize) {
        FreshCompany freshCompany = null;
        OutFreshCompanyQueryDto freshCompanyList = saicRequestService.getFreshCompanyList(provinceCode, startDate, endDate, pageIndex, pageSize);
        List<FreshCompanyQueriesDto> items = freshCompanyList.getItems();

        if(CollectionUtils.isNotEmpty(items)) {
            for (FreshCompanyQueriesDto dto : items) {
                if(dto.getOpenDate().indexOf(" ") != -1) {
                    dto.setOpenDate(dto.getOpenDate().split(" ")[0]);
                }

                List<FreshCompany> list = freshCompanyDao.findByName(dto.getName());
                if(CollectionUtils.isNotEmpty(list)) {
                    continue;
                } else {
                    TransactionDefinition definition = new DefaultTransactionDefinition(
                            TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transaction = transactionManager.getTransaction(definition);

                    try {
                        freshCompany = new FreshCompany();
                        BeanCopierUtils.copyProperties(dto, freshCompany);
                        saveAddress(freshCompany);
                        freshCompanyDao.save(freshCompany);

                        transactionManager.commit(transaction);
                        log.info("新增企业接口调用中:" + freshCompany.getName() + "保存成功");
                    }  catch (Exception e){
                        log.error("新增企业保存失败", e);
                        transactionManager.rollback(transaction);
                    }
                }
            }
        }
    }

    private void saveAddress(FreshCompany freshCompany) {
        AreaDto areaDto = null;

        if(StringUtils.isNotBlank(freshCompany.getUnityCreditCode())) {
            String areaCode = freshCompany.getUnityCreditCode().substring(2, 8);
            List<AreaDto> areaList = areaService.getRegistAreaCode(areaCode);

            if(CollectionUtils.isNotEmpty(areaList)) {
                areaDto = areaList.get(0);
                freshCompany.setAreaName(areaDto.getAreaName());
                freshCompany.setAreaCode(areaCode);
            }

            String cityCode = areaCode.substring(0, 4) + "00";
            List<AreaDto> cityList = areaService.getRegistAreaCode(cityCode);
            if(CollectionUtils.isNotEmpty(cityList)) {
                areaDto = cityList.get(0);
                freshCompany.setCityName(areaDto.getAreaName());
                freshCompany.setCityCode(cityCode);
            }

            String provinceCode = areaCode.substring(0, 2) + "0000";
            List<AreaDto> provinceList = areaService.getRegistAreaCode(provinceCode);
            if(CollectionUtils.isNotEmpty(provinceList)) {
                areaDto = provinceList.get(0);
                freshCompany.setProvinceName(areaDto.getAreaName());
                freshCompany.setProvinceCode(provinceCode);
            }

        }

    }

}
