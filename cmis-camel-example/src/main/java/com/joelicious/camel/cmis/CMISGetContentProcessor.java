package com.joelicious.camel.cmis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cmis.CamelCMISConstants;
import org.apache.log4j.Logger;

public class CMISGetContentProcessor implements Processor {

	static Logger log = Logger.getLogger(CMISGetContentProcessor.class
			.getName());

	public void process(Exchange exchange) throws Exception {

		Object objectId = exchange.getIn().getBody();
		String objectStr = (String) objectId;

		String theQuery = "SELECT * FROM cmis:document where cmis:objectId IN ('"
				+ objectStr + "')";

		log.info("Here is the Content Query: " + theQuery);

		exchange.getIn().setBody(theQuery);

		exchange.getIn().getHeaders()
				.put(CamelCMISConstants.CAMEL_CMIS_RETRIEVE_CONTENT, true);

	}
}