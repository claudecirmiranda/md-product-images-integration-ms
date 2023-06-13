package br.com.nagem.route;

import org.apache.camel.BeanInject;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import br.com.nagem.transformations.FromProductImagesIntegration;

@Component
public class ImagesRoute extends RouteBuilder {

	@BeanInject
	private FromProductImagesIntegration fromProductImagesIntegration;

	@Override
	public void configure() throws Exception {

		//from("quartz://myjob?cron=0+0+*+*+*+?")
		//	.process(fromProductImagesIntegration)
		//	.log("Finally Integrator Products Images.");

	}

}
