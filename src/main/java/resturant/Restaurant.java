package resturant;

import java.util.List;
import resturant.model.Menu;
import resturant.model.MenuItem;
import resturant.model.Pasta;
import resturant.model.Pizza;

public class Restaurant {

  private final Menu menu;
  private final OrdersHandler ordersHandler;

  public Restaurant(Menu menu, OrdersHandler ordersHandler) {
    this.menu = menu;
    this.ordersHandler = ordersHandler;
  }

  public void open() {
    System.out.println("Restaurant opens");
    ordersHandler.scheduledPrintOrders();
  }

  public void addOrder(String destination, List<MenuItem> menuItems) {
    ordersHandler.addItems(destination, menuItems);
  }

  public static void main(String[] args) {

    Menu menu1 = new Menu();
    Restaurant restaurant = new Restaurant(menu1, new OrdersHandler());

    List<MenuItem> menuItems = List.of(
        new Pasta("Pene"),
        new Pizza(true)
    );
    restaurant.addOrder("Tel Aviv",menuItems);
    restaurant.addOrder("Kfar Saba",menuItems);
    restaurant.addOrder("Netanya",menuItems);
    restaurant.addOrder("Kfar Saba",menuItems);
    restaurant.addOrder("Kfar Saba",menuItems);


    //Planed interactiveCLI Input
    //Tel-Aviv,Pasta,Pene
    //Haifa,Pizza,Topping

 /*   Scanner scanner = new Scanner(System.in);
    while (true){
      String rawItem = scanner.next();
      String[] parts = rawItem.split(",");
      String destination = parts[0];
      String type =
    }*/

  }
}
