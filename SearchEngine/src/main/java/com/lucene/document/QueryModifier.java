package com.lucene.document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class QueryModifier {

	public static void main(String[] args) throws IOException {
		String text = "Hello world";
		BufferedWriter output = null;
		int counter = 1;
		try {
			File qryFile = new File("/Users/ammarqureshi/Documents/IR/modified_cran.qry");
			output = new BufferedWriter(new FileWriter(qryFile));

			File cranFile = new File("/Users/ammarqureshi/Documents/IR/cran/cran.qry");
			Scanner sc = new Scanner(cranFile);
			String line;

			while(sc.hasNextLine()) {
				line = sc.nextLine();
				//find .I and append counter to it
				if(line.matches("^.I\\s\\d+$")) {
					output.write(".I " + counter);
					counter++;
				}
				else {
					output.write(line);
				}
				output.newLine();

			}

		} catch ( IOException e ) {
			e.printStackTrace();
		} finally {
			if ( output != null ) {
				output.close();
			}
		}
		System.out.println("written");
	}
}

