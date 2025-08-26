## 这是 seata 的安装

fasdfadfasdfasdf



在使用GitHub时，用户名为vongdefu，vongdefu.github.io仓库中已经完成了vitepress个人播客的搭建，
现在又有一个仓库：vongdefu-sourcecode-hub ，在这个仓库目录下有一个docs文件夹（其它文件是工程文件），里面是对这个仓库源码的说明文档，包含着很多以markdown为主的文件及文件夹。
我现在想要在vongdefu.github.io某一个连接下访问 vongdefu-sourcecode-hub 仓库中的这些说明文档。
我能想到的解决思路是：先在vongdefu-sourcecode-hub仓库中的gp-pages分支提供跟vongdefu.github.io一样的vitepress主题模板，
在监控到docs目录发生变化时，利用actions把docs目录下的文档拷贝到gp-pages分支完成发布，最后再在vongdefu.github.io导航栏中添加一个链接即可。
我之所以这么解决，是考虑到可能跨域的问题，也可能是我多想了。
请问有没有其他解决思路，如果有，请详细阐述，越详细越好；如果没有更好的解决思路，请你根据我提供的解决方案，提供一个action的配置。