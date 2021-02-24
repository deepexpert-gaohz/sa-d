package com.ideatech.ams.image.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

/**
 * @author vantoo
 * @date 10:23 2018/4/28
 */
public class ImageUtils {

    /**
     * 需要压缩的像素
     */
    private final static int NEED_COMPRESS_PIXEL = 1500;

    /**
     * 需要压缩的大小B(字节)
     */
//    private final static int NEED_COMPRESS_SIZE = 400 * 1024;
    public static void compressImage(byte[] byteArray, OutputStream os, String formatName) {
//        byte[] byteArray;
        InputStream bis = null;
        try {
//            byteArray = IOUtils.toByteArray(is);
            bis = new ByteArrayInputStream(byteArray);
            BufferedImage originalImage = ImageIO.read(bis);
            int maxPixel = originalImage.getWidth() > originalImage.getHeight() ? originalImage.getWidth() : originalImage.getHeight();

//            double sizeScale = new BigDecimal(NEED_COMPRESS_SIZE).divide(new BigDecimal(byteArray.length), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
//            按图片大小压缩的比例不正确
            double sizeScale = 1D;
            double pixelScale = new BigDecimal(NEED_COMPRESS_PIXEL).divide(new BigDecimal(maxPixel), 3, BigDecimal.ROUND_HALF_UP).doubleValue();

            if (sizeScale < 1 || pixelScale < 1) {
                BufferedImage bi = Thumbnails.of(originalImage)
                        .scale(sizeScale < pixelScale ? sizeScale : pixelScale)
                        .asBufferedImage();
                ImageIO.write(bi, formatName, os);
            } else {
                os.write(byteArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(bis);
        }
    }

    public static void compressImageByPixel(byte[] byteArray, OutputStream os, String formatName, int width, int height) {
        try {
            BufferedImage bi = Thumbnails.of(ImageIO.read(new ByteArrayInputStream(byteArray)))
                    .size(width,height)
                    .asBufferedImage();
            ImageIO.write(bi, formatName, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getThumbPath(String path, String fileExtension, int width, int height) {
        return StringUtils.replace(path, "." + fileExtension, "_" + width + "x" + height + "." + fileExtension);
    }


}
