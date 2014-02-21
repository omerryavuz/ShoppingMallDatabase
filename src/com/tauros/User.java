package com.tauros;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
public class User implements IUser{
	private String fullName;
	private String id;
	private String address;
	
	

	public User(String ID,String name,String address){
		this.id = ID;
		this.fullName = name;
		this.address = address;
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
	public String getFullName(){
		return this.fullName;
	}
	@Override
	public String getId(){
		return this.id;
	}
	@Override
	public String getAddress(){
		return this.address;
	}
	@Override
	public void updateAddress(String newAdress){
		String query = "update user set userAddress = '" + newAdress + "' where userID = '" + this.id + "'"; 
		
		try{
			Statement st = ShoppingMall.con.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		this.address = newAdress;
	}

	@Override
	public void shopped(IStore store, String date, double amount){
		try {
			date = changeDateFormat(date, "dd.MM.yyyy", "yyyy-MM-dd");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String query = "insert into shopping values ('" + this.id + "','" + store.getStoreName() + "','" + store.getStoreCategory().toString() + "','" + date +"','" + amount + "')";
		try{
			Statement st = ShoppingMall.con.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	@Override
	public IShopping[] getShoppings(IStore store, String startDate, String endDate){
		try {
			startDate = changeDateFormat(startDate, "dd.MM.yyyy", "yyyy-MM-dd");
			endDate = changeDateFormat(endDate, "dd.MM.yyyy", "yyyy-MM-dd");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String query = "select * from shopping where shoppingDate between '" + startDate + "' and '" + endDate + 
						"' and userID = '" + this.id +"' and storeCategory = '" + store.getStoreCategory().toString() + "' and storeName = '" +store.getStoreName() + "' order by -shoppingDate";
		
		Vector<IShopping> vectorShopping = new Vector<>();
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				Date d = result.getDate("shoppingDate");
				double amount = result.getDouble("amount");
				IUser user = (IUser)new User(this.id, this.fullName, this.address);
				String dateStr = changeDateFormat(d.toString(), "yyyy-MM-dd", "dd.MM.yyyy");
				IShopping shopping = (IShopping)new Shopping(user,store,dateStr,amount);
				vectorShopping.add(shopping);
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}
		if(vectorShopping.size() == 0){
			return null;
		}
		IShopping[] shoppings = new IShopping[vectorShopping.size()];
		for(int i=0 ; i<vectorShopping.size() ; i++){
			shoppings[i] = vectorShopping.get(i);
		}
		return shoppings;
	}
	@Override
	public IShopping[] getShoppings(IStore store){

		String query = 	"select * from shopping where "+
						"userID = '" + this.id +"' and storeCategory = '" + store.getStoreCategory().toString() + "' and storeName = '" +store.getStoreName() + "' order by -shoppingDate";
		
		Vector<IShopping> vectorShopping = new Vector<>();
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				Date d = result.getDate("shoppingDate");
				double amount = result.getDouble("amount");
				IUser user = (IUser)new User(this.id, this.fullName, this.address);
				String dateStr = changeDateFormat(d.toString(), "yyyy-MM-dd", "dd.MM.yyyy");
				IShopping shopping = (IShopping)new Shopping(user,store,dateStr,amount);
				vectorShopping.add(shopping);
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}
		if(vectorShopping.size() == 0){
			return null;
		}
		IShopping[] shoppings = new IShopping[vectorShopping.size()];
		for(int i=0 ; i<vectorShopping.size() ; i++){
			shoppings[i] = vectorShopping.get(i);
		}
		return shoppings;
	}
	@Override
	public IShopping[] getShoppings(){

		String query = "select * from shopping where "+
						"userID = '" + this.id +"' order by -shoppingDate";
		
		Vector<IShopping> vectorShopping = new Vector<>();
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				Date d = result.getDate("shoppingDate");
				double amount = result.getDouble("amount");
				IUser user = (IUser)new User(this.id, this.fullName, this.address);
				String dateStr = changeDateFormat(d.toString(), "yyyy-MM-dd", "dd.MM.yyyy");
				String storeName = result.getString("storeName");
				String storeCat = result.getString("storeCategory");
				IStore store = (IStore)new Store(storeName, Category.valueOf(storeCat));
				IShopping shopping = (IShopping)new Shopping(user,store,dateStr,amount);
				vectorShopping.add(shopping);
			}
		}catch(SQLException | ParseException e){
			e.printStackTrace();
		}
		if(vectorShopping.size() == 0){
			return null;
		}
		IShopping[] shoppings = new IShopping[vectorShopping.size()];
		for(int i=0 ; i<vectorShopping.size() ; i++){
			shoppings[i] = vectorShopping.get(i);
		}
		return shoppings;
	}
	@Override
	public boolean checkPrimeMinisterOffer(){
		String query = 	"select s1.userID from shopping s1 " +
						"where s1.userID = '" + this.id + "' " + 
						"group by s1.userID " +
						"having 5 <= ( select count(distinct s2.storeName , s2.storeCategory ) "+
									  "from shopping s2 "+
									  "where s1.userID = s2.userID and date_add(s2.shoppingDate,interval 1 month) >= curdate() and s2.amount >= 100 )"; 
		
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			if(result.next()){
				return true;
			}
			else{
				return false;
			}
		}catch(SQLException e){
			
		}
		return false;

	}
	@Override
	public boolean checkGovernorOffer(){
			String query = 	"select s1.userID from shopping s1 " +
							"where s1.userID = '" + this.id + "' " + 
							"group by s1.userID , s1.storeName , s1.storeCategory " +
							"having 5 <= ( 	select count(distinct s2.shoppingDate ) "+
											"from shopping s2 "+
											"where s1.userID = s2.userID and date_add(s2.shoppingDate,interval 1 month) >= curdate() and s2.amount >= 100  and s1.storeName = s2.storeName and " +
											"s1.storeCategory = s2.storeCategory )"; 
			
			try{
				Statement st = ShoppingMall.con.createStatement();
				ResultSet result = st.executeQuery(query);
				if(result.next()){
					return true;
				}
				else{
					return false;
				}
			}catch(SQLException e){
				
			}
			return false;
	}
	@Override
	public boolean checkMayorOffer(){
		String query = 	"select s1.userID from shopping s1 " +
						"where s1.userID = '" + this.id + "' " + 
						"group by s1.userID " +
						"having 10 <= ( select count(distinct s2.shoppingDate ) "+
										"from shopping s2 "+
										"where s1.userID = s2.userID and date_add(s2.shoppingDate,interval 1 month) >= curdate() )"; 

			
		try{
			Statement st = ShoppingMall.con.createStatement();
			ResultSet result = st.executeQuery(query);
			if(result.next()){
				return true;
			}
			else{
				return false;
			}
		}catch(SQLException e){
			
		}
		return false;
		
	}
}