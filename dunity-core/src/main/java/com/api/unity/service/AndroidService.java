package com.api.unity.service;

import com.api.unity.helper.DownloadHelper;
import com.api.unity.helper.UnzipHelper;
import net.dongliu.apk.parser.ApkFile;
import org.jsoup.Jsoup;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class AndroidService {

    @Inject
    UnityService unityService;

    String apkDownloaderUrl;

    File downloadDir = new File("download");

    public void postConstruct(String apkDownloaderUrl){
        this.apkDownloaderUrl = apkDownloaderUrl;
        downloadDir.mkdirs();
    }

    public String [] files(){
        return downloadDir.list();
    }

    public String getUrlApkDownload(String packageId) throws IOException {
        var url = String.format(apkDownloaderUrl, packageId);
        var doc = Jsoup.connect(url).get();
        var downloadUrl = doc.select("#iframe_download");
        return downloadUrl.attr("src");
    }

    public File downloadFile(String packageId) throws IOException {
        var uri = getUrlApkDownload(packageId);
        var file = DownloadHelper.downloadFile(uri, packageId, downloadDir);
        if(file.getName().endsWith(".xapk")) UnzipHelper.extractApkFromXapk(file);
        return file;
    }

    public File file(String packageId) {
        var apkFileName = String.format("%s.apk", packageId);
        return new File(downloadDir, apkFileName);
    }

    public ApkFile apk(String packageId) throws IOException {
        return new ApkFile(file(packageId));
    }

    public File getFile(String packageId) throws IOException {
        var apkFile = file(packageId);
        return apkFile.exists() ? apkFile : downloadFile(packageId);
    }

    public String manifestXml(String packageId) throws IOException {
        try (var apkFile = apk(packageId)) {
            return apkFile.getManifestXml();
        }
    }

    public byte[] manifestMf(String packageId) throws IOException {
        try (var apkFile = apk(packageId)) {
            return apkFile.getFileData("META-INF/MANIFEST.MF");
        }
    }

    public String meta(String packageId) throws IOException {
        try (var apkFile = apk(packageId)) {
            return apkFile.getApkMeta().toString();
        }
    }

    public String unityVersion(String packageId) throws IOException {
        try(var apkFile = apk(packageId)) {
            return unityService.version(apkFile);
        }
    }

    public boolean unityMono(String packageId) throws IOException {
        try(var apkFile = apk(packageId)) {
            return unityService.hasMono(apkFile);
        }
    }

    public boolean unityIlcpp(String packageId) throws IOException {
        try(var apkFile = apk(packageId)) {
            return unityService.hasIlcpp(apkFile);
        }
    }
}