package com.rosey.cloud.config;

import java.io.File;
import java.nio.file.Path;

/**
 * ClassName: MacosItem <br/>
 * Description: <br/>
 * date: 2021/7/15 10:34 下午<br/>
 *
 * @author tooru<br />
 */
public class MacosItem {

    private String path;

    private File file;

    private Path dirPath;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Path getDirPath() {
        return dirPath;
    }

    public void setDirPath(Path dirPath) {
        this.dirPath = dirPath;
    }
}
