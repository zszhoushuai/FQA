package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.faq.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//下面的注解表示想访问本控制器中的任何方法需要前缀/v1/tags
//这个v1开头的格式是后期微服务的标准名为RESTful
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    private ITagService tagService;

    //查询所有标签@GetMapping("")表示使用类上声明的前缀就可以访问这个方法
    @GetMapping("")
    public R<List<Tag>> tags(){
        List<Tag> list=tagService.getTags();
        return R.ok(list);
    }

    //为其它微服务提供的查询所有标签的功能方法
    @GetMapping("/list")
    public List<Tag> list(){
        return tagService.getTags();
    }

}
