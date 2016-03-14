package rs.prozone.acam.pi.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.prozone.acam.pi.entity.BaseVersionBean;
import rs.prozone.acam.pi.entity.Korisnik;
import rs.prozone.acam.pi.service.security.SecurityUtils;

/**
 * Persistance listener for BaseVersionBean
 * 
 * @author Aleksandar Stojsavljevic
 */
public class PersistListener {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@PreUpdate
	@PrePersist
	public void save(BaseVersionBean entity) throws Exception {
		Korisnik authenticatedKorisnik = SecurityUtils.getCurrentAuthenticatedUser();
		Date now = new Date();
		entity.setIzmenjeno(now);
		entity.setIzmenjen(authenticatedKorisnik.getId());
		if (entity.getKreirano() == null) {
			entity.setKreirano(now);
			entity.setKreiran(authenticatedKorisnik.getId());
		}
	}

}
