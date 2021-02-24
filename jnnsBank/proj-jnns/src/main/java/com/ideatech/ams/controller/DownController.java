//package com.ideatech.ams.controller;
//
//import com.itextpdf.io.util.FileUtil;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URLEncoder;
//import java.nio.file.Path;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import static org.apache.commons.collections4.MapUtils.getLong;
//import static org.apache.commons.collections4.MapUtils.getString;
//
//public class DownController {
//    /**
//     * 附件导出 （导出所有用户上传的文件格式 已压缩包形式导出 ）。
//     * 1.创建一个临时存放文件的tempFile。
//     * 2.在临时文件夹中创建用户文件夹用来存放下载好的文件（用户文件夹可以用时间戳或者uuid来命名）。
//     * 3.把临时文件夹压缩成zip文件，存放到tempfile下面。
//     * 4.根据流的形式把压缩文件读到放到浏览器下载 5.关闭流，删除临时文件中的用户文件夹和压缩好的用户文件夹。
//     *
//     * @param response
//     * @param export
//     * @param request
//     * @throws IOException
//     */
//    @RequestMapping("/fForm/enclosureExport")
//    public void enclosureExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        File file1 = null;
//        OutputStream out = response.getOutputStream();
//        try {
//            Map<String, Object> paramMap = RequestUtils.convertRequestToMap(request);
//            Long formId = getLong(paramMap, "formId");
//            String condition = getString(paramMap, "condition");
//            List<Map<String, Object>> fromDataList = fFormService.findFFormData1(formId, condition);
//            File file = null;
//            // 获取项目路径
//            String rootPath = this.getClass().getClassLoader().getResource("").getPath();
//            // 创建临时文件夹tempFile
//            File tempFile = new File(rootPath + "/tempFile");
//            if (!tempFile.exists()) {
//                tempFile.mkdirs();
//            }
//            // 创建用户文件夹 用来存放文件
//            file1 = new File(tempFile.getPath() + "/" + System.currentTimeMillis());
//            file1.mkdirs();
//            for (Map<String, Object> map : fromDataList) {
//                // 文件夹名称 根据序号名字创建
//                String id = map.get("id").toString();
//                String newFile = file1.getPath() + "/" + "序号" + id;
//                // 新建的文件名称和路径
//                file = new File(newFile);
//                // 获取文件夹路径
//                Path path = file.toPath();
//                // 创建文件夹
//                file.mkdirs();
//                // 要下载的文件（包含各种文件格式，如图片 ，视频，pdf，等等...）
//                Iterator<String> iter = map.keySet().iterator();
//                while (iter.hasNext()) {
//                    String key = iter.next();
//                    String value = map.get(key).toString();
//                    if (value.contains("https://")) {
//                        // 截取后缀名
//                        String str = value.substring(value.lastIndexOf(".") + 1);
//                        // 截取的文件名
//                        String str1 = value.substring(value.indexOf("com/") + 4, value.indexOf("-baidugongyi"));
//                        String newFileNmae = str1 + "." + str;
//                        // 调用下载工具类
//                        ZipFileDownload.dolFile(value, path,newFileNmae);
//                    }
//                }
//            }
//            // 调用方法打包zip文件
//            byte[] data = createZip(file1.getPath());
//            // 压缩包名称
//            String downloadName = file1.getName() + ".zip";
//            response.setHeader("Content-Disposition",
//                    "attachment;filename=" + URLEncoder.encode(downloadName, "utf-8"));
//            response.addHeader("Content-Length", "" + data.length);
//            response.setContentType("application/octet-stream;charset=UTF-8");
//            IOUtils.write(data, out);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 压缩成功后删除项目中文件夹
//                if (file1.exists()) {
//                    FileUtil.delFolder(file1.getPath());
//                }
//                if (out != null) {
//                    out.flush();
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public byte[] createZip(String srcSource) throws Exception {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ZipOutputStream zip = new ZipOutputStream(outputStream);
//        // 将目标文件打包成zip导出
//        File file = new File(srcSource);
//        a(zip, file, "");
//        IOUtils.closeQuietly(zip);
//        return outputStream.toByteArray();
//    }
//
//    public void a(ZipOutputStream zip, File file, String dir) throws Exception {
//        // 如果当前的是文件夹，则进行进一步处理
//        try {
//            if (file.isDirectory()) {
//                // 得到文件列表信息
//                File[] files = file.listFiles();
//                // 将文件夹添加到下一级打包目录
//                zip.putNextEntry(new ZipEntry(dir + "/"));
//                dir = dir.length() == 0 ? "" : dir + "/";
//                // 循环将文件夹中的文件打包
//                for (int i = 0; i < files.length; i++) {
//                    a(zip, files[i], dir + files[i].getName());
//                }
//            } else {
//                // 当前的是文件，打包处理文件输入流
//                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//                ZipEntry entry = new ZipEntry(dir);
//                zip.putNextEntry(entry);
//                zip.write(FileUtils.readFileToByteArray(file));
//                IOUtils.closeQuietly(bis);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            zip.flush();
//            zip.close();
//        }
//    }
//}
