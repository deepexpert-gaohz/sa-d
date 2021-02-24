package com.ideatech.ams.imageCore2Ams.service.Impl;

import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.dao.JnnsImageAllDao;
import com.ideatech.ams.domain.JnnsImageAll;
import com.ideatech.ams.imageCore2Ams.excutor.CoreImageFileIntoExcutor;
import com.ideatech.ams.imageCore2Ams.excutor.ImageFileThread;
import com.ideatech.ams.imageCore2Ams.service.ImageImportListener;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class ImageImportListenerImpl implements ImageImportListener {


    @Value("${import.Image.File.Location}")
    private String importImageFileLocation;

    @Value("${import.Image.File.LocationForFinish}")
    private String importImageFileLocationForFinish;

    @Value("${import.Image.FileType:txt}")
    private String fileType;

    private static final String TEMPIMAGE_EXTENSION = ".tmp";

    @Value("${import.Image.File.chartset:GBK}")
    private String chartset;

    @Value("${import.Image.File.lineEndPrefix:}")
    protected String lineEndPrefix;

    @Value("${import.Image.File.split:,}")
    protected String split;

    @Value("${import.Image.File.lineEnd:true}")
    protected boolean lineEndAuto;

    private String[] names = {"acctNo","acctName","initTypeFlag","initTypeFlag_0","initTypeFlag_1","customerNo","customerName","imageCode","jnBillId","organCode","organFullId","contentCode","accountStatus"};

    @Autowired
    ImageImportListener imageImportListener;

    @Autowired
    JnnsImageService jnnsImageService;

    @Autowired
    AccountBillsAllService accountBillsAllService;

    @Autowired
    JnnsImageAllDao jnnsImageAllDao;

    @Override
    public Boolean preFileListener() throws Exception {
        File folder = new File(importImageFileLocation);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File finishedFilefolder = new File(importImageFileLocationForFinish);
        if (!finishedFilefolder.exists()) {
            finishedFilefolder.mkdirs();
        }

        // 获取本批次处理日期
        Boolean isFirst = null;
        if (folder.listFiles().length > 0) {
            for (File file : folder.listFiles()) {
                if (FilenameUtils.isExtension(file.getName(), fileType)) {
                    isFirst = imageImportListener.saveFile2Core(file);
                }
            }
        }
        return isFirst;
    }

    @Override
    public Boolean saveFile2Core(File file) throws IOException {
        Boolean isFirst = null;
        File finishedFilefolder = new File(importImageFileLocationForFinish);
        String fileExtension = FilenameUtils.getExtension(file.getName());
        String fileExtensionReal = FilenameUtils.EXTENSION_SEPARATOR_STR + fileExtension;
        // 只处理txt结尾的数据
        if (FilenameUtils.isExtension(file.getName(), fileType)) {
            // 开始处理改后缀为tmp
            File tempFile = new File(file.getAbsolutePath().replace(fileExtensionReal, TEMPIMAGE_EXTENSION));
            FileUtils.moveFile(file, tempFile);

            try {

                String str = FileUtils.readFileToString(tempFile, chartset);
                String lineEnd = "";
                String lineEndPrefixNative = EncodeUtils.ascii2native(lineEndPrefix);
                String splitNative = EncodeUtils.ascii2native(split);
                if (lineEndAuto) {//自动增加换行符
                    if (StringUtils.isNotBlank(lineEndPrefixNative)) {
                        int charNative = StringUtils.indexOf(str, lineEndPrefixNative);
                        String property = "";
                        if (charNative > -1 && charNative < str.length()) {
                            while (++charNative < str.length() && (str.charAt(charNative) == '\r' || str.charAt(charNative) == '\n')) {
                                property += str.charAt(charNative);
                            }
                        }
                        lineEnd = lineEndPrefixNative + property;
                    } else {
                        int rIndex = StringUtils.indexOf(str, '\r');
                        int nIndex = StringUtils.indexOf(str, '\n');
                        if (rIndex > -1 || nIndex > -1) {
                            int firstIndex = rIndex > -1 ? (nIndex > -1 ? (rIndex > nIndex ? nIndex : rIndex) : rIndex) : nIndex;
                            String property = "";
                            property += str.charAt(firstIndex);
                            if (firstIndex > -1 && firstIndex < str.length()) {
                                while (++firstIndex < str.length() && (str.charAt(firstIndex) == '\r' || str.charAt(firstIndex) == '\n')) {
                                    property += str.charAt(firstIndex);
                                }
                            }
                            lineEnd = property;
                        } else {

                            throw new EacException("影像存量初始化导入文件的分隔符有误，全文无换行符");
                        }
                    }
                } else {
                    lineEnd = lineEndPrefixNative;
                }
                log.info("影像文件初始化的行分隔符：" + EncodeUtils.ascii2native(lineEnd));
                String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(str, lineEnd);
                log.info("影像文件初始化的行数为：" + lines.length);

               // String[] methodNames = null;
                HashMap<Integer,String > name2indexMap = new HashMap<>();
                int index= 0;
                String initTypeFlag = null;
                if (lines.length > 1) {

                    //System.out.println("导入使用的分隔符："+splitNative);
                    String[] split = lines[0].split(splitNative);

                    for (String name : split) {

                        log.info("存量影像导入数据列："+name);

                        boolean contains =  StringUtils.contains(ArrayUtils.toString(names),name);

                       // boolean contains = ArrayUtils.contains(names, name);

                        if(contains){
                            name2indexMap.put(index,name);
                            if ("initTypeFlag_0".equals(name)){
                                initTypeFlag = "0";
                            }else if ("initTypeFlag_1".equals(name)){
                                initTypeFlag = "1";
                            }
                        }else {
                            log.info("影像文件导入初始化处理失败，数据列["+name+"]非法！");
                            log.info("需要数据为："+ArrayUtils.toString(names));
                            throw  new Exception("文件中含有非法列！");
                        }
                            index++;
                    }
                    ConcurrentHashMap<String, List<JnnsImageAll>> concurrentHashMap =  new ConcurrentHashMap<>();

                   List<ConcurrentHashMap> imageInfoList =  new ArrayList<>();

                   lines =(String[]) ArrayUtils.remove(lines,0);
                    int  imageSize = 0;
                    int number = 0;
                    int  likeAcctNo = 0;
                    int  lineNo2Num = 0;

                  List  lineNo2 =  new ArrayList<String>();

                    for (String line : lines) {
                        if (!lineNo2.contains(line)){
                            lineNo2.add(line);
                            lineNo2Num++;
                        }
                    }

                    log.info("去重后总共有"+lineNo2Num+"条影像信息需要导入！");
                    log.info("开始将影像信息保存进内存容器！concurrentHashMap");
                    for (Object line : lineNo2) {
                        String[] split1 = ((String)line).split(splitNative);

                        JnnsImageAll jnnsImageAll = new JnnsImageAll();
                        ArrayList<JnnsImageAll> jnnsImageList = new ArrayList<>();
                       // String acctNo = null;

                       /* if (split1.length>1){
                            jnnsImageAll.setAcctNo(split1[0].trim());
                            jnnsImageAll.setContentCode(split1[1].trim());
                            if (initTypeFlag != null){
                                //设置该条影像信息是开户还是变更
                                jnnsImageAll.setInitTypeFlag(initTypeFlag);
                            }
                            jnnsImageList.add(jnnsImageAll);

                            concurrentHashMap.put(split[0],jnnsImageList);


                            imageSize++;

                        }*/

                       if (imageSize%20000 == 0){
                           imageInfoList.add(concurrentHashMap);
                           if (imageInfoList.size() < 4){
                               concurrentHashMap =  new ConcurrentHashMap<>();
                           }
                           if (imageInfoList.size() == 4){

                               if (number > 3){
                                   number = 0;
                               }
                               concurrentHashMap = imageInfoList.get(number);
                               number++;
                           }
                       }

                        for (int i = 0; i <split1.length ; i++) {

                            String name = name2indexMap.get(i);

                            if ("acctNo".equalsIgnoreCase(name)){
                                jnnsImageAll.setAcctNo( split1[i]);
                                imageSize++;
                                if ( concurrentHashMap.containsKey(split1[i])){
                                    List<JnnsImageAll> jnnsImageAlls = concurrentHashMap.get(split1[i]);
                                    jnnsImageAlls.add(jnnsImageAll);
                                    likeAcctNo++;
                                }else{
                                    jnnsImageList.add(jnnsImageAll);
                                    if (split1[i] != null){ //concurrentHashMap key值不能为null
                                        concurrentHashMap.put(split1[i],jnnsImageList);

                                    }
                                }

                                //acctNo = name;
                                //jnnsImageAll.setAcctNo(split1[i]);
                                //将流水号暂存进批次号，后面做处理
                             }else if ("contentCode".equalsIgnoreCase(name)){
                                jnnsImageAll.setContentCode(split1[i]);
                            }else if ("initTypeFlag".equalsIgnoreCase(name)){ //存量影像导入类型为0表示开户影像，为1表示变更影像
                                jnnsImageAll.setInitTypeFlag(split1[i]);
                            }
                            if (initTypeFlag != null){
                                //设置该条影像信息是开户还是变更
                                jnnsImageAll.setInitTypeFlag(initTypeFlag);
                            }

                        }


                    }

                    log.info("影像信息保存进内存容器完毕！保存"+imageSize+"条账户影像信息！同账号多流水有"+likeAcctNo+"条！");

                    if (concurrentHashMap.size()>0){

                        //线程池多线程进行表处理及插入，后期改造
                        ExecutorService executorService = Executors.newFixedThreadPool(4);


                        for (int i = 0; i <4 ; i++) {

                            //设置资源管理对象
                            CoreImageFileIntoExcutor coreImageFileIntoExcutor = new CoreImageFileIntoExcutor();

                            if (imageInfoList.size()> i){
                                coreImageFileIntoExcutor.setConcurrentHashMap(imageInfoList.get(i));
                            }else {
                                break;
                            }

                            coreImageFileIntoExcutor.setJnnsImageService(jnnsImageService);

                            coreImageFileIntoExcutor.setAccountBillsAllService(accountBillsAllService);

                            coreImageFileIntoExcutor.setJnnsImageAllDao(jnnsImageAllDao);

                            executorService.execute(new ImageFileThread(coreImageFileIntoExcutor,i));

                        }
                        //释放线程池
                        executorService.shutdown();

                    }

                } else {
                    log.error("文件的行数不对，未使用");
                }


                log.info("影像数据初始化文件导入完成");
                log.info("影像数据初始化文件进行转移");
                try {
                    FileUtils.moveFile(tempFile, new File(finishedFilefolder + File.separator + file.getName().replace(TEMPIMAGE_EXTENSION, fileExtensionReal)));
                } catch (FileExistsException e) {
                    FileUtils.moveFile(tempFile, new File(finishedFilefolder + File.separator + RandomStringUtils.randomNumeric(5) + file.getName().replace(TEMPIMAGE_EXTENSION, fileExtensionReal)));
                } finally {
                    log.info("影像数据初始化文件转移完成");
                }
            } catch (Exception e) {

                // 恢复文件名
                FileUtils.moveFile(tempFile, file);
                log.error("读取影像文件异常", e);
            }
        }

        System.gc();
        return isFirst;

    }
}
