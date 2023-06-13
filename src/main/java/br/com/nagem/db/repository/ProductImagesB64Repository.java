package br.com.nagem.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.nagem.model.ProductB64;

@Repository
public interface ProductImagesB64Repository   extends MongoRepository<ProductB64, String> {
	
	Optional<ProductB64> findById(String id);
	
	List<ProductB64> findAll();  
	

}
