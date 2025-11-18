package medlink.common.config;

import medlink.auth.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

   @Bean
   public FilterRegistrationBean<AuthFilter> authFilter() {
       FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
       registrationBean.setFilter(new AuthFilter());
       registrationBean.addUrlPatterns("/*"); // 필터가 모든 요청에 적용되도록 설정
       registrationBean.setOrder(1); // 필터의 순서를 설정 (0이 가장 우선)
       return registrationBean;
   }
}
