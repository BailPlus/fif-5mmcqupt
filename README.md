# 一个修改科大讯飞fif口语平台的软件

### 感谢由"草莓味小南梁提供的python版，exe位于fif_python.zip，源代码fif_python"
### 当python出问题时可以试下java版，链接如下（比较大，带有环境）：
链接: https://pan.baidu.com/s/1Hkt66u8mXCIui7cC3sBcOQ?pwd=5mm9 提取码: 5mm9

---
只需要两个东西：

1.模块URL

2.Token

模块的URL就是形如

[学习页面](https://static.fifedu.com/static/fiforal/kyxl-web-static/student-h5/index.html#/pages/course/studyPage/studyPage?unitid=8d7a0436acc141a48dbcaa3f1bf58816&type=2&taskId=0)

这样的URL

Token需要在登陆fif平台后，右键选择检查（或f12）打开网页开发者工具，接着选择network（网络），随便访问几个fif内的网站，选择Fetch/XHR选项，看看列出来的几个请求信息，里面会有形如：

authorization:

Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbl9uYW1lIjoiY3F1cHQyMDI0MjEyMDY4IiwidXNlcl9pZCI6ImY2ZGQxYzFiZjI0YTQ4MWViMjAyYTNjN2ZiNDUwYzNiIiwidXNlcl9rZXkiOiI3MTFjZDc0NGI2NTk0OGUwOTMxZGY3ZWM3ZGU3ZjdiOCIsImV4cCI6MTc0MTkyMzYxMCwiaWF0IjoxNzQxOTE2NDEwfQ.5xS-oTnerFDrpXtraCep6CwedDiD2v0IUleJvbM5fM0

这里的

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbl9uYW1lIjoiY3F1cHQyMDI0MjEyMDY4IiwidXNlcl9pZCI6ImY2ZGQxYzFiZjI0YTQ4MWViMjAyYTNjN2ZiNDUwYzNiIiwidXNlcl9rZXkiOiI3MTFjZDc0NGI2NTk0OGUwOTMxZGY3ZWM3ZGU3ZjdiOCIsImV4cCI6MTc0MTkyMzYxMCwiaWF0IjoxNzQxOTE2NDEwfQ.5xS-oTnerFDrpXtraCep6CwedDiD2v0IUleJvbM5fM0

就是我们需要的Token（去掉Bearer）
token有时效性，一段时间内token有效，目测是30分钟

接着打开程序输入url，token即可

（不会的话可以搜一下如何获取网页请求头中的token或问ai！）

----

原理是抓接口，发现fif平台的修改成绩的接口没鉴权，而查询题目的接口需要鉴权

所以可以拿到token去拿到题目，再一次性修改题目成绩

token照理来说是应该可以通过登陆接口获得，可惜我不懂前端

有懂的大佬可以修改一下，或者前端大佬做个自动截取token的浏览器插件会很不错！

----
增加了Python版本，基于~AI~PyQt5做了GUI~太伟大了GPT~，Python版本基本上都是基于Java源码进行了翻译

例如使用了requests库进行请求，json库进行解析，面向过程编程居多（飞舞不会OOP）

Python控制台版本打包后大概是Java版的1/10，GUI版大概是1/2左右

感谢Thanwinde开源，以及教我这个连GitHub协作开发都不太会用的飞舞基本的协作常识

By Kafka           

[打包版下载](https://github.com/Thanwinde/fif-5mmcqupt/blob/master/fif_python.zip)  












