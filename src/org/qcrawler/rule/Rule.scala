package org.qcrawler.rule
import scala.reflect.BeanProperty

class Rule(var s:String){

	def this() = this("");
	
	@BeanProperty var rule:String = "";
	@BeanProperty var js:String = "";
  
}