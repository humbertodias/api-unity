package com.api.unity.service;

import net.dongliu.apk.parser.ApkFile;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class UnityService {

    public String version(ApkFile apkFile) throws IOException {
        var data = apkFile.getFileData("assets/bin/Data/Resources/unity_builtin_extra");
        var sb = new StringBuilder();
        for (int i = 20; data != null && data[i + 1] != '\r' && i < 35; i++) {
            sb.append((char) data[i]);
        }
        return sb.toString();
    }

    /**
     * armeabi: compiled code for all ARM based processors only
     * armeabi-v7a: compiled code for all ARMv7 and above based processors only
     * arm64-v8a: compiled code for all ARMv8 arm64 and above based processors only[9]
     * x86: compiled code for x86 processors only
     * x86_64: compiled code for x86 64 processors only
     * mips: compiled code for MIPS processors only
     */
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
        return data != null && data.length > 0;
    }

    public boolean hasMono(ApkFile apkFile) throws IOException {
        return containsLib(apkFile, "libmono.so") || containsFile(apkFile, "assets/bin/Data/Managed/etc/mono/config");
    }

    public boolean hasIlcpp(ApkFile apkFile) throws IOException {
        return containsLib(apkFile, "libil2cpp.so");
    }
}
