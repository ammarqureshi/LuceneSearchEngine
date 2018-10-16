package com.lucene.document;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {

		String workingDir = System.getProperty("user.dir");
		String indexStoreDir = workingDir.concat("/INDEX_DIR");
		//write
		LuceneIndexWriter indexWriter = new LuceneIndexWriter(indexStoreDir);
		indexWriter.indexDocs();
		
		//read and output
		Similarity bm25Similairty= new BM25Similarity();
		Similarity booleanSimilarity = new BooleanSimilarity();
		Similarity tfidfSimilarity = new ClassicSimilarity();
		
		LuceneIndexReader indexReader = new LuceneIndexReader(indexStoreDir);
		
		System.out.println("With BM25 Similarity");
		indexReader.readDocs(bm25Similairty);
		
		System.out.println("With Boolean Similarity");
		indexReader.readDocs(booleanSimilarity);
		
		System.out.println("With TFIDF Similarity");
		indexReader.readDocs(tfidfSimilarity);
		
	}

}
