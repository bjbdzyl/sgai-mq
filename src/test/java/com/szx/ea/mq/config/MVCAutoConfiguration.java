package com.szx.ea.mq.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szx.core.json.JacksonMapper;
import com.szx.core.web.interceptor.UserPermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.szx.ea.mq.controller"})
public class MVCAutoConfiguration extends WebMvcConfigurerAdapter {
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxInMemorySize(209715200);
		commonsMultipartResolver.setMaxUploadSize(838860800);
		return commonsMultipartResolver;
	}
	
	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		super.configureContentNegotiation(configurer);
		
		Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
		mediaTypes.put("xml", MediaType.APPLICATION_XML);
		mediaTypes.put("json", MediaType.APPLICATION_JSON_UTF8);
		mediaTypes.put("xls", new MediaType("application","vnd.ms-excel"));
		mediaTypes.put("js", new MediaType("application","javascript"));
		
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
		configurer.mediaTypes(mediaTypes);
	}

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        //集团用户过滤
        InterceptorRegistration ir2 = registry.addInterceptor(new UserPermissionInterceptor());
        ir2.addPathPatterns("/**");
        
        ir2.excludePathPatterns("/sys/function/export");
        ir2.excludePathPatterns("/common/*.js");

        // 还可以在这里注册其它的拦截器
        //registry.addInterceptor(new OtherInterceptor()).addPathPatterns("/**");
    }
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
		converters.add(stringHttpMessageConverter());
	}
	
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter(){
		StringHttpMessageConverter sh = new StringHttpMessageConverter();
		sh.setDefaultCharset(Charset.forName("UTF-8"));
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.ALL);
		sh.setSupportedMediaTypes(supportedMediaTypes);
		return sh;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter mjth = new MappingJackson2HttpMessageConverter(jacksonObjectMapper());
		mjth.setPrettyPrint(true);
		return mjth;
	}
	
	@Bean
	public JacksonMapper jacksonObjectMapper(){
		JacksonMapper mapper = new JacksonMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
}
