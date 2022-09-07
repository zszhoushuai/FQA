package cn.tedu.straw.gateway.security;

import cn.tedu.straw.gateway.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailService);
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




}
