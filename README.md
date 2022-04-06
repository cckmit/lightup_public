<div align="center">
    <p align="center">
        <img src="./_web/public/logo_pao.svg" height="150" alt="logo"/>
    </p>
</div>

### 项目介绍

<div><h5>点明(Lightup)是一款采用当下流行的前后端分离架构形式开发的人脸识别考勤系统</h5></div>
<div><h4>结合SpringBoot+AntDesignVue开发，注释丰富，代码简洁。</h4></div>
<div align="center"><h5 align="center">#点明#有谐音"点名"的含义，帮助老师更好的了解学生课堂考勤情况，监督督促学生的课堂出勤，点亮学生更光明的明天</h5></div>

<p align="center">
        <a href="https://www.antdv.com/docs/vue/introduce-cn/">
            <img src="https://img.shields.io/badge/vue-2.x-blue.svg" alt="bootstrap">
        </a> 
        <a href="https://www.antdv.com/docs/vue/introduce-cn/">
            <img src="https://img.shields.io/badge/vue--ant--design-1.5.6-blue.svg" alt="bootstrap">
        </a> 
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.3.1-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.3.2-blue.svg" alt="mybatis-plus">
        </a>  
        <a href="./LICENSE">
            <img src="https://img.shields.io/badge/license-Apache%202-red" alt="license Apache 2.0">
        </a>
</p>

### 快速启动

运行环境需要安装：NodeJs（14.x）、npm、Mysql5.7、Jdk1.8、Maven3.6.3、开发工具idea

* 启动前端：打开_web文件夹，依赖下载完成后，运行npm install命令，再运行npm run serve
* 启动后端：打开application-local中配置数据库信息，运行LightupApplication类启动
* 浏览器访问：http://localhost:8081 （默认前端端口为：8081，后端端口为：8082）


### Lightup项目简要介绍

1、模块化架构设计，层次清晰
```
模块树
├─lightup                    ->项目工程
│  ├─lightup-base            ->框架基础模块
│       ├─lightup-core       ->核心模块
│       ├─lightup-gen        ->代码生成
│       ├─lightup-system     ->基础业务
│  ├─lightup-main            ->业务开始模块
│       ├─业务             ->其他具体业务
```

2、主要涉及技术栈介绍

前后端分离架构，分离开发，分离部署，前后端互不影响。

前端技术采用vue + antdvPro + axios。

后端采用spring boot + mybatis-plus + hutool等，开源可靠。

基于spring security(jwt) + 用户UUID双重认证。

基于AOP实现的接口粒度的鉴权，最细粒度过滤权限资源。

基于hibernate validator实现的校验框架，支持自定义校验注解。

提供Request-No的响应header快速定位线上异常问题。

在线用户可查，可在线踢人，同账号登录可同时在线，可单独在线（通过系统参数配置）。

支持前端 + 后端在线代码生成。

文件，短信，缓存，邮件等，利用接口封装，方便拓展。



### 详细功能

1. 主控面板、控制台页面，可进行工作台，分析页，统计等功能的展示。
2. 用户管理、对不同用户的进行权限维护，可绑定用户职位，班级，角色，数据权限等。
3. 应用管理、通过应用来控制不同维度的菜单展示。
4. 校园院系组织架构维护，支持多层级结构的树形结构。
5. 学生干部职位管理、教师职称管理，职务可作为用户的一个标签，职务目前没有和权限等其他功能挂钩。
6. 菜单管理、菜单目录，菜单，和按钮的维护是权限控制的基本单位。
7. 角色管理、角色绑定菜单后，可限制相关角色的人员登录系统的功能范围。角色也可以绑定数据授权范围。
8. 字典管理、系统内各种枚举类型的维护。
9. 访问日志、用户的登录和退出日志的查看和管理。
10. 操作日志、用户的操作业务的日志的查看和管理。
11. 服务监控、服务器的运行状态，Java虚拟机信息，jvm等数据的查看。
12. 在线用户、当前系统在线用户的查看。
13. 数据监控、druid控制台功能，可查看sql的运行信息。
14. 公告管理、系统的公告的管理。
15. 文件管理、文件的上传下载查看等操作，目前使用腾讯Cos存储。
16. 定时任务、定时任务的维护，通过cron表达式控制任务的执行频率。
17. 系统配置、系统运行的参数的维护，参数的配置与系统运行机制息息相关。
18. 邮件发送、发送邮件功能，该模块尚未完善。
19. 短信发送、短信发送功能，该模块尚未完善。


