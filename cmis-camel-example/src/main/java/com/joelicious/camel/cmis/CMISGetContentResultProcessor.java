package com.joelicious.camel.cmis;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public class CMISGetContentResultProcessor implements Processor {

	static Logger log = Logger.getLogger(CMISGetContentResultProcessor.class
			.getName());

	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {

		log.info("Processing CMISQueryResultProcessor Exchange");

		List<Map<String, Object>> documents = exchange.getIn().getBody(
				List.class);

		log.info("The document size is : " + documents.size());

		String documentContent = null;

		for (Map<String, Object> m : documents) {

			// Set<String> keys = m.keySet();
			// for (String s : keys) {
			// log.info("Here are the keys: " + s);
			// }

			Object obj = m.get("CamelCMISContent");
			InputStream is = (InputStream) obj;

			documentContent = convertStreamToString(is);
		}

		exchange.getOut().setBody(documentContent);

	}

	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}