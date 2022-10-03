package com.dxc.Outstagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dxc.Outstagram.filter.AdminFilter;

@SpringBootApplication
@EnableJpaAuditing
public class OutstagramApplication {
	
	@Autowired
	private AdminFilter af;

	public static void main(String[] args) {
		SpringApplication.run(OutstagramApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	public AuditorAware<String> auditorAware(){
//	    return new SpringSecurityAuditorAware<>();
//	}
	
	@Bean
	public FilterRegistrationBean<AdminFilter> checkAdmin() {
		FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(af);
		registrationBean.addUrlPatterns("/admin/*");
		return registrationBean;
		
	}
	
}
