package cn.tedu.straw.portal.security;

import cn.tedu.straw.portal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration表示当前类是配置类,可能向Spring容器中注入对象
@Configuration
//下面的注解表示通知Spring-Security开启权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends
        WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()//对当前全部请求进行授权
                .antMatchers(
                        "/img/*",
                        "/js/*",
                        "/css/*",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/register"
                )//设置路径
                .permitAll()//允许全部请求访问上面定义的路径
                //其它路径需要全部进行表单登录验证
                .anyRequest().authenticated().and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureUrl("/login.html?error")
                .defaultSuccessUrl("/index.html")
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout");
    }


    //注入一个加密对象(密码中设定了算法ID下面的注入就省略了!)
    /*@Bean
    public PasswordEncoder passwordEncoder(){
        //这个加密对象使用BCrypt加密内容
        return new BCryptPasswordEncoder();
    }*/
}
