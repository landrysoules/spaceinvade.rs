package rs.prozone.acam.pi.repository.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Component;

import rs.prozone.acam.pi.entity.BaseBean;
import rs.prozone.acam.pi.entity.BaseVersionBean;
import rs.prozone.acam.pi.exception.AcasException;
import rs.prozone.acam.pi.repository.EntityRepository;


/**
 * @author vladimir.dejanovic
 *
 */
@Component
public class EntityRepositoryImpl<T extends BaseBean> implements EntityRepository<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DataSource ds;

	private String schemaName;

	private Vector<String> versionTables;

	@SuppressWarnings("unchecked")
	public boolean canBeDeleted(Class<T> clazz, Long id) {
		if (id == null) {
			// TODO baciti pravi exception
			throw new AcasException("100");
		}

		Table tts = clazz.getAnnotation(javax.persistence.Table.class);
		String tableName = tts.name();

		logger.debug("Can be deleted ID #" + id + " from table: " + tableName + "?");

		initSchemaName();
		initVersionClasses();

		String sqlInfo = "SELECT table_name, column_name FROM information_schema.KEY_COLUMN_USAGE WHERE ";
		sqlInfo += "information_schema.KEY_COLUMN_USAGE.CONSTRAINT_SCHEMA = '" + schemaName;
		sqlInfo += "' AND information_schema.KEY_COLUMN_USAGE.REFERENCED_TABLE_NAME='" + tableName + "'";
		Query qInfo = entityManager.createNativeQuery(sqlInfo);
		List<Object[]> list = qInfo.getResultList();
		// ako nema tabela koje referenciraju
		if (list.size() < 1)
			return true;

		// proveravamo za svaku tabelu koja se referencira
		for (Object[] row : list) {
			String tblName = (String) row[0];
			String colName = (String) row[1];
			String sqlCount = "SELECT COUNT(id) FROM " + tblName + " WHERE " + colName + "=" + id;

			// // proveravamo da li postoji kolona deleted
			// if (hasDeletedColumn(tblName)) {
			// sqlCount += " AND (obrisan = 0 OR obrisan IS NULL)";
			// }

			// proveravamo da li je u pitanju version bean za tabelu
			if (versionTables.contains(tblName)) {
				sqlCount += " AND (obrisan = 0 OR obrisan IS NULL)";
			}

			Query qCount = entityManager.createNativeQuery(sqlCount);
			List<?> a = qCount.getResultList();
			BigInteger b = (BigInteger) a.get(0);
			if (b.intValue() > 0) {
				return false;
			}
		}

		return true;
	}

	private void initSchemaName() {
		if (schemaName == null) {
			try {
				// Context ctx = new InitialContext();
				// DataSource ds = (DataSource)
				// ctx.lookup("java:comp/env/edoc");
				Connection con = ds.getConnection();
				String databaseUrl = con.getMetaData().getURL();
				schemaName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1);
				if (schemaName.contains("?"))
					schemaName = schemaName.substring(0, schemaName.indexOf("?"));
			} catch (Exception e) {
				logger.error("Greska prilikom inicijalizacije schemaName", e);
				// TODO obavezno bacati exception!!!
				throw new AcasException("100");
			}
		}
	}

	private void initVersionClasses() {
		if (versionTables == null) {
			versionTables = new Vector<String>();

			// create scanner and disable default filters (that is the 'false'
			// argument)
			final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
			// add include filters which matches all the classes (or use your
			// own)

			provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

			// get matching classes defined in the package
			final Set<BeanDefinition> classes = provider.findCandidateComponents("acas.pi.entity");

			// this is how you can load the class type from BeanDefinition
			// instance
			for (BeanDefinition bean : classes) {
				try {
					Class<?> clazzz = Class.forName(bean.getBeanClassName());

					if (clazzz != null) {
						if (clazzz.getGenericSuperclass().equals(BaseVersionBean.class)) {
							Table table = clazzz.getAnnotation(javax.persistence.Table.class);
							if (table != null) {
								versionTables.add(table.name());
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// napravljena je nova metoda koja ne proverava bazu vec kod
	@SuppressWarnings("unused")
	private boolean hasDeletedColumn(String tableName) {
		String sqlCount = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE ";
		sqlCount += "information_schema.COLUMNS.table_schema='" + schemaName + "' and ";
		sqlCount += "information_schema.COLUMNS.table_name='" + tableName + "' and ";
		sqlCount += "information_schema.COLUMNS.column_name='obrisan'";
		Query qCount = entityManager.createNativeQuery(sqlCount);
		List<?> a = qCount.getResultList();
		BigInteger b = (BigInteger) a.get(0);
		return b.intValue() > 0;
	}

}
