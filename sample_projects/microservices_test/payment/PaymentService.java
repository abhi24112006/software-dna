package payment;

public class PaymentService {

    private final PaymentRepository repository;


    public PaymentService(
            PaymentRepository repository) {

        this.repository =
                repository;

    }


    public String getPayment(
            int id) {

        return repository.findPayment(
                id
        );

    }

}