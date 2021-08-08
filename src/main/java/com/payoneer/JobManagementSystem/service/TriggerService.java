package com.payoneer.JobManagementSystem.service;

import com.payoneer.JobManagementSystem.model.TriggerModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public interface TriggerService {

    TriggerModel createTrigger(TriggerModel triggerModel);

    TriggerModel getTriggerById(Long triggerId);

    List<TriggerModel> getAllTriggers();

    void deleteTrigger(Long triggerId);

}
