import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeShopApplicationTest {

	Map<String, Double> priceMap = new HashMap<>();
	Map<String, Integer> inputItemsMap = new HashMap<>();
	Map<String, Integer> extrasFreeMap = new HashMap<>();
	Map<String, Integer> beverageFreeMap = new HashMap<>();
	Map<String, Integer> extraBeverageFreeMap = new HashMap<>();
	Map<String, Integer> noBeverageFreeMap = new HashMap<>();

	List<String> extraFreeList = new ArrayList<>();
	List<String> noExtraFreeList = new ArrayList<>();

	CoffeeShopApplication coffeeShopApplication;

	@Before
	public void load(){

		priceMap.put("small coffee", 2.5);
		priceMap.put("medium coffee", 3.0);
		priceMap.put("large coffee", 3.5);
		priceMap.put("bacon roll", 4.5);
		priceMap.put("orange juice", 3.95);
		priceMap.put("extra milk", 0.3);
		priceMap.put("foamed milk", 0.5);
		priceMap.put("special roast coffee", 0.9);

		inputItemsMap.put("large coffee", 1);
		inputItemsMap.put("extra milk", 1);
		inputItemsMap.put("small coffee", 1);
		inputItemsMap.put("special roast coffee", 1);
		inputItemsMap.put("orange juice", 1);
		inputItemsMap.put("bacon roll", 1);

		extrasFreeMap.put("small coffee", 1);
		extrasFreeMap.put("medium coffee", 1);
		extrasFreeMap.put("large coffee", 1);
		extrasFreeMap.put("bacon roll", 1);
		extrasFreeMap.put("orange juice", 1);
		extrasFreeMap.put("extra milk", 1);
		extrasFreeMap.put("foamed milk", 1);
		extrasFreeMap.put("special roast coffee", 1);

		extraFreeList.add("bacon roll");
		extraFreeList.add("small coffee");

		noExtraFreeList.add("small coffee");
		noExtraFreeList.add("medium coffee");

		extraBeverageFreeMap.put("bacon roll", 1);
		extraBeverageFreeMap.put("orange juice", 5);

		beverageFreeMap.put("orange juice", 5);

		noBeverageFreeMap.put("bacon roll", 1);
		noBeverageFreeMap.put("orange juice", 1);

		List<String> beverageList = getBeverages();
		Map<String, Double> snackMap = getSnacks();

		coffeeShopApplication = new CoffeeShopApplication();
	}

	@Test
	public void checkCoffeePrice(){
		Map<String, Integer> map = new HashMap<>();
		map.put("small coffee", 1);
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(map, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		double expectedPrice = priceMap.get("small coffee") * map.get("small coffee");
		Assert.assertEquals(expectedPrice, totalPrice, 0.01);
	}

	@Test
	public void checkMultipleCoffeePrice(){
		Map<String, Integer> map = new HashMap<>();
		map.put("small coffee", 2);
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(map, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		double expectedPrice = priceMap.get("small coffee") * map.get("small coffee");
		Assert.assertEquals(expectedPrice, totalPrice, 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidItem(){
		Map<String, Integer> map = new HashMap<>();
		map.put("wrongOrder", 1);
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(map, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		double expectedPrice = priceMap.get("wrongOrder") * map.get("wrongOrder");
		Assert.assertEquals(expectedPrice, totalPrice, 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidNoOfItem(){
		Map<String, Integer> map = new HashMap<>();
		map.put("small coffee", 0);
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(map, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		double expectedPrice = priceMap.get("small coffee") * map.get("small coffee");
		Assert.assertEquals(expectedPrice, totalPrice, 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noItem(){
		Map<String, Integer> map = new HashMap<>();
		map.put(null, 1);
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(map, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		double expectedPrice = priceMap.get(null) * map.get(null);
		Assert.assertEquals(expectedPrice, totalPrice, 0.01);
	}

	@Test
	public void checkPriceOfAllItems(){
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(inputItemsMap, 1, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		Assert.assertEquals(15.65, totalPrice, 0.01);
	}

	@Test
	public void getAllOrderedItemsPrice_extraFreeOnly(){
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(extrasFreeMap, 1, 0);
		Assert.assertEquals(19.15, itemsList.get(itemsList.size()-1).getPrice(), 0.01);
	}

	@Test
	public void getAllOrderedItemsPrice_beverageFreeOnly(){
		boolean isExtraFree = false;
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(beverageFreeMap, 0, 1);
		Assert.assertEquals(15.8, itemsList.get(itemsList.size()-1).getPrice(), 0.01);
	}

	@Test
	public void getAllOrderedItemsPrice_extraAndBeverageFree(){
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(extraBeverageFreeMap, 1, 1);
		Assert.assertEquals(20.3, itemsList.get(itemsList.size()-1).getPrice(), 0.01);
	}

	@Test(expected = NullPointerException.class)
	public void checkPriceOfAllItems_empty(){
		List<OrderItem> itemsList = coffeeShopApplication.getAllOrderedItemsPrice(null, 0, 0);
		double totalPrice = itemsList.get(itemsList.size() - 1).getPrice();
		Assert.assertEquals(12.95, totalPrice, 0.01);
	}
	@Test
	public void isExtraFree(){
		int extraFreeCount = coffeeShopApplication.isExtraFree(extraFreeList);
		Assert.assertEquals(1, extraFreeCount);
	}

	@Test(expected = NullPointerException.class)
	public void isExtraFree_empty(){
		int extraFreeCount = coffeeShopApplication.isExtraFree(null);
		Assert.assertEquals(0, extraFreeCount);
	}
	@Test
	public void noExtraFree(){
		int extraFreeCount = coffeeShopApplication.isExtraFree(noExtraFreeList);
		Assert.assertEquals(0, extraFreeCount);
	}

	@Test
	public void isFifthBeverage(){
		int freeBeverage = coffeeShopApplication.getBeverageFree(beverageFreeMap);
		Assert.assertEquals(1, freeBeverage);
	}

	@Test(expected = NullPointerException.class)
	public void isFifthBeverage_empty(){
		int freeBeverage = coffeeShopApplication.getBeverageFree(null);
		Assert.assertEquals(1, freeBeverage);
	}
	@Test
	public void noFreeBeverage(){
		int freeBeverage = coffeeShopApplication.getBeverageFree(noBeverageFreeMap);
		Assert.assertEquals(0, freeBeverage);
	}

	@Test
	public void getOrderedItems(){
		List<OrderItem> orderItems = coffeeShopApplication.getOrderedItems();
		double totalPrice = orderItems.get(orderItems.size() - 1).getPrice();
		Assert.assertEquals(15.65, totalPrice, 0.01);
	}

	@Test
	public void getBeveragesTest(){
		List<String> list = coffeeShopApplication.getBeverages();
		Assert.assertEquals(4, list.size());
	}

	@Test
	public void getSnacksTest(){
		Map<String, Double> map = coffeeShopApplication.getSnacks();
		Assert.assertEquals(1, map.size());
	}

	@Test
	public void getAllItemsPriceTest(){
		Map<String, Double> map = coffeeShopApplication.getAllItemsPrice();
		Assert.assertEquals(8, map.size());
	}

	public Map<String, Double> getAllItemsPrice(){
		Map<String, Double> map = new HashMap<>();
		map.put("small coffee", 2.5);
		map.put("medium coffee", 3.0);
		map.put("large coffee", 3.5);
		map.put("bacon roll", 4.5);
		map.put("orange juice", 3.95);
		map.put("extra milk", 0.3);
		map.put("foamed milk", 0.5);
		map.put("special roast coffee", 0.9);
		return map;
	}

	public List<String> getBeverages(){
		List<String> beverageList = new ArrayList<>();
		beverageList.add("small coffee");
		beverageList.add("medium coffee");
		beverageList.add("large coffee");
		beverageList.add("orange juice");
		return beverageList;
	}

	public Map<String, Double> getSnacks(){
		Map<String, Double> map = new HashMap<>();
		map.put("bacon roll", 4.5);
		return map;
	}
	public List<OrderItem> getOrderItemsFromUser(){
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(new OrderItem("small coffee", 1, 2.5));
		orderItems.add(new OrderItem("large coffee", 1, 3.5));
		orderItems.add(new OrderItem("orange juice", 1, 3.95));
		orderItems.add(new OrderItem("special roast coffee", 1, 0.9));
		orderItems.add(new OrderItem("bacon roll", 1, 4.5));
		orderItems.add(new OrderItem("extra milk", 1, 0));
		orderItems.add(new OrderItem("Total Price", 1, 15.65));

		return orderItems;
	}
}
