package rs.prozone.acam.pi.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The persistent class for the pi_prava_korisnika database table.
 * 
 * @author Landry Soules
 */
@Entity
@Table(name = "pi_prava_korisnika")
@Audited
@TableGenerator(name = "ACAM_GEN", table = BaseBean.COUNTER_TABLE_NAME, pkColumnName = BaseBean.COUNTER_PK_COLUMN_NAME, valueColumnName = BaseBean.COUNTER_VALUE_COLUMN_NAME, pkColumnValue = "PRAVA_KORISNIKA_NEXT", allocationSize = BaseBean.COUNTER_ALLOCATION_SIZE)
public class PravaKorisnika extends BaseVersionBean {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UlogaKorisnika
	/** The uloga korisnika. */
	@ManyToOne
	@JoinColumn(name = "uloga_id", nullable = false)
	@RestResource(exported = false)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private UlogaKorisnika ulogaKorisnika;

	// bi-directional many-to-one association to Korisnik
	/** The korisnik. */
	@ManyToOne
	@JoinColumn(name = "korisnik_id", nullable = false)
	@RestResource(exported = false)
	// @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Korisnik korisnik;

	/**
	 * Gets the uloga korisnika.
	 *
	 * @return the uloga korisnika
	 */
	public UlogaKorisnika getUlogaKorisnika() {
		return this.ulogaKorisnika;
	}

	/**
	 * Sets the uloga korisnika.
	 *
	 * @param ulogaKorisnika
	 *            the new uloga korisnika
	 */
	public void setUlogaKorisnika(UlogaKorisnika ulogaKorisnika) {
		this.ulogaKorisnika = ulogaKorisnika;
	}

	/**
	 * Gets the korisnik.
	 *
	 * @return the korisnik
	 */
	public Korisnik getKorisnik() {
		return this.korisnik;
	}

	/**
	 * Sets the korisnik.
	 *
	 * @param korisnik
	 *            the new korisnik
	 */
	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
}
