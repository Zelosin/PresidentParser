package org.zelosin.Services;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileTransferringService {

    private static final long TEMPLATE_SIZE = 19032;

    public static synchronized ResponseEntity<Object> processingEntityToFileToDownload() throws IOException {
        System.out.println("1");
        InputStream is = FileTransferringService.class.
                getResourceAsStream("/resources/SampleQueryConfiguration.xml");

        System.out.println("2" + is);
        if(is == null) {
            is = FileTransferringService.class.getClassLoader().getResourceAsStream("SampleQueryConfiguration.xml");
        }
        System.out.println("3" + is);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "template.xml"));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        System.out.println("defore response");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(TEMPLATE_SIZE)
                .contentType(MediaType.APPLICATION_XML)
                .body(new InputStreamResource(is));
    }

    public static synchronized ResponseEntity<Object> processingEntityToFileToDownload(ByteArrayOutputStream pBao, String pFileName){

        InputStreamResource tResourceInputStream = null;

        tResourceInputStream = new InputStreamResource(new ByteArrayInputStream(pBao.toByteArray()));

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", pFileName));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers).contentLength(
                pBao.size()).contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(tResourceInputStream);
    }

}
