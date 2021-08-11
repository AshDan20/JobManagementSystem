package com.payoneer.JobManagementSystem.service.impl;

/**
 * @author Ashish Dandekar
 *
 */

import com.payoneer.JobManagementSystem.exception.ElementNotFoundException;
import com.payoneer.JobManagementSystem.exception.JobManagementException;
import com.payoneer.JobManagementSystem.model.TriggerModel;
import com.payoneer.JobManagementSystem.repository.TriggerRepository;
import com.payoneer.JobManagementSystem.service.TriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TriggerServiceImpl implements TriggerService {

    private static final Logger logger = LoggerFactory.getLogger(TriggerServiceImpl.class);

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

    /**
     * api method to delete trigger
     * @param triggerId
     * @return
     */
    @Override
    public ResponseEntity deleteTrigger(long triggerId) {
        try {
            triggerRepository.deleteById(triggerId);
        } catch (Exception ex) {
            logger.error("Error while deleting the trigger " + triggerId);
            ex.printStackTrace();
            return new ResponseEntity("Trigger can't be deleted as it might be associated with one of the jobs", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Trigger has been deleted", HttpStatus.OK);
    }

}
