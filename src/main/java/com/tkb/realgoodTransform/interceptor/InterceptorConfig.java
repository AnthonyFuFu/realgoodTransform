package com.tkb.realgoodTransform.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	private final String[] excludePath = {"/webjars/**","/static/**", "/images/**" ,"/css/**", "/js/**", "/font-awesome/**", "/error/**"};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor())
			.addPathPatterns("/tkbrule/**").excludePathPatterns(excludePath);
	}
	
	@Bean
	public LoginInterceptor loginInterceptor() {
	    return new LoginInterceptor();
	}
	
	
}
