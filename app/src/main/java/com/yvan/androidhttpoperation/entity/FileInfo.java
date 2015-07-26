package com.yvan.androidhttpoperation.entity;

import java.io.Serializable;

/**
 * 文件类信息
 */
public class FileInfo implements Serializable {
    private int id;
    private String fileUrl;
    private String filename;
    private int length;
    private int finished;

    public FileInfo() {

    }

    public FileInfo(int id, String fileUrl, String filename, int length, int finished) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.filename = filename;
        this.length = length;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileUrl='" + fileUrl + '\'' +
                ", filename='" + filename + '\'' +
                ", length=" + length +
                ", finished=" + finished +
                '}';
    }
}
