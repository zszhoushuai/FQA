package cn.tedu.straw.faq.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class QuestionVo implements Serializable {

    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "标题长度在3~50个字符之间")
    private String title;

    private String[] tagNames={};
    private String[] teacherNickNames={};

    @NotBlank(message = "问题内容不能为空")
    private String content;


}
