# Car Dealership Backend

A car dealership API system, delivering car information, location, and pricing details via a Spring Boot REST API.

## Overview

## Dependencies

- Maven
- Spring Boot
- Java 11+

## Installation

- The complete program depends on all underlying parts--Vehicles service, pricing service, and location service--to function correctly. However, each part can be started independently by running the following command. Substitute the package name for the individual service:

```shell
$ mvn clean package
$ java -jar target/[package-name]-0.0.1-SNAPSHOT.jar
```

### Ports

- The vehicle service is found on PORT 8080.
- The Eureka server is found on PORT 8761.
- The pricing service is found on PORT 8762.
- The location service is found on PORT 9191.

## Instructions

## Vehicle Service

- A REST API to maintain vehicle data and to provide a complete view of vehicle details including price and address.
- Features
  - REST API exploring the main HTTP verbs and features
  - Hateoas
  - Custom API Error handling using `ControllerAdvice`
  - Swagger API docs
  - HTTP WebClient
  - MVC Test
  - Automatic model mapping

### Operations

- Swagger UI: http://localhost:8080/swagger-ui.html

#### Create a Vehicle

- `POST` `/cars`

```json
{
  "condition": "USED",
  "details": {
    "body": "sedan",
    "model": "Impala",
    "manufacturer": {
      "code": 101,
      "name": "Chevrolet"
    },
    "numberOfDoors": 4,
    "fuelType": "Gasoline",
    "engine": "3.6L V6",
    "mileage": 32280,
    "modelYear": 2018,
    "productionYear": 2018,
    "externalColor": "white"
  },
  "location": {
    "lat": 40.73061,
    "lon": -73.935242
  }
}
```

#### Retrieve a Vehicle

- `GET` `/cars/{id}`

- This feature retrieves the Vehicle data from the database and access the Pricing Service and Boogle Maps to enrich the Vehicle information to be presented.

#### Update a Vehicle

- `PUT` `/cars/{id}`

```json
{
  "condition": "USED",
  "details": {
    "body": "sedan",
    "model": "Impala",
    "manufacturer": {
      "code": 101,
      "name": "Chevrolet"
    },
    "numberOfDoors": 4,
    "fuelType": "Gasoline",
    "engine": "3.6L V6",
    "mileage": 32280,
    "modelYear": 2018,
    "productionYear": 2018,
    "externalColor": "white"
  },
  "location": {
    "lat": 40.73061,
    "lon": -73.935242
  }
}
```

#### Delete a Vehicle

- `DELETE` `/cars/{id}`

## Pricing Service

- A REST API to maintain vehicle data and to provide a complete view of vehicle details including price and address.
- Implemented as a microservice using Eureka.
- Uses a Eureka server for microservice discoverability.

## Boogle Maps

- This is a Mock that simulates a Maps WebService where, given a latitude longitude, will return a random address.

## Requirements

### Pricing Service Rubric

- [x] The Pricing Service API is converted to a microservice with Spring Data REST, without the need to explicitly include code for the Controller or Service.
- [x] A Eureka server is implemented and running on port 8761. The Pricing Service is registered on that server and is named pricing-service.
- [ ] The Vehicles API should be able to use the Eureka server to call the pricing service.
- [ ] Within the test folder, at least one additional test has been implemented outside of contextLoads() for the overall Pricing Service Application.

### Vehicle Service Rubric

- [x] The Vehicles API is able to create a new vehicle based on input from the user with a POST request.
- [x] The Vehicles API can receive GET requests from a user, and read back either a list of all existing vehicles, or the data for a single vehicle.
- [x] The Vehicles API can update an existing vehicle through input from the user.
- [x] The Vehicles API can delete an existing vehicle when requested by the user.
- [x] The Vehicles API is able to consume information from the separate Boogle Maps and Pricing Service APIs, and return that information as part of the vehicle information for a single vehicle.
  - Note: Boogle Maps will assign a new random address each time a query is called, so don't fret if it changes between queries.

### Testing Rubric

- [x] Within the CarControllerTest.java file, the TODOs for tests of CRUD operations have been implemented.
  - You are welcome to add additional tests beyond these as desired!
- [x] All necessary dependencies have been added to the relevant POM files, and the code is able to run both from tests and in launching the actual applications.

### Documentation Rubric

- [x] API documentation for the Vehicles API is implemented using Swagger, and all related API queries are able to be run from there.
- [x] The documentation is available at http://localhost:8080/swagger-ui.html when the application is running.
  - Note: You are welcome to add Swagger API documentation for the other APIs, but it is not required.

### Extra Credit

- The Boogle Maps application does not actually store the address assigned to a given vehicle based on latitude and longitude, and instead randomly assigns a new one each time it is called. How could you update this to track which address is assigned to which vehicle? What happens if the vehicle latitude and longitude is updated in the Vehicles API?
- The Pricing Service stores prices based on ID, but that ID is not truly assigned to a specific vehicle - if the vehicle is deleted and a new vehicle uses the old ID, the same price is used. How can you update the Pricing Service (and perhaps the Vehicles API) to assign a new (random) price when a vehicle is removed from the Vehicles API?
- An additional helpful service after the Vehicles API would be to have an Orders/Sales service when a customer wants to order a vehicle. How would you implement this Orders/Sales API and integrate it with the Vehicles API?
