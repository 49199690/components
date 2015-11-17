package cn.nongph.components

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties
import java.io.InputStream
import java.io.IOException
import scala.collection.JavaConversions

/**
 * @author wj11938
 */
object Config {
  val logger = LoggerFactory.getLogger(this.getClass);

  val configFiles = Array("/config.properties");

  val properties = new Properties();
  
  private val sparkConfig = scala.collection.mutable.Map[String, String](); 
  
  def getProperties():Properties = properties;

  def get(name:String):String = properties.getProperty(name);
  
  def get(name:String, defaultValue:String):String = {
    val ret:String = properties.getProperty(name)
    if( ret== null)
      defaultValue;
    else
      ret;
  }

  def getInt(item:String, defaultValue:Int):Int = {
    var ret = defaultValue;
    try {
      ret = Integer.parseInt(properties.getProperty(item));
    } catch {
      case ignore:Exception =>{}
    }
    return ret;
  }
  
   def repartitionNum():Int = Integer.valueOf(properties.getProperty("kmp.repartition.num") );
   
   def getSparkConfig():scala.collection.mutable.Map[String, String] = sparkConfig;
   
   def getSectionWithoutKeyPrefix(sectionName:String):scala.collection.mutable.Map[String, String]={
     val section = scala.collection.mutable.Map[String, String]();
     JavaConversions.asScalaSet( properties.keySet ).foreach{ key => 
       val sSkey = key.toString();
       if( sSkey.toString().startsWith(sectionName) )
           section += ( sSkey.substring(sectionName.length+1) -> properties.getProperty(sSkey) )
     }
     section;
   }
   
   configFiles.foreach { fn => 
      val is:InputStream = this.getClass.getResourceAsStream(fn);
      if( is!=null ) {
        try{
          properties.load(is);
        } finally {
            is.close();
        }
      } else {
        logger.info("can't find resource " + fn);
      }
   }
  
   JavaConversions.asScalaSet( properties.keySet ).foreach { oKey=> 
     val sKey = oKey.toString(); 
     if( sKey.startsWith("spark.") ){
        sparkConfig.put( sKey, properties.getProperty(sKey) );
      }
   }  
}