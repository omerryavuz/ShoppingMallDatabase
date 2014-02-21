package com.tauros;

public class Starter {
	public static IShoppingMall getShoppingMallHandler() {
		IShoppingMall shopping_mall = null;
		
		shopping_mall = (IShoppingMall)new ShoppingMall();
		
		return shopping_mall;
	}
}
