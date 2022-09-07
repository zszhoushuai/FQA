package cn.tedu.straw.search.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "items")
public class Item {

    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart"
            ,searchAnalyzer = "ik_smart")
    private String title;
    @Field(type=FieldType.Keyword)
    private String category;
    @Field(type=FieldType.Keyword)
    private String brand;
    @Field(type=FieldType.Double)
    private Double price;
    @Field(type=FieldType.Keyword,index=false)
    private String images;

}
