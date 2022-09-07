package cn.tedu.straw.portal;

import cn.tedu.straw.portal.model.Tag;
import cn.tedu.straw.portal.service.ITagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TagTest {

    @Autowired
    ITagService tagService;
    @Test
    public void test() {
        List<Tag> list = tagService.getTags();
        for (Tag tag : list)
            System.out.println(tag);
    }


}
