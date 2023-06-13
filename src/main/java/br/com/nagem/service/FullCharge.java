package br.com.nagem.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.nagem.config.LibConfiguration;
import br.com.nagem.utils.Base64Converter;
import br.com.nagem.utils.FTPHelper;
import br.com.nagem.utils.MongoDBHelper;

@Async
@Component
public class FullCharge {

    private LibConfiguration libConfiguration;

    @Autowired
    public FullCharge(LibConfiguration libConfiguration) {
        this.libConfiguration = libConfiguration;
    }

    private FTPClient ftpClient;

    public void startFullCharge() {

        String server = libConfiguration.getServer();
        int port = libConfiguration.getPort();
        String username = libConfiguration.getUsername();
        String password = libConfiguration.getPassword();
        String remoteDir = libConfiguration.getRemoteDirPath();
        String mongoURI = libConfiguration.getUri();

        FTPHelper ftpHelper = new FTPHelper(server, port, username, password, remoteDir);
        FTPFile[] files = ftpHelper.connectAndListFiles();
        ftpClient = ftpHelper.getFtpClient(); // Obter a instância de FTPClient do FTPHelper

        try{

            int qtde = 0;

            for (FTPFile file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg") && isValidFileName(file.getName())) {

                        List<Document> documents = new ArrayList<>();

                        Base64Converter base64Converter = new Base64Converter(ftpClient);


                        String id = file.getName();
                        String done = "N";
                        String imgB64 = base64Converter.convertToBase64(file);

                        Document document = new Document("_id", id)
                                .append("imgB64", imgB64)
                                .append("done", done);
                        
                        documents.add(document);

                        MongoDBHelper mongoDBHelper = new MongoDBHelper(mongoURI);

                        String dbName = "admin";
                        String collectionName = "stageImgB64";
                        mongoDBHelper.insertDocuments(documents, dbName, collectionName);

                        qtde++;
                }
            }

            System.out.println("TOTAL DE ARQUIVOS VALIDOS: " + qtde);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                // Feche a conexão FTP somente após o processamento completo dos arquivos
                ftpHelper.closeConnection();
        }
    }

    private static boolean isValidFileName(String fileName) {
        return fileName.matches("[0-9]+(_[0-9]+)?\\.jpg");
    }

}
