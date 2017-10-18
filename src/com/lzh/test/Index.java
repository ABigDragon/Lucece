package com.lzh.test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author LZH
 *
 */
public class Index {

	/**
	 * д����������
	 */
	private IndexWriter writer;
    /**
     * FileFilter��ʵ���࣬�������˷����������ĵ���
     * @author Administrator
     *
     */
    private static class TextFilesFilter implements FileFilter {
    	//�ع�
    	@Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }
    }
    /**
     * ���췽�������������������·��
     * @param indexdirectory
     * @throws IOException
     */
    public Index(String indexdirectory) throws IOException {
    	// ��Ŀ¼
        Directory directory = FSDirectory.open(Paths.get(indexdirectory));
        //����
        IndexWriterConfig config=new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        writer=new IndexWriter(directory,config);
    }
    /**
     * �ر�indexWriter,��Ҫ������
     * @throws IOException
     */
    public void close() throws IOException{
        writer.close();
    }
    /**
     * �����ļ����������ļ���ѡ����������ļ���д�������ķ���
     * @param dataDir
     * @param filter
     * @return
     * @throws IOException
     */
    public int index(String dataDir,FileFilter filter) throws IOException{
        File[] files=new File(dataDir).listFiles();
        for(File file:files){
            if(!file.isDirectory() && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && (filter==null) || filter.accept(file)){
                indexFile(file);
            }
        }
      //����д����ĵ�����
        return writer.numDocs();
    }
    /**
     * д�������ķ����������ɵ�Document��Ŀ¼������д�뵽������
     * @param file
     * @throws IOException
     */
    private void indexFile(File file) throws IOException{
        System.out.println("indexing..."+file.getCanonicalPath());
        Document doc=getDocument(file);
        writer.addDocument(doc);
    }
    /**
     * ����Document����ķ�����Document������Ƕ��ĵ��������Եķ�װ
     * @param file
     * @return
     * @throws IOException
     */
    protected Document getDocument(File file) throws IOException{
        Document doc=new Document();
      //���������洢
        doc.add(new Field("contents",new FileReader(file), TextField.TYPE_NOT_STORED));
      //�洢���ִ�
        doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
      //�洢���ִ�
        doc.add(new Field("fullpath",file.getCanonicalPath(),TextField.TYPE_STORED));
        return doc;
    }

    public static void main(String[] args) throws IOException {
    	//Ŀ¼����߿���û������
        String indexDir="E:\\lucene\\index";
        //�ļ������Ҫ��.java�ļ�
        String dataDir="E:\\lucene\\example";
        //��ǰʱ��
        long start=System.currentTimeMillis();
        Index index =new Index(indexDir);
        //д������
        int numberIndexed= index.index(dataDir, new TextFilesFilter());
        //�رգ��������Ҫ��
        index.close();
        long end=System.currentTimeMillis();
        System.out.println(numberIndexed);
        //����ʱ��
        System.out.println(end-start);
    }
	
}
