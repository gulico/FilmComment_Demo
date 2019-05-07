package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
/**
 * 使用bean方式注入fastjson解析器
 */
@Bean
public HttpMessageConverters fastJsonHttpMessageConverters() {
    // 创建fastjson对象
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

    FastJsonConfig confg = new FastJsonConfig();
    // 设置是否需要格式化
    confg.setSerializerFeatures(SerializerFeature.PrettyFormat);
    converter.setFastJsonConfig(confg);
    return new HttpMessageConverters(converter);
}
}

