package com.lovejob.model;

import java.io.File;

public  class ImageModle {
        private String smallFileName;//缩略图的名称
        private File smallFile;//缩略图文件
        private String bigFileName;//原图的名称
        private File bigFile;//原图文件

        public ImageModle(File bigFile, String bigFileName, File smallFile, String smallFileName) {
            this.bigFile = bigFile;
            this.bigFileName = bigFileName;
            this.smallFile = smallFile;
            this.smallFileName = smallFileName;
        }

        public File getBigFile() {
            return bigFile;
        }

        public void setBigFile(File bigFile) {
            this.bigFile = bigFile;
        }

        public String getBigFileName() {
            return bigFileName;
        }

        public void setBigFileName(String bigFileName) {
            this.bigFileName = bigFileName;
        }

        public File getSmallFile() {
            return smallFile;
        }

        public void setSmallFile(File smallFile) {
            this.smallFile = smallFile;
        }

        public String getSmallFileName() {
            return smallFileName;
        }

        public void setSmallFileName(String smallFileName) {
            this.smallFileName = smallFileName;
        }
    }