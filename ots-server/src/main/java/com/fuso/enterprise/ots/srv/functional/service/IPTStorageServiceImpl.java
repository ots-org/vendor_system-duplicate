
package com.fuso.enterprise.ots.srv.functional.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fuso.enterprise.ots.srv.api.service.functional.IPTStorageService;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;

@Service
public class IPTStorageServiceImpl implements IPTStorageService {

    private Logger logger = LoggerFactory.getLogger(IPTStorageServiceImpl.class);

    private static final AtomicLong LAST_TIME_MS = new AtomicLong();

    @Override
    public String storeFile(String rdApprovalImageBase64Data) {
        byte[] decodedString = Base64.decodeBase64(rdApprovalImageBase64Data.getBytes(StandardCharsets.UTF_8));
        String pdfFileName = uniqueCurrentTimeMS() + ".pdf";
        String ip = "http;//localhost:8080/";
        ip = getHost(ip);
        String url = "http://" + ip + ":8080/ots/resources" + pdfFileName;
        String uploadpath = "C:\\iptfiles\\pdf\\" + pdfFileName;
        File dwldsPath = new File(uploadpath);
        FileOutputStream os;
        createFolder();
        try {
            os = new FileOutputStream(dwldsPath, false);
            os.write(decodedString);
            os.flush();
            os.close();
            logger.info("Uploaded Patch:{}", url);
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
            throw new BusinessException(ioException, ErrorEnumeration.SYSTEM_ERROR);
        }
        return url;
    }

    private void createFolder() {
        File theDir = new File("C:\\iptfiles\\pdf\\");
        if (!theDir.exists()) {
            try {
                theDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

    }

    private String getHost(String ip) {
        try {
            ip = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e1) {
            logger.error(e1.getMessage());
        }
        return ip;
    }

    public static long uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }
}
