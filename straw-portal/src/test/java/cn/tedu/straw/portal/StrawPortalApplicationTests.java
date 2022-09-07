package cn.tedu.straw.portal;


import cn.tedu.straw.portal.mapper.TagMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.model.Tag;
import cn.tedu.straw.portal.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StrawPortalApplicationTests {

    @Autowired(required = false)
    TagMapper tagMapper;
    @Test
    public void testTag(){
        Tag tag=tagMapper.selectById(14);
        System.out.println(tag);
    }

    //测试类中使用UserMapper
    //查询用户id为3的用户信息并输出到控制台


    @Autowired
    UserMapper userMapper;

    @Test
    public void testUser(){
        User user=userMapper.selectById(3);
        System.out.println(user);
    }






    @Test
    void contextLoads() {

        /*Msg m1=new Msg();
        m1.setId(1);
        m1.setName("新闻");
        m1.setContent("很厉害的新闻");

        Msg m2=new Msg();
        m2.setId(1);
        m2.setName("新闻");
        m2.setContent("很厉害的新闻");

        System.out.println(m1);

        System.out.println(m1.equals(m2));
*/
    }

}
