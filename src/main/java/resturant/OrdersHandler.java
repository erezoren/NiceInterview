package resturant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import resturant.model.MenuItem;
import resturant.model.Order;

public class OrdersHandler {

  private static final int CORE_POOL_SIZE = 10;
  final ScheduledExecutorService printScheduler;
  final List<Order> allOrders;
  final Map<String, List<Order>> destinationOrders;

  public OrdersHandler() {
    printScheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    printScheduler.schedule(this::scheduledPrintOrders, 5, TimeUnit.SECONDS);
    allOrders = new ArrayList<>();
    destinationOrders = new ConcurrentHashMap<>();
  }

  void addItems(String destination, List<MenuItem> menuItems) {
    if (menuItems.isEmpty()) {
      System.out.println("No items ordered");
      return;
    }
    Order order = new Order(destination, System.currentTimeMillis(), menuItems);
    allOrders.add(order);
    destinationOrders.putIfAbsent(destination, new ArrayList<>());
    destinationOrders.get(destination).add(order);
  }

  //Print all remaining orders by the time of arrival
  public synchronized void printAllOrders() {
    System.out.println("Printing all orders");
    allOrders.forEach(this::printOrder);
  }

  synchronized void scheduledPrintOrders() {
    System.out.println("########### Printing aggregated orders ##########");
    if (destinationOrders.isEmpty()) {
      System.out.println("No orders pending");
    }
    List<Order> accumulated = new ArrayList<>();
//Sort by number of orders and if equal by arrival
    List<Entry<String, List<Order>>> sortedByNumOfOrders = destinationOrders.entrySet().stream()
        .sorted((es1, es2) -> {
          List<Order> orders1 = es1.getValue();
          List<Order> orders2 = es2.getValue();
          int size1 = orders1.size();
          int size2 = orders2.size();
          if (size2 != size1) {
            return size2 - size1;
          }
          return (int) (orders1.get(0).getArrivalTime() - orders2.get(0).getArrivalTime());
        })
        .collect(Collectors.toList());

    for (Map.Entry<String, List<Order>> entry : sortedByNumOfOrders) {
      List<Order> orders = entry.getValue();

      Iterator<Order> iterator = orders.iterator();
      while (iterator.hasNext()) {
        Order order = iterator.next();
        accumulated.add(order);
        iterator.remove();
        if (accumulated.size() == 3) {
          flush(accumulated);
          accumulated.clear();
        }
      }
    }

    destinationOrders.entrySet().removeIf(entry -> entry.getValue().isEmpty());

    if (!accumulated.isEmpty()) {
      flush(accumulated);
    }

    printScheduler.schedule(this::scheduledPrintOrders, 5, TimeUnit.SECONDS);
  }

  //Flush accumulated 3 orders
  private synchronized void flush(List<Order> accumulated) {
    accumulated.forEach(this::printOrder);
  }

  private void printOrder(Order order) {
    System.out.printf("\nDestination %s\n", order.getDestination());
    System.out.println("----------Mene Items-------------");
    order.getMenuItems().forEach(mi -> {
      System.out.printf("\n%s, price = %d\n", mi.getDescription(), mi.getPrice());
      allOrders.remove(order);
    });
  }
}
