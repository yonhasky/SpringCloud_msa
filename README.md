# SpringCloud_msa

#### _SpringCloud MicroServiceArchitecture project_

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
- Kafka2.13-2.7.0
- MariaDB 10.6

## [Command]

web : http://localhost:15672
user: guest/guest

- MVN : 
```sh
mvn spring-boot:run
```
## [user-service]
> 유저 서비스
- SpringSecurity
- login > JWT


## [order-service]
> 주문정보 서비스
> MariaDb 사용

## [catalog-service]
> 상품 서비스


## [apigateway-service]
Spring api gateway server
- AuthorizationHeaderFilter : token검증
- CustomFilter : 
- GlobalFilter : 
- LoggingFilter : 

## [discovery-service]
> Eureka discovery server
```sh
http://localhost:8000
```

## [Config-service]
> Spring Cloud Config
- Spring Cloud Bus > amqp
- Git, Native 저장소
- MQ를 통해 busrefresh로 모든 서버 일괄 동기화
```sh
http://localhost:8000/user-service/actuator/busrefresh
```
- RabbitMQ : 
```sh
rabbitmq-server -start
```

## [Kafka]
> download : kafka.apache.org 
- Kafka_2.13-2.7.0 다운로드 후 압축해제
```sh
tar xvf Kafka_2.13-2.7.0.tgz
```

- bin 경로 : window 사용자 > bin/windows/..

- Zookeeper 구동
```sh
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
win : $KAFKA_HOME > .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
- Kafka 구동
```sh
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
win : $KAFKA_HOME > .\bin\windows\kafka-server-start.bat .\config\server.properties
```

> quickstart-events 토픽 테스트
1. Topic 생성
```sh
$KAFKA_HOME/bin/kafka-topic.sh --create --topic quickstart-events --bootstrap-server localhost:9092 --partitions 1
```
2. Topic 목록 확인
```sh
$KAFKA_HOME/bin/kafka-topic.sh --bootstrap-server localhost:9092 --list
```
3. Topic 정보 확인
```sh
$KAFKA_HOME/bin/kafka-topic.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
```
4. 메시지 생산
```sh
$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events
```

5. 메시지 소비
```sh
$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning
```

- Producer
- Consumer
- Kafka broker Cluster : 3대 이상 broker cluster 구성
- Zookeeper 연동: 

## [MariaDB]
> MariaDB 설치
```
https://mariadb.org
```
- MariaDB 10.6
