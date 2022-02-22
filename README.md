# SpringCloud_msa

## _SpringCloud MicroServiceArchitecture project_

## [Tech-stack]
- SpringBoot 2.6.3
- jdk11
- maven
- Spring Cloud
- Spring Cloud Gateway
- Spring Cloud Config
- Spring Cloud Bus
- Eureka
- Spring Security
- JWT
- h2
- jpa
- RabbitMQ : rabbitmq_server-3.9.13(환경변수)
- erlang : erl-24.2.1(환경변수)

## [Command]
- RabbitMQ : 
```sh
rabbitmq-server -start
```
web : http://localhost:15672
user: guest/guest

- MVN : 
```sh
mvn spring-boot:run
```
## [user-service]
유저
- SpringSecurity
- login > JWT


## [order-service]
주문


## [catalog-service]
상품


## [apigateway-service]
Spring api gateway server
- AuthorizationHeaderFilter : token검증
- CustomFilter : 
- GlobalFilter : 
- LoggingFilter : 

## [discovery-service]
Eureka discovery server
```sh
http://localhost:8000
```

## [Config-service]
- Spring Cloud Config
- Spring Cloud Bus > amqp
- Git, Native 저장소
- MQ를 통해 busrefresh로 모든 서버 일괄 동기화
```sh
http://localhost:8000/user-service/actuator/busrefresh
```
