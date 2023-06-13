package br.com.nagem.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("specifications")
public class Altesp {

	private String ID_TAG;

	private String action;

	private String productId;

	private String IDFEMP_TAG; 

	private String technicalSpecificationId;

	private String specificationContent;

	private String techinicalSpecificationDescription;

}

