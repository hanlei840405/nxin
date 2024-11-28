package com.nxin.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class ZipUtils {
    /**
     * 使用gzip压缩字符串
     *
     * @param str 要压缩的字符串
     * @return
     */
    public static String compress(String str) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 使用gzip解压缩
     *
     * @param compressedStr 压缩字符串
     * @return
     */
    public static String unCompress(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return outputStream.toString();
    }
}
