package com.mshz.cloud.tms.business.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 数据批量入库处理工具类
 * @author Liuyue
 */
@Component
public class ModifyDataBatchUtil {

    /**
     * 每次处理数据数量，1000条
     */
    private static final int BATCH_SIZE = 1000;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 批量处理修改或者插入
     * @param data          需要批量入库的数据模型集合
     * @param mapperClass   数据模型对应的Mapper类
     * @param function      自定义处理逻辑
     * @return int          处理影响的总行数
     */
    public  <T, U, V> int batchInsertOrUpdate(List<T> data, Class<U> mapperClass, BiFunction<T, U, V> function) {
        int i = 1;
        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            U mapper = batchSqlSession.getMapper(mapperClass);
            int size = data.size();
            for (T element : data) {
                function.apply(element,mapper);
                if ((i % BATCH_SIZE == 0) || i == size) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            // 非事务环境下强制commit，事务情况下该commit相当于无效
            batchSqlSession.commit(!TransactionSynchronizationManager.isSynchronizationActive());
        } catch (Exception e) {
            e.printStackTrace();
            batchSqlSession.rollback();
            // 向上抛出异常
//            throw new CustomException(e.getMessage());
        } finally {
            batchSqlSession.close();
        }
        return i - 1;
    }

}
