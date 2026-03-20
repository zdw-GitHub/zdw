package com.mshz.cloud.tms.business.util;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author david
 * * @date 2026/3/19
 */

/**
 * 雪花算法 ID 生成器，生成 18 位数字 ID
 */
public class SnowflakeIdGenerator {
    // ============================== 配置参数 ==============================
    /** 起始时间戳 (2020-01-01) */
    private final long epoch = 1577836800000L;

    /** 机器 ID 所占位数 (5 位，支持 32 个节点) */
    private final long workerIdBits = 5L;
    /** 数据标识 ID 所占位数 (5 位，支持 32 个节点) */
    private final long datacenterIdBits = 5L;
    /** 支持的最大机器 ID */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /** 支持的最大数据标识 ID */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    /** 序列号所占位数 (12 位，每毫秒最多 4096 个) */
    private final long sequenceBits = 12L;

    /** 机器 ID 左移位数 */
    private final long workerIdShift = sequenceBits;
    /** 数据标识 ID 左移位数 */
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    /** 时间戳左移位数 */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 序列号掩码，用于防止溢出 */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // ============================== 成员变量 ==============================
    /** 工作机器 ID */
    private long workerId;
    /** 数据中心 ID */
    private long datacenterId;
    /** 毫秒内序列号 */
    private long sequence = 0L;
    /** 上次生成 ID 的时间戳 */
    private long lastTimestamp = -1L;

    // ============================== 构造方法 ==============================
    /**
     * 构造函数
     * @param workerId 工作机器 ID (0~31)
     * @param datacenterId 数据中心 ID (0~31)
     */
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId 不能大于 %d 或小于 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenterId 不能大于 %d 或小于 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 默认构造函数，根据 IP 自动生成 workerId 和 datacenterId（简单实现，可按需调整）
     */
    public SnowflakeIdGenerator() {
        this.datacenterId = getDatacenterId();
        this.workerId = getWorkerId();
    }

    // ============================== 生成 ID 的核心方法 ==============================
    /**
     * 获取下一个 ID
     * @return 18 位数字字符串
     */
    public synchronized String nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上次生成的时间，说明系统时钟回拨，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("时钟回拨，拒绝生成 ID %d 毫秒", lastTimestamp - timestamp));
        }

        // 如果是同一毫秒内，则增加序列号
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 如果同一毫秒内序列号溢出，则等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置为 0
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 组合各部分生成 64 位 long
        long id = ((timestamp - epoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;

        // 转换为字符串，并确保长度为 18 位（若不足 18 位则左补零）
        String idStr = Long.toString(id);
        if (idStr.length() > 18) {
            // 理论上不会发生，但若超过 18 位，可截取后 18 位（但会丧失唯一性，不建议）
            throw new RuntimeException("生成的 ID 超过 18 位，请调整起始时间戳或机器 ID 范围");
        }
        // 补零到 18 位（统一长度，方便展示）
        return String.format("%018d", id);
    }

    /** 等待下一毫秒 */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /** 返回当前毫秒时间戳 */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    // ============================== 自动生成 workerId 和 datacenterId 的辅助方法 ==============================
    private long getDatacenterId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            if (mac != null) {
                long id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                return id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            // 忽略异常，使用默认值
        }
        return 1L;
    }

    private long getWorkerId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            if (mac != null) {
                long id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                return id % (maxWorkerId + 1);
            }
        } catch (Exception e) {
            // 忽略异常，使用默认值
        }
        return (System.currentTimeMillis() & 0xFFFF) % (maxWorkerId + 1);
    }
}
