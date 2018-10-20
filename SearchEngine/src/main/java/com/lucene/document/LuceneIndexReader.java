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

	public void readDocs(Similarity similarity) throws IOException, ParseException
	{
		IndexSearcher searcher = createSearcher();
		searcher.setSimilarity(similarity);

		ArrayList<String> queryList = CranQueries.extractCranQueries("cran.qry");
		ArrayList<ArrayList<String[]>> queryResult = new ArrayList<>();
		int queryIx=1;

		for(String query:queryList) {

			QueryParser qp = new QueryParser("words", new EnglishAnalyzer());
			qp.setAllowLeadingWildcard(true);
			Query wordQuery = qp.parse(query);
			TopDocs foundDocs = searcher.search(wordQuery, 50);
			ArrayList<String[]> tempQueryList = new ArrayList<>();
			int ranking = 1;

			for (ScoreDoc sd : foundDocs.scoreDocs) 
			{
				Document d = searcher.doc(sd.doc);
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
				//get next highest ranked retrieved document
				ranking++;
			}
			queryResult.add(tempQueryList);

			queryIx++;
		}

		//write system response to file
		String workingDir = System.getProperty("user.dir");
		String systemFilePath = workingDir.concat("/systemFile.txt");

		//make a new directory to store results from different retrieval models
		String similarityDirPath = workingDir.concat("_simiarlityFiles");
		new File(similarityDirPath).mkdirs();

		if(similarity.getClass() == BM25Similarity.class) {
			systemFilePath = (similarityDirPath.concat("/bm25_systemFile.txt"));
		}

		else if(similarity.getClass() == BooleanSimilarity.class) {
			systemFilePath = (similarityDirPath.concat("/boolean_systemFile.txt"));
		}	
		else if(similarity.getClass() == ClassicSimilarity.class) {
			systemFilePath = (similarityDirPath.concat("/tfidf_systemFile.txt"));
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
