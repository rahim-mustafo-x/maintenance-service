package org.safa.maintenanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MaintenanceServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(MaintenanceServiceApplication.class, args);
    }

}
