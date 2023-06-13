package br.com.nagem.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;

public class MongoDBHelper {
    private String uri;
    
    public MongoDBHelper(String uri) {
        this.uri = uri;
    }

    public void insertDocuments(List<Document> documents, String dbName, String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(new ConnectionString(uri))) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            for (Document document : documents) {
                String documentId = document.getString("_id");

                // Verifica se o documento já existe
                Document existingDocument = collection.find(eq("_id", documentId)).first();

                if (existingDocument != null) {
                    String existingImgB64 = existingDocument.getString("imgB64");
                    String newImgB64 = document.getString("imgB64");

                    // Verifica se o valor do campo imgB64 é igual
                    if (existingImgB64.equals(newImgB64)) {
                        // O valor do campo imgB64 é igual, pula a inserção/atualização
                        continue;
                    } else {
                        // O valor do campo imgB64 é diferente, atualiza o documento existente
                        collection.replaceOne(eq("_id", documentId), document);
                        System.out.println("Documento atualizado no MongoDB com sucesso.");
                        continue;
                    }
                }

                // O documento não existe, insere na coleção
                collection.insertOne(document);
                System.out.println("Documento inserido no MongoDB com sucesso.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
