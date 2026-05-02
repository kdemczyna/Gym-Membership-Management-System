package api.gymmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.h2.server.web.JakartaWebServlet;



@SpringBootApplication
public class GymManagementApplication {


	public static void main(String[] args) {
		SpringApplication.run(GymManagementApplication.class, args);
	}
	@Bean
	public ServletRegistrationBean<JakartaWebServlet> h2Console() {
		ServletRegistrationBean<JakartaWebServlet> bean =
				new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
		bean.addInitParameter("webAllowOthers", "true");
		return bean;
	}


}
