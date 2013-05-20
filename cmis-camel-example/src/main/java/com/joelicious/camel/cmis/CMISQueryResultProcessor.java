package com.joelicious.camel.cmis;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public class CMISQueryResultProcessor implements Processor {

	static Logger log = Logger.getLogger(CMISQueryResultProcessor.class
			.getName());

	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {

		log.info("Processing CMISQueryResultProcessor Exchange");

		List<Map<String, Object>> documents = exchange.getIn().getBody(
				List.class);

		log.info("The document size is : " + documents.size());

		for (Map<String, Object> m : documents) {

			log.info("cmis:name : " + m.get("cmis:name"));

		}

	}

}
