package com.lucene.document;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.NormalizationZ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndexWriter 
{
	private String index_dir;

	public LuceneIndexWriter(String index_dir) {
		this.index_dir = index_dir;

	}

	public void indexDocs() throws IOException {

		IndexWriter writer = createWriter();
		ArrayList<CranfieldDocument> parsedDocs = CranfieldParser.parseCranfieldDocs();
		List<Document> documents = new ArrayList<>();

		Document tempDoc = new Document();
		for(CranfieldDocument parsedDoc:parsedDocs) {
		//	System.out.println("\n\n" + parsedDoc.getId() + " was added");
			tempDoc = createDocument(parsedDoc.getId(), parsedDoc.getTitle(), parsedDoc.getAuthors(), parsedDoc.getBibliog(), parsedDoc.getWords());
			documents.add(tempDoc);
		}
		

		//clean everything 
		writer.deleteAll();
		//add the documents
		writer.addDocuments(documents);
		System.out.println(documents.size() + " documents indexed");
		writer.commit();
		writer.close();
	}

	public Document createDocument(String id, String title, String authors, String bibliog, String words) {
		Document document = new Document();
		document.add(new StringField("id", id, Field.Store.YES));
		document.add(new TextField("title", title, Field.Store.YES) );
		document.add(new TextField("authors", authors, Field.Store.YES) );
		document.add(new TextField("bibliography", bibliog, Field.Store.YES) );
		document.add(new TextField("words", words, Field.Store.YES) );
		return document;
	}

	private IndexWriter createWriter() throws IOException 
	{
		FSDirectory dir = FSDirectory.open(Paths.get(index_dir));
		//IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer(CharArraySet.EMPTY_SET));

		IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer(CharArraySet.EMPTY_SET));

		//		IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer(new CharArraySet(Arrays.asList(
		//				"?" , "a", "an", "and", "are", "as", "at", "be", "but", "by",
		//			      "for", "if", "in", "into", "is", "it",
		//			      "no", "not", "of", "on", "or", "such",
		//			      "that", "the", "their", "then", "there", "these",
		//			      "they", "this", "to", "was", "will", "with"),false)));

		//config.setSimilarity(new BM25Similarity());
		//	IndexWriterConfig config = new IndexWriterConfig(new SnowballAnalyzer());

		IndexWriter writer = new IndexWriter(dir, config);
		return writer;
	}
}
