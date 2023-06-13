package br.com.nagem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.nagem.service.DailyCharge;
import br.com.nagem.service.FTP_SendTopic;
import br.com.nagem.service.FullCharge;
import br.com.nagem.utils.MonitorIntegracao;

@RestController
@RequestMapping("/img")
public class ImageController {


	@Autowired
	private DailyCharge dailyCharge;

	@Autowired
	private FullCharge fullCharge;

	@Autowired
	FTP_SendTopic sendTopic;

	@Autowired
	MonitorIntegracao monitorIntegracao;

	@GetMapping("/sendtopic/{qtd}")
	public String sendTopic(@PathVariable("qtd") int qtd) {

		sendTopic.SendTopic(qtd);

		return "Started Stage-SendTopic Images.";

	}

	@GetMapping("/ping")
	public String ping() {

		return "Pong";

	}

	/* NOVO PROCESSO DE CARGAS */
    @GetMapping("/dailycharge")
    public String dailyCharge() {
        dailyCharge.startCharge();
        return "Carga diária iniciada";
    }

    @GetMapping("/fullcharge")
    public String monitori() {
        fullCharge.startFullCharge();
        return "Iniciando carga total";
    }

    @ResponseBody
    @GetMapping(value = "/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
    public String monitorIntegracao() {
        return monitorIntegracao.monitorCarga();
    }

}	
