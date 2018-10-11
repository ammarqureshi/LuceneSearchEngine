package com.lucene.document;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		String workingDir = System.getProperty("user.dir");
		String indexStoreDir = workingDir.concat("/INDEX_DIR");
		//write
		LuceneIndexWriter indexWriter = new LuceneIndexWriter(indexStoreDir);
		indexWriter.indexDocs();
		//read and output
		LuceneIndexReader indexReader = new LuceneIndexReader(indexStoreDir);
		indexReader.readDocs();
		
	}

}
