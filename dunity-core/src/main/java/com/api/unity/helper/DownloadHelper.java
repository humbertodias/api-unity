package com.api.unity.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadHelper {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public static File downloadFile(String fileURL, String packageId, File saveDir)
            throws IOException {

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            String fileName = getFileName(disposition, fileURL);

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("FileName = " + fileName);

            String ext =  fileName.substring(fileName.lastIndexOf(".")+1);
            File file = new File(saveDir, packageId + "." + ext);

            // opens input stream from the HTTP connection
            try(InputStream inputStream = httpConn.getInputStream()) {
                // opens an output stream to save into file
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            httpConn.disconnect();
            System.out.println("File downloaded");

            return file;
        } else {
            throw new RuntimeException("No file to download. Server replied HTTP code: " + responseCode);
        }
    }

    private static String getFileName(String disposition, String fileURL) {
        if (disposition != null) {
            // extracts file name from header field
            int index = disposition.indexOf("filename=");
            return disposition.substring(index + 10, disposition.length() - 1);
        } else {
            // extracts file name from URL
            return fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
        }
    }

}