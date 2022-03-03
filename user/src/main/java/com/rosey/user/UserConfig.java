package com.rosey.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ClassName: UserConfig <br/>
 * Description: <br/>
 * date: 2021/8/30 8:06 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration
@Import(UserConfigABC.class)
public class UserConfig {

    @Bean
    public String getStr(){
        return "123";
    }

}
