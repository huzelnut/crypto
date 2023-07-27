## Crypto investments recommendation service

---

### What is it?

This is application to get crypto prices recommendations.

---

### Requirements

+ Maven 3.8.1+
+ Docker 20.10.14+
+ JDK 17+ (to run locally)

---

### Environment variables

Application uses these environment variables. There's some property which you can change:

| Property name                     | Description                                                  | Default value  |
|-----------------------------------|--------------------------------------------------------------|----------------|
| REC_APP_SERVER_PORT               | Server port on which application will be started             | 8090           |
| REC_APP_LOG_FILENAME              | Log file path                                                | log/crypto.log |

---

### Build executable JAR

1. Build executable JAR with this application. If executed from application source folder, the command is:
    ```
    mvn clean package
    ```
    or (if you want to skip tests while building):
    ```
    mvn clean package -DskipTests
    ```
2. Built JAR will be in /target folder:
    ```
    target\crypto-<VERSION>.jar
    ```
    where:
   + \<VERSION> - current version of our application (e.g. '0.0.1')

---

### Run application in a Docker container

1. Build Docker image with this application:
    ```
    docker build -t platunov/crypto:<VERSION> .
    ```
    where:
   + \<VERSION> - current version of our application (e.g. '0.0.1')
2. Run image from previous step as a Docker container:
    ```
    docker run --name <REC_APP_CONTAINER_NAME> -e REC_APP_JDBC_URL=<REC_APP_JDBC_URL> -v <APP_VOLUME>:/opt/crypto/log -p <APP_OUTER_PORT>:<APP_INNER_PORT> -d platunov/crypto:<VERSION>
    ```
    where:
   + \<APP_CONTAINER_NAME> - application container name (e.g. 'crypto_backend')
   + \<APP_VOLUME> - application volume name to store logs (e.g. 'crypto_backend_log')
   + \<APP_OUTER_PORT> - outer port (at host) to be used to get to application API (e.g. 8090)
   + \<APP_INNER_PORT> - inner port (in container) to be exposed (e.g. 8090)
   + \<VERSION> - current version of our application (e.g. '0.0.1')

   Also here you can add environment variables from **Environment variables** block. Add the property with the *-e* key in format *<PROP_VALUE>:<PROP_KEY>*.

Example commands:
```
docker build -t platunov/crypto:0.0.1 .
docker run --name crypto_backend -e REC_APP_LOG_FILENAME=log/crypto.log -v crypto_backend_log:/opt/crypto/log -p 8090:8090 -d platunov/crypto:0.0.1
```

---

### Run tests

This build uses Maven Surefire plugin to run tests. Also tests need Docker installed to run. To run tests, perform this command from repository folder:

```
mvn test
```
Reports with tests results will be available in repository folder by next path:
```
\target\surefire-reports\
```

---


### API

API will be available by the path:

```
localhost:<APP_OUTER_PORT>/api
```
where:
+ \<APP_OUTER_PORT> - outer port you had exposed when running application container

---

### Swagger UI

Also available Swagger UI to get API definition. Swagger UI is located by next address:

```
<HOST>:<PORT>/api/swagger-ui/index.html
```
where:
+ \<HOST> - your host (e.g. localhost, if you run application locally)
+ \<PORT> - port on which application run (e.g. 8090 if you run application by example commands)
For example:
```
http://localhost:8090/api/swagger-ui/index.html
```
---

### Known TODO's

+ create 'black-list' for currencies (make currency unavailable - no more recommendations about this currency but keep information)
+ for InMemoryPriceRepository: optimize getting highest/lowest prices operations (e.g. implement indexes for price field)
+ implement exception handling (and re-throw exceptions to HTTP responses in a single style)
+ add database integration for storing currencies prices