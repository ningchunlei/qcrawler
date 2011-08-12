package org.test.qcrawler.http
import org.junit.Test
import org.qcrawler.http._
import org.cyberneko.html.parsers.DOMParser
import javax.script.ScriptEngineManager
import java.io.FileReader
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import org.w3c.dom.Node
import org.qcrawler.rule.Url
import org.qcrawler.rule.Rule
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.qcrawler.rule.Qcrawler
import java.util.ArrayList
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.TypeDescription
import scala.collection.JavaConversions._
import org.qcrawler.QcrawlerClient
import java.io.File
import java.util.HashMap

class HttpUrlTest {
	
  //@Test
  def get(){
    var resp = HttpUrl.get("http://www.taobao.com");
    //println(resp.getText);
    //println(resp.cookie);
  }
  
  //@Test
  def extractText(){
    var resp = HttpUrl.get("http://www.baidu.com");
    var d = new DOMParser();
    resp.text = resp.getText().drop(15);
    println(resp.text);
    d.parse(new InputSource(new ByteArrayInputStream(resp.text.getBytes("gbk"))));
   
    /*var factory = new ScriptEngineManager();
    var g:Array[Any] = new Array[Any](1);
    
    // create a JavaScript engine
    var engine = factory.getEngineByName("JavaScript");
    //engine.put("document", );
	engine.eval(new FileReader("sizzle.js"));
	engine.put("document", d.getDocument);
    engine.put("g", g);
	engine.eval(""" var b =  $("a");
					g[0] =  $.getText(b);
					

    """);
	println(g.apply(0));*/
  }
  
  @Test
  def getC(){
    var h:Hello = new Hello();
    val m:java.util.Map[String,Any] = new java.util.HashMap[String,Any]();
    m.put("h",h);
    //m.put("x",h);
    QcrawlerClient.exeFile(new File("qcrawler.yaml"),m);
    println(h.getTitle());
  }
  
 // @Test
  def testyaml(){
	 
      var constructor = new Constructor(classOf[Qcrawler]);
      var qd = new TypeDescription(classOf[Qcrawler]);
      qd.putMapPropertyType("spider",classOf[Url],classOf[Object]);
      
	  constructor.addTypeDescription(qd);
	  var yaml = new Yaml(constructor);
	  var req = yaml.load(new FileReader("qcrawler.yaml")).asInstanceOf[Qcrawler];
	  var set = req.getSpider().keys;
	  for(x <- set){
	     println(x);
	     println(req.getSpider().get(x));
	  }
  }
  
}