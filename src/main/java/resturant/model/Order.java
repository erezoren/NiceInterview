package resturant.model;

import java.util.List;
import lombok.Data;
import lombok.Singular;

@Data
public class Order {

  final String destination;
  @Singular
  final List<MenuItem> menuItems;
}
