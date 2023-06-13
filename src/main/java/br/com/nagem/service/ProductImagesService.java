package br.com.nagem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nagem.db.repository.ProductImagesRepository;
import br.com.nagem.db.repository.ProductRepository;
import br.com.nagem.model.ProductImages;
import br.com.nagem.model.Products;

@Service
public class ProductImagesService {

	@Autowired
	ProductImagesRepository pi;
	
	@Autowired
	ProductRepository pr;
	
	
	
	public ProductImages CreateProductImage(ProductImages productImages) {
		
		pi.save(productImages);

		return productImages;

	}

	public Optional<ProductImages> FindProductImage(String id) {
		
		Optional<ProductImages> resp = pi.findById(id);

		if (!resp.isPresent()) {

			return null;

		} else {

			return resp;

		}
	}
	
	public Optional<Products> FindProduct(String id) {
		
		Optional<Products> resp = pr.findById(id);

		if (!resp.isPresent()) {

			return null;

		} else {

			return resp;

		}
	}

}