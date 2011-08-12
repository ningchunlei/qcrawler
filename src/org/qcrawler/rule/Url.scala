package org.qcrawler.rule
import scala.reflect.BeanProperty

class Url(var s:String) {
	
  def this() = this("");
  @BeanProperty var url:String = "";
  @BeanProperty var js:String = "";
  
}