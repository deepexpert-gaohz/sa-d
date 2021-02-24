package com.ideatech.ams.readData;

import com.ideatech.ams.readData.dao.JnnsAlteritemDao;
import com.ideatech.ams.readData.service.ReadDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author panxiaochou
 * date  2020-8-27
 * 流读文件且写入数据库
 */

@Service
@Slf4j
public class ReadData implements ReadDataService {

    @Autowired
    private JnnsAlteritemDao jnnsAlteritemDao;

    @Override
    public void readData() {

        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH,-1);
        //String inputFile = "/home/weblogic/data/odm_wbd_gsbg_alteritem_mointor_out_"+sd.format(c.getTime())+".txt";
        String inputFile = "F:\\project\\backupFile\\外部集市数据\\odm_wbd_gsbg_alteritem_mointor_out_20201025.txt";
        log.info("---------------");
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            String line = null;
            while (in.ready()) {
                line = in.readLine();
                String[] arr = line.split("@_@");
                AlteritemMointorInfo am = new AlteritemMointorInfo();

                am.setCustomerId(arr[1]);
                am.setAlterItem(arr[2]);
                am.setAlterBefore(arr[3]);
                am.setAlterAfter(arr[4]);
                am.setWarnTime(arr[5]);
                am.setDataDt(arr[6]);
                am.setEtlDate(arr[7]);


                jnnsAlteritemDao.save(am);
                log.info("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    }

