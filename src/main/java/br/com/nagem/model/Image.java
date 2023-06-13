package br.com.nagem.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

	@JsonProperty("filename")
	private String filename;
	
	@JsonProperty("file")
	private String file;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}
