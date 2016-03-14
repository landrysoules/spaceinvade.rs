package rs.prozone.acam.pi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import rs.prozone.acam.pi.service.KorisnikService;

@Configuration
public class AuthConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	KorisnikService korisnikService;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(korisnikService);
	}

}
