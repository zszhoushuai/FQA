package cn.tedu.straw.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
//支持连缀书写
@Accessors(chain = true)
public class UserVo {
    private Integer id;
    private String username;
    private String nickname;

    //两个面板中显示的数据
    //问题数量
    private int questions;
    //收藏数量
    private int collections;


}
