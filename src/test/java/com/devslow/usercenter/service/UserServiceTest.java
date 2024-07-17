package com.devslow.usercenter.service;

import com.devslow.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

/**
 * 用户服务测试
 */
@SpringBootTest
@MapperScan("com.devslow.usercenter.mapper")
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
     void testAddUser() {
         User user=new User();

         user.setUsername("testSlow");
         user.setUserAccount("slow");
         user.setAvatarUrl("https://blogimgoss.oss-cn-nanjing.aliyuncs.com/img/avatar.jpg");
         user.setGender(0);
         user.setUserPassword("123456");
         user.setPhone("19339708329");
         user.setEmail("xiaojibaike@gmal.com");
        boolean result = userService.save(user);
        Assertions.assertTrue(result);


    }

    @Test
    void userRegister() {
        String userAccount="slow";
        String password="";
        String checkPassword="12345678";
        long result = userService.userRegister(userAccount, password, checkPassword);
//        用户密码为空
        Assertions.assertEquals(-1,result);

//        用户账户不小于4位
        userAccount="sl";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);

//        密码不小于8位
        userAccount="slow";
        password="123456";
        checkPassword="12345678";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);

//        账户不能重复
        userAccount="slow";
        password="12345678";
        checkPassword="12345678";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);

//        账号不能包含特殊字符
        userAccount="slow@@&&##";
        password="123456";
        checkPassword="12345678";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);

        //    密码与校验密码相同
        userAccount="slow";
        password="12345678";
        checkPassword="12345688";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
    }



}