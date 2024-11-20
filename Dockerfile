FROM openjdk:17-jdk-slim

COPY target/wallet-0.0.1-SNAPSHOT.jar wallet.jar

ENTRYPOINT ["java", "-jar", "/wallet.jar"]
