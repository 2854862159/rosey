package com.rosey.user;

import com.rosey.user.config.User;
import org.springframework.context.annotation.Bean;

/**
 * ClassName: UserConfigABC <br/>
 * Description: <br/>
 * date: 2021/8/30 9:02 下午<br/>
 *
 * @author tooru<br />
 */
public class UserConfigABC {

    @Bean
    public User user(){
        return new User();
    }

}
