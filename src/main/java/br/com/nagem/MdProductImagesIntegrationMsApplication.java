package br.com.nagem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@SpringBootApplication
//@EnableScheduling
@Service
@Component
@CamelOpenTracing
public class MdProductImagesIntegrationMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdProductImagesIntegrationMsApplication.class, args);
		
		 	LocalDateTime agora = LocalDateTime.now();
	        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	        String dataHoraFormatada = agora.format(formato);
	        System.out.println("Current date and time: " + dataHoraFormatada);
		
     
	}
}
