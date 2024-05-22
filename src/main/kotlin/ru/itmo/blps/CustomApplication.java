package ru.itmo.blps;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.stereotype.Component;


//@Component
//@Slf4j
//class SubmitSchedule implements JavaDelegate {
//    public final String asd = "adsasdad";
//
//    @Override
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        var v = delegateExecution.getVariable("program_name_var");
//        System.out.println(v);
//
//    }
//}

@SpringBootApplication
//@ComponentScan(
//        basePackages = {"ru.itmo.blps.generated.api.admin", "org.openapitools.configuration"},
//        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
//)
@EnableProcessApplication
public class CustomApplication {
//
//    @Autowired
//    private SubmitSchedule submitSchedule;

    public static void main(String[] args) {
        System.out.println("asdasd!@#!@#!@#!@asdasd");
        SpringApplication.run(CustomApplication.class, args);
    }

    @PostConstruct
    public void asd() {
//        System.out.println(submitSchedule.asd);
        System.out.println("asdadasda!!@#!@#asdasdasd");
    }
}
