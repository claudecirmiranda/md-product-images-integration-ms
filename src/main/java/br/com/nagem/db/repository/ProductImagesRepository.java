package br.com.nagem.db.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.nagem.model.ProductImages;

@Repository
public interface ProductImagesRepository  extends MongoRepository<ProductImages, String> {
	
	Optional<ProductImages> findById(String id);

}
