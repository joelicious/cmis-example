package com.joelicious.camel.cmis;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cmis.CamelCMISConstants;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.log4j.Logger;

public class CMISAddProcessor implements Processor {

	static Logger log = Logger.getLogger(CMISAddProcessor.class.getName());

	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {

		List<String> params = exchange.getIn().getBody(List.class);

		String fileName = params.get(0);
		String content = params.get(1);
		String folderPath = params.get(2);

		exchange.getIn().setBody(content);

		exchange.getIn()
				.getHeaders()
				.put(PropertyIds.CONTENT_STREAM_MIME_TYPE,
						"text/plain; charset=UTF-8");

		exchange.getIn().getHeaders().put(PropertyIds.NAME, fileName);

		exchange.getIn().getHeaders()
				.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");

		exchange.getIn().getHeaders()
				.put(CamelCMISConstants.CMIS_FOLDER_PATH, folderPath);

	}

}
