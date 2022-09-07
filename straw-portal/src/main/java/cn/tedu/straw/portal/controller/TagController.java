package cn.tedu.straw.portal.controller;

import cn.tedu.straw.portal.model.Tag;
import cn.tedu.straw.portal.model.User;
import cn.tedu.straw.portal.service.ITagService;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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


}
