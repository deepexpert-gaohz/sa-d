package com.ideatech.ams.test;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.AmsApp;
import com.ideatech.ams.compare.dao.data.CompareDataRepository;
import com.ideatech.ams.compare.dao.jpa.CompareRepositoryFinder;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.data.CompareData;
import com.ideatech.ams.compare.service.CompareDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author vantoo
 * @date 2019-01-22 20:19
 */
@SpringBootTest(classes = AmsApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CompareDataTest {

    @Autowired
    private CompareRepositoryFinder compareRepositoryFinder;

    @Autowired
    private CompareDataService compareDataService;

    @Test
    public void save() {
        CompareDataDto compareData = new CompareDataDto();
        compareData.setAcctNo("3");

        for (int i = 1; i < 10; i++) {
            DataSourceDto dataSourceDto = new DataSourceDto();
            dataSourceDto.setDomain("CompareData" + i);
            compareDataService.saveCompareData(compareData, dataSourceDto);
        }
    }

    @Test
    public void query() {
        CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository("CompareData1");

        List<String> dataList1 = repository.getAcctNoByTaskId(1L);
        System.out.println(JSON.toJSONString(dataList1.get(0)));

        List<Object[]> dataList2 = repository.getAcctNoAndDepositorNameByTaskId(1L);
        for (Object[] objects : dataList2) {
            System.out.println(objects[0].toString());
            System.out.println(objects[1].toString());
        }

        List<CompareData> dataList = repository.findByTaskId(1L);
        System.out.println(JSON.toJSONString(dataList));

        repository.deleteByOrganFullId("1");

        dataList = repository.findByTaskId(1L);
        System.out.println(JSON.toJSONString(dataList));
    }

}
