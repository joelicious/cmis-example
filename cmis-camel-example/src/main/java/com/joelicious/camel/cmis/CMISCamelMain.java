package com.joelicious.camel.cmis;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;

public class CMISCamelMain {

	static Logger log = Logger.getLogger(CMISQueryProcessor.class.getName());

	public static void main(String args[]) throws Exception {

		log.info("Starting Application");
		CamelContext camel = new DefaultCamelContext();
		CMISQueryRoute queryRoute = new CMISQueryRoute();
		CMISAddRoute addRoute = new CMISAddRoute();
		CMISGetContentRoute contentRoute = new CMISGetContentRoute();

		camel.addRoutes(queryRoute);
		camel.addRoutes(addRoute);
		camel.addRoutes(contentRoute);

		log.info("Starting Camel Route");
		camel.start();

		ProducerTemplate producer = camel.createProducerTemplate();

		// Test the query
		producer.sendBody("direct:query",
				"SELECT * FROM cmis:folder where cmis:name IN ('folder-one')");

		// Test an add file
		List<String> params = new ArrayList<String>();
		params.add("camel-joe.file");
		params.add("Joe is the coolest person ever");
		params.add("/folder-one");
		Object addResultObject = producer.requestBody("direct:add", params);

		String newNodeId = (String) addResultObject;
		log.info("The new node id is: " + newNodeId);

		Object getContentObject = producer.requestBody("direct:getContent",
				newNodeId);

		String theContent = (String) getContentObject;
		log.info("The content for " + newNodeId + " is: " + theContent);

		Thread.sleep(5000);

		camel.stop();

	}
}
