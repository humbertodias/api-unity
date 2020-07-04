package com.api.unity.service;

import com.api.unity.helper.HttpDownloadUtility;
import net.dongliu.apk.parser.ApkFile;
import org.jsoup.Jsoup;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class AndroidService {

    File downloadDir = new File("download");

    @PostConstruct
    void setup() {
        downloadDir.mkdirs();
    }

    public String getApkPure(String packageId) throws IOException {
        var url = String.format("https://m.apkpure.com/android/%s/download?from=details", packageId);
        var doc = Jsoup.connect(url).get();
        var downloadUrl = doc.select("#iframe_download");
        return downloadUrl.attr("src");
    }

    public File downloadFile(String packageId) throws IOException {
        var uri = getApkPure(packageId);
        return HttpDownloadUtility.downloadFile(uri, packageId, downloadDir);
    }

    public File file(String packageId) {
        var apkFileName = String.format("%s.apk", packageId);
        return new File(downloadDir, apkFileName);
    }

    public File getFile(String packageId) throws IOException {
        var apkFile = file(packageId);
        return apkFile.exists() ? apkFile : downloadFile(packageId);
    }

    public String manifestXml(String packageId) throws IOException {
        try (var apkFile = new ApkFile(file(packageId))) {
            return apkFile.getManifestXml();
        }
    }

    public byte[] manifestMf(String packageId) throws IOException {
        try (var apkFile = new ApkFile(file(packageId))) {
            return apkFile.getFileData("META-INF/MANIFEST.MF");
        }
    }

    public String unityVersion(String packageId) throws IOException {
        try (var apkFile = new ApkFile(file(packageId))) {
            var data = apkFile.getFileData("assets/bin/Data/Resources/unity_builtin_extra");
            var sb = new StringBuilder();
            for (int i = 20; data != null && data[i + 1] != '\r' && i < 35; i++) {
                sb.append((char) data[i]);
            }
            return sb.toString();
        }
    }

    public boolean containsLib(ApkFile apkFile, String libName) throws IOException {
        String[] archs = {
                "armeabi",
                "armeabi-v7a",
                "arm64-v8a",
                "x86",
                "x86_64",
                "mips"
        };
        for (var arch : archs) {
            var path = String.format("lib/%s/%s", arch, libName);
            if (containsFile(apkFile, path)) return true;
        }
        return false;
    }

    boolean containsFile(ApkFile apkFile, String path) throws IOException {
        var data = apkFile.getFileData(path);
        if (data != null && data.length > 0) return true;
        return false;
    }

    /**
     * armeabi: compiled code for all ARM based processors only
     * armeabi-v7a: compiled code for all ARMv7 and above based processors only
     * arm64-v8a: compiled code for all ARMv8 arm64 and above based processors only[9]
     * x86: compiled code for x86 processors only
     * x86_64: compiled code for x86 64 processors only
     * mips: compiled code for MIPS processors only
     *
     * @param packageId
     * @return
     * @throws IOException
     */
    public boolean unityMono(String packageId) throws IOException {
        var apkFile = new ApkFile(file(packageId));
        return containsLib(apkFile, "libmono.so") || containsFile(apkFile, "assets/bin/Data/Managed/etc/mono/config");
    }

    public boolean unityIlcpp(String packageId) throws IOException {
        var apkFile = new ApkFile(file(packageId));
        return containsLib(apkFile, "libil2cpp.so");
    }

    public String meta(String packageId) throws IOException {
        try (ApkFile apkFile = new ApkFile(file(packageId))) {
            return apkFile.getApkMeta().toString();
        }
    }
}