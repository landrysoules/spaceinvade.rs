package rs.prozone.acam.pi.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseBean implements Serializable {

	private static final long serialVersionUID = -8588354681550181582L;

	public static final String COUNTER_TABLE_NAME = "PI_TABLE_COUNTER";

	public static final String COUNTER_PK_COLUMN_NAME = "TABLE_NAME";

	public static final String COUNTER_VALUE_COLUMN_NAME = "COUNTER_VALUE";

	public static final int COUNTER_ALLOCATION_SIZE = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ACAM_GEN")
	protected Long id;

	public BaseBean() {
		super();
	}

	public BaseBean(BaseBean bean) {
		super();

		if (bean == null)
			return;

		this.id = bean.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
