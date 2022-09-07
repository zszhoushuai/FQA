package cn.tedu.straw.search.vo;

import cn.tedu.straw.commons.model.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
//为了防止ES中出现重复的数据,添加上EqualsAndHashCode方法
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "questions")
public class QuestionVo implements Serializable {

    private static final long serialVersionUID=1L;

    public static final Integer POSTED=0;
    public static final Integer SOLVING=1;
    public static final Integer SOLVED=2;

    @Id
    private  Integer id;

    @Field(type = FieldType.Text,analyzer = "ik_smart",
                searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_smart",
            searchAnalyzer = "ik_smart")
    private  String content;

    @Field(type = FieldType.Text,analyzer = "ik_smart",
            searchAnalyzer = "ik_smart")
    private String tagNames;

    @Field(type = FieldType.Keyword)
    private String userNickName;

    @Field(type = FieldType.Integer)
    private Integer userId;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Integer)
    private Integer pageViews;

    @Field(type = FieldType.Integer)
    private Integer publicStatus;

    @Field(type = FieldType.Integer)
    private Integer deleteStatus;

    @Field(type = FieldType.Date,
            format = DateFormat.basic_date_time)
    private LocalDateTime createtime;

    @Field(type = FieldType.Date,
            format = DateFormat.basic_date_time)
    private LocalDateTime modifytime;

    @Transient//临时的和ES无关
    private List<Tag> tags;



}
