package com.tauros;

public interface IUser{
	public String getFullName();
	public String getId();
	public String getAddress();
	public void updateAddress(String newAdress);
	public void shopped(IStore store, String date, double amount);
	public IShopping[] getShoppings(IStore store, String startDate, String endDate);
	public IShopping[] getShoppings(IStore store);
	public IShopping[] getShoppings();
	public boolean checkPrimeMinisterOffer();
	public boolean checkGovernorOffer();
	public boolean checkMayorOffer();
}
