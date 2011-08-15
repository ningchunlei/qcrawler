package org.test.qcrawler.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.cyberneko.html.parsers.DOMParser;
import org.junit.Test;

import org.qcrawler.QcrawlerClient;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

public class CMain {
	
	@Test
	public void test(){
		Hello h = new Hello();
	    java.util.Map m = new java.util.HashMap();
	    m.put("h",h);
	    QcrawlerClient.exeFile(new File("qcrawler.yaml"),m);
	    System.out.println(h.getTitle());
		
		/*try{
			DOMFragmentParser d = new DOMFragmentParser();
	        HTMLDocument document = new HTMLDocumentImpl();
	        DocumentFragment fragment = document.createDocumentFragment();
	        
		    d.setProperty("http://cyberneko.org/html/properties/default-encoding","gb2312");
		    d.setFeature("http://cyberneko.org/html/features/balance-tags",false);
		    d.parse(new InputSource(new ByteArrayInputStream(getContentFromInputStream(new FileInputStream("a.txt")))),fragment);
		    print(fragment, "");
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	public static void print(Node node, String indent) {
        System.out.println(indent+node.getNodeName());
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indent+" ");
            child = child.getNextSibling();
        }
    }


	
	public static byte[] getContentFromInputStream(InputStream inputStream)
	{
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		byte [] inBuffer = new byte[1024];
		int i;
		try
		{
			while((i=inputStream.read(inBuffer))!=-1)
			{
				baos.write(inBuffer,0,i);
			}
			inBuffer = baos.toByteArray();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			inBuffer = null;
		}
		finally
		{
			try {
				baos.close();

			} catch (IOException e) {}
		}
		return inBuffer;
	}
	
}
