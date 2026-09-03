package order;

public class OrderService {

    private final OrderRepository repository;


    public OrderService(
            OrderRepository repository) {

        this.repository =
                repository;

    }


    public String getOrder(
            int id) {

        return repository.findOrder(
                id
        );

    }

}