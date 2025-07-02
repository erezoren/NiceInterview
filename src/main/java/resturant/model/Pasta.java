package resturant.model;

public class Pasta implements MenuItem {

  final String kind;

  public Pasta(String kind) {
    this.kind = kind;
  }

  @Override
  public int getPrice() {
    return 50;
  }

  @Override
  public String getDescription() {
    return kind;
  }
}
