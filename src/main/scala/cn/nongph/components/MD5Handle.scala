package cn.nongph.components

import java.security.MessageDigest

/**
 * @author wj11938
 */
object MD5Handle {
  private val md = new ThreadLocal[MessageDigest]
  
  def encode( str:String ):String = {
    if( md.get==null )
      md.set(MessageDigest.getInstance("MD5"));
    md.get.update( str.getBytes )
    convertToHexString(md.get.digest());
  }
  
  def convertToHexString( data:Array[Byte] ):String = {
    val sb = new StringBuilder();
    for(i <- 0 until data.length){
     sb.append(Integer.toHexString(0xff & data(i)));
    }
    sb.toString();
  }
}