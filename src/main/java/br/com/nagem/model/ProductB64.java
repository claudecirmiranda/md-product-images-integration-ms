package br.com.nagem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@Document("stageimagesb64")
@ToString
public class ProductB64 {
	
	@Id
	private String productId;
	
	private byte[] btImg;
		
	private String imgB64;

	private String done;

}
