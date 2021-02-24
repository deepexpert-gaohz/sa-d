package com.ideatech.ams.test;

import com.ideatech.ams.AmsApp;
import com.ideatech.ams.controller.notice.NoticeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = AmsApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NoticeTest {

    @Autowired
    private NoticeController noticeController;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController).build();
    }

    @Test
    public void count() throws Exception {
        ResultActions perform = this.mockMvc.perform(MockMvcRequestBuilders.get("/notice/count"));
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
}
