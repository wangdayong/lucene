package dayong.lucene.index.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import dayong.utils.ConverterUtil;


public class IndexDao {
	
	private Analyzer analyzer;
	
	private Directory directory;
	
	private IndexDao(String path){
		Path filePath= FileSystems.getDefault().getPath(path, "access.log");
		this.setAnalyzer(new StandardAnalyzer());
    	try {
			this.setDirectory(SimpleFSDirectory.open(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 建立索引的函数
	 * @param path
	 * @return
	 */
	public boolean indexFile(String filePath){
		try {
			Tika tika=new Tika();
			InputStream is=new FileInputStream(filePath);
			System.out.println(tika.parseToString(is));
			
	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        IndexWriter iwriter = new IndexWriter(directory, config);
	        Document doc =ConverterUtil.toDoc(filePath);
	        if(doc==null){
	        	iwriter.close();
	        	directory.close();
	        	return false;
	        }
	        iwriter.addDocument(doc);
        	iwriter.close();
        	directory.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (TikaException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	
	public static void main(String[] args) {
		String indexPath="C:\\Users\\Administrator\\index";
		IndexDao indexDao=new IndexDao(indexPath);
		String path="C:\\Users\\Administrator\\Desktop\\新人文档\\Id映射一对一文档.docx";
		indexDao.indexFile(path);
	}

}
