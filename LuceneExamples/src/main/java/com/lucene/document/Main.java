package com.lucene.document;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		//write
		LuceneIndexWriter indexWriter = new LuceneIndexWriter("/Users/ammarqureshi/Documents/IR/INDEX_DIR");
		indexWriter.indexDocs();
		//read and output
		LuceneIndexReader indexReader = new LuceneIndexReader("/Users/ammarqureshi/Documents/IR/INDEX_DIR");
		indexReader.readDocs();
		
	}

}
