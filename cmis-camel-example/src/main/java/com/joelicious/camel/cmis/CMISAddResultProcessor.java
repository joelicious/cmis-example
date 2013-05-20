package com.joelicious.camel.cmis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public class CMISAddResultProcessor implements Processor {

	static Logger log = Logger
			.getLogger(CMISAddResultProcessor.class.getName());

	public void process(Exchange exchange) throws Exception {

		String newNodeId = exchange.getIn().getBody(String.class);

		log.info("The newNodeId is: " + newNodeId);

		exchange.getOut().setBody(newNodeId);

	}

}
