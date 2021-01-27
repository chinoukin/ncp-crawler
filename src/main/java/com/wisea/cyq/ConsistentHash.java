package com.wisea.cyq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash算法java简易实现
 *
 * @author IT云清
 * 参考：https://blog.csdn.net/zhanglu0223/article/details/100579254
 */
public class ConsistentHash {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsistentHash.class);

    /**
     * 虚拟节点个数
     * 每个真实节点对应的虚拟节点个数
     */
    private static final int VIRTUAL_NUM = 5;

    /**
     * 虚拟节点
     * eg:<656715414,192.168.1.1&&VN3>
     * 真实节点数量一般偏少，引入虚拟节点来平衡
     * 每个真实节点对应多个虚拟节点，这样每个节点尽可能在hash环上均匀分布，可以根据虚拟节点找到真实节点
     */
    private static SortedMap<Integer, String> shards = new TreeMap<>();

    /**
     * 真实节点
     */
    private static List<String> realNodes = new LinkedList<>();

    /**
     * 模拟初始节点
     */
    private static String[] servers = {"116.116.1.1", "116.116.1.2", "116.116.1.3", "116.116.1.5", "116.116.1.6"};


    /**
     * 初始化虚拟节点
     */
    static {
        for (String server : servers) {
            realNodes.add(server);
            LOGGER.info("添加真实节点{}", server);
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = server + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                LOGGER.info("添加虚拟节点{}，hash为{}", virtualNode, hash);
            }
        }
    }


    public static void main(String[] args) {
        test3();
    }

    public static void test2() {
        //测试定位node
        LOGGER.info(getSever("aa"));
        LOGGER.info(getSever("涨三"));
        LOGGER.info(getSever("num_19120000"));
        LOGGER.info(getSever("num_19120000"));
        LOGGER.info("------------------");
        //测试添加节点
        addNode("192.192.116.1");
        addNode("192.192.116.2");
        LOGGER.info("------------------");
        //测试删除节点
        delNode("116.116.1.1");
    }

    public static void test3() {
        //测试定位node
        LOGGER.info(getSever("aa"));
        LOGGER.info(getSever("涨三"));
        LOGGER.info(getSever("num_19120000"));
        LOGGER.info(getSever("num_19120000"));
        LOGGER.info("------------------");
        //测试添加节点
        addNode("192.192.116.1");
        addNode("192.192.116.2");
        LOGGER.info("------------------");
        //测试删除节点
        delNode("116.116.1.5");

        //再次测试定位node
        LOGGER.info(getSever("aa"));
        LOGGER.info(getSever("涨三"));
        LOGGER.info(getSever("num_19120000"));
        LOGGER.info(getSever("num_19120000"));
    }

    /**
     * 获取真实节点ip
     *
     * @param str 字符串
     * @return
     */
    public static String getSever(String str) {
        //计算hash
        int hash = getHash(str);
        Integer key = null;
        //寻找最近的虚拟node
        SortedMap<Integer, String> tailMap = shards.tailMap(hash);
        //获取在hash环上 右侧最近的虚拟节点的key
        key = tailMap.isEmpty() ? shards.lastKey() : tailMap.firstKey();
        //根据hash获取虚拟节点
        String virtualNode = shards.get(key);
        //返回虚拟节点的真实ip
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }


    /**
     * 添加节点
     *
     * @param node
     */
    public static void addNode(String node) {
        if (!realNodes.contains(node)) {
            realNodes.add(node);
            LOGGER.info("新增真实节点上线，{}", node);
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                LOGGER.info("新增虚拟节点{}，hash为{}", virtualNode, hash);
            }
        }

    }

    /**
     * 删除节点
     *
     * @param node
     */
    public static void delNode(String node) {
        if (realNodes.contains(node)) {
            //下线真实节点
            realNodes.remove(node);
            LOGGER.info("真实节点下线，{}", node);
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                //移除虚拟节点
                shards.remove(hash);
                LOGGER.info("下线虚拟节点{}，hash为{}", virtualNode, hash);
            }
        }
    }

    /**
     * FNV1_32_HASH算法
     *
     * @param str 任意字符串
     * @return 返回int类型的hash值
     */
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}

