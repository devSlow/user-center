# 数据库初始化

```sql
-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态 0-正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0-普通用户 1-管理员'
)
    comment '用户';
```
# 使用mybatisPlusX插件生成代码
MyBatisX 插件，自动根据数据库生成 domain 实体对象、
mapper（操作数据库的对象）、
mapper.xml（定义了 mapper对象和数据库的关联，可以在里面自己写 SQL）、
service（包含常用的增删改查）、serviceImpl（具体实现 service）
