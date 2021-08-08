package com.payoneer.JobManagementSystem.service.impl;

import com.payoneer.JobManagementSystem.exception.ElementNotFoundException;
import com.payoneer.JobManagementSystem.model.TriggerModel;
import com.payoneer.JobManagementSystem.repository.TriggerRepository;
import com.payoneer.JobManagementSystem.service.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Component
public class TriggerServiceImpl implements TriggerService {

    @Autowired
    TriggerRepository triggerRepository;


    @Override
    public TriggerModel createTrigger(TriggerModel triggerModel) {
        triggerRepository.save(triggerModel);
        return triggerModel;
    }

    @Override
    public TriggerModel getTriggerById(Long triggerId) {
            return triggerRepository
                    .findById(triggerId)
                    .orElseThrow(() -> new ElementNotFoundException("no trigger found"));
    }

    @Override
    public List<TriggerModel> getAllTriggers() {
        return triggerRepository.findAll();
    }

    @Override
    public void deleteTrigger(Long triggerId) {

    }
}
