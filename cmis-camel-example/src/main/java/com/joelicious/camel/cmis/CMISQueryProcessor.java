package com.joelicious.camel.cmis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public class CMISQueryProcessor implements Processor {

	static Logger log = Logger.getLogger(CMISQueryProcessor.class.getName());

	public void process(Exchange exchange) throws Exception {

		log.info("Processing CMISQueryProcessor Exchange");

	}
}
