package rs.prozone.acam.pi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import rs.prozone.acam.pi.entity.Korisnik;


@RepositoryRestResource
public interface KorisnikRepository extends CrudRepository<Korisnik, Long> {

	@Query("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme and obrisan is null")
	public Korisnik findKorisnikByKorisnickoIme(@Param("korisnickoIme") String korisnickoIme);

	public int countByKorisnickoIme(String korisnickoIme);


}
