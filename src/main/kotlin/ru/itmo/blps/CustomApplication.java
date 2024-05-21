package ru.itmo.blps;

import jakarta.annotation.PostConstruct;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
        basePackages = {"ru.itmo.blps.generated.api.admin", "org.openapitools.configuration"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableProcessApplication
public class CustomApplication {
    public static void main(String[] args) {
        System.out.println("asdasd!@#!@#!@#!@asdasd");
        SpringApplication.run(CustomApplication.class, args);
    }

    @PostConstruct
    public void asd() {
        System.out.println("asdadasda!!@#!@#asdasdasd");
    }
}