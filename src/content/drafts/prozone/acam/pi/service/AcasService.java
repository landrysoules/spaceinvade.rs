package rs.prozone.acam.pi.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


import rs.prozone.acam.pi.entity.BaseBean;
import rs.prozone.acam.pi.entity.search.Pager;
import rs.prozone.acam.pi.entity.search.SearchResult;

public interface AcasService<T extends BaseBean, ID extends Serializable> {

	public T save(T entity) throws Exception;

	public T get(ID id) throws Exception;

	public List<T> getAll() throws Exception;

	public void delete(ID id) throws Exception;

	public void delete(Iterable<T> entities) throws Exception;

	public boolean canBeDeleted(Long id);

	public void physicalDelete(ID id) throws Exception;

	public Iterable<T> saveAll(List<T> entities) throws Exception;

	public SearchResult<T> search(T searchForm, Pager pager, Set<String> ignoreFields) throws Exception;

}
