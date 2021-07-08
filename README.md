# VBlog

## 后端目录
```
backend
│
├── admin                           // 管理员模块
│   └── team.blogserver.admin
│       └── controller
│       └── service
├── common                          // 公共模块
│   └── team.blogserver.common
│       ├── mapper
│       ├── model
│       │   ├── domain
│       │   └── dto
│       └── mybatisplus
├── core                            // 核心模块
│   └── team.ark.core
│       ├── cache
│       │   ├── config
│       │   └── store
│       ├── config
│       ├── druid
│       ├── exception
│       ├── lock
│       ├── request
│       ├── response
│       ├── security
│       ├── transaction
│       └── util
├── main                            // 启动模块
│   └── team.blogserver
│       ├── security
│       └── swagger
├── normal                          // 普通用户模块
│   └── team.blogserver.normal
│       ├── controller
│       └── service
│
```

## 前端目录

## 其他
- 后端使用Lombok插件
- 各人自己fork项目，并根据最新master分支来开个人功能分支，做完推到自己分支上后再向主仓库发pull request
- 不做Service接口
- JWT认证，走完登录接口：/user/login后，拿到token，请求其他需要登录的接口时再header中带上X-Token字段，值就是token内容 
- 创建文件带header
```
/**
 * 描述
 *
 * @author 作者
 * @date 时间
 */
```
