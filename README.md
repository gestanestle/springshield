# Backend Service with SpringBoot and PostgreSQL
REST API ready to consume from front-end technologies.

This project implements user signup, email verification with Gmail SMTP, and login authentication. Endpoints are secured with custom authorization and Json Web Tokens. The technologies used are Spring Web, Spring Security, Spring JPA, and PostgreSQL. The main classes have been unit tested with JUnit and Mockito.  

## Docker 

To start the SpringBoot application in the docker containers, navigate to the root directory and run the following command in the terminal:

```
docker compose up
```

To stop the containers:

```
docker compose down
```

## Swagger

This application is embedded with Swagger IO. To view the API documentation of this service, go to the following url:

```
http://localhost:8080/swagger-ui/index.html
``` 

The API documentation should display it like this:

![Screenshot from 2022-11-20 17-09-27](https://user-images.githubusercontent.com/83026862/202910745-30270223-13ee-43fc-84df-e6c3719cf057.png)


## Project structure

```bash
.
├── docker-compose.yml
├── Dockerfile
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── krimo
│   │   │           └── BackendService
│   │   │               ├── BackendServiceApplication.java
│   │   │               ├── email
│   │   │               │   └── EmailSenderService.java
│   │   │               ├── exception
│   │   │               │   ├── ApiExceptionHandler.java
│   │   │               │   ├── ApiException.java
│   │   │               │   └── RequestException.java
│   │   │               ├── registration
│   │   │               │   ├── controller
│   │   │               │   │   ├── ActivationController.java
│   │   │               │   │   └── RegistrationController.java
│   │   │               │   └── service
│   │   │               │       ├── ActivationService.java
│   │   │               │       ├── RegistrationService.java
│   │   │               │       └── ServiceImpl.java
│   │   │               ├── requestbody
│   │   │               │   ├── UserObject.java
│   │   │               │   └── VerificationTokenObject.java
│   │   │               ├── security
│   │   │               │   ├── config
│   │   │               │   │   ├── filter
│   │   │               │   │   │   ├── CustomAuthenticationFilter.java
│   │   │               │   │   │   └── CustomAuthorizationFilter.java
│   │   │               │   │   └── SecurityConfig.java
│   │   │               │   ├── PasswordEncoder.java
│   │   │               │   ├── UsernamePasswordAuthToken.java
│   │   │               │   └── utils
│   │   │               │       └── JWTUtility.java
│   │   │               ├── token
│   │   │               │   ├── VerificationToken.java
│   │   │               │   ├── VerificationTokenRepo.java
│   │   │               │   └── VerificationTokenService.java
│   │   │               └── user
│   │   │                   ├── controller
│   │   │                   │   └── UserController.java
│   │   │                   ├── entity
│   │   │                   │   ├── model
│   │   │                   │   │   ├── User.java
│   │   │                   │   │   └── UserRole.java
│   │   │                   │   ├── repository
│   │   │                   │   │   └── UserRepo.java
│   │   │                   │   └── service
│   │   │                   │       ├── UserEmailService.java
│   │   │                   │       ├── UserServiceImpl.java
│   │   │                   │       └── UserService.java
│   │   │                   └── update
│   │   │                       ├── DeleteService.java
│   │   │                       ├── UpdateServiceImpl.java
│   │   │                       └── UpdateService.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── banner.txt
│   │       ├── static
│   │       └── templates
│   └── test
│       ├── java
│       │   └── com
│       │       └── krimo
│       │           └── BackendService
│       │               ├── BackendServiceApplicationTests.java
│       │               ├── registration
│       │               │   └── service
│       │               │       └── ServiceImplTest.java
│       │               ├── token
│       │               │   ├── VerificationTokenRepoTest.java
│       │               │   └── VerificationTokenServiceTest.java
│       │               └── user
│       │                   ├── entity
│       │                   │   ├── repository
│       │                   │   │   └── UserRepoTest.java
│       │                   │   └── service
│       │                   │       └── UserServiceImplTest.java
│       │                   └── update
│       │                       └── UpdateServiceImplTest.java
│       └── resources
│           └── application.yml
└── target


```


