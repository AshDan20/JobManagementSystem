package com.payoneer.JobManagementSystem.controller;

import com.payoneer.JobManagementSystem.model.TriggerModel;
import com.payoneer.JobManagementSystem.service.TriggerService;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ashish Dandekar
 *
 */
@RestController
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    TriggerService triggerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TriggerModel> getAllTriggers(){
        return triggerService.getAllTriggers();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TriggerModel getTriggerById(@PathVariable ("id") long id)
    {
        return triggerService.getTriggerById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createTrigger(@RequestBody TriggerModel triggerModel){
        return triggerService.createTrigger(triggerModel);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTrigger(@PathVariable ("id") long triggerId){
        return triggerService.deleteTrigger(triggerId);
    }
}
