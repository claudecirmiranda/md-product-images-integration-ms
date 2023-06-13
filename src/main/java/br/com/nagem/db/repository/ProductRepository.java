package br.com.nagem.db.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.nagem.model.Products;

@Repository
public interface ProductRepository  extends MongoRepository<Products, String> {
	
	Optional<Products> findById(String id);

}
