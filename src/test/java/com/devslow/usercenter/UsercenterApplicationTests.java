package com.devslow.usercenter;

import com.devslow.usercenter.model.domain.User;
import com.devslow.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@MapperScan("com.devslow.usercenter.mapper")
@SpringBootTest
class UsercenterApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private UserService userService;
    @Test
    void UserServiceTest() {
        User user = new User();
        user.setUsername("devSlow");
        user.setUserAccount("slow");
        user.setAvatarUrl("https://blogimgoss.oss-cn-nanjing.aliyuncs.com/img/avatar.jpg");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("19339708329");
        user.setEmail("xiaojibaike@gmail.com");
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
    }
    @Test
    void encryption() {
        String result = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(result);
    }

}
