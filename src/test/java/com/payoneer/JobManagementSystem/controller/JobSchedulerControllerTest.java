package com.payoneer.JobManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payoneer.JobManagementSystem.repository.TriggerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")

class JobSchedulerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test - schedule send email job")
    void scheduleSendEmailJob() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/scheduler/1")   //SendEmailJob is configured with id 1 in DB
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }
}