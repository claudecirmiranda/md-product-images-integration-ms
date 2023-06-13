package br.com.nagem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@Document("productimages")
@ToString
public class ProductImages {
	
	@Id
	private String productId;
		
	private String fileName;
	
	private String modificationDate;
	
	private String flagStatus;
	
	private String path;
	
	

}
