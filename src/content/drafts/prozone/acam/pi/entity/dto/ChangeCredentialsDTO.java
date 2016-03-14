package rs.prozone.acam.pi.entity.dto;

/**
 * DTO za promenu podatka korisnika.
 * 
 * @author milos.poljak
 *
 */
public class ChangeCredentialsDTO {

	private String originalKorisnickoIme;

	private String novoKorisnickoIme;

	private String novoKorisnickoImeCheck;

	private String staraLozinka;

	private String novaLozinka;

	private String novaLozinkaCheck;

	public String getOriginalKorisnickoIme() {
		return originalKorisnickoIme;
	}

	public void setOriginalKorisnickoIme(String originalKorisnickoIme) {
		this.originalKorisnickoIme = originalKorisnickoIme;
	}

	public String getNovoKorisnickoIme() {
		return novoKorisnickoIme;
	}

	public void setNovoKorisnickoIme(String novoKorisnickoIme) {
		this.novoKorisnickoIme = novoKorisnickoIme;
	}

	public String getNovoKorisnickoImeCheck() {
		return novoKorisnickoImeCheck;
	}

	public void setNovoKorisnickoImeCheck(String novoKorisnickoImeCheck) {
		this.novoKorisnickoImeCheck = novoKorisnickoImeCheck;
	}

	public String getStaraLozinka() {
		return staraLozinka;
	}

	public void setStaraLozinka(String staraLozinka) {
		this.staraLozinka = staraLozinka;
	}

	public String getNovaLozinka() {
		return novaLozinka;
	}

	public void setNovaLozinka(String novaLozinka) {
		this.novaLozinka = novaLozinka;
	}

	public String getNovaLozinkaCheck() {
		return novaLozinkaCheck;
	}

	public void setNovaLozinkaCheck(String novaLozinkaCheck) {
		this.novaLozinkaCheck = novaLozinkaCheck;
	}

}
