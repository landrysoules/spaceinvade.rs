package rs.prozone.acam.pi.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.service.KorisnikService;

@RestController
public class AuthController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KorisnikService korisnikService;

	@RequestMapping("/user")
	public Principal user(Principal user) {
		Korisnik korisnik = korisnikService.findKorisnikByKorisnickoIme(((Korisnik) user).getPassword());
		if (korisnik != null)
			return (Principal) korisnik;
		return null;
	}
}
