package com.lzh.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author LZH
 *
 */
public class Searcher {

	/**
	 * 搜索
	 * @param indexDir 索引路径
	 * @param keyword 搜索关键字
	 * @throws IOException 
	 */
	public static void serach(String indexDir,String keyword) throws IOException{
		
		// 打开索引目录
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		
		// 创建搜索的Query
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		// 创建分词器
		//Analyzer analyzer = new StandardAnalyzer();
		// 在content中搜索，创建term确定要搜索的内容，其中，第2个参数为搜索的域
		Term term = new Term("contents", keyword);
		Query query = new TermQuery(term);
		TopDocs topDocs=searcher.search(query, 1000);
	    System.out.println("共检索出 " + topDocs.totalHits + " 条记录");
	    System.out.println();
	    
	    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	    Document document = null;
        for (ScoreDoc scDoc : scoreDocs) {
        	document = searcher.doc(scDoc.doc);
        	//相似度
            float score = scDoc.score; 
            System.out.println(score);
            System.out.println(document.get("filename"));  
        }
       
	}
	
	public static void main(String[] args) {
		try {
			Searcher.serach("E:\\lucene\\index", "hello");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
