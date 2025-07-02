package resturant.model;

import java.util.List;
import lombok.Data;
import lombok.Singular;

@Data
public class Order {

  final String destination;
  final long arrivalTime;
  @Singular
  final List<MenuItem> menuItems;
}
