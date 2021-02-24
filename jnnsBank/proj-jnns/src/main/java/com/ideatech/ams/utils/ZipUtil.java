package com.ideatech.ams.utils;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @version V1.0
 * @author: sunhui
 * @date: 2019/12/24 20:19
 * @description: 压缩及解压工具类
 */

@Slf4j
public class ZipUtil {
    /**
     * 使用GBK编码可以避免压缩中文文件名乱码
     */
    private static final String CHINESE_CHARSET = "UTF-8";

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    private ZipUtil(){}

    /**
     * <p>
     * 压缩文件
     * </p>
     *
     * @param sourceFolder 需压缩文件 或者 文件夹 路径
     * @param zipFilePath  压缩文件输出路径
     * @throws Exception
     */
    public static void zip(String sourceFolder, String zipFilePath){
        try {
            OutputStream out = new FileOutputStream(zipFilePath);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            ZipOutputStream zos = new ZipOutputStream(bos);
            // 解决中文文件名乱码
            //zos.setEncoding(CHINESE_CHARSET);
            File file = new File(sourceFolder);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
            bos.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("出现异常，解压失败");
        }
    }

    /**
     * 压缩文件
     *
     * @param sourceFolders 一组 压缩文件夹 或 文件
     * @param zipFilePath   压缩文件输出路径
     * @throws Exception
     */
    public static void zip(File[] sourceFolders, String zipFilePath)  {
        try {
            OutputStream out = new FileOutputStream(zipFilePath);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            ZipOutputStream zos = new ZipOutputStream(bos);
            // 解决中文文件名乱码
            //zos.Encoding(CHINESE_CHARSET);

            for (int i = 0; i < sourceFolders.length; i++) {
                File file = new File(sourceFolders[i].getPath());
                String basePath = null;
                basePath = file.getParent();
                zipFile(file, basePath, zos);
            }

            zos.closeEntry();
            zos.close();
            bos.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("出现异常解压失败");
        }

    }

    /**
     * 递归压缩文件
     *
     * @param parentFile
     * @param basePath
     * @param zos
     * @throws Exception
     */
    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos)
            throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is;
        BufferedInputStream bis;
        byte[] cache = new byte[CACHE_SIZE];
        for (File file : files) {
            if (file.isDirectory()) {

                basePath = basePath.replace('\\', '/');
                if (basePath.substring(basePath.length() - 1).equals("/")) {
                    pathName = file.getPath().substring(basePath.length()) + "/";
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                }

                zos.putNextEntry((ZipEntry) new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length());
                pathName = pathName.replace('\\', '/');
                if (pathName.substring(0, 1).equals("/")) {
                    pathName = pathName.substring(1);
                }

                is = new FileInputStream(file);
                bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                int nRead = 0;
                while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    zos.write(cache, 0, nRead);
                }
                bis.close();
                is.close();
            }
        }
    }


    /**
     * zip下载
     * @param res
     * @param fileName 压缩包名称
     */
    public static void download(HttpServletResponse res, String fileName, String path) {
        res.setHeader("content-type", "text/plain");
        res.setHeader("content-type", "application/x-msdownload;");
        res.setContentType("text/plain; charset=utf-8");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(path+File.separator+"accounts.zip")));
            int i = bis.read(buff);

            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("压缩文件失败");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("压缩文件成功");
    }

    /**
     * 解压文件到指定目录
     */
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        // 解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        zip.close();
    }


    /**
     *
     * @param zipSavePath
     * @param sourceFile
     */
    public static void zipCompress(String zipSavePath, File sourceFile) {
        try {
            //创建zip输出流
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipSavePath+"accounts.zip"));
            compress(zos, sourceFile, sourceFile.getName());
            zos.close();
        } catch (Exception e) {
            log.error("zip compress file exception: {}, zipSavePath={}, sourceFile={}", e, zipSavePath, sourceFile.getName());
        }
    }


    /**
     * 备用
     * @param zos
     * @param sourceFile
     * @param fileName
     * @throws Exception
     */
    private static void compress(ZipOutputStream zos, File sourceFile, String fileName) throws Exception {
        if (sourceFile.isDirectory()) {
            //如果是文件夹，取出文件夹中的文件（或子文件夹）
            File[] fileList = sourceFile.listFiles();
            if (fileList.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                zos.putNextEntry((com.ideatech.common.zip.ZipEntry) new ZipEntry(fileName + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (File file : fileList) {
                    compress(zos, file, fileName + "/" + file.getName());
                }
            }
        } else {
            if (!sourceFile.exists()) {
                zos.putNextEntry( new ZipEntry("/"));
                zos.closeEntry();
            } else {
                //单个文件，直接将其压缩到zip包中
                zos.putNextEntry(new ZipEntry(fileName));
                FileInputStream fis = new FileInputStream(sourceFile);
                byte[] buf = new byte[1024];
                int len;
                //将源文件写入到zip文件中
                while ((len = fis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
        }
    }

    /**
     * 备用
     * 解压缩zip方法
     */


    public static void decompressZip(String zipPath, String descDir) {
        File zipFile = new File(zipPath);
        boolean flag = false;
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, Charset.forName("gbk"));//防止中文目录，乱码
            for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                //指定解压后的文件夹+当前zip文件的名称
                String outPath = (descDir+"account"+zipEntryName).replace("/", File.separator);
                System.out.println("zipEntryName"+zipEntryName);
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                //保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
                System.err.println("当前zip解压之后的路径为：" + outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }
                in.close();
                out.close();
            }
            flag = true;
            //必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
