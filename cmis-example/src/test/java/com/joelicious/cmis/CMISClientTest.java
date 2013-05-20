package com.joelicious.cmis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.joelicious.cmis.CMISConnection;

public class CMISClientTest {

	static Logger log = Logger.getLogger(CMISConnection.class.getName());

	protected static final String CMIS_ENDPOINT_TEST_SERVER = "http://localhost:9090/chemistry-opencmis-server-inmemory/atom";
	protected static final String OPEN_CMIS_SERVER_WAR_PATH = "target/dependency/chemistry-opencmis-server-inmemory-0.8.0.war";

	protected static Server cmisServer;

	@Test
	public void testConnection() {

		log.info("The repository name is: "
				+ CMISConnection.instance().getRepositoryName());

		CMISConnection.instance().putAStringAsAFile("folder-one", "joe.txt",
				"joe is cool");

		CMISConnection.instance().query(
				"SELECT * FROM cmis:folder where cmis:name IN ('folder-one')");
		CMISConnection.instance().query(
				"SELECT * FROM cmis:document where cmis:name IN ('joe.txt')");

	}

	protected Session createSession() {

		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.ATOMPUB_URL, CMIS_ENDPOINT_TEST_SERVER);
		parameter.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());

		Repository repository = sessionFactory.getRepositories(parameter)
				.get(0);

		return repository.createSession();
	}

	protected void deleteAllContent() {

		log.info("Deleting all content");

		Session session = createSession();
		Folder rootFolder = session.getRootFolder();
		ItemIterable<CmisObject> children = rootFolder.getChildren();
		for (CmisObject cmisObject : children) {
			if ("cmis:folder".equals(cmisObject
					.getPropertyValue(PropertyIds.OBJECT_TYPE_ID))) {
				List<String> notDeltedIdList = ((Folder) cmisObject)
						.deleteTree(true, UnfileObject.DELETE, true);
				if (notDeltedIdList != null && notDeltedIdList.size() > 0) {
					throw new RuntimeException("Cannot empty repo");
				}
			} else {
				cmisObject.delete(true);
			}
		}
		session.getBinding().close();

	}

	@BeforeClass
	public static void startServer() throws Exception {

		log.info("Creating a Jetty Server");
		cmisServer = new Server(9090);
		cmisServer.setHandler(new WebAppContext(OPEN_CMIS_SERVER_WAR_PATH,
				"/chemistry-opencmis-server-inmemory"));

		log.info("Starting the Jetty Server");
		cmisServer.start();

	}

	@AfterClass
	public static void stopServer() throws Exception {

		log.info("Stopping the Jetty Server");
		cmisServer.stop();
	}

	@Before
	public void setUp() throws Exception {
		deleteAllContent();
	}

}
