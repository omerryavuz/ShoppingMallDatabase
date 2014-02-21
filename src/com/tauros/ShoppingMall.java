package com.tauros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ShoppingMall implements IShoppingMall{
	private String user = "e1819655";
	private String password = "9D:MH749";
	private String host = "144.122.71.133";
	private String database = "phw1_e1819655";
	private int port = 3306;
	
	public static Connection con;

	@Override
	public void onStart(){
		String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con =  DriverManager.getConnection(url, this.user, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean createDatabase() throws DatabaseAlreadyCreated{
		
		String query = 	"create table user ("+
						"userID			char(11) not null,"	+
						"userName 		varchar(100),"			+
						"userAddress	varchar(500),"			+
						"primary key 	(userID))"				;
		
		String query2 = "create table store ("+
						"storeName		varchar(100),"				+
						"storeCategory	varchar(20),"				+
						"primary key 	(storeName,storeCategory))"	;
		
		
		String query3 = "create table shopping ("+
						"userID			char(11) not null,"+
						"storeName		varchar(100),"+
						"storeCategory 	varchar(20),"+
						"shoppingDate	date,"+
						"amount			double,"+
						"foreign key	(userID) 	references user (userID) on delete cascade,"+
						"foreign key	(storeName,storeCategory) references store (storeName,storeCategory) on delete cascade)";
						
		try{
			Statement st = this.con.createStatement();
			st.executeUpdate(query);
			st.executeUpdate(query2);
			st.executeUpdate(query3);
			
		}catch(SQLException e ){
			DatabaseAlreadyCreated a = new DatabaseAlreadyCreated();
			throw a;
	//		e.printStackTrace();
		}
		return true;
	}
	@Override
	public void addStore(String storeName, Category category) throws StoreAlreadyExists{
		String query = "insert into store values ('" + storeName + "','" + category.toString() +"')"; 
		try{
			Statement st = this.con.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			StoreAlreadyExists s = new StoreAlreadyExists();
			throw s;
		//	e.printStackTrace();
		}
	}
	@Override
	public void removeStore(IStore store) throws StoreNotExist{
		String query = 	"delete from store " +
						"where storeName = '" + store.getStoreName() + "' and storeCategory = '" + store.getStoreCategory() + "'";
		
		String query2 = "select * from store " +
				"where storeName = '" + store.getStoreName() + "' and storeCategory = '" + store.getStoreCategory() + "'";
		
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query2);
			if(result.next()){
				Statement st2 = this.con.createStatement();
				st2.executeUpdate(query);
			}
			else{
				StoreNotExist a = new StoreNotExist();
				throw a;
			}
		}catch(SQLException e){
			StoreNotExist a = new StoreNotExist();
			throw a;
//			e.printStackTrace();
		}	
	}
	@Override
	public void registerUser(String fullname, String id, String address) throws UserAlreadyExists{
		String query = "insert into user values ('" + id + "','" + fullname + "','" + address + "')";
		try{
			Statement st = this.con.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			UserAlreadyExists a = new UserAlreadyExists();
			throw a;
//			e.printStackTrace();
		}
		
	}
	@Override
	public void unregisterUser(IUser user) throws UserNotExist{
		
		String query = 	"delete from user " +
						"where userID = '" + user.getId() + "'";
		
		String query2 = "select * from user " +
						"where userID = '" + user.getId() + "'";

		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query2);
			if(result.next()){
				Statement st2 = this.con.createStatement();
				st2.executeUpdate(query);
			}
			else{
				UserNotExist a = new UserNotExist();
				throw a;
			}
			
		}catch(SQLException e){
			UserNotExist a = new UserNotExist();
			throw a;
//			e.printStackTrace();
		}
	}
	@Override
	public IStore[] searchStore(String nameIncludes){
		String query = "select * from store where storeName like '%" + nameIncludes + "%' order by storeName"; 
		Vector<IStore> storeVector = new Vector<>();
		
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String storeName = result.getString("storeName");
				String categoryName = result.getString("storeCategory");
				IStore store = (IStore)new Store(storeName,Category.valueOf(categoryName));
				storeVector.add(store);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(storeVector.size() == 0){
			return null;
		}
		IStore[] stores = new IStore[storeVector.size()];
		for(int i=0 ;i<storeVector.size() ; i++){
			stores[i] = storeVector.get(i);
		}
		return stores;
	}
	@Override
	public IStore[] searchStore(String nameIncudes, Category category){
		String query = "select * from store where storeCategory = '"+category.toString() +"' and storeName like '%" + nameIncudes +"%'";
		
		Vector<IStore> storeVector = new Vector<>();
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String storeName = result.getString("storeName");
				IStore store = (IStore)new Store(storeName,category);
				storeVector.addElement(store);
			}
				
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(storeVector.size() == 0){
			return null;
		}
		IStore[] stores  = new IStore[storeVector.size()];

		for(int i=0 ; i<storeVector.size() ; i++){
			stores[i] = storeVector.get(i);
		}
		return stores;
	}
	@Override
	public IStore[] searchStore(Category category){
		String query = "select * from store where storeCategory = '" + category.toString() +"' " + " order by storeName" ;
		Vector<IStore> storeVector = new Vector<>();
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String storeName = result.getString("storeName");
				IStore store = (IStore)new Store(storeName,category);
				storeVector.addElement(store);
			}
				
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(storeVector.size() == 0){
			return null;
		}
		IStore[] stores  = new IStore[storeVector.size()];

		for(int i=0 ; i<storeVector.size() ; i++){
			stores[i] = storeVector.get(i);
		}
		return stores;
	}
	@Override
	public IUser[] searchUser(String nameIncludes){
		String query = "select * from user where userName like '%" + nameIncludes + "%' order by userName";
		Vector<IUser> userVector = new Vector<>();
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()){
				String userID = result.getString("userID");
				String userName = result.getString("userName");
				String userAddress = result.getString("userAddress");
				IUser user = (IUser)new User(userID,userName,userAddress);
				userVector.add(user);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		if(userVector.size() == 0){
			return null;
		}
		IUser[] users = new IUser[userVector.size()];
		for(int i=0 ; i<userVector.size() ; i++){
			users[i] = userVector.get(i);
		}
		return users;
	}
	@Override
	public IUser getUser(String id){
		
		String query = "select * from user where userID = '" + id + "'"; 
		try{
			Statement st = this.con.createStatement();
			ResultSet result = st.executeQuery(query);

			while(result.next()){
				String userid = result.getString("userID");
				String name = result.getString("userName");
				String address = result.getString("userAddress");
				IUser user = (IUser)new User(userid, name, address);
				return user;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;	
	}	
}
