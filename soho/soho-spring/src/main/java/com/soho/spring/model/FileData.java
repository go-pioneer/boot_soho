package com.soho.spring.model;

import java.io.Serializable;

/**
 * 文件上传处理返回对象
 *
 * @author shadow
 */
public class FileData implements Serializable {

    private String savePath;
    private String orgFileName;
    private String fileExt;
    private String newFileName;
    private String newFilePath;
    private String reFileName;
    private String reFilePath;
    private String newFileUrl;
    private String reFileUrl;

    public FileData() {

    }

    public FileData(String savePath, String orgFileName, String fileExt, String newFileName, String newFilePath) {
        this.savePath = savePath;
        this.orgFileName = orgFileName;
        this.fileExt = fileExt;
        this.newFileName = newFileName;
        this.newFilePath = newFilePath;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    public String getReFileName() {
        return reFileName;
    }

    public void setReFileName(String reFileName) {
        this.reFileName = reFileName;
    }

    public String getReFilePath() {
        return reFilePath;
    }

    public void setReFilePath(String reFilePath) {
        this.reFilePath = reFilePath;
    }

    public String getNewFileUrl() {
        return newFileUrl;
    }

    public void setNewFileUrl(String newFileUrl) {
        this.newFileUrl = newFileUrl;
    }

    public String getReFileUrl() {
        return reFileUrl;
    }

    public void setReFileUrl(String reFileUrl) {
        this.reFileUrl = reFileUrl;
    }

}
