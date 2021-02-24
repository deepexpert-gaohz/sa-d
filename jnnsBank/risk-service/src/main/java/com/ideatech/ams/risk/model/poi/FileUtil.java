package com.ideatech.ams.risk.model.poi;

import com.ideatech.common.util.FileUtils;
import com.ideatech.common.zip.ZipEntry;
import com.ideatech.common.zip.ZipOutputStream;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static boolean createZip(String sourcePath, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        boolean flag=true;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            zos.setEncoding("gbk");// 此处修改字节码方式。
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            flag=false;
            //log.error("创建ZIP文件失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                flag=false;
                //log.error("创建ZIP文件失败", e);
            }
        }
        return flag;
    }
    /**
     * @Description 文件操作对象
     * @Author wanghongjie
     * @Date 2018/10/23
     **/
    public static boolean mkdirs(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            return true;
        }
        return false;
    }

    public static boolean exists(String path){
        File file = new File(path);
        return file.exists();
    }
    /**
     * 文件转成base64字符串
     * @param file
     * @return
     * @throws IOException
     */
    public static String encodeFileToBase64Binary(File file) throws IOException {
        String encodedfile = null;
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String( Base64.encodeBase64(bytes), "UTF-8");

        return encodedfile;
    }
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {// 处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else { // 空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                } catch (FileNotFoundException e) {
                    //log.error("创建ZIP文件失败", e);
                } catch (IOException e) {
                    //log.error("创建ZIP文件失败", e);
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        //log.error("创建ZIP文件失败", e);
                    }
                }
            }
        }
    }


    public static void downLoadFile(HttpServletResponse response, String filePath) throws Exception{
        //导出压缩文件
        File file = new File(filePath);
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

        InputStream fin  = new FileInputStream(file);
        byte[] b = new byte[1024];
        int i = 0;
        while ((i = fin.read(b)) > 0) {
            toClient.write(b, 0, i);
        }
        fin.close();
        toClient.flush();
        toClient.close();
        response.flushBuffer();
        System.out.println("export file finish");
    }

    /**
     *
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            // log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return FileUtils.deleteFile(fileName);
            } else {
                return FileUtils.deleteDirectory(fileName);
            }
        }
    }

    /**
     *
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                // log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                // log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            // log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     *
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            //log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            //log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            // log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            //  log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }
    /**
     * 将List均匀分成5个list
     * @yangcq
     * @param list
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageList(List<T> list, int n){
        List<List<T>> resultList = new ArrayList<List<T>>();

        int remaider = list.size()%n;
        int number = list.size()/n;
        int offset =0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value = null;
            if(remaider>0){
                value = list.subList(i*number+offset,(i+1)*number+offset+1);
                remaider--;
                offset++;

            }else{
                value = list.subList(i*number+offset,(i+1)*number+offset);
            }
            resultList.add(value);
        }
        return resultList;
    }
}
