package com.udacity.pricing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

  @Autowired
  private MockMvc mvc;

  @Test
  public void contextLoads() {
  }

  @Test
  public void getPrice() throws Exception {
    int vehicleId = 1;
    mvc.perform(
      get(new URI("/services/price?vehicleId=" + vehicleId))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").value(vehicleId))
      .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("USD"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.price").exists());
  }
}
