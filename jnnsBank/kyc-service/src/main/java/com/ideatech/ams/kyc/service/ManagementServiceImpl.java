package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dao.DirectorDao;
import com.ideatech.ams.kyc.dao.ManagementDao;
import com.ideatech.ams.kyc.dao.SuperviseDao;
import com.ideatech.ams.kyc.dto.ManagementDto;
import com.ideatech.ams.kyc.entity.Director;
import com.ideatech.ams.kyc.entity.Management;
import com.ideatech.ams.kyc.entity.Supervise;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    private ManagementDao managementDao;
    
    @Autowired
    private DirectorDao directorDao;
    
    @Autowired
    private SuperviseDao superviseDao;
    

    @Override
    public void insertBatch(Long saicInfoId, List<ManagementDto> managementDtoList) {
        Management management = null;

        int size = managementDtoList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (ManagementDto managementDto: managementDtoList) {
            management = new Management();
            BeanCopierUtils.copyProperties(managementDto, management);
//            management.setId(Calendar.getInstance().getTimeInMillis());
            management.setSaicinfoId(saicInfoId);
            managementDao.save(management);
        }
    }

	@Override
	public JSONObject getManagersInfoBySaicInfoIdInLocal(Long saicInfoId) {
        JSONObject jsonObject = new JSONObject();
        List<Director> director = directorDao.findBySaicinfoId(saicInfoId);
        List<Supervise> supervise = superviseDao.findBySaicinfoId(saicInfoId);
        List<Management> management = managementDao.findBySaicinfoId(saicInfoId);
        jsonObject.put("directorList",director);
        jsonObject.put("superviseList",supervise);
        jsonObject.put("managementList",management);
        return jsonObject;
	}
    
    
}
