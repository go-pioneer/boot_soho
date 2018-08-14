package com.soho.spring.utils;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.FileData;
import com.soho.spring.model.OSSConfig;
import com.soho.spring.model.RetCode;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 文件上传处理
 *
 * @author shadow
 */
public class FileUtils {

    private final static Map<String, String> fileExt = new HashMap<>(3);

    static {
        fileExt.put(".jpg", "ffd8ffe0");
        fileExt.put(".jpeg", "ffd8ffe0");
        fileExt.put(".png", "89504e47");
    }

    /**
     * @param multipartFile 上传文件流
     * @param maxFileSizeKb 限制文件图片大小,单位/kb
     * @param maxWidth      限制图片最大宽度
     * @param userTempDir   临时目录
     * @param thumbnail     是否生成缩略图
     * @return FileData
     * @throws BizErrorEx
     */
    public static FileData uploadImageByReSize(MultipartFile multipartFile, Integer maxFileSizeKb, Integer maxWidth, String userTempDir, boolean thumbnail) throws BizErrorEx {
        if (multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传图片文件为空");
        }
        String orgFileName = multipartFile.getOriginalFilename();
        int indexOf = orgFileName.lastIndexOf(".");
        if (indexOf == -1) {
            throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传图片文件名称异常");
        }
        String orgFileExt = orgFileName.substring(indexOf).toLowerCase().trim();
        if (!fileExt.containsKey(orgFileExt)) {
            throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传图片文件只允许JPG|JPEG|PNG格式");
        }
        int number = new Random().nextInt(99999);
        number = number < 10000 ? number + 10000 : number;
        String newFileName = System.currentTimeMillis() + "" + number + orgFileExt;
        String lastFileName = null;
        try {
            OSSConfig ossConfig = SpringUtils.getBean(OSSConfig.class);
            String savePath = ossConfig.getSavePath() + File.separator + userTempDir + File.separator;
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            lastFileName = savePath + newFileName;
            File saveFile = new File(lastFileName);
            multipartFile.transferTo(saveFile);
            if (EMath.GT(multipartFile.getSize(), maxFileSizeKb * 1024)) {
                throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传的图片文件【" + orgFileName + "】大小规格超出限定:" + maxFileSizeKb + "KB");
            }
            if (!checkFileHead(lastFileName, orgFileExt)) {
                if (saveFile.exists() && saveFile.isFile()) {
                    saveFile.delete();
                }
                throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传的图片文件【" + orgFileName + "】内容格式异常,请重新尝试");
            }
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(saveFile));
            if (sourceImg.getWidth() > maxWidth) {
                throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传的图片文件【" + orgFileName + "】宽度规格超出限定:" + maxWidth);
            }
            FileData fileData = new FileData(savePath, orgFileName, orgFileExt, newFileName, lastFileName);
            if (".jpg".equals(orgFileExt)) {
                Thumbnails.of(lastFileName).scale(1.0f).toFile(lastFileName);
                if (thumbnail) {
                    String reFileName = newFileName.replaceAll("\\.", "_s.");
                    String reFilePath = savePath + File.separator + reFileName;
                    Thumbnails.of(lastFileName).scale(0.5f).toFile(reFilePath);
                    fileData.setReFileName(reFileName);
                    fileData.setReFilePath(reFilePath);
                }
            }
            return fileData;
        } catch (Exception e) {
            if (e instanceof BizErrorEx) {
                throw (BizErrorEx) e;
            }
            e.printStackTrace();
        }
        throw new BizErrorEx(RetCode.UPLOAD_ERROR_STATUS, "上传图片文件失败,请重新尝试");
    }

    private static boolean checkFileHead(String filePath, String ext) {
        try {
            FileInputStream is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            StringBuilder stringBuilder = new StringBuilder();
            if (b == null || b.length <= 0) {
                return false;
            }
            for (int i = 0; i < b.length; i++) {
                int v = b[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            String header = stringBuilder.toString().toLowerCase();
            is.close();
            if (fileExt.get(ext).equals(header)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
