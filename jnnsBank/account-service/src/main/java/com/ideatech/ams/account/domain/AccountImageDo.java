package com.ideatech.ams.account.domain;

import com.ideatech.ams.account.entity.AccountImage;
import com.ideatech.ams.image.entity.ImageType;
import lombok.Data;

@Data
public class AccountImageDo {
    private AccountImage accountImage;
    private ImageType imageType;

    public AccountImageDo(AccountImage accountImage, ImageType imageType) {
        this.accountImage = accountImage;
        this.imageType = imageType;
    }
}
