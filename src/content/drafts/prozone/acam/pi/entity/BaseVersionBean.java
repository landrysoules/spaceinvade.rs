package rs.prozone.acam.pi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;


import com.fasterxml.jackson.annotation.JsonIgnore;

import rs.prozone.acam.pi.listener.PersistListener;

/**
 * The Class BaseVersionBean.
 * 
 * @author Landry Soules
 */
@MappedSuperclass
@EntityListeners(PersistListener.class)
public abstract class BaseVersionBean extends BaseBean {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2950185911199487363L;

	/** The izmenjen. */
	@JsonIgnore
	@Column(name = "izmenjen")
	private Long izmenjen;

	/** The izmenjeno. */
	@JsonIgnore
	@Column(name = "izmenjeno")
	@Temporal(TemporalType.TIMESTAMP)
	private Date izmenjeno;

	/** The kreiran. */
	@Column(name = "kreiran")
	private Long kreiran;

	/** The kreirano. */
	@Column(name = "kreirano")
	@Temporal(TemporalType.TIMESTAMP)
	private Date kreirano;

	/** The obrisan. */
	@Column(name = "obrisan")
	private Long obrisan;

	/** The obrisano. */
	@Column(name = "obrisano")
	@Temporal(TemporalType.TIMESTAMP)
	private Date obrisano;

	/** The verzija. */
	@Version
	@Column(name = "verzija")
	private Integer verzija;

	/** The action. */
	@Transient
	private EntityAction action;

	public BaseVersionBean() {
		super();
	}

	public BaseVersionBean(BaseVersionBean bean) {
		super(bean);

		if (bean == null)
			return;

		this.izmenjen = bean.getIzmenjen();
		this.izmenjeno = bean.getIzmenjeno();
		this.kreiran = bean.getKreiran();
		this.kreirano = bean.getKreirano();
		this.obrisan = bean.getObrisan();
		this.obrisano = bean.getObrisano();
		this.verzija = bean.getVerzija();
		this.action = bean.getAction();
	}

	/**
	 * Gets the izmenjen.
	 *
	 * @return the izmenjen
	 */
	public Long getIzmenjen() {
		return izmenjen;
	}

	/**
	 * Sets the izmenjen.
	 *
	 * @param izmenjen
	 *            the new izmenjen
	 */
	public void setIzmenjen(Long izmenjen) {
		this.izmenjen = izmenjen;
	}

	/**
	 * Gets the izmenjeno.
	 *
	 * @return the izmenjeno
	 */
	public Date getIzmenjeno() {
		return izmenjeno;
	}

	/**
	 * Sets the izmenjeno.
	 *
	 * @param izmenjeno
	 *            the new izmenjeno
	 */
	public void setIzmenjeno(Date izmenjeno) {
		this.izmenjeno = izmenjeno;
	}

	/**
	 * Gets the kreiran.
	 *
	 * @return the kreiran
	 */
	public Long getKreiran() {
		return kreiran;
	}

	/**
	 * Sets the kreiran.
	 *
	 * @param kreiran
	 *            the new kreiran
	 */
	public void setKreiran(Long kreiran) {
		this.kreiran = kreiran;
	}

	/**
	 * Gets the kreirano.
	 *
	 * @return the kreirano
	 */
	public Date getKreirano() {
		return kreirano;
	}

	/**
	 * Sets the kreirano.
	 *
	 * @param kreirano
	 *            the new kreirano
	 */
	public void setKreirano(Date kreirano) {
		this.kreirano = kreirano;
	}

	/**
	 * Gets the obrisan.
	 *
	 * @return the obrisan
	 */
	public Long getObrisan() {
		return obrisan;
	}

	/**
	 * Sets the obrisan.
	 *
	 * @param obrisan
	 *            the new obrisan
	 */
	public void setObrisan(Long obrisan) {
		this.obrisan = obrisan;
	}

	/**
	 * Gets the obrisano.
	 *
	 * @return the obrisano
	 */
	public Date getObrisano() {
		return obrisano;
	}

	/**
	 * Sets the obrisano.
	 *
	 * @param obrisano
	 *            the new obrisano
	 */
	public void setObrisano(Date obrisano) {
		this.obrisano = obrisano;
	}

	/**
	 * Gets the verzija.
	 *
	 * @return the verzija
	 */
	public Integer getVerzija() {
		return verzija;
	}

	/**
	 * Sets the verzija.
	 *
	 * @param verzija
	 *            the new verzija
	 */
	public void setVerzija(Integer verzija) {
		this.verzija = verzija;
	}

	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public EntityAction getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action
	 *            the action to set
	 */
	public void setAction(EntityAction action) {
		this.action = action;
	}

}
