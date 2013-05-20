package com.joelicious.cmis;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.log4j.Logger;

/**
 * A class that 
 * 
 * @author joelicious
 *
 */
public class CMISConnection {

	private Session session;

	static private CMISConnection _instance = null;

	static Logger log = Logger.getLogger(CMISConnection.class.getName());

	static public CMISConnection instance() {
		if (_instance == null) {
			_instance = new CMISConnection();
		}
		return _instance;
	}

	protected CMISConnection() {

		log.info("Initializing the CMIS Connection");

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());
		parameters.put(SessionParameter.ATOMPUB_URL, CMISProperties.instance()
				.getAtomPubUrl());

		if (CMISProperties.instance().getUser() != "") {
			parameters.put(SessionParameter.USER, CMISProperties.instance()
					.getUser());
		}

		if (CMISProperties.instance().getPassword() != "") {
			parameters.put(SessionParameter.PASSWORD, CMISProperties.instance()
					.getPassword());
		}

		log.info("Creating a session");

		// create the session

		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Repository repository = sessionFactory.getRepositories(parameters).get(
				0);

		session = repository.createSession();

		log.info("Session is started");

	}

	public String getRepositoryName() {

		// get repository info
		RepositoryInfo repInfo = session.getRepositoryInfo();
		log.info("Repository name: " + repInfo.getName());
		return repInfo.getName();

	}

	// Only supporting one level deep directory for now.
	public void putAStringAsAFile(String directory, String fileName,
			String fileContent) {

		log.info("Creating the " + fileName + " document with the content "
				+ fileContent);

		// Create a simple text document in the new folder
		// First, create the content stream
		final String textFileName = fileName;
		String mimetype = "text/plain; charset=UTF-8";
		String content = fileContent;
		String filename = textFileName;

		byte[] buf = null;
		try {
			buf = content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream input = new ByteArrayInputStream(buf);
		ContentStream contentStream = session.getObjectFactory()
				.createContentStream(filename, buf.length, mimetype, input);

		Folder foundFolder = null;

		Folder root = session.getRootFolder();
		ItemIterable<CmisObject> children = root.getChildren();
		for (CmisObject o : children) {
			if (o.getName().equalsIgnoreCase(directory)) {
				foundFolder = (Folder) o;
			}
		}

		// If folder wasn't found, create it
		if (foundFolder == null) {

			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, directory);

			try {

				foundFolder = root.createFolder(newFolderProps);

			} catch (CmisConstraintException cmisException) {

				log.warn("Exception: " + cmisException.toString());
				cmisException.printStackTrace();

			}

		}

		ObjectId id = null;

		// Create the Document Object
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID,
				ObjectType.DOCUMENT_BASETYPE_ID);
		properties.put(PropertyIds.NAME, filename);

		try {
			id = foundFolder.createDocument(properties, contentStream,
					VersioningState.NONE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("The id of the new file is: " + id.getId());

	}

	public void query(String theQuery) {

		ItemIterable<QueryResult> q = session.query(theQuery, false);
		log.info("***results from query " + theQuery);

		int i = 1;
		for (QueryResult qr : q) {

			log.info("--------------------------------------------\n"
					+ i
					+ " , "
					+ qr.getPropertyByQueryName("cmis:objectTypeId")
							.getFirstValue()
					+ " , "
					+ qr.getPropertyByQueryName("cmis:name").getFirstValue()
					+ " , "
					+ qr.getPropertyByQueryName("cmis:createdBy")
							.getFirstValue()
					+ " , "
					+ qr.getPropertyByQueryName("cmis:objectId")
							.getFirstValue());

			i++;
		}

	}

}
