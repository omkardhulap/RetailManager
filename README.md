# RetailManager

This is spring boot application, exposing 2 microservices
* /shop/add/ -> For a given shop address, find its location.
* /shop/getNearest/ -> Find the nearest shop for a given location

Spring Boot provides embedded tomcat, hence we do not require external container to execute application. However, built JAR can be deployed in external server. 

## Prerequisite

* Gradle 3.0	https://gradle.org/gradle-download/
* Java 8

## Build

Gradle is used to build jar. Use following command to build application through command line. Or add respective arguments to Gradle run configuration in eclipse.
Following command will execute test case post built.

```
gradle build
```

To build without running tests

```
gradle build -x test
```

On successful build the built jar can be found at `build/libs/` path.

## Test

Use the following command to run unit and integration tests separately for the rest end points.

```
gradle test
```

## Run

Once jar is built it can be run with the following command in cmd or in Eclipse run project as java application.

```
java -jar path/to/jar/retail-manager-0.1.0.jar
```

On successful startp following message will be displayed on colsole.
 ```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.0.RELEASE)

Application has started successfully!! Hurrey!!
```

## Application API

### Add Shop
Service URL -> http://localhost:8080/retail/add

This service uses **Google's GeoCoding API** to find latitude & longtitude for a given address and adds complete shop address to the in-memory cache.

```
Service Endpoint - /shop/add
REQUEST BODY     - {"shopName" : "My_Shop","shopAddress" : {"number": "amba bhavani temple kopar road dombivli india","postCode" : 421202}}
HTTP METHOD      - POST
HTTP RESPONSE    - 201 OK
RESPONSE BODY    - {"successful": true}
```

_**Please note that the `number` key in the request body actually takes the address (except pin code)**_

### Get Nearest Shop
Service URL -> http://localhost:8080/shop/nearest?customerLatitude=56.90678428&customerLongitude=67.78942

This api gets the nearest shop to the given location (latitude, longtitude) passed in as request parameter. This service uses **Google Distance Matrix API** to to get shortest distance from multiple destinations

Google Distance Matrix API URI Example 
* https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=55.55,77.77&destinations=111.40,81.50|42.10,29.07|-122.0,-23.60|-32.50,82.90|42.06,-21.50&key=AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw

```
Service Endpoint - /shop/getNearest
REQUEST PARAMS   - customerLatitude, customerLongitude
HTTP METHOD      - GET
HTTP RESPONSE    - 200 OK
RESPONSE BODY    - {"shopName" : "My_Shop","shopAddress" : {"number":"amba bhavani temple kopar road dombivli india","postCode":421202,"location":{"latitude":19.2144658,"longitude":73.0772353}}}

```
