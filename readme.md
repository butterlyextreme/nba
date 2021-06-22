
# Introduction

The GameApplication is built as a SpringBoot application. After unzipping the application it can be started from the command line using the following cmd: **mvn spring-boot:run** from the root of the application. 

## Usage
The REST API of the micro-service can be accessed and explored here:
> http://localhost:8080/swagger-ui.html.

To access the built in H2 database go here here:
>http://localhost:8080/h2-console/login.do
>sa/password

## RapidAPI
I found the NBA Rapid API not to work as documented. This was evident  especially with regards retrieving the stats and games using the optional parameters of **date** and **game-id** To me if the API makes optional parameters available to filter, then they should not be ignored. No matter what I filled in I would get the dame list dating back a few years. I see others have made similar comments on the Q/A section of the API. 

## Unit Test Coverage
I only provided samples of what I would normally use more extensively for the different aspects of the application. 
- Controllers covered with SpringCloud Contract
- WebClients covered with WireMock
- Repository layer covered with Component Test
- Service Layer covered with Mockito 

>Lacking entirely are Integration tests.

