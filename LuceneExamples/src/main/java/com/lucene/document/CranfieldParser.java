package com.lucene.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CranfieldParser {
	static CranfieldDocument doc =  new CranfieldDocument();
	static ArrayList<CranfieldDocument> docs = new ArrayList<>();


	public static ArrayList<CranfieldDocument> parseCranfieldDocs(String fileName) throws FileNotFoundException{
		File file = new File("/Users/ammarqureshi/Documents/IR/cran/cran.all.1400");
		//URL url = getClass().getResource(fileLocation);
		//URL path =  CranfieldParser.class.getResource(fileName);
		//File file = new File(path.getFile());
	//	File file= new File(fileLocation);
		//File file = new File("/Users/ammarqureshi/Documents/cranfieldTest.txt");
		Scanner sc = new Scanner(file);
		String line;
		String acc = new String();
		String currTag = null;

		while(sc.hasNextLine()) {
			line = sc.nextLine();
			line = line.trim();

			if(line.matches("^.I\\s\\d+$")) {

				if(currTag!=null) {
					addField(currTag, acc);
					docs.add(doc);
				}
				doc = new CranfieldDocument();
				String[] temp = line.split("\\s+");
				currTag = temp[0];
				acc = temp[1];
			}
			else if(line.equals(".T")) {
				addField(currTag, acc);
				currTag = ".T";
				acc = new String();
			}
			else if(line.equals(".A")) {
				addField(currTag, acc);
				currTag = ".A";
				acc = new String();

			}
			else if(line.equals(".B")) {
				addField(currTag, acc);
				acc = new String();
				currTag = ".B";
			}
			else if(line.equals(".W")) {
				addField(currTag, acc);
				acc = new String();
				currTag = ".W";
			}
			else {
				acc += " "+ line;
			}

		}
		addField(currTag, acc);
		docs.add(doc);
		sc.close();


		return docs;

	}

	private static void addField(String currTag, String line) {

		//	System.out.println("adding field: " + currTag + "content: " + line);
		switch(currTag) {

		case ".I":
			doc.setId(line);
			break;
		case ".A":
			doc.setAuthors(line);
			break;
		case ".B":
			doc.setBibliog(line);
			break;
		case ".W":
			doc.setWords(line);
			break;
		case ".T":
			doc.setTitle(line);
		}
	}

}
