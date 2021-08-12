package com.payoneer.JobManagementSystem.service;

import com.payoneer.JobManagementSystem.model.TriggerModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public interface TriggerService {

    ResponseEntity<String> createTrigger(TriggerModel triggerModel);

    TriggerModel getTriggerById(Long triggerId);

    List<TriggerModel> getAllTriggers();

    ResponseEntity deleteTrigger(long triggerId);
}
