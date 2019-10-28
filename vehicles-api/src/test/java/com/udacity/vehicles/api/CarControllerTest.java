package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private JacksonTester<Car> json;

  @MockBean
  private CarService carService;

  @MockBean
  private PriceClient priceClient;

  @MockBean
  private MapsClient mapsClient;

  /**
   * Creates pre-requisites for testing, such as an example car.
   */
  @Before
  public void setup() {
    Car car = getCar("white", 1L, Condition.USED);
    Car updatedCar = getCar("true blue", 2L, Condition.NEW);
    updatedCar.setId(2L);
    given(carService.save(any())).willReturn(car);
    given(carService.findById(1L)).willReturn(car);
    given(carService.list()).willReturn(Collections.singletonList(car));
    given(carService.findById(2L)).willReturn(updatedCar);
  }

  /**
   * Tests for successful creation of new car in the system
   *
   * @throws Exception when car creation fails in the system
   */
  @Test
  public void createCar() throws Exception {
    Car car = getCar("white", 1L, Condition.USED);
    mvc.perform(
      post(new URI("/cars"))
        .content(json.write(car).getJson())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.condition").value("USED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.details.externalColor").value("white"));
  }

  /**
   * Tests if the read operation appropriately returns a list of vehicles.
   *
   * @throws Exception if the read operation of the vehicle list fails
   */
  @Test
  public void listCars() throws Exception {
    mvc.perform(
      get(new URI("/cars"))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.carList[0].id").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.carList[0].details.model").value("Impala"));
  }

  /**
   * Tests the read operation for a single car by ID.
   *
   * @throws Exception if the read operation for a single car fails
   */
  @Test
  public void findCar() throws Exception {
    mvc.perform(
      get(new URI("/cars/1"))
        .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.details.model").value("Impala"));
  }

  /**
   * Tests the update process for a single car.
   *
   * @throws Exception if the update operation fails.
   */
  @Test
  public void updateCar() throws Exception {
    Car car = getCar("true blue", 2L, Condition.NEW);
    mvc.perform(
      put(new URI("/cars/2"))
        .content(json.write(car).getJson())
        .contentType(MediaType.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.condition").value("NEW"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.details.externalColor").value("true blue"));
  }

  /**
   * Tests the deletion of a single car by ID.
   *
   * @throws Exception if the delete operation of a vehicle fails
   */
  @Test
  public void deleteCar() throws Exception {
    mvc.perform(delete(new URI("/cars/1")))
        .andExpect(status().isNoContent());
    mvc.perform(get("cars/1"))
       .andExpect(status().isNotFound());
  }

  /**
   * Creates an example Car object for use in testing.
   *
   * @return an example Car object
   */
  private Car getCar(String color, Long id, Condition condition) {
    Car car = new Car();
    car.setLocation(new Location(40.730610, -73.935242));
    Details details = new Details();
    Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
    details.setManufacturer(manufacturer);
    details.setModel("Impala");
    details.setMileage(32280);
    details.setExternalColor(color);
    details.setBody("sedan");
    details.setEngine("3.6L V6");
    details.setFuelType("Gasoline");
    details.setModelYear(2018);
    details.setProductionYear(2018);
    details.setNumberOfDoors(4);
    car.setDetails(details);
    car.setCondition(condition);
    car.setCreatedAt(LocalDateTime.now());
    car.setModifiedAt(LocalDateTime.now());
    car.setId(id);
    return car;
  }
}