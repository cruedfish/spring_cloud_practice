package com.giveu.guauthserver;


import com.haistore.redis.ProtostuffSerializer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.List;

@Component
public class JedisClient {


	private String host = "39.107.90.7";

	private int port = 6379;

	private String password="yinhai";

	protected Logger logger = Logger.getLogger(this.getClass());

	private Jedis jedis;

	private JedisPool jedisPool;

	private JedisPoolConfig jedisPoolConfig;

	private   ProtostuffSerializer protostuffSerializer;

	public JedisClient(){
	     protostuffSerializer = new ProtostuffSerializer();

	     jedisPoolConfig = new JedisPoolConfig();

		  jedisPool = new JedisPool(jedisPoolConfig, host, port, 0, password);

		 jedis = jedisPool.getResource();
	}

	public boolean exists(String key) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			flag = jedis.exists(protostuffSerializer.serialize(key));
		} catch (Exception ex) {
			logger.error("JedisService.exists 出错[key=" + key + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return flag;
	}


	public String set(String key, String value, int seconds) {
		// TODO Auto-generated method stub
		String responseResult = null;
		String result = "";
		try {
			responseResult = jedis.set(protostuffSerializer.serialize(key),
					protostuffSerializer.serialize(value));
			if (seconds != 0) {
				jedis.expire(protostuffSerializer.serialize(key), seconds);
			}
			result = responseResult;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("JedisService.set 出错[key=" + key + ",value=" + value + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return result;
	}


	public String get(String key) {
		// TODO Auto-generated method stub
		String resultData = "";
		try {
			byte[] result = jedis.get(protostuffSerializer.serialize(key));
			if (result == null) {
				return "";
			}
			resultData = protostuffSerializer.deserialize(result);
		} catch (Exception ex) {
			logger.error("JedisService.get 出错[key=" + key + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return resultData;
	}


	public String getSet(String key, String value, int seconds) {
		// TODO Auto-generated method stub
		String resultData = "";
		try {
			byte[] result = jedis.getSet(protostuffSerializer.serialize(key),
					protostuffSerializer.serialize(value));
			jedis.expire(protostuffSerializer.serialize(key), seconds);
			return protostuffSerializer.deserialize(result);
		} catch (Exception ex) {
			logger.error("JedisService.getSet 出错[key=" + key + ",value=" + value + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return resultData;
	}


	public void delKey(String key) {
		// TODO Auto-generated method stub
		try {
			jedis.del(protostuffSerializer.serialize(key));
		} catch (Exception ex) {
			logger.error("JedisService.delKey 出错[key=" + key, ex);
		}
	}


	public void delNativeKey(String key) {
		try {
			jedis.del(key);
		} catch (Exception ex) {
			logger.error("del native key err: " + key, ex);
		}
	}


	public Long geoadd(String key, double longitude, double latitude, byte[] obj) {
		// TODO Auto-generated method stub

		try {
			return jedis.geoadd(protostuffSerializer.serialize(key), longitude, latitude, obj);
		} catch (Exception ex) {
			logger.error("JedisService.geoadd 出错[key=" + key + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return null;
	}


	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude) {
		// TODO Auto-generated method stub
		GeoUnit geoUnit = GeoUnit.KM;
		GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();
		param.withDist();
		param.sortAscending();
		param.count(10);
		param.withCoord();
		// 查询距离10000km
		double searchDistance = 10000.0;

		List<GeoRadiusResponse> geoRadiusResponses = null;
		try {
			// jedis = jedisSentinelPool.jedis;
			geoRadiusResponses = jedis.georadius(protostuffSerializer.serialize(key), longitude, latitude,
					searchDistance, geoUnit, param);
		} catch (Exception ex) {
			logger.error("JedisService.geoadd 出错[key=" + key + "]", ex);
		} finally {
			closeResource(jedis);
		}
		return geoRadiusResponses;
	}

//	@SuppressWarnings("unchecked")
//
//	public Map<String, Object> getMapData(String key) {
////		// TODO Auto-generated method stub
//		Map<String, Object> resultData = new HashMap<>();
//
//		try {
//			byte[] result = jedis.get(protostuffSerializer.serialize(key));
//			if (result == null) {
//				return null;
//			}
//			String resultDataStr = protostuffSerializer.deserialize(result);
//			resultData = new JsonUtil().toObject(resultDataStr, Map.class);
//		} catch (Exception ex) {
//			logger.error("JedisService.get 出错[key=" + key + "]", ex);
//		} finally {
//			closeResource(jedis);
//		}
//		return resultData;
//	}

	/**
	 * 回收释放资源
	 *
	 * @param jedis
	 */
	private void closeResource(Jedis jedis) {
		try {
			if (jedis != null) {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error("JedisServicecloseResource error.", e);
		}
	}


	public String getLockValue(String key) {
		return jedis.get(key);
	}

	/**
	 * 方法名称：lock <br>
	 * 描述： 如为第一次，则加上锁,每次调用值会自动加一<br>
	 * 作者：100196 <br>
	 * 修改日期：2017年7月31日下午3:56:18
	 *
	 * @param key
	 * @param seconds
	 * @return
	 */

	public boolean lock(String key, int seconds) {
		try {
			// 如为第一次，则加上锁，返回false,10秒
			if (jedis.incr(key) == 1) {
				jedis.expire(key, seconds);
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			closeResource(jedis);
		}
		return true;
	}


	public void unlock(String key) {
		try {
			jedis.del(key);
		} catch (Exception e) {
			logger.error("JedisServicecloseResource error.", e);
		}
		finally {
			closeResource(jedis);
		}
	}
	public synchronized  void lpush(String key, String... strings) {
		try {
			jedis.lpush(key, strings);
			jedis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeResource(jedis);
		}
	}
}
