package rs.prozone.acam.pi.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.entity.dto.ChangeCredentialsDTO;

/**
 * The Interface KorisnikService.
 * 
 * @author Landry Soules
 */
@Service
public interface KorisnikService extends AcasService<Korisnik, Long>, UserDetailsService, AuthenticationManager {

	/**
	 * Find korisnik by korisnicko ime.
	 *
	 * @param korisnickoIme
	 *            the korisnicko ime
	 * @return the korisnik
	 */
	public Korisnik findKorisnikByKorisnickoIme(String korisnickoIme);

	/**
	 * Does korisnik exists.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean doesKorisnikExists(String username);

	public Korisnik saveB(Korisnik korisnik) throws Exception;

	/**
	 * Promena korisnickog imena i lozinke.
	 * 
	 * @param dto
	 * @throws Exception
	 */
	public void changeCredentials(ChangeCredentialsDTO dto) throws Exception;

	/**
	 * Proverava uneti pass da li odgovara onom iz baze.
	 * 
	 * @param cleanPass
	 *            Uneti pass sa forme.
	 * @param hashPass
	 *            Pass iz baze.
	 * @return <strong>true</strong> ako sifre odgovaraju, <strong>false</code>
	 *         u suprotnom.
	 * @throws Exception
	 */
	public Boolean validatePassword(String cleanPass, String hashPass) throws Exception;

	/**
	 * Loguje korisnika u potpunosti.
	 * 
	 * @param korisnik
	 * @param request
	 * @param isAdminLoggingIn
	 *            flag u zavisnosti da li se admin loguje ili ne
	 * @throws Exception
	 */
	public void loginUser(Korisnik korisnik, HttpServletRequest request, Boolean isAdminLoggingIn) throws Exception;

	/**
	 * Provera da li prosledjeno korisnicko ime odgovara ulogovanom korisniku.
	 * 
	 * @param username
	 * @return
	 */
	public Boolean checkUsername(String username);

	/**
	 * Proverava da li prosledjena lozinka odgovara ulogovanom korisniku.
	 * 
	 * @param password
	 * @return
	 */
	public Boolean checkPassword(String password);

	/**
	 * Proverava da li je prosledjen username slobodan ili zauzet.
	 * 
	 * @param username
	 * @return
	 */
	public Boolean checkAvailableUsername(String username);

	/**
	 * Cuva podatke korisnika prilikom logovanja.
	 * 
	 * @param request
	 */
	public void saveUserData(HttpServletRequest request);
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
