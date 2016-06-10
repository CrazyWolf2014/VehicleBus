package com.cnlaunch.x431pro.module.setting.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class FormFile {
    private String contentType;
    private byte[] data;
    private File file;
    private String filname;
    private InputStream inStream;
    private String parameterName;

    public FormFile(String filname, byte[] data, String parameterName, String contentType) {
        this.contentType = "application/octet-stream";
        this.data = data;
        this.filname = filname;
        this.parameterName = parameterName;
        if (contentType != null) {
            this.contentType = contentType;
        }
    }

    public FormFile(String filname, File file, String parameterName, String contentType) {
        this.contentType = "application/octet-stream";
        this.filname = filname;
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (contentType != null) {
            this.contentType = contentType;
        }
    }

    public File getFile() {
        return this.file;
    }

    public InputStream getInStream() {
        return this.inStream;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getFilname() {
        return this.filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String toString() {
        return "FormFile [data=" + Arrays.toString(this.data) + ", inStream=" + this.inStream + ", file=" + this.file + ", filname=" + this.filname + ", parameterName=" + this.parameterName + ", contentType=" + this.contentType + "]";
    }
}
