 **# keywords** 

#### 介绍
简单的一个编程小游戏 和划水(跑路)队友一起做的，没用什么框架，就是简单的canvas paint bitmap完成的代码都有注释 

#### 软件架构
简单的一个编程小游戏 和划水(跑路)队友一起做的，没用什么框架，就是简单的canvas paint bitmap完成的代码都有注释
没有架构



#### 安装教程


    
1. 下载项目
1. 导入idea/Android studio
1. 运行项目


#### 使用说明

1.  author: @无关几何 @ly(跑路了)


#### 参与贡献

1.   维护: @无关几何
1.   提交代码 @无关几何

  
#### sql
  --drop database keyword
  create database keyword on(
  name='keywordDB',
  filename='c:\database\keywordDB.mdf',
  size=10mb,
  maxsize=40mb,
  filegrowth=2mb
  ) log on(
  name='keywordDB_log',
  filename='c:\database\keywordDB_log.ldf',
  size=5mb,
  maxsize=20mb,
  filegrowth=1mb
  )
  
  create table score(
  	username varchar(20) primary key not null,
  	password varchar(20) not null,
  	score int default(0) not null
  )



