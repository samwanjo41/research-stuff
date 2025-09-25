# MongoDB Atlas Barebone Project (with SSL Certificates)

This project demonstrates how to connect securely to **MongoDB Atlas** using **SSL certificates** in Java/Spring Boot.

---

## 🔑 Prerequisites
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) cluster
- Java 11+ (or 17+ recommended)
- OpenSSL installed
- JDK `keytool` available in your PATH
- `ca.pem` (CA certificate from Atlas)
- `client.pem` (client certificate from Atlas)

---

## 📂 Certificate Setup

Atlas provides `.pem` files (CA and client certificates).  
Java applications typically need **PKCS12 (`.p12`)** or **Java KeyStore (`.jks`)** formats.  
Below are the conversion steps.

---

### 1. Convert `client.pem` to PKCS12 keystore
```bash
openssl pkcs12 -export \
  -in client.pem \
  -out client-keystore.p12 \
  -name mongoClient \
  -passout pass:changeit

openssl pkcs12 -export \
  -in ca.pem \
  -out ca-truststore.p12 \
  -name mongoCA \
  -passout pass:changeit
```

### 2. Convert ca.pem to PKCS12 truststore
```bash
openssl pkcs12 -export \
  -in ca.pem \
  -out ca-truststore.p12 \
  -name mongoCA \
  -passout pass:changeit
```

### 3. Import CA certificate into Java TrustStore (JKS)
```bash
keytool -importcert \
  -file ca.pem \
  -alias mongoCA \
  -keystore truststore.jks \
  -storepass changeit \
  -noprompt
```

### 4. Convert .p12 to .jks
```bash
keytool -importkeystore \
  -srckeystore client-keystore.p12 \
  -srcstoretype pkcs12 \
  -destkeystore client-keystore.jks \
  -deststoretype jks \
  -srcstorepass changeit \
  -deststorepass changeit

keytool -importkeystore \
  -srckeystore ca-truststore.p12 \
  -srcstoretype pkcs12 \
  -destkeystore truststore.jks \
  -deststoretype jks \
  -srcstorepass changeit \
  -deststorepass changeit
```
# Spring Boot Configuration
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster-url>/test?ssl=true

javax.net.ssl.keyStore=client-keystore.p12
javax.net.ssl.keyStorePassword=changeit
javax.net.ssl.keyStoreType=PKCS12

javax.net.ssl.trustStore=truststore.jks
javax.net.ssl.trustStorePassword=changeit
javax.net.ssl.trustStoreType=JKS
