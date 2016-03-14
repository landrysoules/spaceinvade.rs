package rs.prozone.acam.pi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.entity.dto.ChangeCredentialsDTO;
import rs.prozone.acam.pi.exception.AcasException;
import rs.prozone.acam.pi.util.Encryption;


/**
 * Validator za korisnika.
 * 
 * @author milos.poljak
 *
 */
@Component
public class KorisnikValidator extends AcasBaseValidator {
	
	@Autowired
	private Encryption encryption;

	/**
	 * Proverava da li su unesene sifre iste.
	 * 
	 * @param oldPass
	 * @param newPass
	 */
	private void passwordsMatch(String newPass, String newPassCheck) {
		logger.debug("U metodi passwordsMatch, poredi sifre.");
		if (newPass != null && newPassCheck != null) {
			if (!newPass.equals(newPassCheck)) {
				logger.debug("Sifre se ne slazu!");
				throw new AcasException("main.toaster.validation.lozinkeNisuIste");
			}
		} else {
			logger.debug("Neka od sifri nije uneta!");
			throw new AcasException("main.toaster.validation.lozinkeNeispravne");
		}
	}

	/**
	 * Proverava da li se slazu sifre ulogovanog korisnika i stara sifra koju je
	 * uneo.
	 * 
	 * @param korisnik
	 * @param staraLozinka
	 */
	private void passwordFromUser(Korisnik korisnik, String staraLozinka) {
		if (staraLozinka != null) {
			try {
				if (! encryption.encrypt(staraLozinka).equals( korisnik.getPassword())) {
					logger.debug("Stara sifra i korisnikova sifra nisu iste!");
					throw new AcasException("main.toaster.validation.staraLozinkaError");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw new AcasException("main.toaster.validation.staraLozinkaError");
			}
		} else {
			throw new AcasException("main.toaster.validation.staraLozinkaBlank");
		}
	}

	/**
	 * Proverava da li je username zaista od korisnika.
	 * 
	 * @param korisnik
	 * @param stariUsername
	 */
	private void usernameFromUser(Korisnik korisnik, String stariUsername) {
		logger.debug("U metodi usernameFromUser, poredi korisnicko ime.");
		if (stariUsername != null) {
			if (!korisnik.getUsername().equals(stariUsername)) {
				logger.debug("Korisnicka imena se razlikuju!");
				throw new AcasException("main.toaster.validation.staroKorisnickoImeError");
			}
		} else {
			throw new AcasException("main.toaster.validation.korisnickoImeBlank");
		}
	}

	/**
	 * Proverava da li su novi username-ovi isti.
	 * 
	 * @param stariUsername
	 * @param noviUsername
	 */
	private void usernamesMatch(String noviUsername, String noviUsernameCheck) {
		logger.debug("U metodi usernamesMatch, porede se novo korisnicko ime: {} i ponovo uneto korisnicko ime: {}", noviUsername, noviUsernameCheck);
		if (noviUsername != null && noviUsernameCheck != null) {
			if (!noviUsername.equals(noviUsernameCheck)) {
				throw new AcasException("main.toaster.validation.korisnickaImenaNisuIsta");
			}
		} else {
			throw new AcasException("main.toaster.validation.korisnickaImenaNeispravna");
		}
	}

	/**
	 * Validator.
	 * 
	 * @param korisnik
	 * @param dto
	 */
	public void checkNewCredentials(Korisnik korisnik, ChangeCredentialsDTO dto) {
		String stariUsername = dto.getOriginalKorisnickoIme();
		String noviUsername = dto.getNovoKorisnickoIme();
		String noviUsernameCheck = dto.getNovoKorisnickoImeCheck();
		String staraLozinka = dto.getStaraLozinka();
		String novaLozinka = dto.getNovaLozinka();
		String novaLozinkaCheck = dto.getNovaLozinkaCheck();

		if (stariUsername == null && noviUsername == null && noviUsernameCheck == null && staraLozinka == null && novaLozinka == null && novaLozinkaCheck == null) {
			throw new AcasException("main.toaster.validation.podaciNisuUneseni");
		}

		if ((stariUsername != null && !stariUsername.equals("")) || (noviUsername != null && !noviUsername.equals("")) || (noviUsernameCheck != null && !noviUsernameCheck.equals(""))) {
			usernameFromUser(korisnik, stariUsername);
			usernamesMatch(noviUsername, noviUsernameCheck);
		}

		if ((staraLozinka != null && !staraLozinka.equals("")) || (novaLozinka != null && !novaLozinka.equals("")) || (novaLozinkaCheck != null && !novaLozinkaCheck.equals(""))) {
			passwordFromUser(korisnik, staraLozinka);
			passwordsMatch(novaLozinka, novaLozinkaCheck);
		}
	}

}
