package me.zeanzai.nacosconsumer.entity;

public class MyFiles {
    private String path;
    private String fileName;

    @Override
    public String toString() {
        return "MyFiles{" +
                "path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
