package org.qcrawler.http
import scala.reflect.BeanProperty

class RequestInfo(var s:String){
	
	def this() = this("");
  
	@BeanProperty var userAgent:String = "";
	@BeanProperty var cookie:String = "";
	@BeanProperty var refer:String = "";
  	@BeanProperty var accept:String = "";
  	@BeanProperty var language:String = "";
  	@BeanProperty var encoding:String = "";
  	@BeanProperty var charset:String = "";
  	
}