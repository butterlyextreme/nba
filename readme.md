
# Introduction

The GameApplication is built as a SpringBoot application. After unzipping the application it can be started from the command line using the following cmd: **mvn spring-boot:run** from the root of the application. 

## Usage
The REST API of the micro-service can be accessed and explored here:
> http://localhost:8080/swagger-ui.html.

To access the built in H2 database go here here:
>http://localhost:8080/h2-console/login.do
>sa/password

## Unit Test Coverage
I only provided samples of what I would normally use more extensively for the different aspects of the application. 
- Controllers covered with SpringCloud Contract
- WebClients covered with WireMock
- Repository layer covered with Component Test
- Service Layer covered with Mockito 

>Lacking entirely are Integration tests.

