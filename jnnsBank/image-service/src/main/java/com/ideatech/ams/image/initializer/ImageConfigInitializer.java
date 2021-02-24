package com.ideatech.ams.image.initializer;

import com.ideatech.ams.image.dao.ImageConfigTerraceDao;
import com.ideatech.ams.image.entity.ImageConfigTerrace;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageConfigInitializer extends AbstractDataInitializer {
    @Autowired
    private ImageConfigTerraceDao imageConfigTerraceDao;

    @Value("${ams.image.socketPort}")
    private String socketPort;

    @Value("${ams.image.username}")
    private String username;

    @Value("${ams.image.password}")
    private String password;

    @Value("${ams.image.serverName}")
    private String serverName;

    @Value("${ams.image.groupName}")
    private String groupName;

    @Value("${ams.image.coreGroupName}")
    private String coreGroupName;

    @Value("${ams.image.STARTCOLUMN}")
    private String STARTCOLUMN;

    @Value("${ams.image.modelCode}")
    private String modelCode;

    @Value("${ams.image.filePartName}")
    private String filePartName;


    @Value("${ams.image.ip}")
    private String ip;

    @Override
    protected void doInit() throws Exception {

        ImageConfigTerrace config =imageConfigTerraceDao.findOne(1001L);
        if(config==null){
            config = new ImageConfigTerrace();
            config.setId(1001L);
        }
        config.setIp(ip);
        config.setSocketPort(socketPort);
        config.setUsername(username);
        config.setPassword(password);
        config.setServerName(serverName);
        config.setGroupName(groupName);
        config.setCoreGroupName(coreGroupName);
        config.setSTARTCOLUMN(STARTCOLUMN);
        config.setModelCode(modelCode);
        config.setFilePartName(filePartName);
        imageConfigTerraceDao.save(config);
    }

    @Override
    protected boolean isNeedInit() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
