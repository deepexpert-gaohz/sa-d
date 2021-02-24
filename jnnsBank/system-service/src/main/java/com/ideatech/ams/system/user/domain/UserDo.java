package com.ideatech.ams.system.user.domain;

import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.entity.UserPo;
import lombok.Data;

@Data
public class UserDo {

    private UserPo userPo;
    private OrganizationPo organizationPo;

    public UserDo(UserPo userPo, OrganizationPo organizationPo) {
        this.userPo = userPo;
        this.organizationPo = organizationPo;
    }
}
