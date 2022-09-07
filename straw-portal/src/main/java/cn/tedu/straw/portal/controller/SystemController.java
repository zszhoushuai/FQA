package cn.tedu.straw.portal.controller;

import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.service.ServiceException;
import cn.tedu.straw.portal.vo.R;
import cn.tedu.straw.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Slf4j//启动日志功能!
public class SystemController {

    //显示登录页面的方法
    @GetMapping("/login.html")
    public ModelAndView loginForm(){
        //ModelAndView("login");对应的是resources/templates/login.html
        return new ModelAndView("login");
    }
    //显示注册页面的方法
    @GetMapping("/register.html")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public R registerStudent(
            //控制器接收的参数前加@Validated
            //表示要按这个类规定的验证规则,验证这个对象属性的值
            @Validated RegisterVo registerVo,
            //固定用法,在验证参数后再跟一个参数:BindingResult
            //这个参数中记录保存上面验证过程中的验证信息和结果
            BindingResult validaResult){
        //在控制器调用业务逻辑前,先判断BindingResult对象中是否有错误
        if(validaResult.hasErrors()){
            //如果验证结果中包含任何错误信息,进入这个if
            //获得其中的一个错误信息显示,一般是按顺序的第一个错误信息
            String error=validaResult.getFieldError()
                        .getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        System.out.println(registerVo);
        log.debug("得到信息为:{}",registerVo);
        try{
            userService.registerStudent(registerVo);
            return R.created("注册成功!");
        }catch (ServiceException e){
            log.error("注册失败",e);
            return R.failed(e);
        }
    }

    //下面两个属性值来自application.properties配置文件
    @Value("${straw.resource.path}")
    private File resourcePath;
    @Value("${straw.resource.host}")
    private String resourceHost;

    //接收表单上传的文件
    @PostMapping("/upload/file")
    public R<String> upload(MultipartFile imageFile) throws IOException {
        /*
            我们需要保证任何用户上传的文件的文件名都不能重复
            我们为了尽量避免文件名的重复,采用以下策略
            1.将原有文件名修改为使用UUID生成的字符串
            2.不同的日期创建不同的文件夹
            3.保留文件的扩展名,还能方便文件识别
         */
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
