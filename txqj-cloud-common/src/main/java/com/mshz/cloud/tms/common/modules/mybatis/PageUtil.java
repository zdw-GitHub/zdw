package com.mshz.cloud.tms.common.modules.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @msg 分页工具
 * @author wangzt
 */
public class PageUtil {


    /**
     * Mybatis-Plus分页封装
     *
     * @param page 分页VO
     * @param <T>  范型
     * @return 分页响应
     */
    public static <T> Page<T> initPage(PageVo page) {

        Page<T> p;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        String sort = page.getSort();
        String order = page.getOrder();

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 50000) {
            pageSize = 50000;
        }
        if (StrUtil.isNotBlank(sort)) {
            boolean isAsc = false;
            if (StrUtil.isBlank(order)) {
                isAsc = false;
            } else {
                if ("desc".equals(order.toLowerCase())) {
                    isAsc = false;
                } else if ("asc".equals(order.toLowerCase())) {
                    isAsc = true;
                }
            }
            p = new Page<>(pageNo, pageSize);
            if (isAsc) {
                p.addOrder(OrderItem.asc(sort));
            } else {
                p.addOrder(OrderItem.desc(sort));
            }

        } else {
            p = new Page<>(pageNo, pageSize);
        }
        return p;
    }

    /**
     * List 手动分页
     *
     * @param page 分页对象
     * @param list 分页集合
     * @return 范型结果
     */
    public static <T> List<T> listToPage(PageVo page, List<T> list) {

        int pageNo = page.getPageNo() - 1;
        int pageSize = page.getPageSize();

        if (pageNo < 0) {
            pageNo = 0;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        int fromIndex = pageNo * pageSize;
        int toIndex = pageNo * pageSize + pageSize;

        if (fromIndex > list.size()) {
            return new ArrayList<>();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * 转换分页类型
     *
     * @param originPage 原分页
     * @param records    新分页数据
     * @param <T>        新类型
     * @return 新类型分页
     */
    public static <T> Page<T> convertPage(Page originPage, List<T> records) {
        Page<T> resultPage = new Page<>();
        if (originPage != null) {
            resultPage.setCurrent(originPage.getCurrent());
            resultPage.setPages(originPage.getPages());
            resultPage.setTotal(originPage.getTotal());
            resultPage.setSize(originPage.getSize());
            resultPage.setRecords(records);
        }
        return resultPage;
    }
}
