package resturant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import resturant.model.MenuItem;
import resturant.model.Order;

public class OrdersHandler {

  final ScheduledExecutorService printScheduler;
  final List<Order> allOrders;
  final Map<String, List<Order>> destinationOrders;

  public OrdersHandler(ScheduledExecutorService printScheduler) {
    this.printScheduler = printScheduler;
    this.printScheduler.schedule(this::scheduledPrintOrders, 5, TimeUnit.SECONDS);
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
    boolean doneDelivery = false;
    Iterator<Entry<String, List<Order>>> iterator = destinationOrders.entrySet().iterator();
    doneDelivery = handleFirstDestination(iterator, accumulated, doneDelivery);
    if (!doneDelivery) {
      handleRestOfOrders(iterator, accumulated);
    }
    printScheduler.schedule(this::scheduledPrintOrders, 5, TimeUnit.SECONDS);
  }

  private boolean handleFirstDestination(Iterator<Entry<String, List<Order>>> iterator, List<Order> accumulated, boolean doneDelivery) {
    Entry<String, List<Order>> firstDestination = iterator.next();
    accumulated.addAll(firstDestination.getValue());
    if (accumulated.size() == 3) {
      flush(accumulated);
      accumulated.clear();
      doneDelivery = true;
    }
    return doneDelivery;
  }

  private void handleRestOfOrders(Iterator<Entry<String, List<Order>>> iterator, List<Order> accumulated) {
    String maxDestination = "";
    int count = 0;
    while (iterator.hasNext()) {
      Entry<String, List<Order>> next = iterator.next();
      if (next.getValue().size() > count) {
        maxDestination = next.getKey();
        count = next.getValue().size();
      }
    }
    List<Order> orders = destinationOrders.get(maxDestination);
    for (Order nextOrder : orders) {
      accumulated.add(nextOrder);
      if (accumulated.size() == 3) {
        flush(accumulated);
        accumulated.clear();
        break;
      }
    }
  }

  //Flush accumulated 3 orders
  private synchronized void flush(List<Order> accumulated) {
    accumulated.forEach(order -> {
      printOrder(order);
      allOrders.remove(order);
      destinationOrders.get(order.getDestination()).remove(order);
    });
  }

  private void printOrder(Order order) {
    System.out.printf("\nDestination %s\n", order.getDestination());
    System.out.println("----------Mene Items-------------");
    order.getMenuItems().forEach(mi -> {
      System.out.printf("\n%s, price = %d\n", mi.getDescription(), mi.getPrice());
    });
  }
}
