package org.qcrawler.http
import java.util.HashMap
import scala.reflect.BeanProperty

class ResponseInfo {
	
	@BeanProperty var cookie:HashMap[String,String] = new HashMap[String,String]();
	@BeanProperty var charset:String = "";
	@BeanProperty var httpCode:Int = 0;
	@BeanProperty var text:String = "";
}