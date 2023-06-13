package br.com.nagem.transformations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import br.com.nagem.dto.Products;
import br.com.nagem.model.Image;
import br.com.nagem.model.ProductImages;
import br.com.nagem.service.ProductImagesService;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "FromProductImagesIntegration")
public class FromProductImagesIntegration implements Processor {

	@Value("${url.product}")
	private String url;

	@Autowired
	ProductImagesService pis;

	@Autowired
	private Tracer tracer;

	@Value("${topic-producer}")
	private String topicProducer;

	@Value("${image.ftp.server}")
	private String server;

	@Value("${image.ftp.port}")
	private int port;

	@Value("${image.ftp.user}")
	private String user;

	@Value("${image.ftp.pass}")
	private String pass;

	@Value("${image.ftp.remoteDirPath}")
	private String remoteDirPath;
	
	private static final long MEMORY_THRESHOLD = 200 * 1024 * 1024;

	private static long totalMemoryUsed = 0;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void process(Exchange exchange) throws Exception {

		System.out.println("Start Integrator Products Images.");

		RestTemplate restTemplate = new RestTemplate();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date dataModificacaoImagem = new Date();

		Image im = new Image();

		Span span = tracer.buildSpan("/process").start();

		FTPClient ftpClient = new FTPClient();

		try {

			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.changeWorkingDirectory(remoteDirPath);

			FTPFile[] files = ftpClient.listFiles();

			LocalDate today = LocalDate.now();
			Arrays.stream(files).filter(file -> file.getName().toLowerCase().endsWith(".jpg")).filter(file -> {
				Calendar cal = file.getTimestamp();
				LocalDate modificationDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH));
				return today.equals(modificationDate);
			}).sorted((file1, file2) -> file2.getTimestamp().compareTo(file1.getTimestamp())).forEach(file -> {

				try {

					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ftpClient.retrieveFile(file.getName(), outputStream);
					byte[] bytes = outputStream.toByteArray();

					String base64 = Base64.getEncoder().encodeToString(bytes);
					
					totalMemoryUsed += base64.getBytes().length;

					Calendar cal = file.getTimestamp();
					LocalDate modificationDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
							cal.get(Calendar.DAY_OF_MONTH));

					String id;

					String filName;
					

					if (file.getName().contains("_") && file.getName().contains(".")) {

						id = file.getName().substring(0, file.getName().lastIndexOf("_")).replace("/", "");

						log.info("Id Image:  " + file.getName().replace("/", ""));

						filName = file.getName().substring(0, file.getName().lastIndexOf("_")) + "." + file.getName();

						im.setFile(base64);
						im.setFilename(filName.replace("/", ""));

					} else {

						id = file.getName().substring(0, file.getName().lastIndexOf(".")).replace("/", "");

						log.info("Id Image:  " + file.getName().replace("/", ""));

						filName = file.getName().substring(0, file.getName().lastIndexOf(".")) + "." + file.getName();

						im.setFile(base64);
						im.setFilename(filName.replace("/", ""));

					}

					System.out.println("Base64file:" + (new Gson().toJson(im).replace("\\u003d", "")));

					ResponseEntity<String> asString = restTemplate.getForEntity(url + id, String.class);

					if (asString.getBody() != null) {

						ProductImages model = new ProductImages();

						String body = asString.getBody();

						Products readValue = new ObjectMapper().readValue(body, Products.class);

						if (readValue != null) {
							Optional<ProductImages> retDb = pis.FindProductImage(id);

							if (retDb == null) {

								log.info("Product flag I");

								model.setProductId(id);
								model.setFlagStatus("I");
								model.setPath(remoteDirPath + file.getName());
								model.setModificationDate(dateFormat.format(dataModificacaoImagem).toString());

								ProductImages prdImgI = pis.CreateProductImage(model);

								log.info("Product image successfully inserted in the database: "
										+ prdImgI.getProductId());

								kafkaTemplate.send(topicProducer, (new Gson().toJson(im).replace("\\u003d", "")));

								log.info("Send Topic: " + topicProducer);

							} else {

								log.info("Product flag A");

								model.setProductId(id);
								model.setFlagStatus("A");
								model.setPath(remoteDirPath + file.getName());
								model.setModificationDate(dateFormat.format(dataModificacaoImagem).toString());

								ProductImages prdImgA = pis.CreateProductImage(model);

								log.info("Product image updated successfully in the database: "
										+ prdImgA.getProductId());

								kafkaTemplate.send(topicProducer, (new Gson().toJson(im).replace("\\u003d", "")));

								log.info("Send Topic: " + topicProducer);

							}

						}
					} else {

						log.info("The image has no product linked: " + id);

					}


					if (!today.equals(modificationDate)) {
						System.out.println("Image has no date modified today");
					}
					
					if (totalMemoryUsed >= MEMORY_THRESHOLD) {

						totalMemoryUsed = 0;
						System.gc();

						Thread.sleep(1000);
					}

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}

			);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}