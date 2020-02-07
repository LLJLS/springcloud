# spring-cloud 笔记

## 单体应用存在问题

- 随着业务的发展，开发变得越来越复杂。
- 修改、新增某个功能，需要对整个系统进行测试、重新部署。
- 一个模块出现问题，很可能导致整个系统崩溃。
- 多个开发团队同时对数据进行管理，容易产生安全漏洞。
- 各个模块使用同一种技术开发，各个模块很难根据实际情况选择更合适的技术框架，局限性很大。
- 模块内容过于复杂，如果员工离职，可能需要很长时间才能完成工作安排。

## 分布式、集群

集群：一台服务器无法负荷高并发的数据访问量，那么就设置十台服务器一起分担压力，十台不行就设置一百台（物理层面）。很多人干同一件事情，来分摊压力。

分布式：将一个复杂问题拆分成若干个简单的小问题，将一个大型的项目架构拆分成若干个微服务来协同完成（软件设计层面）。将一个庞大的工作拆分成若干个小步骤，分别由不同的人完成这些小步骤，最终将所有的结果进行整合实现大的需求。

服务治理的核心由三部分组成：服务提供者、服务消费者、注册中心

## 服务治理的核心由三部分组成：服务提供者、服务消费者、注册中心

在分布式系统架构中，每个微服务在启动时，将自己的信息存储在注册中心，叫做服务注册。

服务消费者从注册中心获取服务提供者的网络信息，通过该信息调用服务，叫做服务发现。

Spring Cloud 的服务治理使用 Eureka 来实现，Eureka 是 Netflix 开源的基于 REST 的服务治理解决方案，Spring Cloud 集成了 Eureka，提供服务注册和服务发现的功能，可以和基于 Spring Boot 搭建的微服务应用轻松完成整合，开箱即用，Spring Cloud Eureka。

## Spring Cloud Eureka

Eureka Server，注册中心

Eureka Client，所有要进行注册的微服务通过 Eureka Client 连接到 Eureka Server，完成注册。

1. ### Eureka Server 代码实现

   - #### 创建父工程，pom.xml

     ```xml
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.2.1.RELEASE</version>
     </parent>
     
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
     </dependencies>
     
     <dependencyManagement>
         <dependencies>
             <dependency>
                 <groupId>org.springframework.cloud</groupId>
                 <artifactId>spring-cloud-dependencies</artifactId>
                 <version>Hoxton.BUILD-SNAPSHOT</version>
                 <scope>import</scope>
                 <type>pom</type>
             </dependency>
              <dependency>
                 <groupId>org.projectlombok</groupId>
                 <artifactId>lombok</artifactId>
                 <version>1.18.10</version>
                 <scope>provided</scope>
             </dependency>
         </dependencies>
     </dependencyManagement>
     ```

   * #### 在父工程下创建Module,pom.xml

     ```xml
     <dependencies>
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-eureka-server</artifactId>
         </dependency>
     </dependencies>
     ```

   * #### 创建配置文件 application.yml,添加 Eureka Server 相关配置

     ```yaml
     server:
         port: 8010
     eureka:
       client:
         register-with-eureka: false
         fetch-registry: false
         serviceUrl:
           defaultZone: http://localhost:8010/eureka/
     ```

     > 属性说明

     `server.port`: 当前 Eureka Server 服务端口。

     `eurka.client.register-with-eureka`: 是否将当前的 Eureka Server 服务作为客户端进行注册。

     `eureka.client.fetch-registry`: 是否获取其他 Eureka Server 服务的数据。

     `eureka.client.serverUrl.defaultZone`: 注册中心的访问地址。

   * 创建启动类

     ```java
     package com.kevin.springcloud;
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
     
     @SpringBootApplication
     @EnableEurekaServer
     public class EurekaServerApplication {
         public static void main(String[] args) {
             SpringApplication.run(EurekaServerApplication.class,args);
         }
     }
     ```

     > 注解说明

     `@SpringBootApplication`: 声明该类是 Spring Boot 服务的入口。

     `@EnableEurekaServer`: 声明该类是一个Eureka Server 微服务，提供服务注册和发现功能，即注册中心。

2. ### Eureka Client 代码实现

   * 创建Module，pom.xml

     ```xml
     <dependencies>
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
         </dependency>
     </dependencies>
     ```

   * 创建配置文件 application.yml, 添加 Eureka Client 相关配置

     ```yaml
     server:
         port: 8020
     spring:
       application:
         name: provider
     eureka:
       client:
         serviceUrl:
           default-zone: http://localhost:8010/eureka/
         instance:
           prefer-ip-address: true
     ```

     > 属性说明

     `spring.application.name`: 当前服务注册在 Eureka Server 上的名称。

     `eureka.client.serverUrl.default-zone`: 注册中心的访问地址。

     `eureka.client.instance.prefer-ip-address`: 是否将当前服务的IP注册到 Eureka Server。

   * 创建启动类

     ```java
     package com.kevin.springcloud;
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
     
     @SpringBootApplication
     //@EnableEurekaClient
     public class EurekaClientApplication {
         public static void main(String[] args) {
             SpringApplication.run(EurekaClientApplication.class,args);
         }
     }
     ```

   * 创建实体类 Student.java

     ```java
     package com.kevin.springcloud.entity;
     
     import lombok.AllArgsConstructor;
     import lombok.Data;
     import lombok.NoArgsConstructor;
     import lombok.RequiredArgsConstructor;
     
     @Data
     @NoArgsConstructor
     @AllArgsConstructor
     public class Student {
         private Long id;
         private String name;
         private Integer age;
     }
     ```

   * 创建仓库接口 StudentRepository.java

     ```java
     package com.kevin.springcloud.repository;
     
     import com.kevin.springcloud.entity.Student;
     import java.util.Collection;
     
     public interface StudentRepository {
         public Collection<Student> findAll();
         public Student findById(Long id);
         public void saveOrUpdate(Student student);
         public void deleteById(Long id);
     }
     ```

   * 创建仓库接口实现类 StudentRepositoryImpl.java

     ```java
     package com.kevin.springcloud.repository.impl;
     
     import com.kevin.springcloud.entity.Student;
     import com.kevin.springcloud.repository.StudentRepository;
     import org.springframework.stereotype.Repository;
     
     import java.util.Collection;
     import java.util.HashMap;
     import java.util.Map;
     
     @Repository
     public class StudentRepositoryImpl implements StudentRepository {
         private static Map<Long,Student> studentMap;
     
         static {
             studentMap = new HashMap<>();
             studentMap.put(1L,new Student(1L,"Mike",25));
             studentMap.put(2L,new Student(2L,"Tom",26));
             studentMap.put(3L,new Student(3L,"Jerry",27));
         }
         @Override
         public Collection<Student> findAll() {
             return studentMap.values();
         }
     
         @Override
         public Student findById(Long id) {
             return studentMap.get(id);
         }
     
         @Override
         public void saveOrUpdate(Student student) {
             studentMap.put(student.getId(),student);
         }
     
         @Override
         public void deleteById(Long id) {
             studentMap.remove(id);
         }
     }
     ```

   * 创建控制类 StudentHandler.java

     ```java
     package com.kevin.springcloud.controller;
     
     import com.kevin.springcloud.entity.Student;
     import com.kevin.springcloud.repository.StudentRepository;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.web.bind.annotation.*;
     
     import java.util.Collection;
     
     @RestController
     @RequestMapping("/student")
     public class StudentHandler {
         @Autowired
         private StudentRepository studentRepository;
     
         @GetMapping("/findAll")
         public Collection<Student> findAll() {
             return studentRepository.findAll();
         }
     
         @GetMapping("/findById/{id}")
         public Student findById(@PathVariable("id") Long id) {
             return studentRepository.findById(id);
         }
     
         @PostMapping("/save")
         public void save(@RequestBody Student student) {
             studentRepository.saveOrUpdate(student);
         }
     
         @PutMapping("/update")
         public void update(@RequestBody Student student) {
             studentRepository.saveOrUpdate(student);
         }
     
         @DeleteMapping("/deleteById/{id}")
         public void deleteById(@PathVariable("id") Long id) {
             studentRepository.deleteById(id);
         }
     }
     ```

## RestTemplate 的使用 

- 什么是 RestTemplate?

RestTemplate 是 Spring 框架提供的基于 REST 的服务组件，底层是对 HTTP 请求及响应的封装，提供了很多访问 REST 服务的方法，可以简化代码开发。

- 如何使用 RestTemplate?

  1. 创建 Maven Module，pom.xml

  2. 创建实体类

     ```java
     package com.kevin.springcloud.entity;
     
     import lombok.AllArgsConstructor;
     import lombok.Data;
     import lombok.NoArgsConstructor;
     
     @Data
     @NoArgsConstructor
     @AllArgsConstructor
     public class Student {
         private Long id;
         private String name;
         private Integer age;
     }
     ```

  3. 创建 controller

     ```java
     package com.kevin.springcloud.controller;
     
     import com.kevin.springcloud.entity.Student;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.web.bind.annotation.*;
     import org.springframework.web.client.RestTemplate;
     
     import java.util.Collection;
     
     @RestController
     @RequestMapping("/rest")
     public class RestHandler {
         @Autowired
         RestTemplate restTemplate;
     
         @GetMapping("/findAll")
         public Collection<Student> findAll() {
             return restTemplate.getForEntity("http://localhost:8020/student/findAll",Collection.class).getBody();
         }
         @GetMapping("/findAll2")
         public Collection<Student> findAll2() {
             return restTemplate.getForObject("http://localhost:8020/student/findAll",Collection.class);
         }
     
         @GetMapping("/findById/{id}")
         public Student findById(@PathVariable("id") Long id) {
             return restTemplate.getForEntity("http://localhost:8020/student/findById/{id}",Student.class,id).getBody();
         }
         @GetMapping("/findById2/{id}")
         public Student findById2(@PathVariable("id") Long id) {
             return restTemplate.getForObject("http://localhost:8020/student/findById/{id}",Student.class,id);
         }
     
         @PostMapping("/save")
         public void save(@RequestBody Student student) {
             restTemplate.postForEntity("http://localhost:8020/student/save",student,null);
         }
         @PostMapping("/save2")
         public void save2(@RequestBody Student student) {
             restTemplate.postForObject("http://localhost:8020/student/save",student,null);
         }
     
         @PutMapping("/update")
         public void update(@RequestBody Student student) {
             restTemplate.put("http://localhost:8020/student/update",student);
         }
     
         @DeleteMapping("/deleteById/{id}")
         public void deleteById(@PathVariable("id") Long id) {
             restTemplate.delete("http://localhost:8020/student/deleteById",id);
         }
     
     }
     ```

  4. 创建启动类

     ```java
     package com.kevin.springcloud;
     
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.context.annotation.Bean;
     import org.springframework.web.client.RestTemplate;
     
     @SpringBootApplication
     public class RestTemplateApplication {
         public static void main(String[] args) {
             SpringApplication.run(RestTemplateApplication.class,args);
         }
     
         @Bean
         public RestTemplate restTemplate() {
             return new RestTemplate();
         }
     }
     ```

- 服务消费者 consumer

  1. 创建 Module pom.xml

     ```xml
     <dependencies>
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
         </dependency>
     </dependencies>
     ```

     

  2. 配置文件 application.yml

     ```yaml
     server:
       port: 8030
     spring:
       application:
         name: consumer
     eureka:
       client:
         service-url:
           defaultZone: http://localhost:8010/eureka/
       instance:
         prefer-ip-address: true
     ```

  3. 实体、controller、启动类 一样

## 服务网关

Spring Cloud 集成了 Zuul 组件，实现服务网关。

- 什么是zuul？

Zuul 是 Netflix 提供的一个开源的 API 网关服务器，是客户端和网站后端所有请求的中间层，对外开放 API，将所有请求导入统一的入口，屏蔽了服务端的具体实现逻辑，Zuul可以实现反向代理的功能，在网关内部实现动态路由、身份认证、IP过滤、数据监控等。

* 如何使用 Zuul？

  -  创建 Maven Module，pom.xml

    ```xml
     <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
            </dependency>
        </dependencies>
    ```

  - 配置文件 application.yml

    ```yaml
    server:
      port: 8040
    spring:
      application:
        name: zuul
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8010/eureka/
        instance:
          prefer-ip-address: true
    zuul:
      routes:
        provider: /p/**
        consumer: /c/**
    ```

    > 属性说明

    `zuul.routes.*`: 给各个服务提供映射。

  - 启动类

    ```java
    package com.kevin.springcloud;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
    import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
    
    @SpringBootApplication
    @EnableZuulProxy
    @EnableEurekaClient
    public class ZuulApplication {
        public static void main(String[] args) {
            SpringApplication.run(ZuulApplication.class,args);
        }
    }
    ```

    > 注解说明

    `@EnableZuulProxy`: 包含了 `@EnableZuulServer`,这是网关的启动类。

    `@EnableAutoConfiguration`: 可以帮助 Spring Boot 应用将所有符合条件的 `@Configuration` 配置加载到当前 Spring Boot 创建并使用的 IOC 容器中。

- Zuul 自带了负载均衡功能，修改 provider 代码。

## Ribbon 负载均衡

- 什么是 Ribbon？

Spring Cloud Ribbon 是一个负载均衡解决方案，Ribbon 是 Netflix 发布的负载均衡器，Spring Cloud Ribbon 是基于 Netflix Ribbon 实现的，是一个用于对 HTTP 请求进行控制的负载均衡客户端。

在注册中心对 Ribbon 进行注册后，Ribbon 就可以基于某种负载均衡算法，如轮询、随机、加权轮询、加权随机等自动帮助服务消费者调用接口，开发者也可以根据具体需求自定义 Ribbon 负载均衡算法，实际开发中，Spring Cloud Ribbon 需要结合 Spring Cloud Eureka 来使用，Eureka SErver 提供所有可以调用的服务提供者列表，Ribbon 基于特定的负载均衡算法从这些服务提供者中选择调用的具体实例。

- 创建 Module,pom.xml

  ```xml
  <dependencies>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
      </dependency>
  </dependencies>
  ```

- 创建配置文件，application.yml

  ```yaml
  server:
    port: 8050
  spring:
    application:
      name: ribbon
  eureka:
    client:
      service-url: 
        defaultZone: http://localhost:8010/eureka/
      instance:
        prefer-ip-address: true
  ```

- 创建启动类

```java
package com.kevin.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class,args);
    }
}

@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

> 注解说明

`@LoadBalanced`: 声明一个基于 Ribbon 的负载均衡。 

- Handler

```java
package com.kevin.springcloud.controller;

import com.kevin.springcloud.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/ribbon")
public class RibbonHandler {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/findAll")
    public Collection<Student> findAll() {
        return restTemplate.getForObject("http://provider/student/findAll",Collection.class);
    }

    @GetMapping("/port")
    public String port() {
        return restTemplate.getForObject("http://provider/student/port",String.class);
    }
}
```

## Fegin

- 什么是 Fegin？

与 Ribbon 一样，Fegin 也是由 Netflix 提供的，Fegin 是一个声明式，模板化的 Web Service 客户端，它简化了开发者编写 Web 服务客户端的操作，开发者可以通过简单的接口和注解来调用HTTP API，Spring Cloud Fegin，它聚合了 Ribbon 和 Hystrix，具有可插拔，基于注解、负载均衡、服务熔断等一系列便捷功能。



相比较于 Ribbon + RestTemplate 的方式，Fegin大大简化了代码的开发，Fegin 支持多种注解，包括Fegin 注解、JAX-RS 注解、Spring MVC 注解等，Spring Cloud 对 Fegin 进行了优化，整合了Ribbon 和 Eureka，从而让Fegin的使用更加方便。



- Ribbon 和 Fegin 的区别

Ribbon 是一个通用的 HTTP 客户端工具，Fegin 是基于 Ribbon 实现的。

- Fegin 的特点

1. Fegin 是一个声明式的 Web Service 客户端。
2. 支持 Fegin 注解、Spring MVC 注解、JAX-RS 注解。
3. Fegin 基于 Ribbon 实现，使用起来更加简单。
4. Fegin 集成了 Hystrix，具有服务熔断功能。

- 创建 Module,pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
</dependencies>
```

- 创建配置文件，application.yml

```yaml
server:
  port: 8060
spring:
  application:
    name: fegin
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8010/eureka/
  instance:
    prefer-ip-address: true
```

- 创建启动类

```java
package com.kevin.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FeginApp {
    public static void main(String[] args) {
        SpringApplication.run(FeginApp.class,args);
    }
}
```

-  创建声明式接口

```java
package com.kevin.springcloud.service;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.impl.FeginServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "provider")
public interface FeginService {
    @GetMapping("/student/findAll")
    public Collection<Student> findAll();
    @GetMapping("/student/port")
    public String port();
}
```

- handler

```java
package com.kevin.springcloud.controller;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.FeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/fegin")
public class FeginHandler {

    @Autowired
    FeginService feginService;

    @GetMapping("/findAll")
    public Collection<Student> findAll(){
        return feginService.findAll();
    }

    @GetMapping("/port")
    public String port() {
        return feginService.port();
    }
}
```

- 服务熔断，application.yml 添加熔断机制。

```yml
feign:
  hystrix:
    enabled: true
```

`fegin.hystrix.enabled`: 是否开启熔断器。

- 创建 FeginService 接口的实现类 FeginError，定义容错处理逻辑，通过`@Component`注解将 FeginError 注入到 IOC 容器中。

```java
package com.kevin.springcloud.service.impl;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.FeginService;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class FeginError implements FeginService {
    @Override
    public Collection<Student> findAll() {
        return null;
    }

    @Override
    public String port() {
        return "服务维护中。。。";
    }
}
```

- 在 FeginService 中通过 `@FeginClient`的 fallback 属性设置映射。

```java
package com.kevin.springcloud.service;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.impl.FeginError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "provider",fallback = FeginError.class)
public interface FeginService {
    @GetMapping("/student/findAll")
    public Collection<Student> findAll();
    @GetMapping("/student/port")
    public String port();
}
```

## Hystrix 容错机制

在不改变各个微服务调用关系的前提下，针对错误情况进行预先处理。

- 设计原则

1. 服务隔离机制
2. 服务降级机制
3. 熔断机制
4. 提供实时的监控和报警功能
5. 提供实时的配置修改功能

Hystrix 数据监控需要结合 Spring Cloud Actuator 来使用，Acuator 提供了对服务的健康、数据统计，可以通过 hystrix.stream 节点获取监控的请求数据，提供了可视化对的监控界面。

- 创建 Maven，pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
    </dependency>
    <dependency>
        <groupId>com.netflix.hystrix</groupId>
        <artifactId>hystrix-javanica</artifactId>
        <version>RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

- 创建配置文件，application.yml

```yml
server:
  port: 8070
spring:
  application:
    name: hystrix
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8010/eureka/
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

- 创建启动类

```java
package com.kevin.springcloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableHystrix
public class HystrixApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApp.class,args);
    }


//    @Bean
//    public ServletRegistrationBean getServlet(){
//        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
//        registrationBean.setLoadOnStartup(1);
//        registrationBean.addUrlMappings("/actuator/hystrix.stream");
//        registrationBean.setName("HystrixMetricsStreamServlet");
//        return registrationBean;
//    }
}
```

> 注解说明

`@EnableCircuitBreaker`: 声明启用数据监控。

`@EnableHystrixDashboard`: 声明启用可视化数据监控。

- handler

```java
package com.kevin.springcloud.controller;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.servce.FeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/hystrix")
public class HystrixHandler {

    @Autowired
    private FeginService feginService;


    @GetMapping("/findAll")
    public Collection<Student> findAll() {
        return feginService.findAll();
    }

    @GetMapping("/port")
    public String port() {
        return feginService.port();
    }
}
```

- 启动成功后，访问 http://localhost:8070/actuator/hystrix.stream 可以监控到请求数据。
- 访问 http://localhost:8070/hystrix,可以看到可视化的监控界面，输入要监控的地址节点即可看到 该节点可视化数据监控。

## Spring Cloud Config

Spring Cloud Config, 通过服务端可以为多个客户端提供配置服务。Spring Cloud Config 可以把配置文件存储在本地，也可以将配置文件存储在远程git仓库，创建 Config Server，通过它管理所有配置文件。

### Spring Cloud Config 本地配置

#### 本地文件系统

- 创建Maven，pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
</dependencies>
```

- 创建配置文件，application.yml

```yaml
server:
  port: 8090
spring:
  application:
    name: configserver
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared
```

> 属性说明

`spring.profiles.active`: 配置文件的获取方式。

`spring.cloud.config.server.native.search-locations: 本地配置文件存放路径。`

- resources 路径下创建 shared 文件夹，并在此路径下创建 nativeconfigclient-dev.yml

```yaml
data: lol
```

- 创建启动类

```java
package com.kevin.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class NativeConfigServerApp {
    public static void main(String[] args) {
        SpringApplication.run(NativeConfigServerApp.class,args);
    }
}
```

> 注解说明

`@EnableConfigServer`: 声明启动配置中心。

#### 客户端

- 创建Maven,pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
</dependencies>
```

- 创建配置文件，bootstrap.yml

```yaml
spring:
  application:
    name: nativeconfigclient
  profiles:
    active: dev
  cloud:
      uri: http://localhost:8090
      fail-fast: true
```

> 属性说明

`spring.cloud.config.uri`: 本地 Config Server 访问路径。

`spring.cloud.config.fail-fase`: 设置客户端优先判断 Config Server 访问是否正常。

通过 `spring.application.name` 和 `spring.profiles.active` 拼接目标配置文件名，nativeconfigclient-dev.yml,去 Config Server 中查找此文件。

###  Spring Cloud Config 远程配置

- 创建 Maven，pom.xml