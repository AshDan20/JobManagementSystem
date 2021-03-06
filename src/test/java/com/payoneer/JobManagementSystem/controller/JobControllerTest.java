package com.payoneer.JobManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.model.TriggerModel;
import com.payoneer.JobManagementSystem.repository.JobRepository;
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

class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test if all the jobs are returned")
    void testGetAllJobs() throws Exception {
        mockMvc
                .perform(get("/job"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize((int) jobRepository.count())));
    }


    @Test
    @DisplayName("Test - new job creation")
    public void testCreateJob() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        JobModel jobObj = createJobModelObj();
        String requestJson=ow.writeValueAsString(jobObj);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/job")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test - delete job")
    public void testDeleteJobById() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/job/deleteJob/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    @DisplayName("Test - get job by ID")
    public void testGetJobById() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .get("/job/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.jobname").value("SendEmailJob"));
    }



    //mock jobModelObj
    private JobModel createJobModelObj() {
        JobModel temp = new JobModel();
        temp.setJobname("DBUpdateJob");
        temp.setJobgroup("Data-Load-Group");
        TriggerModel jobTrigger = new TriggerModel();
        jobTrigger.setId(2l);
        temp.setTrigger(jobTrigger);
        return temp;
    }
}
