import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CoffeeShopApplication {

	public static void main(String[] args){
		new CoffeeShopApplication().getOrderedItems();
	}

	public List<OrderItem> getOrderedItems(){
		//get Input Items in map
		Map<String, Integer> orderedItemsMap = getOrderItems();
		//get Input Items name in list
		List<String> orderlist = orderedItemsMap.entrySet().stream().map(a -> a.getKey()).collect(Collectors.toList());
		//check for offers and return
		int extraFree = isExtraFree(orderlist);
		int freeBeverage = getBeverageFree(orderedItemsMap);
		//get the bill
		List<OrderItem> outputList = getAllOrderedItemsPrice(orderedItemsMap, extraFree, freeBeverage);
		System.out.println("\nOrdered Item | Quantity | Price");
		outputList.stream().forEach(a ->
		{
			System.out.println(a.getName() +" | " +a.getNoOfItem() +" | " +a.getPrice());
		});
		return outputList;
	}

	public Map<String, Integer> getOrderItems(){
		String orderString = "large coffee with extra milk, small coffee with special roast coffee, bacon roll, orange juice";
		String[] strings = orderString.split(", |\\ with ");
		Map<String, Integer> inputItemsMap =new HashMap<>();
		List.of(strings).stream().forEach(a -> inputItemsMap.put(a, 1));
		inputItemsMap.entrySet().stream().forEach(System.out::println);
		return inputItemsMap;
	}

	public int isExtraFree(List<String> extraFreeList) {
		AtomicInteger beverageCount = new AtomicInteger(0);
		AtomicInteger snackCount = new AtomicInteger(0);

		// Check for beverages in extraFreeList
		extraFreeList.stream()
				.filter(a -> new CoffeeShopApplication().getBeverages().contains(a))
				.forEach(a -> beverageCount.incrementAndGet());

		// Check for snacks in extraFreeList
		extraFreeList.stream()
				.filter(a -> new CoffeeShopApplication().getSnacks().containsKey(a))
				.forEach(a -> snackCount.incrementAndGet());

		if(beverageCount.get() > 0 && snackCount.get() > 0){
			return Math.min(beverageCount.get(), snackCount.get());
		}
		else{
			return 0;
		}
	}

	public int getBeverageFree(Map<String, Integer> beverageFreeMap){
		int freeBeverage = beverageFreeMap.entrySet().stream()
				.filter(a -> new CoffeeShopApplication().getBeverages().contains(a.getKey()) && a.getValue() > 4)
				.mapToInt(a -> {
					int reminder = a.getValue()/5;
					return reminder;
				})
				.findAny().orElse(0);

		return freeBeverage;
	}

	public List<OrderItem> getAllOrderedItemsPrice(Map<String, Integer> allOrderItems, int extraFreeCount, int freeBeverage){
		Map<String, Double> priceMap = new CoffeeShopApplication().getAllItemsPrice();
		List<OrderItem> newItemsList = new ArrayList();
		List<OrderItem> itemsList = allOrderItems.entrySet().stream()
				.map(a -> {
					if(a != null && priceMap.containsKey(a.getKey()) && a.getValue() > 0){
						OrderItem items = new OrderItem();
						items.setName(a.getKey());
						items.setNoOfItem(a.getValue());
						items.setPrice(priceMap.get(a.getKey()) * a.getValue());
						return items;
					}
					else{
						throw new IllegalArgumentException("Your order contains invalid items. Please order valid item only ");
					}
				})
				.collect(Collectors.toList());
		System.out.println("Bill before adding offers : " +itemsList);

		if(extraFreeCount > 0){
			OrderItem extraItem = new OrderItem();
			extraItem.setName("extra milk");
			extraItem.setNoOfItem(extraFreeCount);
			extraItem.setPrice(0);
			itemsList.add(extraItem);
		}
		System.out.println("Bill after adding extra free : " +itemsList);

		if(freeBeverage > 0){
			double beveragePrice = priceMap.get("orange juice");
			itemsList = itemsList.stream()
					.map(a -> {
						if(a.getName().equals("orange juice")) {
							a.setNoOfItem(a.getNoOfItem() - freeBeverage);
							a.setPrice(beveragePrice * a.getNoOfItem());
							return a;
						}
						return a;
					}).collect(Collectors.toList());
			System.out.println("New Bill after updating free beverage : " +itemsList);

			OrderItem extraItem = new OrderItem();
			extraItem.setName("orange juice");
			extraItem.setNoOfItem(freeBeverage);
			extraItem.setPrice(0);
			itemsList.add(extraItem);
		}
		System.out.println("Bill after adding free beverage : " +itemsList);
		int totalNoOfItems = itemsList.stream().mapToInt(a -> a.getNoOfItem()).sum();
		System.out.println("totalNoOfItems : " +totalNoOfItems);
		double totalPrice = itemsList.stream().mapToDouble(a -> a.getPrice()).sum();
		System.out.println("totalPrice : " +totalPrice);

		OrderItem extraItem = new OrderItem();
		extraItem.setName("Total Price");
		extraItem.setNoOfItem(totalNoOfItems);
		extraItem.setPrice(totalPrice);
		itemsList.add(extraItem);
		System.out.println("Bill after adding Total Price : " +itemsList);

		return itemsList;
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

}
