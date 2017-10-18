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
	 * 写入索引的类
	 */
	private IndexWriter writer;
    /**
     * FileFilter的实现类，用来过滤符合条件的文档。
     * @author Administrator
     *
     */
    private static class TextFilesFilter implements FileFilter {
    	//重构
    	@Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }
    }
    /**
     * 构造方法，用来传入索引存放路径
     * @param indexdirectory
     * @throws IOException
     */
    public Index(String indexdirectory) throws IOException {
    	// 打开目录
        Directory directory = FSDirectory.open(Paths.get(indexdirectory));
        //索引
        IndexWriterConfig config=new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        writer=new IndexWriter(directory,config);
    }
    /**
     * 关闭indexWriter,不要忘记了
     * @throws IOException
     */
    public void close() throws IOException{
        writer.close();
    }
    /**
     * 遍历文件夹下所有文件，选择符合条件文件，写入索引的方法
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
      //返回写入的文档总数
        return writer.numDocs();
    }
    /**
     * 写入索引的方法，将生成的Document（目录）对象写入到索引中
     * @param file
     * @throws IOException
     */
    private void indexFile(File file) throws IOException{
        System.out.println("indexing..."+file.getCanonicalPath());
        Document doc=getDocument(file);
        writer.addDocument(doc);
    }
    /**
     * 生成Document对象的方法，Document对象就是对文档各个属性的封装
     * @param file
     * @return
     * @throws IOException
     */
    protected Document getDocument(File file) throws IOException{
        Document doc=new Document();
      //分析但不存储
        doc.add(new Field("contents",new FileReader(file), TextField.TYPE_NOT_STORED));
      //存储并分词
        doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
      //存储并分词
        doc.add(new Field("fullpath",file.getCanonicalPath(),TextField.TYPE_STORED));
        return doc;
    }

    public static void main(String[] args) throws IOException {
    	//目录，里边可以没有内容
        String indexDir="E:\\lucene\\index";
        //文件，里边要有.java文件
        String dataDir="E:\\lucene\\example";
        //当前时间
        long start=System.currentTimeMillis();
        Index index =new Index(indexDir);
        //写入索引
        int numberIndexed= index.index(dataDir, new TextFilesFilter());
        //关闭，这个是需要的
        index.close();
        long end=System.currentTimeMillis();
        System.out.println(numberIndexed);
        //索引时间
        System.out.println(end-start);
    }
	
}
