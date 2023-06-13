package br.com.nagem.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import com.google.gson.Gson;

import br.com.nagem.db.repository.ProductImagesB64Repository;
import br.com.nagem.model.Image;
import br.com.nagem.model.ProductImages;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "FTPSt3_SendTopic")
public class FTP_SendTopic {

	@Value("${topic-producer}")
	private String topicProducer;

	@Autowired
	ProductImagesB64Repository productImagesB64Repository;

	@Autowired
	ProductImagesService pis;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.data.mongodb.uri}")
    private String mongodbUri;

	private static final String MONGODB_DATABASE = "admin";
    private static final String MONGODB_COLLECTION = "stageImgB64";

	@Async
	public void SendTopic(int qtde) {

		//Span span = tracer.buildSpan("/process").start();

		log.info("Start Stage-SendTopic Integrator Products Images.");


        // Conectar ao MongoDB
        ConnectionString connectionString = new ConnectionString(mongodbUri);
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(MONGODB_DATABASE);
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION);

        Document filter = new Document("done", "N");

		FindIterable<Document> result = collection.find(filter).limit(qtde);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date dataModificacaoImagem = new Date();

		Image im = new Image();

	    // Iterar sobre os documentos encontrados
        for (Document document : result) {
            // Processar cada documento encontrado
			String idImg = document.getString("_id");
			String imgB64 = document.getString("imgB64");

            System.out.println(document.getString("_id"));

			String id;

			String filName;

			if (idImg.contains("_") && idImg.contains(".")) {
				id = idImg.substring(0, idImg.lastIndexOf("_"))
								.replace("/", "");

				//log.info("Id Image:  " + idImg.replace("/", ""));

				filName = idImg.substring(0, idImg.lastIndexOf("_")) + "."
				+ idImg;


				im.setFile(imgB64);
				im.setFilename(filName);
		
				System.out.println("NOME DO ARQUIVO+"+ filName);


			} else {

				id = idImg.substring(0, idImg.lastIndexOf("."))
						.replace("/", "");

				//log.info("Id Image:  " + idImg.replace("/", ""));

				filName = idImg.substring(0, idImg.lastIndexOf(".")) + "."
						+ idImg;

				im.setFile(imgB64);
				im.setFilename(filName);

				System.out.println("NOME DO ARQUIVO+"+ filName);

			}

			Optional<ProductImages> retDb = pis.FindProductImage(id);

			if (retDb == null) {

				log.info("Product flag I");
				ProductImages model = new ProductImages();
				model.setProductId(id);
				model.setFlagStatus("I");
				model.setPath("./" + idImg);
				model.setModificationDate(dateFormat.format(dataModificacaoImagem).toString());

				ProductImages prdImgI = pis.CreateProductImage(model);

				log.info("***** Product image successfully inserted in the database: " + prdImgI.getProductId());

				System.out.println("NOME DO ARQUIVO+"+ filName);

				kafkaTemplate.send(topicProducer, (new Gson().toJson(im).replace("\\u003d", "")));

				log.info("Send Topic: " + topicProducer);

				productImagesB64Repository.deleteById(idImg);

		        Document filterId = new Document("_id", idImg);
		        Document updtDone = new Document("$set", new Document("done", "E"));
		        collection.updateOne(filterId, updtDone);


			} else {

				log.info("Product flag A");

				ProductImages model = new ProductImages();
				model.setProductId(id);
				model.setFlagStatus("A");
				model.setPath("./" + idImg);
				model.setModificationDate(dateFormat.format(dataModificacaoImagem).toString());

				ProductImages prdImgA = pis.CreateProductImage(model);

				log.info("***** Product image updated successfully in the database: " + prdImgA.getProductId());

				System.out.println("NOME DO ARQUIVO+"+ filName);

				kafkaTemplate.send(topicProducer, (new Gson().toJson(im).replace("\\u003d", "")));

				log.info("Send Topic: " + topicProducer);

		        Document filterId = new Document("_id", idImg);
		        Document updtDone = new Document("$set", new Document("done", "E"));
		        collection.updateOne(filterId, updtDone);

			}

        }

		log.info("End Stage-SendTopi Integrator Products Images.");

	}

}
