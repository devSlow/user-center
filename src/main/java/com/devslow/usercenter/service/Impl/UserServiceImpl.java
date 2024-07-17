package com.devslow.usercenter.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devslow.usercenter.common.ErrorCode;
import com.devslow.usercenter.exception.BusinessException;
import com.devslow.usercenter.mapper.UserMapper;
import com.devslow.usercenter.model.domain.User;
import com.devslow.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.devslow.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户注册
 *
 * @author slow
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-07-12 21:21:26
 * @return 返回新用户id
 */

/*
1. 用户在前端输入账户和密码、以及校验码（todo）
2. 校验用户的账户、密码、校验密码，是否符合要求
   1. 非空
   2. 账户长度 **不小于** 4 位
   3. 密码就 **不小于** 8 位吧
   4. 账户不能重复
   5. 账户不包含特殊字符
   6. 密码和校验密码相同
3. 对密码进行加密（密码千万不要直接以明文存储到数据库中）
4. 向数据库插入用户数据
* */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    /**
     * 盐值：混淆密码
     */
    private static final String SALT = "slow";
    //    Service调用Dao
    @Autowired
    UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 账号密码校验
        if (!(StringUtils.hasLength(userAccount) && StringUtils.hasLength(userPassword) && StringUtils.hasLength(checkPassword))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码不能为空");
        }
//        账户不小于4
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
//        密码及校验密码不小于8
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
//        账户不能包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern specialCharPattern = Pattern.compile(pattern);
        Matcher matcher = specialCharPattern.matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能包含特殊字符");
        }
//        密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验密码需要相同");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
//        使用md5对密码加密
        String handlePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//         向数据库插入数据
        User user = new User();

        user.setUserAccount(userAccount);
        user.setUserPassword(handlePassword);
        int saveResult = userMapper.insert(user);
        if (saveResult <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存错误");
        }
//     返回用户ID
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 返回脱敏用户信息
     * @author slow
     * @currentTime 2024-7-13 18:56:47
     */
    /*
1. 校验用户账户和密码是否合法

    1. 非空
    2. 账户长度 **不小于** 4 位
    3. 密码就 **不小于** 8 位吧
    4. 账户不包含特殊字符

2. 校验密码是否输入正确，要和数据库中的密文密码去对比
3. 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
4. 我们要记录用户的登录态（session），将其存到服务器上（用后端 SpringBoot 框架封装的服务器 tomcat 去记录）

        如何知道是哪个用户登录了？
          1. 连接服务器端后，得到一个 session 状态（匿名会话），返回给前端
          2. 登录成功后，得到了登录成功的 session，并且给该sessio n设置一些值（比如用户信息），返回给前端一个设置 cookie 的 ”命令“
                session => cookie
          3. 前端接收到后端的命令后，设置 cookie，保存到浏览器内
          4. 前端再次请求后端的时候（相同的域名），在请求头中带上cookie去请求
          5. 后端拿到前端传来的 cookie，找到对应的 session
          6. 后端从 session 中可以取出基于该 session 存储的变量（用户的登录信息、登录名）



5. 返回脱敏后的用户信息
6. 记录用户登录态
    * */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 账号密码校验
        if (!(StringUtils.hasLength(userAccount) && StringUtils.hasLength(userPassword))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码不能为空");
        }
//        账户不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
//        密码及校验密码不小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
//        账户不能包含特殊字符
        String pattern = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern specialCharPattern = Pattern.compile(pattern);
        Matcher matcher = specialCharPattern.matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能包含特殊字符");
        }

//        使用md5对密码加密
        String handlePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//   查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 判断用户输入数据是否和数据库中存在的列名相同
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", handlePassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            log.info("user login fail,userAccount can not match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
        }
        //     用户信息脱敏
        User desensitizationUser=getDesensitizationUser(user);
//        记录用户登录态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,desensitizationUser);
        log.info("user login success");
        return desensitizationUser;
    }

    /**
     * 用户注销
     * @param request
     * @author slow
     * @currentTime 2024-7-14 13:54:16
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户脱敏
     * @param originalUser
     * @return
     */
    @Override
    public User getDesensitizationUser(User originalUser){
        if (originalUser==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User desensitizationUser=new User();
        desensitizationUser.setId(originalUser.getId());
        desensitizationUser.setUsername(originalUser.getUsername());
        desensitizationUser.setUserAccount(originalUser.getUserAccount());
        desensitizationUser.setAvatarUrl(originalUser.getAvatarUrl());
        desensitizationUser.setGender(originalUser.getGender());
        desensitizationUser.setPhone(originalUser.getPhone());
        desensitizationUser.setEmail(originalUser.getEmail());
        desensitizationUser.setUserStatus(originalUser.getUserStatus());
        desensitizationUser.setCreateTime(originalUser.getCreateTime());
        desensitizationUser.setUserRole(originalUser.getUserRole());
        return desensitizationUser;
    }
}




