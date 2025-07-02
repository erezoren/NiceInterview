package resturant.model;

public class Pizza implements MenuItem {

  final boolean withTopping;

  public Pizza(boolean withTopping) {
    this.withTopping = withTopping;
  }

  @Override
  public int getPrice() {
    return withTopping ? 45 : 30;
  }

  @Override
  public String getDescription() {
    return String.format("Pizza with%s toppings", withTopping ? "" : "out");
  }
}
