mvn exec:java -Dexec.mainClass=com.mjkim.concurrency.simpleserver.serial.server.SerialServer
mvn exec:java -Dexec.mainClass=com.mjkim.concurrency.simpleserver.serial.client.MultipleSerialClients

mvn exec:java -Dexec.mainClass=com.mjkim.concurrency.simpleserver.concurrent.server.ConcurrentServer
mvn exec:java -Dexec.mainClass=com.mjkim.concurrency.simpleserver.concurrent.client.MultipleConcurrentClients


