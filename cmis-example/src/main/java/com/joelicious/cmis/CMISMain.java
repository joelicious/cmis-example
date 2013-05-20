package com.joelicious.cmis;

public class CMISMain {

	public static void main(String[] args) {

		CMISConnection.instance().getRepositoryName();
		CMISConnection.instance().putAStringAsAFile("folder-one", "joe.txt", "joe is cool");
		CMISConnection.instance().query("SELECT * FROM cmis:folder where cmis:name IN ('folder-one')");
		CMISConnection.instance().query("SELECT * FROM cmis:document where cmis:name IN ('joe.txt')");

	}

}
