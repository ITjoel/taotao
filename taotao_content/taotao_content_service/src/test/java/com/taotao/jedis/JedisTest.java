package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.content.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {


	@Test
	public void testJedisSingle() throws Exception {
		Jedis jedis = new Jedis("192.168.113.130", 6379);
		jedis.set("mytest", "1000");
		String result = jedis.get("mytest");
		System.out.println(result);
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception {
		//创建一个连接池对象
		JedisPool jedisPool = new JedisPool("192.168.113.130", 6379);
		//从连接池获得连接
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get("mytest");
		System.out.println(result);
		//每次jedis使用完毕后需要关闭，连接池回收资源。
		jedis.close();
		//系统结束前关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception {
		//连接集群使用JedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.113.130", 6661));
		nodes.add(new HostAndPort("192.168.113.130", 6662));
		nodes.add(new HostAndPort("192.168.113.130", 6663));
		nodes.add(new HostAndPort("192.168.113.130", 6664));
		nodes.add(new HostAndPort("192.168.113.130", 6665));
		nodes.add(new HostAndPort("192.168.113.130", 6666));
		//系统中可以是单例
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("jediscluster", "123456");
		String result = jedisCluster.get("jediscluster");
		System.out.println(result);
		//系统结束前关闭JedisCluster
		jedisCluster.close();
	}
	
	@Test
	public void testJedisClientPool() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("client", "hello");
		String result = jedisClient.get("client");
		System.out.println(result);
	}
}
