package rs.prozone.acam.pi.entity.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.prozone.acam.pi.entity.BaseBean;


public class SearchResult<T extends BaseBean> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2448715391679484162L;

	protected List<T> results = new ArrayList<T>();

	protected Long total;

	protected Integer index;

	protected Integer totalPages;

	protected Boolean first = false;

	protected Boolean last = false;

	protected Pager pager;

	public SearchResult() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchResult(SearchResult searchResult) {
		if (searchResult == null)
			return;

		results = searchResult.getResults();
		total = searchResult.getTotal();
		index = searchResult.getIndex();
		totalPages = searchResult.getTotalPages();
		first = searchResult.getFirst();
		last = searchResult.getLast();
		pager = searchResult.getPager();
	}

	public SearchResult(List<T> results, Long total) {
		setResults(results);
		setTotal(total);
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
