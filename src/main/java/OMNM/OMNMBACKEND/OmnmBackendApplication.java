package OMNM.OMNMBACKEND;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@SpringBootApplication
//12
public class OmnmBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmnmBackendApplication.class, args);
	}

	@Bean
	public HttpFirewall allowUrlSemicolonHttpFirewall(){
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true);
		return firewall;
	}

}
