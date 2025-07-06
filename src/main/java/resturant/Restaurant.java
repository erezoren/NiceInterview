package resturant;

import java.util.List;
import java.util.concurrent.Executors;
import resturant.model.Menu;
import resturant.model.MenuItem;
import resturant.model.Pasta;
import resturant.model.Pizza;

public class Restaurant {

  private OrdersHandler ordersHandler;

  public Restaurant(int numDeliverers) {
    this.ordersHandler = new OrdersHandler(Executors.newScheduledThreadPool(numDeliverers));
  }

  public void open() {
    System.out.println("Restaurant opens");
    ordersHandler.scheduledPrintOrders();
  }

  public void addOrder(String destination, List<MenuItem> menuItems) {
    ordersHandler.addItems(destination, menuItems);
  }

  public static void main(String[] args) {

    Restaurant restaurant = new Restaurant(4);

    List<MenuItem> menuItems = List.of(
        new Pasta("Pene"),
        new Pizza(true)
    );
    restaurant.addOrder("Tel Aviv", menuItems);
    restaurant.addOrder("Kfar Saba", menuItems);
    restaurant.addOrder("Netanya", menuItems);
    restaurant.addOrder("Kfar Saba", menuItems);
    restaurant.addOrder("Kfar Saba", menuItems);
  }
}
