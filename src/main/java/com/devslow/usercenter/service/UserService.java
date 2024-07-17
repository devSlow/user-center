package com.devslow.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devslow.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务接口
* @author slow
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-07-12 21:21:26
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册接口
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 返回用户注册的id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录接口
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销接口
     * @autho slow
     * @CurrentTime 2024-7-14 13:51:20
     * @return 注销成功返回1
     */
    int userLogout(HttpServletRequest request);

    /**
     *  用户脱敏
     * @param originalUser
     * @return
     */
    User getDesensitizationUser(User originalUser);
}
