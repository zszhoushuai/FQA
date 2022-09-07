package cn.tedu.straw.faq.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AnswerVo implements Serializable {
    @NotNull(message = "问题编号不能为空")
    private Integer questionId;

    @NotBlank(message = "回复内容不能为空")
    private String content;

}
