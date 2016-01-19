package dayong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class ConverterUtil {

	public static Document toDoc(String filePath) {
		try {
			Document doc = new Document();
			Tika tika=new Tika();
			
			File file=new File(filePath);
			if(file.isDirectory())
				return null;
			doc.add(new Field("name", file.getName(), TextField.TYPE_STORED));
			doc.add(new Field("date", DateTools.dateToString(new Date(), DateTools.Resolution.SECOND), TextField.TYPE_STORED));
			doc.add(new Field("content", tika.parseToString(new FileInputStream(filePath)), TextField.TYPE_STORED));
			doc.add(new Field("path", file.getAbsolutePath(), TextField.TYPE_STORED));
			return doc;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (TikaException e) {
			e.printStackTrace();
			return null;
		}
	}

}
