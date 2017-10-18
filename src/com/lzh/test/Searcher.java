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
	 * ����
	 * @param indexDir ����·��
	 * @param keyword �����ؼ���
	 * @throws IOException 
	 */
	public static void serach(String indexDir,String keyword) throws IOException{
		
		// ������Ŀ¼
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		
		// ����������Query
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
		// �����ִ���
		//Analyzer analyzer = new StandardAnalyzer();
		// ��content������������termȷ��Ҫ���������ݣ����У���2������Ϊ��������
		Term term = new Term("contents", keyword);
		Query query = new TermQuery(term);
		TopDocs topDocs=searcher.search(query, 1000);
	    System.out.println("�������� " + topDocs.totalHits + " ����¼");
	    System.out.println();
	    
	    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	    Document document = null;
        for (ScoreDoc scDoc : scoreDocs) {
        	document = searcher.doc(scDoc.doc);
        	//���ƶ�
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
