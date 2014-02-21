package com.tauros;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Store implements IStore{
	private String storeName;
	private Category category;
	
	
	
	public Store(String sname , Category c){
		this.storeName = sname;
		this.category = c;
	}
	public String changeDateFormat(String date , String from , String to) throws ParseException{
		String newDateString;
		SimpleDateFormat sdf = new SimpleDateFormat(from);
		Date d = sdf.parse(date);
		sdf.applyPattern(to);
		newDateString = sdf.format(d);
		return newDateString;
	}
	@Override
	public String getStoreName(){
		return this.storeName;
	}
	@Override
	public Category getStoreCategory(){
		return this.category;
	
	}
	@Override
	public void setStoreName(String newName) throws StoreAlreadyExists{
		String query = "select * from store where storeName = '" + newName +"' and storeCategory = '" + this.category.toString() + "'";
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			if(!result.next()){
				query = "update store set storeName = '" + newName +
						"' where storeName = '" + this.storeName + "' and storeCategory = '" + this.category.toString() + "'";
				st.executeUpdate(query);
				this.storeName = newName;
				return;
			}
			else{
				StoreAlreadyExists s = new StoreAlreadyExists();
				throw s;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	@Override
	public void setStoreCategory(Category newCategory) throws StoreAlreadyExists{
		String query = "select * from store where storeName = '" + this.storeName +"' and storeCategory = '" + newCategory.toString() + "'";
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			if(!result.next()){
				query = "update store set storeCategory = '" + newCategory.toString() +
						"' where storeName = '" + this.storeName + "' and storeCategory = '" + this.category.toString() + "'";
				st.executeUpdate(query);
				this.category = newCategory;
				return;
			}
			else{
				StoreAlreadyExists s = new StoreAlreadyExists();
				throw s;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}		
		
	}
	@Override
	public IShopping[] getShoppings(String startDate, String endDate){
		try {
			startDate = changeDateFormat(startDate, "dd.MM.yyyy", "yyyy-MM-dd");
			endDate = changeDateFormat(endDate, "dd.MM.yyyy", "yyyy-MM-dd");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String query = "select * from shopping where shoppingDate between '" + startDate + "' and '" + endDate + 
						"' and storeName = '" + this.storeName +"' and storeCategory = '" + this.category.toString() +"' order by -shoppingDate";
		
		Vector<IShopping> vectorShopping = new Vector<>();
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String userId = result.getString("userID");
				Date d = result.getDate("shoppingDate");
				double amount = result.getDouble("amount");
				query = "select * from user where userID = '" + userId +"'";
				Statement st2 = ShoppingMall.con.createStatement();
				ResultSet resultForUser = st2.executeQuery(query);
				IUser user = null ;
				while(resultForUser.next()){
					String userName = resultForUser.getString("userName");
					String userAdd = resultForUser.getString("userAddress");
					user = (IUser)new User(userId, userName, userAdd);
					break;
				}
				IStore store  = (IStore)new Store(this.storeName, this.category);
				String dateStr = changeDateFormat(d.toString(), "yyyy-MM-dd", "dd.MM.yyyy");
				IShopping shopping = (IShopping)new Shopping(user,store,dateStr,amount);
				vectorShopping.add(shopping);
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}
		IShopping[] shoppings = new IShopping[vectorShopping.size()];
		for(int i=0 ; i<vectorShopping.size() ; i++){
			shoppings[i] = vectorShopping.get(i);
		}
		return shoppings;
		
	}
	@Override
	public IShopping[] getShoopings(String date){
		IShopping[] shoppings = getShoppings(date, date);
		return shoppings;
	}
	@Override
	public IShopping[] getShoppings(){
		String query = "select * from shopping where storeName = '" + this.storeName +"' and storeCategory = '" + this.category.toString() +"' order by -shoppingDate";
		
		Vector<IShopping> vectorShopping = new Vector<>();
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String userId = result.getString("userID");
				Date d = result.getDate("shoppingDate");
				double amount = result.getDouble("amount");
				query = "select * from user where userID = '" + userId +"'";
				Statement st2 = ShoppingMall.con.createStatement();
				ResultSet resultForUser = st2.executeQuery(query);
				IUser user = null ;
				while(resultForUser.next()){
					String userName = resultForUser.getString("userName");
					String userAdd = resultForUser.getString("userAddress");
					user = (IUser)new User(userId, userName, userAdd);
					break;
				}
				IStore store  = (IStore)new Store(this.storeName, this.category);
				String dateStr = changeDateFormat(d.toString(), "yyyy-MM-dd", "dd.MM.yyyy");
				IShopping shopping = (IShopping)new Shopping(user,store,dateStr,amount);
				vectorShopping.add(shopping);
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}
		IShopping[] shoppings = new IShopping[vectorShopping.size()];
		for(int i=0 ; i<vectorShopping.size() ; i++){
			shoppings[i] = vectorShopping.get(i);
		}
		return shoppings;
	}
}