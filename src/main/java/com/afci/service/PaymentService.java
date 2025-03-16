package com.afci.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.Customer;
import com.afci.data.Order;
import com.afci.data.Payment;
import com.afci.data.PaymentStatus;
import com.afci.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public List<Payment> getPaymentsByCustomer(Customer customer) {
        return paymentRepository.findByCustomer(customer);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Payment payment) {
        if (paymentRepository.existsById(payment.getId())) {
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Paiement non trouvé avec l'ID : " + payment.getId());
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // Méthode pour traiter un paiement
    public Payment processPayment(Long orderId, Payment payment) {
        // Récupérer l'Order à partir de l'ID
        Optional<Order> orderOptional = orderService.getOrderById(orderId);

        // Vérifier si l'Order existe
        if (orderOptional.isPresent()) {
            // Si l'Order existe, on affecte l'Order à l'objet Payment
            payment.setOrder(orderOptional.get());

            // Définir le statut du paiement sur "PENDING"
            payment.setStatus(PaymentStatus.PENDING);

            // Sauvegarder le paiement
            return paymentRepository.save(payment);
        } else {
            // Si l'Order n'existe pas, lever une exception ou retourner une valeur appropriée
            throw new RuntimeException("Order with ID " + orderId + " not found");
        }
    }

    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(status);
            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Paiement non trouvé avec l'ID : " + id);
        }
    }

    public List<Payment> getOrderPayments(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
