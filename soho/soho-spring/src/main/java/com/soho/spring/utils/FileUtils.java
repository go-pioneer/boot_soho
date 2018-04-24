package com.soho.spring.utils;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.ConfigData;
import com.soho.spring.model.FileData;
import com.soho.spring.model.RetData;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
        fileExt.put(".jpg", "ffd8ff");
        fileExt.put(".jpeg", "ffd8ffe0");
        fileExt.put(".png", "89504e47");
    }

    public static FileData uploadImageByReSize(MultipartFile multipartFile, String userDir, boolean thumbnail) throws BizErrorEx {
        if (multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new BizErrorEx(RetData.UPLOAD_ERROR_STATUS, "上传图片文件为空");
        }
        String orgFileName = multipartFile.getOriginalFilename();
        int indexOf = orgFileName.lastIndexOf(".");
        if (indexOf == -1) {
            throw new BizErrorEx(RetData.UPLOAD_ERROR_STATUS, "上传图片文件名称异常");
        }
        String orgFileExt = orgFileName.substring(indexOf).toLowerCase().trim();
        if (!fileExt.containsKey(orgFileExt)) {
            throw new BizErrorEx(RetData.UPLOAD_ERROR_STATUS, "上传图片文件只允许JPG|JPEG|PNG格式");
        }
        int number = new Random().nextInt(99999);
        number = number < 10000 ? number + 10000 : number;
        String newFileName = System.currentTimeMillis() + "" + number + orgFileExt;
        String lastFileName = null;
        try {
            String savePath = SpringUtils.getBean(ConfigData.class).getSavePath() + File.separator + userDir + File.separator;
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            lastFileName = savePath + newFileName;
            multipartFile.transferTo(new File(lastFileName));
            if (!checkFileHead(lastFileName, orgFileExt)) {
                File file = new File(lastFileName);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                throw new BizErrorEx(RetData.UPLOAD_ERROR_STATUS, "上传的图片文件【" + orgFileName + "】内容格式异常,请重新尝试");
            }
            Thumbnails.of(lastFileName).scale(1.0f).toFile(lastFileName);
            FileData fileData = new FileData(savePath, orgFileName, orgFileExt, newFileName, lastFileName);
            if (thumbnail) {
                String reFileName = newFileName.replaceAll("\\.", "_s.");
                String reFilePath = savePath + File.separator + reFileName;
                Thumbnails.of(lastFileName).scale(0.5f).toFile(reFilePath);
                fileData.setReFileName(reFileName);
                fileData.setReFilePath(reFilePath);
            }
            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BizErrorEx(RetData.UPLOAD_ERROR_STATUS, "上传图片文件失败,请重新尝试");
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
