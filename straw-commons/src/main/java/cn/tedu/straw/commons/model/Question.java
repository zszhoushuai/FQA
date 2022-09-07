package cn.tedu.straw.commons.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    //定义问题状态的常量
    public static final Integer POSTED=0; //已添加/未回复
    public static final Integer SOLVING=1;//正在采纳/已回复
    public static final Integer SOLVED=2; //已经采纳/已解决


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题的标题
     */
    @TableField("title")
    private String title;

    /**
     * 提问内容
     */
    @TableField("content")
    private String content;

    /**
     * 提问者用户名
     */
    @TableField("user_nick_name")
    private String userNickName;

    /**
     * 提问者id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @TableField("createtime")
    private LocalDateTime createtime;

    /**
     * 状态，0-》未回答，1-》待解决，2-》已解决
     */
    @TableField("status")
    private Integer status;

    /**
     * 浏览量
     */
    @TableField("page_views")
    private Integer pageViews;

    /**
     * 该问题是否公开，所有学生都可见，0-》否，1-》是
     */
    @TableField("public_status")
    private Integer publicStatus;

    @TableField("modifytime")
    private LocalDate modifytime;

    @TableField("delete_status")
    private Integer deleteStatus;

    @TableField("tag_names")
    private String tagNames;

    //为问题实体类添加标签集合
    //@TableField(exist = false)表示数据库中没有这样的列,防止报错
    @TableField(exist = false)
    private List<Tag> tags;



}
