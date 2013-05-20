package com.joelicious.camel.cmis;

import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;

public class CMISAddRoute extends RouteBuilder {

	static Logger log = Logger.getLogger(CMISAddRoute.class.getName());

	public void configure() throws Exception {

		from("direct:add")
				.process(new CMISAddProcessor())
				.to("cmis://http://localhost:9080/cmis/resources?username=cmis_user&password=cmis_password")
				.process(new CMISAddResultProcessor());

	}
}
