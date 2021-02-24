package com.ideatech.ams.compare.domain;

import com.ideatech.ams.compare.entity.DataSource;
import com.ideatech.ams.system.user.entity.UserPo;
import lombok.Data;

@Data
public class DataSourceDo {
    private DataSource dataSource;
    private UserPo userPo;

    public DataSourceDo(DataSource dataSource, UserPo userPo) {
        this.dataSource = dataSource;
        this.userPo = userPo;
    }
}
