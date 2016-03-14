package rs.prozone.acam.pi.service.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.entity.UlogaKorisnika;
import rs.prozone.acam.pi.service.KorisnikService;

/**
 * Utility class for Spring Security.
 * 
 * @author Landry Soules
 */
@Component
public final class SecurityUtils {

	@Autowired
	private KorisnikService korisnikService;

	/**
	 * Instantiates a new security utils.
	 */
	private SecurityUtils() {
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the current login
	 */
	public static String getCurrentLogin() {
		Korisnik korisnik = getCurrentAuthenticatedUser();
		if (korisnik != null) {
			return korisnik.getUsername();
		} else {
			return null;
		}
	}

	/**
	 * Gets the current authenticated user.
	 *
	 * @return the current authenticated user
	 */
	public static Korisnik getCurrentAuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof Korisnik) {
			return ((Korisnik) principal);
		} else {
			return null;
		}
	}

	/**
	 * Gets the current user data.
	 *
	 * @return the current user data
	 */
	public static Map<String, Object> getCurrentUserDataFromRequest(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> userData = new HashMap<String, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof Korisnik) {
			Korisnik user = (Korisnik) principal;

			userData.put("userName", user.getKorisnickoIme());
		
			userData.put("roles", AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
		}


		return userData;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrentUserData() {
		Map<String, Object> userData = new HashMap<String, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Korisnik user = null;
		if (principal instanceof Korisnik) {
			user = ((Korisnik) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			userData.put("userName", user.getKorisnickoIme());
			userData.put("userId", user.getId());
			}
		List<UlogaKorisnika> authorities = (List<UlogaKorisnika>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> roles = new ArrayList<String>();
		// for (UlogaKorisnika role : authorities) {
		// roles.add(role.getAuthority());
		// }
		for (GrantedAuthority role : authorities) {
			roles.add(role.getAuthority());
		}
		userData.put("roles", roles);

		return userData;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getCurrentAuthenticatedUserRoles() {
		Korisnik user = SecurityUtils.getCurrentAuthenticatedUser();
		List<String> roles = new ArrayList<String>();
		if (user != null) {
			List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
			if (authorities != null)
				if (authorities.size() > 0) {
					for (GrantedAuthority authority : authorities) {
						roles.add(authority.getAuthority());
					}
				}
		}
		return roles;
	}


	/**
	 * Inicijalizuje anonimnog korisnika.
	 * 
	 * @param korisnik
	 */
	public static void initAnonymous(UserDetails korisnik) {
		AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken("anonymous", korisnik, AuthorityUtils.createAuthorityList("ANONYMOUS"));
		SecurityContextHolder.getContext().setAuthentication(anonymousToken);
	}

}
