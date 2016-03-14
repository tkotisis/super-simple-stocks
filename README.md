# Super Simple Stocks

Super Simple Stocks is a very simple stock exchange simulator.

## Getting Started

### Requirements
* Java 1.7
* maven 3.0.5 or later

### Build and Run

#### Build
```sh
cd super-simple-stocks/
mvn clean install
```

#### Run exchange server
```sh
cd super-simple-stocks-webapp/
mvn jetty:run
```

Exchange service will be available at ```localhost:8080```.

#### Run server integration tests
```sh
cd super-simple-stocks/super-simple-stocks-webapp
mvn failsafe:integration-test
```

#### Run exchange client
```sh
cd super-simple-stocks/super-simple-stocks-client
java -jar target/super-simple-stocks-client-1.0-SNAPSHOT-jar-with-dependencies.jar
```
