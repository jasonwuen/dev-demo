package cn.net.wujun.dev.fastjson.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FastJsonHttpMessageConvertersConfiguration.
 */
@Configuration
@ConditionalOnProperty("http.converters.fastjson")
public class FastJsonAutoConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        logger.info("fastJsonHttpMessageConverter init");
        SerializeConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy
                .SnakeCase;
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty
        );
        ValueFilter valueFilter = new ValueFilter() {
            //o 是class
            //s 是key值
            //o1 是value值
            public Object process(Object o, String s, Object o1) {
                if (null == o1) {
                    o1 = "";
                }
                return o1;
            }
        };
        fastJsonConfig.setSerializeFilters(valueFilter);
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }
}
