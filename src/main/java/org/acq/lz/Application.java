package org.acq.lz;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args){

        try {
            SpringApplication.run(Application.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}