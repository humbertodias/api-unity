package com.api.unity.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipFile;

public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    public static File extractApkFromXapk(File xapk) throws IOException {
        var apkFileName = xapk.getName().replace(".xapk", ".apk");
        return extractFile(xapk, apkFileName);
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param entryPath
     * @throws IOException
     */
    private static File extractFile(File file, String entryPath) throws IOException {

        var zipIn = new ZipFile(file);
        var fileOut = new File(file.getParent(), entryPath);
        var entry = zipIn.getEntry(entryPath);
        if (entry != null) {
            try (var inputStream = zipIn.getInputStream(entry)) {
                try (var bos = new BufferedOutputStream(new FileOutputStream(fileOut))) {
                    var bytesIn = new byte[BUFFER_SIZE];
                    var read = 0;
                    while ((read = inputStream.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                }
            }
        }

        return fileOut;
    }

}