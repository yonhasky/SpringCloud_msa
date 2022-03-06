# SpringCloud_msa

#### _SpringCloud MicroServiceArchitecture project_

### [Tech-stack]
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

### [Command]

web : http://localhost:15672
user: guest/guest

- MVN : 
```sh
mvn spring-boot:run
```
### [user-service]
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

> Zookeeper 구동
- linux
```sh
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
```
- windows
```
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
> Kafka 구동
- linux
```sh
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
```
- windows
```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

> quickstart-events 토픽 테스트
1. Topic 생성
```sh
$KAFKA_HOME/bin/kafka-topic.sh --create --topic quickstart-events --bootstrap-server localhost:9092 --partitions 1
```
2. Topic 목록 확인
```sh
$KAFKA_HOME/bin/kafka-topic.sh --bootstrap-server localhost:9092 --list
win : .\bin\windows\kafka-topic.bat --bootstrap-server localhost:9092 --list
```
3. Topic 정보 확인
```sh
$KAFKA_HOME/bin/kafka-topic.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
win: .\bin\windows\kafka-topics.bat --describe --topic my_topic_users --bootstrap-server localhost:9092
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

## [Kafka Connect]
> Kafka Connect 설치
```
curl -O http://packages.confluent.io/archive/6.1/confluent-community-6.1.0.tar.gz
tar xvf confluent-community-6.1.0.tar.gz
cd  $KAFKA_CONNECT_HOME
```

> Kafka Connect 실행
```
./bin/connect-distributed ./etc/kafka/connect-distributed.properties
win: .\bin\windows\connect-distributed.bat .\etc\kafka\connect-distributed.properties
```

> JDBC Connector 설치
```
- https://docs.confluent.io/5.5.1/connect/kafka-connect-jdbc/index.html
- confluentinc-kafka-connect-jdbc-10.3.3.zip 
```

> etc/kafka/connect-distributed.properties 파일 마지막에 아래 plugin 정보 추가
- plugin.path=[confluentinc-kafka-connect-jdbc-10.0.1 폴더]

> JdbcSourceConnector에서 MariaDB 사용하기 위해 mariadb 드라이버 복사
- ./share/java/kafka/ 폴더에 mariadb-java-client-2.7.2.jar  파일 복사

> Kafka Source Connect 추가(postman으로 가능)
```
echo '

{
  "name" : "my-source-connect",
  "config" : {
  "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
  "connection.url":"jdbc:mysql://localhost:3306/mydb",
  "connection.user":"root",
  "connection.password":"test1357",
  "mode": "incrementing",
  "incrementing.column.name" : "id",
  "table.whitelist":"users",
  "topic.prefix" : "my_topic_",
  "tasks.max" : "1"
  }
}

' | curl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json"

```

> Kafka Sink Connect 추가(mariaDB)
```
echo '

{
  "name":"my-sink-connect",
  "config":{
  "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
  "connection.url":"jdbc:mysql://localhost:3306/mydb",
  "connection.user":"root",
  "connection.password":"test1357",
  "auto.create":"true",
  "auto.evolve":"true",
  "delete.enabled":"false",
  "tasks.max":"1",
  "topics":"my_topic_users"
  }
}

'| curl -X POST -d @- http://localhost:8083/connectors --header "content-Type:application/json"
```

## [MariaDB]
> MariaDB 설치
```
https://mariadb.org
```
- MariaDB 10.6
> H2-console
```
- Saved Settings : Generic MySQL
- Driver Class: org.mariadb.jdbc.Driver
- JDBC URL: jdbc:mysql://localhost:3306/test
- ID: root
- PW: test
```
- 데이터베이스 생성
```
create database mydb;
```
- 데이터베이스 등록
```
use mydb;
```
