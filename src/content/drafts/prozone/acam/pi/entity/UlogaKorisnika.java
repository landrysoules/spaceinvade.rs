package rs.prozone.acam.pi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.security.core.GrantedAuthority;

/**
 * The persistent class for the pi_uloga_korisnika database table.
 * 
 */
@Entity
@Table(name = "pi_uloga_korisnika")
@TableGenerator(name = "ACAM_GEN", table = BaseBean.COUNTER_TABLE_NAME, pkColumnName = BaseBean.COUNTER_PK_COLUMN_NAME, valueColumnName = BaseBean.COUNTER_VALUE_COLUMN_NAME, pkColumnValue = "ULOGA_KORISNIKA_NEXT", allocationSize = BaseBean.COUNTER_ALLOCATION_SIZE)
public class UlogaKorisnika extends BaseBean implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	public enum UlogaKorisnikaEnum {
		ROLE_User, ROLE_Admin, ROLE_Work_group, ROLE_Admin_process;
	}

	@Column(name = "naziv")
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(UlogaKorisnikaEnum naziv) {
		this.authority = naziv.toString();
	}

	@Override
	public String toString() {
		return "UlogaKorisnika [authority=" + authority + "]";
	}
}