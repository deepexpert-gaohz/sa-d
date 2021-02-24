package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.Initializer.DataInitializerService;
import com.ideatech.ams.system.dict.dao.DictionaryDao;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.entity.DictionaryPo;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author van
 * @date 11:36 2018/7/5
 */
@Service
public class DataInitializerServiceImpl implements DataInitializerService {

    @Autowired
    DictionaryDao dictionaryDao;
    @Autowired
    OptionDao optionDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrganizationDao organizationDao;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public StringBuffer printDictionaryList() {
        StringBuffer stringBuffer = new StringBuffer();
        List<DictionaryPo> dictionaryList = dictionaryDao.findAll(new Sort(Sort.Direction.ASC, "id"));
        for (DictionaryPo dictionary : dictionaryList) {
            stringBuffer.append("createDictionary(");
            stringBuffer.append(dictionary.getId() + "L,");
            stringBuffer.append(getConverterValue(dictionary.getName(), true));
            stringBuffer.append(LINE_SEPARATOR);
        }
        return stringBuffer;
    }

    @Override
    public StringBuffer printOptionList() {

        StringBuffer stringBuffer = new StringBuffer();
        List<OptionPo> optionList = optionDao.findAll(new Sort(Sort.Direction.ASC, "dictionaryId"));
        for (OptionPo option : optionList) {
            stringBuffer.append("createOption(");
            stringBuffer.append(option.getId() + "L,");
            stringBuffer.append(option.getDictionaryId() + "L,");
            stringBuffer.append(0 + ",");
            stringBuffer.append(getConverterValue(option.getName()));
            stringBuffer.append('"');
            stringBuffer.append(getConverterValue(option.getValue(), true));
            stringBuffer.append(LINE_SEPARATOR);
        }
        return stringBuffer;
    }

    @Override
    public StringBuffer printOrganList() {
        List<OrganizationPo> organList = organizationDao.findAll(new Sort(Sort.Direction.ASC, "fullId"));
        StringBuffer stringBuffer = new StringBuffer();
        for (OrganizationPo organ : organList) {
            stringBuffer.append("createOrgan(");
            stringBuffer.append(organ.getId() + "L,");
            stringBuffer.append(getConverterValue(organ.getName()));
            stringBuffer.append(getConverterValue(organ.getFullId()));
            stringBuffer.append(getConverterValue(organ.getCode()));
            stringBuffer.append(getConverterValue(organ.getPbcCode()));
            stringBuffer.append(getConverterValue(organ.getInstitutionCode()));
            stringBuffer.append((getConverterValue(organ.getParentId(), true)));
            stringBuffer.append(LINE_SEPARATOR);
        }
        return stringBuffer;

    }

    @Override
    public StringBuffer printUserList() {
        List<UserPo> userList = userDao.findAll(new Sort(Sort.Direction.ASC, "orgId"));
        StringBuffer stringBuffer = new StringBuffer();
        for (UserPo user : userList) {
            stringBuffer.append("createUser(");
            stringBuffer.append(user.getId() + "L,");
            stringBuffer.append(getConverterValue(user.getUsername()));
            stringBuffer.append(getConverterValue(user.getCname()));
            stringBuffer.append(getConverterValue(user.getMobile()));
            stringBuffer.append(getConverterValue(user.getEnabled()));
            stringBuffer.append(getConverterValue(user.getDeleted()));
            stringBuffer.append(user.getOrgId() + "L,");
            stringBuffer.append(getConverterValue(user.getRoleId(), true));
            stringBuffer.append(LINE_SEPARATOR);
        }
        return stringBuffer;
    }

    public static String getConverterValue(Object value, boolean end) {
        String result = getConverterValue(value);
        if (end) {
            result = StringUtils.removeEnd(result, ",") + ");";
        }
        return result;
    }

    public static String getConverterValue(Object value) {
        StringBuffer strBuff = new StringBuffer();
        boolean isStr = false;
        if (value instanceof String) {
            isStr = true;
        }
        String objValue = "";
        if (value == null) {
            objValue = null;
        } else {
            if (value instanceof Long) {
                objValue = value.toString() + "L";
            } else {
                objValue = value.toString().trim();
            }
        }
        if (isStr) {
            strBuff.append('"');
        }
        strBuff.append(objValue);
        if (isStr) {
            strBuff.append('"');
        }

        strBuff.append(",");
        return strBuff.toString();
    }

//    private void printFile(String filePath, StringBuffer stringBuffer) throws IOException {
//        File file = new File(filePath);
//        if (file.exists()) {
//            FileUtils.deleteQuietly(file);
//        }
//        file.createNewFile();
//        try {
//            FileUtils.writeStringToFile(file, stringBuffer.toString() + LINE_SEPARATOR, true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
