package resturant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import resturant.model.MenuItem;
import resturant.model.Pasta;
import resturant.model.Pizza;

@RunWith(MockitoJUnitRunner.class)
public class OrdersHandlerTest {

  private  ScheduledExecutorService scheduler;
  private OrdersHandler ordersHandler;

  @Before
  public void setUp(){
    scheduler = mock(ScheduledExecutorService.class);
    when(scheduler.schedule(any(Runnable.class),anyLong(),any())).then(invocationOnMock -> {
      System.out.println("Print Something");
      return null;
    });
    ordersHandler = new OrdersHandler(scheduler);
  }
  @Test
  public void scheduledPrintOrders(){
    List<MenuItem> menuItems = List.of(
        new Pasta("Pene"),
        new Pizza(true)
    );
    ordersHandler.addItems("Tel Aviv",menuItems);
    ordersHandler.scheduledPrintOrders();
    verify(scheduler,times(2)).schedule(any(Runnable.class),anyLong(),any());
  }
}