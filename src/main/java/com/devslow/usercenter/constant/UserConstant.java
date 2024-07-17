package com.devslow.usercenter.constant;

/**
 * 用户常量接口
 * @author slow
 * @CurrentTime 2024-7-17 21:03:02
 * 接口的属性：public static xxx
 */
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE="user_login_state";
    /**
     * 普通用户
     */
    int DEFAULT_ROLE=0;
    /**
     * 管理员用户
     */
    int ADMIN_ROLE=1;
}
