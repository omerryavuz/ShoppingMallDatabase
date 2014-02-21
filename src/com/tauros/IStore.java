package com.tauros;

public interface IStore{
	public String getStoreName();
	public Category getStoreCategory();
	public void setStoreName(String newName) throws StoreAlreadyExists;
	public void setStoreCategory(Category newCategory) throws StoreAlreadyExists;
	public IShopping[] getShoppings(String startDate, String endDate);
	public IShopping[] getShoopings(String date);
	public IShopping[] getShoppings();
}
