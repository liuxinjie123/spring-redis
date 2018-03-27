package com.dream.springredis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Service
public class JedisPoolUtil {

    private Integer seconds;

    JedisCluster jc;

    public JedisCluster getJc() {
        return jc;
    }

    public JedisPoolUtil(GenericObjectPoolConfig genericObjectPoolConfig, String redisServer, Integer timeout) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //Jedis Cluster will attempt to discover cluster nodes automatically
        String[] redisServerArr = redisServer.split(",");
        for (int i = 0; i < redisServerArr.length; i++) {
            String[] ipPort = redisServerArr[i].split(":");
            jedisClusterNodes.add(new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1]))); //root 6tfc%RDX || 99
        }
        jc = new JedisCluster(jedisClusterNodes, timeout, genericObjectPoolConfig);
    }

    public void set(String key, Serializable value) throws IOException {
        jc.setex(key.getBytes(Charset.forName("UTF-8")), seconds, toByteArray(value));
    }

    public void set(String key, Integer seconds, Serializable value) throws IOException {
        jc.setex(key.getBytes(Charset.forName("UTF-8")), seconds, toByteArray(value));
    }

    public Serializable get(String key) throws IOException, ClassNotFoundException {
        return toObject(jc.get(key.getBytes(Charset.forName("UTF-8"))));
    }

    public Object getObject(String key) throws IOException, ClassNotFoundException {
        return toObject2(jc.get(key.getBytes(Charset.forName("UTF-8"))));
    }

    public void setStr(String key, String value) throws IOException {
        jc.set(key, value);
    }

    public void setStr(String key, int seconds, String value) throws IOException {
        jc.setex(key, seconds, value);
    }

    public String getStr(String key) throws IOException, ClassNotFoundException {
        return jc.get(key);
    }

    public Serializable getByUTF8(String key) throws Exception {
        Object obj = new Object();
        obj = toObject(jc.get(key.getBytes(Charset.forName("UTF-8"))));
        return (Serializable) obj;
    }

    public Long remove(String key) throws IOException, ClassNotFoundException {
        return jc.del(key.getBytes(Charset.forName("UTF-8")));
    }

    public void reName(String oldkey, String newKey) throws IOException, ClassNotFoundException {
        jc.rename(oldkey, newKey);
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    /**
     * 设置hash缓存，默认30分钟过期
     *
     * @param key
     * @param hash
     * @throws Exception
     */
    public void hmset(String key, Map<String, String> hash) throws Exception {
        // 不设置过期时间
        hmset(key, 0, hash);
    }

    /**
     * 设置hash缓存
     *
     * @param key
     * @param seconds
     *        过期时间
     * @param hash
     * @throws Exception
     */
    public void hmset(String key, int seconds, Map<String, String> hash) throws Exception {
        boolean seted = false;
        if (seconds > 0) {
            seted = true;
        } else {
            Set<String> set = jc.hkeys(key);
            if (null != set && set.size() > 0) {
                seted = true;
            }
        }
        if (seted) {
            // 移除空值
            HashMap<String, String> name = new HashMap<String, String>();
            for (Entry<String, String> entry : hash.entrySet()) {
                if (!StringUtils.isBlank(entry.getValue())) {
                    // hash.remove(entry.getKey());
                    name.put(entry.getKey(), entry.getValue());
                }
            }
            if (!name.isEmpty()) {
                jc.hmset(key, name);
            }
        }
        if (seconds > 0) {
            Long exp = jc.ttl(key);
            if (exp.longValue() < 0) {
                jc.expire(key, seconds);
            }
        }
    }

    /**
     * 获取缓存hash值
     *
     * @param key
     * @param fields
     * @return
     * @throws Exception
     */
    public Map<String, String> hmget(String key, String... fields) throws Exception {
        List<String> values = jc.hmget(key, fields);
        Map<String, String> hash = new HashMap<String, String>();
        for (int idx = 0; idx < fields.length; idx++) {
            hash.put(fields[idx], values.get(idx));
        }
        return hash;
    }

    /**
     * 根据key获取所有值
     *
     * @param key
     * @return
     * @throws Exception
     */
    public  Map<String, String> hmgetAll(String key) throws Exception {
        Map<String, String> map = jc.hgetAll(key);
        return map;
    }

    /**
     * 获取缓存hash值
     *
     * @param key
     * @param field
     * @return
     * @throws Exception
     */
    public String hmget(String key, String field) throws Exception {
        List<String> values = jc.hmget(key, field);
        if (null != values && values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    /**
     * 删除缓存hash值
     *
     * @param key
     * @param fields
     * @throws Exception
     */
    public void hdel(String key, String... fields) throws Exception {
        jc.hdel(key, fields);
    }

    /**
     * 锁缓存
     *
     * @param key
     * @return
     * @throws Exception
     */
    public boolean lock(String key) throws Exception {
        // 默认5分钟
        return lock(key, 300);
    }

    /**
     * 锁缓存
     *
     * @param key
     * @param seconds
     * @return
     * @throws Exception
     */
    public boolean lock(String key, int seconds) {
        String fmt = "yyyy-MM-dd HH:mm:ss.SSS";
        String now = DateFormatUtils.format(new Date(), fmt);
        String locked = jc.set(key, now, "nx", "ex", seconds);
        // 获得锁
        if (!StringUtils.isEmpty(locked)) {
            return true;
        } else {// 未获得锁
            // 尝试再次获得锁
            String lockTime = jc.get(key);
            // 如果在获得锁时间期间，其他任务释放了锁
            if (StringUtils.isNotBlank(lockTime)) {
                now = DateFormatUtils.format(new Date(), fmt);
                locked = jc.set(key, now, "nx", "ex", seconds);
                // 获得锁
                if (StringUtils.isNotBlank(locked)) {
                    return true;
                } else {// 未获得锁
                    return false;
                }
            } else {// 未获得锁
                return false;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     * @throws Exception
     */
    public long unlock(String key) {
        return jc.del(key);
    }

    /**
     * 将bytes[]输在还原为Object对象
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public Serializable toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null) {
            return null;
        }
        Serializable obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (Serializable) ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return obj;
    }

    public Object toObject2(byte[] bytes) throws IOException, ClassNotFoundException {
        if (bytes == null) {
            return null;
        }
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (Serializable) ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return obj;
    }

    /**
     * 将Object转化成byte[]
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public byte[] toByteArray(Object obj) throws IOException {
        if (obj == null) {
            return null;
        }
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            throw e;
        }
        return bytes;
    }

}
