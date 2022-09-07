package cn.tedu.straw.resource.controller;

import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/v1/images")
@Slf4j
public class ImageController {
    //下面两个属性值来自application.properties配置文件
    @Value("${spring.resources.static-locations}")
    private File resourcePath;
    @Value("${straw.resource.host}")
    private String resourceHost;

    //接收表单上传的文件
    @PostMapping
    public R<String> upload(MultipartFile imageFile) throws IOException {

        //按照当前日期创建文件夹
        String path= DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        //path="2020/12/16"
        File folder=new File(resourcePath,path);
        //folder->F:/resource/2020/12/16
        folder.mkdirs();//创建一串文件夹带s的!!!!
        log.debug("上传的文件夹为:{}",folder.getAbsolutePath());
        //按照上传文件的原始文件名,保留扩展名xx.xx.jpg
        //                              012345678
        String fileName=imageFile.getOriginalFilename();
        String ext=fileName.substring(fileName.lastIndexOf("."));
        //使用UUID生成文件名
        String name= UUID.randomUUID().toString()+ext;
        log.debug("生成的文件名:{}",name);
        //F:/resource/2020/12/16/uuid.jpg
        File file=new File(folder,name);
        //向硬盘写入文件
        imageFile.transferTo(file);
        //直接返回路径方便调用测试
        String url=resourceHost+"/"+path+"/"+name;
        log.debug("访问这个文件的路径为:{}",url);
        return R.ok(url);
    }
}
