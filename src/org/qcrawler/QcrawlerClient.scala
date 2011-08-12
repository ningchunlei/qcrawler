package org.qcrawler
import java.io.File
import org.qcrawler.rule.Rule
import org.qcrawler.rule.Qcrawler
import scala.collection.JavaConversions._
import scala.util.matching.Regex
import java.util.regex.Pattern
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.TypeDescription
import org.qcrawler.rule.Url
import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import org.qcrawler.http.HttpUrl
import org.apache.log4j.Logger
import org.cyberneko.html.parsers.DOMParser
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import javax.script.ScriptEngineManager
import java.io.InputStreamReader

class QcrawlerClient{
  
}

object QcrawlerClient {
	
    type javaList = java.util.List[Rule];
    type javaMap = java.util.Map[String,Any];
    var log:Logger = Logger.getLogger(classOf[QcrawlerClient]); 
    
    
	def exeHtml(html:String,rule:javaList,context:javaMap,encoding:String,js:String){
		var ht = "";
		for(h <- rule){
		    if(h.getRule!=""){
		    	val p = Pattern.compile(h.getRule,Pattern.DOTALL); 
		    	var m = p.matcher(html);
		        if(m.find){
		    		ht += m.group;
		        }
		    }
		}
		exeDom(ht,js,context,encoding);
	}
	
	def exeFile(file:File,context:javaMap){
	  var constructor = new Constructor(classOf[Qcrawler]);
      var qd = new TypeDescription(classOf[Qcrawler]);
      qd.putMapPropertyType("spider",classOf[Url],classOf[java.util.List[Rule]]);
      
	  constructor.addTypeDescription(qd);
	  var yaml = new Yaml(constructor);
	  var req = yaml.load(new FileReader(file)).asInstanceOf[Qcrawler];
	  exeUrl(req,context);
	}
	
	def exeUrl(qcrawler:Qcrawler,context:javaMap){
		 var set = qcrawler.getSpider().keys;
		 for(x <- set){
			  var resp = HttpUrl.get(x.getUrl);
			  if(resp.httpCode!=200){
				  log.error(String.format("Get %s,Status=%d",x.getUrl,resp.getHttpCode.asInstanceOf[java.lang.Integer]));
			  }
			 val source = scala.io.Source.fromFile(x.getJs)
			 val lines = source .mkString	
			 source.close ()
			 exeHtml(resp.getText,qcrawler.getSpider().get(x),context,resp.getCharset,lines);
		 }
	}
	
	def exeDom(dom:String,js:String,context:javaMap,encoding:String){
	    var d = new DOMParser();
	    d.setProperty("http://cyberneko.org/html/properties/default-encoding",encoding);
	    d.parse(new InputSource(new ByteArrayInputStream(dom.getBytes(encoding))));
	    var factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    var engine = factory.getEngineByName("JavaScript");
	    engine.put("document", d.getDocument);
	    context.foreach((x) => engine.put(x._1,x._2));
	    //println(classOf[QcrawlerClient].getResource("sizzle.js"));
		engine.eval(new InputStreamReader(classOf[QcrawlerClient].getResourceAsStream("sizzle.js")));
		engine.eval(js);
	}
	
	def main(args: Array[String]) {
		 var constructor = new Constructor(classOf[Qcrawler]);
	      var qd = new TypeDescription(classOf[Qcrawler]);
	      qd.putMapPropertyType("spider",classOf[Url],classOf[Object]);
		  constructor.addTypeDescription(qd);
		  var yaml = new Yaml(constructor);
		  var req = yaml.load(new FileReader("qcrawler.yaml")).asInstanceOf[Qcrawler];
		  
		}
	
}