package com.ideatech.ams.utils;

import com.ideatech.ams.domain.AccountInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ideatech.ams.utils.CreateXmlUtil.mergeXml;

/**
 * @version V1.0
 * @author: sunhui
 * @date: 2019/12/27 2:04
 * @description:
 */
@Slf4j
public class CopyDirectoryUtil {

    private CopyDirectoryUtil(){};

    /**
     * 将所选中的账户的文件夹，全部移动的另外的文件夹中
     *
     * @param accountInfos 所需数据的实体类集合
     * @throws Exception
     */
    public static ArrayList<String> moveDir(List<AccountInfo> accountInfos, String parentPath, String newPath) {
        ArrayList<String> newIds = new ArrayList<>();
        //HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        if (!accountInfos.isEmpty()) {
            try {
                File newFile = new File(newPath);
                File file = new File(parentPath);

                //判断接收到的文件夹名是否存在
                String[] list = file.list();
                if (!(list.length > 0)){
                    log.error(parentPath+"原始文件夹为空");
                    return null;
                }
                for (int i = 0; i < list.length; i++) {
                    for (int j = 0; j < accountInfos.size(); j++) {
                        log.info("accountInfos.get(j).getImgDir()"+accountInfos.get(j).getImgDir());
                        log.info("list[i]"+list[i]);
                        if (list[i].equals(accountInfos.get(j).getImgDir())) {
                            //file1:原路径每个文件的对象
                            File file1 = new File(parentPath + File.separator + list[i]);
                            //判断file1是否为相匹配的文件夹
                            if (file1.isDirectory() && file1.list().length>0) {
                                CopyDirectoryUtil.copyDir(file1.getPath(), newPath + File.separator + accountInfos.get(j).getImgDir());
                                CreateXmlUtil.createXml(newPath, file1, accountInfos.get(j));
                                log.info("accountInfos.get(j).getImgDir().substring(7, accountInfos.get(j).getImgDir().length())"+accountInfos.get(j).getImgDir().substring(7, accountInfos.get(j).getImgDir().length()));
                                String subId = accountInfos.get(j).getImgDir().substring(7, accountInfos.get(j).getImgDir().length());
                                newIds.add(subId);
                            }else {
                                log.error(file1.getName()+"账户文件夹无内容");
                                continue;
                            }
                        }
                    }
                }
                if (!newIds.isEmpty()){
                    mergeXml(newPath);
                    File zipFile = new File(newPath + File.separator + "accounts.zip");
                    if (zipFile.exists() && zipFile.isFile()) {
                        zipFile.delete();
                    }
                    ZipUtil.zip(newFile.listFiles(), zipFile.getPath());
                }else {
                    log.error("未匹配到相应的账户文件夹");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("程序异常，请联系管理员");
            }
        }
        else {
            log.error("获取账户信息失败");
            return null;
        }

        return newIds;
    }


    /**
     * 复制文件夹
     *
     * @param sourcePath 原文件夹对象的路径，已在调用方法前做过判断
     * @param newPath 需要复制到的文件位置
     * @throws Exception
     */
    public static void copyDir(String sourcePath, String newPath) throws Exception {
        //file:传入的文件夹路径的对象
        File file = new File(sourcePath);
        //如果新文件夹路径对象不存在，则创建对应的文件夹
        if (!((new File(newPath)).exists())) {
            new File(newPath).mkdir();
        }
        File[] files = file.listFiles();
        //判断file文价夹内的对象是文件还是文件夹
        if (files.length > 0) {
            for (File s : files) {
                //如果是文件，则复制
                if (s.isFile()) {
                    String str = s.getName().toUpperCase();
                    if (str.endsWith("JPG") || str.endsWith("JEPG") || str.endsWith("PNG") || str.endsWith("PDF")){
                        File file1 = new File(newPath + File.separator + s.getName());
                        copyFileUsingFileStreams(s, file1);
                        //copyFileUsingFileStreams(s,new File(newPath));
                    }
                    else{
                        log.error("影像文件格式不正确");
                        return;
                    }
                } else {
                    //如果是文件夹，那根据文档要求，账户文件夹内只能有影像文件，不能有文价夹，所以提示错误
                    log.error("账户文件夹内只能有影像文件，不能有文价夹");
                    return;
                    //copyDir(s.getPath(), newPath + File.separator + file.getName() + File.separator + s.getName());
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 通过文件流方式复制文件
     * @param source 被复制文件的对象
     * @param dest 对应位置的文件对象
     * @throws IOException
     */
    private static void copyFileUsingFileStreams(File source, File dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public static void main(String[] args) {
        String id="account1173717795886080";

        System.out.println(id.substring(7,id.length()));
    }
}
