package rs.prozone.acam.pi.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the pi_korisnik database table.
 * 
 */
@Entity
@Table(name = "pi_korisnik")
@Audited
@TableGenerator(name = "ACAM_GEN", table = BaseBean.COUNTER_TABLE_NAME, pkColumnName = BaseBean.COUNTER_PK_COLUMN_NAME, valueColumnName = BaseBean.COUNTER_VALUE_COLUMN_NAME, pkColumnValue = "KORISNIK_NEXT", allocationSize = BaseBean.COUNTER_ALLOCATION_SIZE)
public class Korisnik extends BaseVersionBean implements UserDetails {

	private static final long serialVersionUID = 1L;

	@PostLoad
	public void populateAuthorities() {
		Collection<UlogaKorisnika> ulogeKorisnika = new ArrayList<UlogaKorisnika>();
		for (PravaKorisnika pKorisnika : this.getPravaKorisnika()) {
			ulogeKorisnika.add(pKorisnika.getUlogaKorisnika());
		}
		this.setAuthorities(ulogeKorisnika);
	}

	@Column(name = "korisnicko_ime")
	private String korisnickoIme;

	@Column(name = "lozinka")
	private String lozinka;

	@Column(name = "naziv")
	private String naziv;

	@Transient
	private Long expires;

	@Transient
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Collection<UlogaKorisnika> authorities;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "korisnik", cascade = CascadeType.ALL)
	private Collection<PravaKorisnika> pravaKorisnika;

	public String getKorisnickoIme() {
		return this.korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return this.lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<UlogaKorisnika> authorities) {
		this.authorities = authorities;
	}

	@JsonIgnore
	public Collection<PravaKorisnika> getPravaKorisnika() {
		return pravaKorisnika;
	}

	public void setPravaKorisnika(Collection<PravaKorisnika> pravaKorisnika) {
		this.pravaKorisnika = pravaKorisnika;
	}

	@Override
	public String getPassword() {
		return lozinka;
	}

	@Override
	public String getUsername() {
		return korisnickoIme;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Korisnik [korisnickoIme=" + korisnickoIme + ", naziv=" + naziv + ", authorities=" + authorities + "]";
	}

	public Korisnik() {
		super();
	}

	public Korisnik(Korisnik korisnik) {
		super();
		this.korisnickoIme = korisnik.korisnickoIme;
		this.lozinka = korisnik.lozinka;
		this.naziv = korisnik.naziv;
		this.expires = korisnik.expires;
		this.authorities = korisnik.authorities;
		this.pravaKorisnika = korisnik.pravaKorisnika;
	}

}