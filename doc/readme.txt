#20180524
添加了cjShop-common-config工程,
本工程中删除:resources.properties和public_system.properties,
改为从cjShop-common-config工程打成的jar包中引入资源文件.

删除了pojo,改为由cjShop-common-core工程的jar引入.


#20180523
添加了dubbo的xml配置方式 和 注解配置的方式.
两种方式都已实现.
目前支持的工程:
    服务生产者: cjShop-service-rest
    服务接口: cjShop-facade-rest
    服务消费者: cjShop-front
两种配置方式, 见各自的dubbo配置文件 和 service中的 注释.
当前默认的是注解的配置方式.



