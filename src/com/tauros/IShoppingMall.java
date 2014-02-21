package com.tauros;

public interface IShoppingMall {
	public void onStart();
	public boolean createDatabase() throws DatabaseAlreadyCreated;
	public void addStore(String storeName, Category category) throws StoreAlreadyExists;
	public void removeStore(IStore store) throws StoreNotExist;
	public void registerUser(String fullname, String id, String address) throws UserAlreadyExists;
	public void unregisterUser(IUser user) throws UserNotExist;
	public IStore[] searchStore(String nameIncludes);
	public IStore[] searchStore(String nameIncudes, Category category);
	public IStore[] searchStore(Category category);
	public IUser[] searchUser(String nameIncludes);
	public IUser getUser(String id);
}
