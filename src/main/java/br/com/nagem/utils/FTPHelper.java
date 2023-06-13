package br.com.nagem.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

public class FTPHelper {
    private String server;
    private int port;
    private String username;
    private String password;
    private String remoteDir;
    private FTPClient ftpClient; // Adicionado o campo ftpClient

    public FTPHelper(String server, int port, String username, String password, String remoteDir) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.remoteDir = remoteDir;
        this.ftpClient = new FTPClient();
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public FTPFile[] connectAndListFiles() {
        FTPFile[] files = null;

        try {
            System.out.println("***** CONECTANDO AO SERVIDOR FTPS");
            ftpClient.connect(server, port);
            ftpClient.login(username, password);

            // Verificar se a conexão foi bem sucedida
            if (ftpClient.isConnected()) {
                System.out.println("Conexão FTPS estabelecida com sucesso.");
            } else {
                System.out.println("Falha ao estabelecer conexão FTPS.");
                return null;
            }

            // Alterar o diretório remoto
            System.out.println("***** ALTERANDO DIRETÓRIO REMOTO PARA: " + remoteDir);
            boolean changeDirSuccess = ftpClient.changeWorkingDirectory(remoteDir);

            if (changeDirSuccess) {
                System.out.println("Alteração para o diretório remoto bem-sucedida: " + remoteDir);
                System.out.println("***** BUSCANDO ARQUIVOS VALIDOS NO DIRETÓRIO REMOTO");
            } else {
                System.out.println("Falha ao alterar para o diretório remoto: " + remoteDir);
                return null;
            }

            // Listar os arquivos do diretório remoto
            files = ftpClient.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return files;
    }

    public void closeConnection() {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
                System.out.println("Conexão FTPS encerrada com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

}
