package org.zelosin.Controllers.RestControllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zelosin.Base.ProcessingSample;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/provider")
public class DataProviderController {

    private final static String PROCESSING_KEY =  "PP_PROCESSING_SETTINGS";

    @GetMapping
    public ResponseEntity<Object>  contentProviderHandler(
            @RequestParam(required = false, value="type") String callType,
            HttpSession session) {
        JSONObject retunrningObject = new JSONObject();
        ProcessingSample tProcessingSample = (ProcessingSample) session.getAttribute(PROCESSING_KEY);
        ResponseEntity<Object> response;

        if(tProcessingSample == null){
            tProcessingSample = new ProcessingSample();
        }

        if(tProcessingSample.mBasicServicesCompiler != null) {
            switch (callType){
                case ("members"):{
                    retunrningObject = tProcessingSample.mBasicServicesCompiler.mDepartmentMembersAssumer.provideContent();
                    break;
                }
                case ("prime"):{
                    retunrningObject = tProcessingSample.mBasicServicesCompiler.mQueryConfigurationsAssumer.provideContent();
                    break;
                }
                case ("sheets"):{
                    retunrningObject = tProcessingSample.mBasicServicesCompiler.mSheetConfigurationsAssumer.provideContent(tProcessingSample.mBasicServicesCompiler.mAjaxConfigurationsAssumer);
                    break;
                }
                case ("params"):{
                    retunrningObject.put("query", tProcessingSample.mBasicServicesCompiler.mQueryConfigurationsAssumer.provideQueryContent());
                    retunrningObject.put("ajax", tProcessingSample.mBasicServicesCompiler.mAjaxConfigurationsAssumer.provideAJAXContent());
                    break;
                }
                default:
                    response = new ResponseEntity<>("Не удалось получить данные - неправильный запрос.", HttpStatus.BAD_REQUEST);
            }
            response = new ResponseEntity<>(retunrningObject.toMap(), HttpStatus.ACCEPTED);
        }
        else {
            response = new ResponseEntity<>("Не добавлен файл кофигурации.", HttpStatus.NO_CONTENT);
        }

        session.setAttribute(PROCESSING_KEY, tProcessingSample);
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> contentDirectProviderHandler(
            @RequestBody String receivedJSONString,
            @RequestParam(required = false, value="type") String callType,
            HttpSession session) {
        ResponseEntity<Object> response = new ResponseEntity<>("Данные успешно обновлены.", HttpStatus.ACCEPTED);
        ProcessingSample tProcessingSample = (ProcessingSample) session.getAttribute(PROCESSING_KEY);
        if(tProcessingSample == null) {
            tProcessingSample = new ProcessingSample();
            response = new ResponseEntity<>("Не удалось обновить данные - время сессии истекло.", HttpStatus.FORBIDDEN);
        }
        if(tProcessingSample.mBasicServicesCompiler != null){
            switch (callType){
                case ("members"):{
                    tProcessingSample.mBasicServicesCompiler.mDepartmentMembersAssumer.set(new JSONObject(receivedJSONString));
                    break;
                }
                case ("prime"):{
                    tProcessingSample.mBasicServicesCompiler.mQueryConfigurationsAssumer.set(new JSONObject(receivedJSONString));
                    break;
                }
                case ("sheets"):{
                    tProcessingSample.mBasicServicesCompiler.mSheetConfigurationsAssumer.set(new JSONObject(receivedJSONString));
                    break;
                }
                default:
                    response = new ResponseEntity<>("Не удалось обновить данные - неправильный запрос.", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            response = new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        session.setAttribute(PROCESSING_KEY, tProcessingSample);
        return response;
    }
}
