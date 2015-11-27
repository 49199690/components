package cn.nongph.components.hash;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希算法<br>
 * 
 * 主要应用在分布式服务中
 * 
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年11月25日-下午1:55:28
 * @since 2015年11月25日-下午1:55:28
 */
public class ConsistentHash {
	
	//MurmurHash算法，可换成其他的哈希算法
	private MurmurHash hashAlg = new MurmurHash();

	// 服务器IP列表
	private String[] hosts = {"192.168.3.1:8080", "192.168.3.2:8080", "192.168.3.3:8080"};
	
	//各个服务器对应的权重值
	private int[] weights = {1, 4, 8};
	
	//存放服务器虚拟节点
	private TreeMap<Long, String> virtualNodes = new TreeMap<Long, String>();
	
	//存放服务器物理节点的资源信息
	private Map<String, Object> resources = new HashMap<String, Object>();
	
	public void initialize() {
		//构建Hash环结构
		for(int i = 0; i < hosts.length; i++) {
			for(int n = 0; n < 160*weights[i]; n++) {
				long nodeHash = this.hashAlg.hash("SHARD-" + i + "-NODE-" + n);
				this.virtualNodes.put(nodeHash, hosts[i]);
			}
			
			// TODO 根据 servers[i]创建对应的资源
			Object res = hosts[i] + "_RES";
			this.resources.put(hosts[i], res);
		}
	}
	
	/**
	 * 获取资源
	 * @param key
	 * @return
	 */
	public Object getResource(String key) {
		String host = this.getHost(key);
		return this.resources.get(host);
	}
	
	/**
	 * 获取服务器主机
	 * @param key
	 * @return
	 */
	private String getHost(String key) {
		long nodeHash = this.findNearestNode(key);
		return this.virtualNodes.get(nodeHash);
	}
	
	/**
	 * 查找hash环上临近的虚拟节点
	 * @param key
	 * @return
	 */
	private long findNearestNode(String key) {
		long hash = this.hashAlg.hash(key);
		SortedMap<Long, String> tail = this.virtualNodes.tailMap(hash);
		
		return (tail.isEmpty() ? this.virtualNodes.firstKey() : tail.firstKey());
	}
	
}

