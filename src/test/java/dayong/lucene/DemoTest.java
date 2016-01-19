package dayong.lucene;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.Test;


public class DemoTest {
	
	static Path path = FileSystems.getDefault().getPath("C:\\Users\\Administrator\\index", "access.log");

    
    public static Analyzer analyzer = new StandardAnalyzer();
    /**
     * 初始添加文档
     * @throws Exception
     */
    @Test
    public void indexsTest() throws Exception {
    	Directory directory = SimpleFSDirectory.open(path);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "This is the text to be indexed.";
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();
        directory.close();
    }

    /**
     * 获得IndexWriter对象
     * @return
     * @throws Exception
     */
    @Test
    public void queryTest() throws Exception {
    	Directory directory = SimpleFSDirectory.open(path);
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse("Id");
        ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
        for (int i = 0; i < hits.length; i++) {
          Document hitDoc = isearcher.doc(hits[i].doc);
          System.out.println(hitDoc.get("content"));
        }
        ireader.close();
        directory.close();
    }
    
    @Test
    public void indexDel(){
    	IndexWriterConfig config=new IndexWriterConfig(analyzer);
    	IndexWriter iw=null;
    	Directory directory=null;
    	try {
			directory = SimpleFSDirectory.open(path);
			iw=new IndexWriter(directory, config);
			iw.deleteAll();
			directory.close();
			iw.commit();
			iw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void tikaTest() throws IOException, TikaException{
		Tika tika=new Tika();
		String path1="C:\\Users\\Administrator\\Desktop\\王大勇\\服务资源目录管理系统.zip";
		InputStream is1=new FileInputStream(path1);
		System.out.println(tika.parseToString(is1));
    }

}
