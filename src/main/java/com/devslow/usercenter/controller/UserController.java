package com.devslow.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devslow.usercenter.common.BaseResponse;
import com.devslow.usercenter.common.ErrorCode;
import com.devslow.usercenter.exception.BusinessException;
import com.devslow.usercenter.mapper.UserMapper;
import com.devslow.usercenter.model.domain.User;
import com.devslow.usercenter.model.domain.request.UserLoginRequest;
import com.devslow.usercenter.model.domain.request.UserRegisterRequest;
import com.devslow.usercenter.service.UserService;
import com.devslow.usercenter.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.devslow.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.devslow.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author slow
 * @CurrentTime 2024-7-13 19:40:13
 */
@Slf4j
@RestController
// 用于编写restful风格的api，返回值为JSON类型
@RequestMapping("/user")
public class UserController {
//    Controller 调用Service，需要引入Service

    @Resource //默认按照名称进行注入
    private UserService userService;
    @Autowired // 默认按照类型进行注入
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return id
     */
    /*
     * 注册接口
     *       接收参数：用户账号，密码，确认密码
     *       请求类型：POST
     *       请求类型：JSON格式的数据
     *       返回值：用户注册的id
     * */
    @PostMapping("/register")
//    封装需要请求的用户参数 UserRegisterRequest
//    @RequestBody:用于在请求体中传递参数
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (!((StringUtils.hasLength(userAccount)) && (StringUtils.hasLength(userPassword)) && (StringUtils.hasLength(checkPassword)))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return User
     * @CurrentTime 2024-7-13 20:22:16
     */
    /*
     * 登录接口
     *       接收参数：用户账号，密码，session
     *       请求类型：POST
     *       请求类型：JSON格式的数据
     *       返回值：用户信息（脱敏后）
     * */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (!((StringUtils.hasLength(userAccount)) && (StringUtils.hasLength(userPassword)))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
//        return new BaseResponse<>(0,user,"ok");
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return User
     * @CurrentTime 2024-7-13 20:22:16
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     * @author slow
     * @CurrentTime 2024-7-14 13:18:48
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = currentUser.getId();
//        todo 校验用户是否合法
        User user = userService.getById(userId);
        User desensitizationUser = userService.getDesensitizationUser(user);
        return ResultUtils.success(desensitizationUser);
    }
//用户管理接口：必须鉴权
//    查询用户
//    删除用户

    /**
     * 查询用户接口
     * tag 根据用户名查询
     *
     * @param username
     * @return List<User>
     * @author slow
     * @CurrentTime 2024-7-14 06:39:42
     * @Update 完成分页数据模块开发
     * @UpdateTime 2024-7-15 20:10:16
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestParam(required = false) String username, HttpServletRequest request, @RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "3") int pageSize) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "仅管理员可以进行查询");
        }
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(username)) {
            queryWrapper.like("username", username);
        }
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
//        List<User> userList = userService.list(queryWrapper);
//        return userList.stream().map(user -> {
//           user.setUserPassword(null);
//           return user;
//        }).collect(Collectors.toList());

        //    脱敏后的用户集合
        List<User> userList = new ArrayList<>();
        //取出所有用户并得到原始的单个用户
        for (User user : userPage.getRecords()) {
            User desensitizationUser = userService.getDesensitizationUser(user);
            userList.add(desensitizationUser);
        }
        return ResultUtils.success(userList);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return boolean
     * @author slow
     * @CurrentTime 2024-7-14 06:46:40
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(HttpServletRequest request, @RequestBody Long id) {
        if (!isAdmin(request)) {
            log.info("delete user failed, user is not admin");
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (id <= 0) {
            log.info("delete user failed, id is invalid ");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不能小于0");
        }
        log.info("delete user is success ");
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request) {
        //        仅管理员可删除用户
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
