package com.tkb.realgoodTransform.config;

import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.tkb.realgoodTransform.utils.ec.SSL;

@Configuration
public class RestTemplateConfig {
	
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		RestTemplate restTemplate = new RestTemplate(factory);
		//支持中文編碼
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
	
	@Bean
	public ClientHttpRequestFactory simClientHttpRequestFactory() {
		SSL factory = new SSL();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(5000);
		return factory;
	}
}
