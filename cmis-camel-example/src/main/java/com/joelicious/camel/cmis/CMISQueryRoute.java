package com.joelicious.camel.cmis;

import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;

public class CMISQueryRoute extends RouteBuilder {

	static Logger log = Logger.getLogger(CMISQueryRoute.class.getName());

	public void configure() throws Exception {

		from("direct:query")
				.process(new CMISQueryProcessor())
				.to("cmis://http://localhost:9080/cmis/resources?username=cmis_user&password=cmis_password&queryMode=true")
				.process(new CMISQueryResultProcessor());

	}

}
