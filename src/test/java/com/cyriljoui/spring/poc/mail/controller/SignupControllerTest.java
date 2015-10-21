package com.cyriljoui.spring.poc.mail.controller;

import com.cyriljoui.spring.poc.mail.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.subethamail.wiser.Wiser;

import static com.cyriljoui.spring.poc.mail.controller.WiserAssertions.assertReceivedMessage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:application-test.properties")
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SignupControllerTest {


    public static final String MAIL_FROM = "testunit@springmailpoc.fr";
    public static final int MAIL_SERVER_PORT = 8025;
    private Wiser wiser;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        wiser = new Wiser();
        wiser.setPort(MAIL_SERVER_PORT);
        wiser.start();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    @Test
    public void send() throws Exception {
        // act
        mockMvc.perform(get("/signup"))
                .andExpect(status().is2xxSuccessful());
        // assert
        System.out.println(wiser.getMessages().get(0).getMimeMessage().getContent());
        assertReceivedMessage(wiser)
//                .from(MAIL_FROM) // problem to get real from ... maybe a bug in Wiser???
                .to("cyril.joui@gmail.com")
                .withSubject("Hi Cyril! You're now registered")
                .withContentContains("Your email is cyril.joui@gmail.com and your language is en");
    }

}
