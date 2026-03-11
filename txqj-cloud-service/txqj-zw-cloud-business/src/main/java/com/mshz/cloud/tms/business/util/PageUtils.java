package com.mshz.cloud.tms.business.util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mshz.cloud.common.util.sql.SqlUtil;
import com.mshz.cloud.tms.business.domain.PageDomain;
import com.mshz.cloud.tms.business.domain.TableSupport;

import java.util.List;

/**
 * 分页工具类
 *
 * @author zdw
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    public static void startPage2()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = 2;
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }


    /**
     * 解决处理逻辑后总数丢失
     * @param list
     * @param total
     * @return
     * @param <T>
     */
    public static <T> List<T> listToPage(List<T> list, long total) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        Page<T> page = new Page<>();
        page.addAll(list);
        page.setTotal(total);
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        int pages = total % pageSize == 0 ? (int) (total / pageSize) : (int) (total / pageSize)+1;
        page.setPages(pages);
        return page;
    }
}
