package org.qcrawler.http
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.HttpResponse
import org.apache.http.params.CoreConnectionPNames
import org.apache.http.HttpEntity
import org.apache.http.Header
import java.io.ByteArrayOutputStream
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import java.io.FileReader
import org.apache.http.HttpResponseInterceptor
import org.apache.http.protocol.HttpContext
import org.apache.http.HeaderElement
import org.apache.http.client.entity.GzipDecompressingEntity
import org.apache.http.util.EntityUtils
import scala.collection.mutable.ListBuffer
import org.apache.http.cookie.Cookie
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import java.io.File
import java.util.zip.GZIPInputStream
import java.io.InputStreamReader
import java.io.InputStream
import java.io.ByteArrayInputStream

object HttpUrl {
	
  var cm:ThreadSafeClientConnManager = new ThreadSafeClientConnManager();
  cm.setMaxTotal(100);
  var httpclient:DefaultHttpClient = new DefaultHttpClient(cm);
  httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,5000)
  httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000)
  
  def get(url:String,req:RequestInfo):ResponseInfo={
    var httpget = new HttpGet(url);
    var response:ResponseInfo = new ResponseInfo();
    if(req.accept!=""){
    	httpget.addHeader("Accept",req.accept)
    }
    if(req.charset!=""){
       httpget.addHeader("Accept-Charset",req.charset)
    }
    if(req.cookie!=""){
       httpget.addHeader("Cookie",req.cookie)
    }
    if(req.encoding!=""){
       httpget.addHeader("Accept-Encoding",req.encoding)
    }
    
    if(req.userAgent!=""){
       httpget.addHeader("User-Agent",req.userAgent)
    } 
    
    if(req.refer!=""){
       httpget.addHeader("Referer",req.refer)
    }
    
    if(req.language!=""){
       httpget.addHeader("Accept-Language",req.language)
    }
    
    /* httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
            def process(response:HttpResponse,context:HttpContext){
                var entity:HttpEntity = response.getEntity();
                var ceheader:Header = entity.getContentEncoding();
                if (ceheader != null) {
                    var codecs:Array[HeaderElement] = ceheader.getElements();
                    for (cd <- codecs) {
                        if (cd.getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

     });*/

    var resp:HttpResponse = httpclient.execute(httpget);
    response.httpCode = resp.getStatusLine().getStatusCode;
    if(response.httpCode == 200){
    	var entity:HttpEntity = resp.getEntity();
    	if(entity.getContentType!=null){
    		response.charset = EntityUtils.getContentCharSet(entity);
    	}else{
    		response.charset = "utf-8";
    	}
        var cookies:Buffer[Cookie] = httpclient.getCookieStore().getCookies();
        if(cookies!=null){
        	cookies.foreach(i => response.cookie.put(i.getName,i.getValue));
        }
        var out = new ByteArrayOutputStream();
        
        for( x <- resp.getHeaders("Content-Encoding")){
          if(x.getValue.equalsIgnoreCase("gzip")){
              entity.writeTo(out);
        	  var gzin = new GZIPInputStream(new ByteArrayInputStream(out.toByteArray));
              val source = scala.io.Source.fromInputStream(gzin,response.charset);
              response.text = source.mkString;
              source.close();
              return response;
          }
        }
    	entity.writeTo(out);
    	response.text = new String(out.toByteArray,response.charset);
    
    }
    return response;
  }
  
  def get(url:String):ResponseInfo={
	  var constructor = new Constructor();
	  constructor.addTypeDescription(new TypeDescription(classOf[RequestInfo], "!client"));
	  var yaml = new Yaml(constructor);
	  var req = yaml.load(new FileReader(new File(getClass().getResource("RequestInfo.yaml").toURI()))).asInstanceOf[RequestInfo];
	  get(url,req);
  }
  
   def get(url:String,client:File):ResponseInfo={
	  var constructor = new Constructor();
	  constructor.addTypeDescription(new TypeDescription(classOf[RequestInfo], "!client"));
	  var yaml = new Yaml(constructor);
	  var req = yaml.load(new FileReader(client)).asInstanceOf[RequestInfo];
	  get(url,req);
  }

  
}