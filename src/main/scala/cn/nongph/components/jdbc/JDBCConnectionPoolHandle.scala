package cn.nongph.components.jdbc;

import java.sql.Connection;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author felix.wu
 * @since 2014-11-19
 */
object JDBCConnectionPoolHandle {
	val logger:Logger = LoggerFactory.getLogger(this.getClass);
	
	var pool = new GenericObjectPool[Connection]( new JDBCConnectionFactory() );
	pool.setMaxActive( 10 );
	pool.setMaxIdle( 10 );
	pool.setMinIdle( 0 );                             //空闲时全部清除对象
	pool.setTimeBetweenEvictionRunsMillis(3*60*1000); //每隔3分钟清理空闲时间过期对象
	pool.setMinEvictableIdleTimeMillis(3*60*1000);    //空闲时间超过3分钟算过期  
	pool.setTestWhileIdle( true );
	pool.setTestOnBorrow( true );
	pool.setNumTestsPerEvictionRun(10);               //对整个空闲队列进行清理  
	
	Runtime.getRuntime().addShutdownHook( new Thread(){
    override def run():Unit = JDBCConnectionPoolHandle.closePool 
		} );
	logger.info( "create connection pool success" );	
	
	def getConnection():Connection = {
		try {
			return pool.borrowObject();
		} catch {
      case e:Exception =>logger.error( "can't get connection from pool", e );
			return null;
		}
	}
	
	def closeConnection(conn:Connection):Unit = {
		try {
			pool.returnObject( conn );
		} catch {
      case e:Exception => logger.error( "can't return connection to pool", e );
		}
	}
	
	def closePool():Unit = {
		try {
			pool.close();
			logger.info( "close connection pool success" );
		} catch {
      case e:Exception =>logger.error( " close connection pool failed", e );
		}
	}
}
