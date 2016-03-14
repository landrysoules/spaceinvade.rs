package rs.prozone.acam.pi.entity.search;

import java.io.Serializable;

public class Pager implements Serializable {

	private static final long serialVersionUID = 692170298306156879L;

	public static final String DIR_ASC = "asc";
	
	public static final String DIR_DESC= "desc";
	
	private Integer page;
	
	private Integer size;
	
	private String sort;
	
	public Pager() {
		
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
