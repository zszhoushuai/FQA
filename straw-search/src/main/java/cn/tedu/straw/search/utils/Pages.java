package cn.tedu.straw.search.utils;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

public class Pages {

    //将Page类型转换成PageInfo类型
    public static <T> PageInfo<T> pageInfo(Page<T> page){
        int pageNum=page.getNumber()+1;
        int pageSize=page.getSize();
        int totalPage=page.getTotalPages();
        //当前页面实际大小
        int size=page.getNumberOfElements();
        //当前页的第一行,在数据库中的行号 行号从0开始
        int startRow=page.getNumber()*pageSize;
        //当前页的最后一样,在数据库中的行号 行号从0开始
        int endRow=page.getNumber()*pageSize+size-1;
        //查询总条数
        long totalCount=page.getTotalElements();

        PageInfo<T> pageInfo=new PageInfo<>(page.getContent());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages(totalPage);
        pageInfo.setStartRow(startRow);
        pageInfo.setEndRow(endRow);
        pageInfo.setSize(size);
        pageInfo.setTotal(totalCount);
        //从新计算所有其他属性
        pageInfo.calcByNavigatePages(PageInfo.DEFAULT_NAVIGATE_PAGES);
        return pageInfo;
    }
}
