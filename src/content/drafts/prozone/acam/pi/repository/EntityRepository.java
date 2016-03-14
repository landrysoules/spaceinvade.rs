package rs.prozone.acam.pi.repository;

import rs.prozone.acam.pi.entity.BaseBean;

/**
 * @author vladimir.dejanovic
 *
 */
public interface EntityRepository<T extends BaseBean> {

	public boolean canBeDeleted(Class<T> clazz, Long id);

}
