package com.lucene.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class CranQueries {


	public static void main(String...args) throws FileNotFoundException {

		ArrayList<String> queryList = CranQueries.extractCranQueries("/Users/ammarqureshi/Documents/IR/modified_cran.qry");
		System.out.println("finsihed");
		int i=1;
		for(String query:queryList) {
			System.out.println("\n\nQuery" +i + ": " + query);	
			i++;
		}
	}

	public static ArrayList<String> extractCranQueries(String fileName) throws FileNotFoundException{

		//File file= new File(filepath);
		Scanner sc = new Scanner(CranQueries.class.getResourceAsStream(fileName));
		String line;
		String accQuery = new String();
		String currTag = new String();
		ArrayList<String> queryList = new ArrayList<>();

		while(sc.hasNextLine()) {

			line = sc.nextLine();
			//System.out.println("line:" + line);
			if(line.matches("^.I\\s\\d+$")) {
				//add accumulated query to list
				if(currTag.equals(".W")) {
					queryList.add(accQuery);
				}
				currTag = ".I";
				accQuery = new String();
			}

			else if(line.equals(".W")) {
				accQuery = new String();
				currTag = ".W";
			}

			else {
				accQuery += " " + line;
			}

		}
		queryList.add(accQuery);


		sc.close();

		return queryList;

	}

}
