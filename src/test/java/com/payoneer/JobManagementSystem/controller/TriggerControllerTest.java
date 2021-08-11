package com.payoneer.JobManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.model.TriggerModel;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")

class TriggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test - get all triggers")
    void getAllTriggers() throws Exception {
        mockMvc
                .perform(get("/trigger"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize((int) triggerRepository.count())));
    }

    @Test
    @DisplayName("Test - get trigger by ID")
    void getTriggerById() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .get("/trigger/4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.name").value("EveryTwoMinutesTrigger"));
    }

    @Test
    @DisplayName("Test - create new trigger")
    void createTrigger() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        TriggerModel triggerObj = new TriggerModel(null, "Test-Trigger-Name", "Test-Trigger-Group", "0 0/2 * 1/1 * ? *");
        String requestJson=ow.writeValueAsString(triggerObj);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/trigger")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test - delete trigger")
    void deleteTrigger() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/trigger/6")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}