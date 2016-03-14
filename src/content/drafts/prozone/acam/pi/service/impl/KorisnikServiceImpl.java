package rs.prozone.acam.pi.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import rs.prozone.acam.pi.entity.BaseBean;
import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.entity.dto.ChangeCredentialsDTO;
import rs.prozone.acam.pi.exception.AcasException;
import rs.prozone.acam.pi.repository.KorisnikRepository;
import rs.prozone.acam.pi.service.KorisnikService;
import rs.prozone.acam.pi.service.security.SecurityUtils;
import rs.prozone.acam.pi.util.Encryption;
import rs.prozone.acam.pi.validation.KorisnikValidator;

@Service
public class KorisnikServiceImpl extends AcasServiceImpl<Korisnik, Long> implements KorisnikService {

	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private Pattern pattern;
	
	@Autowired
	private Encryption encryption;
	
	@Autowired
	private KorisnikValidator validator;
	
	@Autowired
	private KorisnikRepository korisnikRepository;


	@Override
	public Korisnik save(Korisnik korisnik) throws Exception {
		return super.save(korisnik);
	}

	@Override
	public boolean doesKorisnikExists(String username) {
		return korisnikRepository.countByKorisnickoIme(username) > 0;
	}

	@Override
	public Korisnik findKorisnikByKorisnickoIme(String korisnickoIme) {
		return korisnikRepository.findKorisnikByKorisnickoIme(korisnickoIme);
	}

	@SuppressWarnings("unchecked")
	public Korisnik saveB(Korisnik korisnik) throws Exception {
		return null;

	}

	@Override
	public void changeCredentials(ChangeCredentialsDTO dto) throws Exception {
		Korisnik korisnik = SecurityUtils.getCurrentAuthenticatedUser();
		Boolean korisnikUpdated = false;

		validator.checkNewCredentials(korisnik, dto);

		if (dto.getOriginalKorisnickoIme() != null && dto.getNovoKorisnickoIme() != null
				&& dto.getNovoKorisnickoImeCheck() != null) {
			korisnikUpdated = true;
			korisnik.setKorisnickoIme(dto.getNovoKorisnickoIme());
		}

		if (dto.getStaraLozinka() != null && dto.getNovaLozinka() != null && dto.getNovaLozinkaCheck() != null) {
			korisnikUpdated = true;
			korisnik.setLozinka(encryption.encrypt(dto.getNovaLozinka()));
		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Korisnik user = findKorisnikByKorisnickoIme(username);
		if (user == null) {
			throw new AcasException("main.toaster.validation.pogresnoKorisnickoIme");
		}

		if (!user.getAuthorities().isEmpty()) {
			Boolean workGroupFlag = false;
			for (GrantedAuthority auth : user.getAuthorities()) {
				if (auth.getAuthority().equals("ROLE_Work_group")) {
					workGroupFlag = true;
				}
			}

			if (!workGroupFlag) {
				throw new AcasException("main.toaster.validation.pogresnoKorisnickoIme");
			}
		}

		logger.debug("Korisnik {} is now authenticated.", user);

		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_User"));

		Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user, user.getLozinka(),
				grantedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);

		return user;
	}

	@Override
	public Boolean validatePassword(String cleanPass, String hashPass) throws Exception {
		if (encryption.encrypt(cleanPass).equals(hashPass)) {
			logger.debug("Sifre su iste.");
			return true;
		} else {
			logger.error("Sifre nisu iste!");
			return false;
		}
	}

	@Override
	public void loginUser(Korisnik korisnik, HttpServletRequest request, Boolean isAdminLoggingIn) throws Exception {
		Assert.notNull(korisnik.getKorisnickoIme());
		Korisnik korisnikFromDB = (Korisnik) findKorisnikByKorisnickoIme(korisnik.getKorisnickoIme());
		if (korisnikFromDB == null) {
			throw new AcasException("main.toaster.validation.pogresnoKorisnickoIme");
		}
		String lozinka = korisnik.getLozinka();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		if (lozinka != null) {
			if (validatePassword(lozinka, korisnikFromDB.getLozinka())) {
				Set<String> role = AuthorityUtils.authorityListToSet(korisnikFromDB.getAuthorities());
				if (isAdminLoggingIn) {
							}
				for (String rola : role) {
					if (!rola.equals("ROLE_User")) {
						grantedAuthorities.add(new SimpleGrantedAuthority(rola));
					}
				}

				Authentication authentication = new UsernamePasswordAuthenticationToken(korisnikFromDB,
						korisnikFromDB.getLozinka(), grantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);

				if (isAdminLoggingIn) {
					saveUserData(request);
				}
			} else {
				throw new AcasException("main.toaster.validation.pogresnaLoznika");
			}
		}
	}

	@Override
	public void saveUserData(HttpServletRequest request) {
		String ipAddress = fetchIpAddress(request);
		logger.trace("Detected IP: {}", ipAddress);

	}

	private String fetchIpAddress(final HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ipAddress = request.getHeader(header);
			if (StringUtils.hasText(ipAddress) && isIPValid(ipAddress)) {
				return ipAddress;
			}
		}
		return request.getRemoteAddr();
	}

	private boolean isIPValid(final String ipAddress) {
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		return pattern.matcher(ipAddress).matches();
	}

	@Override
	protected Predicate handleCollection(Field field, CriteriaBuilder criteriaBuilder, Path<?> path,
			Collection<?> value) {
		Predicate predicate = null;
		String fieldName = field.getName();
		if (fieldName.endsWith("_inO")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_inO"));
			Set<Long> ids = new HashSet<Long>();
			for (Object o : value) {
				if (o instanceof BaseBean) {
					for (Field filedObject : o.getClass().getDeclaredFields()) {
						ReflectionUtils.makeAccessible(filedObject);
						Object val = ReflectionUtils.getField(filedObject, o);
						if (val != null && val.getClass().equals(entityClass)) {
							ids.add(((BaseBean) val).getId());
						}
					}
				}
			}
			if (!ids.isEmpty())
				predicate = criteriaBuilder.and(path.get("id").in(ids));
			else
				predicate = criteriaBuilder.and(path.get("id").isNull());

		} else {
			super.handleCollection(field, criteriaBuilder, path, value);
		}
		return predicate;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		return authentication;

	}

	@Override
	public Boolean checkUsername(String username) {
		if (SecurityUtils.getCurrentLogin().equals(username)) {
			return true;
		}

		return false;
	}

	@Override
	public Boolean checkPassword(String password) {
		Korisnik korisnik = SecurityUtils.getCurrentAuthenticatedUser();
		if (korisnik != null) {
			try {
				if (encryption.encrypt(password).equals(korisnik.getLozinka())) {
					return true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}

		return false;
	}

	@Override
	public Boolean checkAvailableUsername(String username) {
		return korisnikRepository.countByKorisnickoIme(username) > 0;
	}
}
