package com.ideatech.ams.imageCore2Ams.service.Impl;

import com.ideatech.ams.imageCore2Ams.service.ImageImportAccess;
import com.ideatech.ams.imageCore2Ams.service.ImageImportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ImageImportAccessImpl implements ImageImportAccess {


    @Autowired
    ImageImportListener imageImportListener;


    @Override
    public void imageProcess() throws Exception {

        imageImportListener.preFileListener();

    }
}
