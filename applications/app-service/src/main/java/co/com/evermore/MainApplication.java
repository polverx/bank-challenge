package co.com.evermore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
public class MainApplication {

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", "none");
        System.setProperty("jdk.tls.disabledAlgorithms", "");
        SpringApplication.run(MainApplication.class, args);
    }
}
