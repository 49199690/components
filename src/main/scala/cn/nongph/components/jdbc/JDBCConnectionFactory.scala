package cn.nongph.components.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.nongph.components.Config

/**
 * 
 * @author felix.wu
 * @since 2015-10-08
 */
class JDBCConnectionFactory extends PoolableObjectFactory[Connection]{
	val logger:Logger = LoggerFactory.getLogger(this.getClass);

	def makeObject():Connection = {
		val url = Config.get("kmp.jdbc.url");
		val user = Config.get("kmp.jdbc.username");
		val password = Config.get("kmp.jdbc.password");
    val driver = Config.get("kmp.jdbc.driver");
    Class.forName(driver);
		val conn = DriverManager.getConnection( url, user, password );
		return conn;
	}
  
	def destroyObject(conn:Connection):Unit = conn.close

	def validateObject(conn:Connection):Boolean = {
		try {
			return conn.isValid(0);
		} catch {
      case e:SQLException =>{
        logger.error("test connection failed", e);
        return false;
      }
    }
	}

	def activateObject(obj:Connection):Unit = {}

	def passivateObject(obj:Connection):Unit = {}

}
