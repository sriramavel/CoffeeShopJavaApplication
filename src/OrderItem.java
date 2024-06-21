public class OrderItem {

    private String name;
    private int noOfItem;
    private double price;

    public OrderItem() {
    }

    public OrderItem(String name, int noOfItem, double price) {
        this.name = name;
        this.noOfItem = noOfItem;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getNoOfItem(){
        return noOfItem;
    }

    public void setNoOfItem(int countOfItems){
        this.noOfItem = countOfItems;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}