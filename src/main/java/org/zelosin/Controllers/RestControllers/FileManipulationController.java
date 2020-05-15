package org.zelosin.Controllers.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zelosin.Base.ProcessingSample;
import org.zelosin.Services.FileTransferringService;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/transfer")
public class FileManipulationController {

    private final static String PROCESSING_KEY =  "PP_PROCESSING_SETTINGS";

    @ResponseBody
    @GetMapping(value = "/configuration")
    public ResponseEntity<Object> fileDownloadTransfer(HttpSession session, @RequestParam(required = false, value="type") String callType) {
        ProcessingSample tProcessingSample = (ProcessingSample) session.getAttribute(PROCESSING_KEY);

        if(callType.equals("sample")){
            try {
                return FileTransferringService.processingEntityToFileToDownload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(tProcessingSample == null)
            tProcessingSample = new ProcessingSample();
        ResponseEntity<Object> response = new ResponseEntity<>("",HttpStatus.OK);


        if(tProcessingSample.mBasicServicesCompiler != null) {
            switch (callType) {
                case ("basic"): {
                    response = FileTransferringService.processingEntityToFileToDownload(tProcessingSample.basicProcessingCycle(),
                            tProcessingSample.mBasicServicesCompiler.mQueryConfigurationsAssumer.mExportPath, "application/vnd.ms-excel");
                    break;
                }
                case ("query"): {
                    tProcessingSample.queryCycle();
                    break;
                }
                case ("reset"): {
                    tProcessingSample = null;
                    break;
                }
                case ("parse"): {
                    response = FileTransferringService.processingEntityToFileToDownload(tProcessingSample.parseCycle(),
                            tProcessingSample.mBasicServicesCompiler.mQueryConfigurationsAssumer.mExportPath, "application/vnd.ms-excel");
                    break;
                }
                case ("template"): {
                    try {
                        response  = FileTransferringService.processingEntityToFileToDownload(tProcessingSample.build(),
                                "Шаблон.xml", "application/xml");
                    } catch (TransformerException | ParserConfigurationException e) {
                        response = new ResponseEntity<>("Не удалось посторить конфигурационный файл.", HttpStatus.NO_CONTENT);
                    }
                    break;
                }
            }
        }else {
            response = new ResponseEntity<>("Не удалось обработать конфигурацию.", HttpStatus.NO_CONTENT);
        }
        session.setAttribute(PROCESSING_KEY, tProcessingSample);
        return response;
}

    @PostMapping(value = "/configuration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUploadTransfer(HttpSession session, @RequestParam("file") MultipartFile file) throws IOException {

        if(file.isEmpty()){
            return new ResponseEntity<>("CANCEL", HttpStatus.OK);
        }
        else {
            ProcessingSample tProcessingSample = (ProcessingSample) session.getAttribute(PROCESSING_KEY);
            if (tProcessingSample == null)
                tProcessingSample = new ProcessingSample();


            tProcessingSample.loadConfigFile(new ByteArrayInputStream(file.getBytes()));
            session.setAttribute(PROCESSING_KEY, tProcessingSample);

            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
    }
}