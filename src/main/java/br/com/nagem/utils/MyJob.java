package br.com.nagem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.nagem.service.DailyCharge;

@Component
public class MyJob {
    
	@Autowired
	private DailyCharge dailyCharge;

    //@Scheduled(fixedRate = 1800000) // 30 minutos em milissegundos
    public void executeJob() {

        dailyCharge.startCharge();
        System.out.println("Carga diária iniciada pelo JOB");

    }
}
