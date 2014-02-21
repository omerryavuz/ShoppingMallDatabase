package com.tauros;

public class Shopping implements IShopping{
	private IUser user;
	private IStore store;
	private String date;
	private double amount;
	
	public Shopping(IUser user , IStore store ,String date , double amount ){
		this.user = user;
		this.store = store;
		this.date = date;
		this.amount = amount;
	}
	
	@Override
	public IUser getUser(){
		return this.user;
	}
	@Override
	public IStore getStore(){
		return this.store;
	}
	@Override
	public String getDate(){
		return this.date;
	}
	@Override
	public double getAmount(){
		return this.amount;
		
	}
}