package br.com.nagem.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Converter {

    private FTPClient ftpClient;

    public Base64Converter(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String convertToBase64(FTPFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.retrieveFile(file.getName(), outputStream);
        byte[] fileBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(fileBytes);
    }
}
