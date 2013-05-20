package com.joelicious.cmis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CMISProperties {

	static private CMISProperties _instance = null;

	static Logger log = Logger.getLogger(CMISProperties.class.getName());

	private Properties props = null;
	private String user = null;
	private String password = null;
	private String atompubUrl = null;

	protected CMISProperties() {
		
		props = new Properties();

		try {

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cmis.properties");

			props.load(inputStream);

		} catch (FileNotFoundException e) {
			System.out.println("cmis.properties Not Found: " + e);
		} catch (IOException e) {
			System.out.println("IO Exception: " + e);
		}

	}

	static public CMISProperties instance() {
		if (_instance == null) {
			_instance = new CMISProperties();
		}

		return _instance;
	}

	public String getUser() {

		this.user = props.getProperty("user");
		if (user == null) {
			user = "";
		}
		return user;
	}

	public String getPassword() {

		this.password = props.getProperty("password");
		if (password == null) {
			password = "";
		}
		return password;
	}

	public String getAtomPubUrl() {
		atompubUrl = props.getProperty("atompubUrl");
		if (atompubUrl == null) {
			log.warn("atompubUrl must have a value");
			System.exit(0);
		}
		return atompubUrl;
	}
}
