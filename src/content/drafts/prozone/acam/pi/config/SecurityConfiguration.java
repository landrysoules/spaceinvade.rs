package rs.prozone.acam.pi.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Klasa za security.
 * 
 * @author milos.poljak
 *
 */
@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// FIXME: See if web configure makes sense (basically it will be used only if eventuallt tomcat serves all static resources.
	@Override
	public void configure(WebSecurity web) throws Exception {
		// @formatter:off
		web
			.ignoring()
				.antMatchers("/images/**", "/styles/**", "/js/**", "/scripts/**", "/fonts/**");
		// @formatter:on
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.httpBasic()
		.and()
			.authorizeRequests()
		//	.antMatchers("/", "/index.html", "/login/**", "/rest/korisniks/agencijaLogin/**", "/rest/korisniks/exists/**", "/rest/korisniks/login/**", "/izvestaj.jsp", "/rest/report/public/generate", "/rest/report/public/generateSprovodjenje").permitAll()
				.anyRequest().authenticated()
				.and().
			    csrf().disable();
	}
}
