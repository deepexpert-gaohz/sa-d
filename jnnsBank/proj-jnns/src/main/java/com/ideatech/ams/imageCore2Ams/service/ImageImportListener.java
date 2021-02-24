package com.ideatech.ams.imageCore2Ams.service;

import java.io.File;
import java.io.IOException;

public interface ImageImportListener {

    public Boolean preFileListener() throws Exception;

    Boolean saveFile2Core(File file) throws IOException;

}
