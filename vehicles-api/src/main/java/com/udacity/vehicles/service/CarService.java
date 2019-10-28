package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

  private final CarRepository repository;
  private MapsClient map;
  private PriceClient price;

  public CarService(CarRepository repository, MapsClient map, PriceClient price) {
    this.repository = repository;
    this.map = map;
    this.price = price;
  }

  /**
   * Gathers a list of all vehicles
   *
   * @return a list of all vehicles in the CarRepository
   */
  public List<Car> list() {
    List<Car> cars = repository.findAll();

    for(Car car : cars) {
      String carPrice = car.getPrice();
      Location carAddress = car.getLocation();
      car.setPrice(carPrice);
      car.setLocation(carAddress);
    }

    return cars;
  }

  /**
   * Gets car information by ID (or throws exception if non-existent)
   *
   * @param id the ID number of the car to gather information on
   * @return the requested car's information, including location and price
   */
  public Car findById(Long id) {
    Optional<Car> optionalCar = repository.findById(id);

    if(optionalCar.isPresent()) {

      Car car = optionalCar.get();

      // Note: The car class file uses @transient, meaning you will need to call the pricing service each time to get the price.
      String carPrice = this.price.getPrice(id);
      car.setPrice(carPrice);

       // Note: The Location class file also uses @transient for the address,
       Location carLocation = this.map.getAddress(car.getLocation());
       car.setLocation(carLocation);

    }

    return optionalCar.orElseThrow(CarNotFoundException::new);
  }

  /**
   * Either creates or updates a vehicle, based on prior existence of car
   *
   * @param car A car object, which can be either new or existing
   * @return the new/updated car is stored in the repository
   */
  public Car save(Car car) {
    if (car.getId() != null) {
      return repository.findById(car.getId())
          .map(carToBeUpdated -> {
            carToBeUpdated.setDetails(car.getDetails());
            carToBeUpdated.setLocation(car.getLocation());
            carToBeUpdated.setCondition(car.getCondition());
            return repository.save(carToBeUpdated);
          }).orElseThrow(CarNotFoundException::new);
    }

    return repository.save(car);
  }

  /**
   * Deletes a given car by ID
   *
   * @param id the ID number of the car to delete
   */
  public void delete(Long id) {
    Optional<Car> optionalCar = Optional.ofNullable(repository.findById(id).orElseThrow(CarNotFoundException::new));
    optionalCar.ifPresent(repository::delete);
  }
}
