package br.com.nagem.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LibConfiguration {

    private String server;
    private int port;
    private String username;
    private String password;
    private String remoteDirPath;
    private String uri;

    @Value("${image.ftp.server}")
    public void setServer(String server) {
        this.server = server;
    }

    @Value("${image.ftp.port}")
    public void setPort(int port) {
        this.port = port;
    }

    @Value("${image.ftp.user}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${image.ftp.pass}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${image.ftp.remoteDirPath}")
    public void setRemoteDirPath(String remoteDirPath) {
        this.remoteDirPath = remoteDirPath;
    }

    @Value("${spring.data.mongodb.uri}")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRemoteDirPath() {
        return remoteDirPath;
    }
   
    public String getUri() {
        return uri;
    }

}
