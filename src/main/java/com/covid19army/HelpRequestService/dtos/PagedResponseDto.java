package com.covid19army.HelpRequestService.dtos;

import java.util.List;

public class PagedResponseDto<T> {
	private List<T> t;
	int currentPage;
	long totalItems;
	int totalPages;
	
	public List<T> getData() {
		return t;
	}
	public void setData(List<T> t) {
		this.t = t;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
