package com.lucene.document;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.CharArraySet;

public class LuceneIndexReader 
{
	private String index_dir;

	public LuceneIndexReader(String index_dir) {
		this.index_dir = index_dir;
	}

	//private static final String INDEX_DIR = "/Users/ammarqureshi/Documents/IR/INDEX_DIR";

	public void readDocs(Similarity similarity) throws IOException, ParseException
	{
		IndexSearcher searcher = createSearcher();
		searcher.setSimilarity(similarity);

		ArrayList<String> queryList = CranQueries.extractCranQueries("cran.qry");
		ArrayList<ArrayList<String[]>> queryResult = new ArrayList<>();
		int queryIx=1;

		for(String query:queryList) {

			//	QueryParser qp = new QueryParser("words", new EnglishAnalyzer(CharArraySet.EMPTY_SET));

			//			QueryParser qp = new QueryParser("words",new EnglishAnalyzer(new CharArraySet(Arrays.asList(
			//				"?" , "a", "an", "and", "are", "as", "at", "be", "but", "by",
			//			      "for", "if", "in", "into", "is", "it",
			//			      "no", "not", "of", "on", "or", "such",
			//			      "that", "the", "their", "then", "there", "these",
			//			      "they", "this", "to", "was", "will", "with"),false)));

			QueryParser qp = new QueryParser("words",new EnglishAnalyzer(new CharArraySet(Arrays.asList(
					"a", "an", "and", "are", "as", "at", "be", "but", "by",
					"for", "if", "in", "into", "is", "it",
					"no", "not", "of", "on", "or", "such",
					"that", "the", "their", "then", "there", "these",
					"they", "this", "to", "was", "will", "with"),false)));

			//			QueryParser qp = new QueryParser("words",new EnglishAnalyzer(new CharArraySet(Arrays.asList(
			//			"the","of","and","to","in","is","for","ar","with","on","by","that",
			//			"an","at","flow","be","result","thi","as","from","it","which","number",
			//			"pressur","effect","us","present","boundari","obtain","theori",    "a", "an", "and", "are", "as", "at", "be", "but", "by",
			//		      "for", "if", "in", "into", "is", "it",
			//		      "no", "not", "of", "on", "or", "such",
			//		      "that", "the", "their", "then", "there", "these",
			//		      "they", "this", "to", "was", "will", "with"),false)));


			//	QueryParser qp = new QueryParser("words", new EnglishAnalyzer(CharArraySet.EMPTY_SET));

			qp.setAllowLeadingWildcard(true);
			//		qp.setAnalyzer(new EnglishAnalyzer(new CharArraySet(Arrays.asList("the","of","and","to","in","is","for","ar" ),false)));
			//	qp.setAnalyzer( new EnglishAnalyzer());


			//	qp.setAnalyzer(new StandardAnalyzer());
			//	qp.setAnalyzer(new EnglishAnalyzer(CharArraySet.EMPTY_SET));

			Query wordQuery = qp.parse(query);
			TopDocs foundDocs = searcher.search(wordQuery, 50);
			ArrayList<String[]> tempQueryList = new ArrayList<>();
		
			//System.out.println("\n\nTotal Results for query " + queryIx +": " + foundDocs.totalHits);

			int ranking = 1;
			for (ScoreDoc sd : foundDocs.scoreDocs) 
			{
				Document d = searcher.doc(sd.doc);
				//		System.out.println("\nQuery:" + queryIx + ": " + query);

				//create info about the retrieved document
				String[] info = {
						Integer.toString(queryIx), 
						"0", 
						String.format(d.get("id")), 
						Integer.toString(ranking), 
						Float.toString(sd.score),
						"0"
				};
				tempQueryList.add(info);

				//		System.out.println("Id:" + String.format(d.get("id")));
				//		System.out.println("\nTitle:" + String.format(d.get("title")));
				//		System.out.println("\nAuthors:" + String.format(d.get("authors")));
				//			System.out.println("\nBibliography:" + String.format(d.get("bibliography")));
				//			System.out.println("\nWords:"+ String.format(d.get("words")));
				//		System.out.println("\nScore: " + sd.score);
				//	System.out.println("\nExplain: " + searcher.explain(wordQuery, sd.doc) );


				//get next highest ranked retrieved document
				ranking++;
			}
			queryResult.add(tempQueryList);

			queryIx++;
		}

		//write system response to file
		//	File file = new File("/Users/ammarqur/systemFile.txt");
		String workingDir = System.getProperty("user.dir");
		//	System.out.println("user dir: " + user_dir);

		String systemFilePath = workingDir.concat("/systemFile.txt");

		if(similarity.getClass() == BM25Similarity.class) {
			System.out.println("this is bm25 similarity");
			new File(workingDir.concat("bm25similarity")).mkdirs();
			systemFilePath = workingDir.concat("bm25similarity/systemFile.txt");
		}
		
		else if(similarity.getClass() == BooleanSimilarity.class) {
			System.out.println("this is boolean similarity");
			new File(workingDir.concat("booleansimilarity")).mkdirs();
			systemFilePath = workingDir.concat("booleansimilarity/systemFile.txt");
		}	
		else if(similarity.getClass() == ClassicSimilarity.class) {
			System.out.println("this is TFIDF similarity");
			new File(workingDir.concat("tfidfsimilarity")).mkdirs();
			systemFilePath = workingDir.concat("tfidfsimilarity/systemFile.txt");
		}
		
		
		File file = new File(systemFilePath);
		
		BufferedWriter output = null;
		output = new BufferedWriter(new FileWriter(file));

		for(ArrayList<String[]> res: queryResult) {

			for(String[] info: res) {
				for(String temp: info) {
					output.write(temp + "\t");
				}
				output.newLine();
			}
		}
		output.close();
		System.out.println("written to system file @ " + systemFilePath);
	}


	private IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(index_dir));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
