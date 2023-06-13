package br.com.nagem.utils;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.nagem.config.LibConfiguration;

import org.springframework.stereotype.Component;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MonitorIntegracao {

    private LibConfiguration libConfiguration;

    @Autowired
    public MonitorIntegracao(LibConfiguration libConfiguration) {
        this.libConfiguration = libConfiguration;
    }


    public String monitorCarga(){

        String mongoURI = libConfiguration.getUri();

        try (MongoClient mongoClient = MongoClients.create(new ConnectionString(mongoURI))) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = database.getCollection("stageImgB64");

            Document queryN = new Document("done", "N");
            long totalN =  collection.countDocuments(queryN);

            Document queryE = new Document("done", "E");
            long totalE =  collection.countDocuments(queryE);

            return "{\"Total a processar\":"+totalN+",\"Em processamento\":"+totalE+"}";

        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return "{\"code\":\"204\",\"message\":\"No Content\"}";
    }

}
