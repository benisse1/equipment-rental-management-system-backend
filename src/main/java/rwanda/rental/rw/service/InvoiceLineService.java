package rwanda.rental.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.InvoiceLine;
import rwanda.rental.rw.domain.RentalOrder;
import rwanda.rental.rw.domain.RentalOrderStatus;
import rwanda.rental.rw.repository.InvoiceLineRepository;
import rwanda.rental.rw.repository.RentalOrderRepository;

@Service
public class InvoiceLineService {

    @Autowired
    private InvoiceLineRepository invoiceRepo;

    @Autowired
    private RentalOrderRepository orderRepo;

    public String saveInvoice(UUID orderId, InvoiceLine line) {
        Optional<RentalOrder> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isEmpty()) {
            return "The rental order with that id does not exist";
        }
        RentalOrder order = orderOpt.get();
        if (order.getStatus() != RentalOrderStatus.RETURNED) {
            return "Invoice lines are only for RETURNED rental orders";
        }

        line.setBilledDate(LocalDate.now());
        line.setRentalOrder(order);
        invoiceRepo.save(line);
        return "Invoice line saved successfully";
    }

    public List<InvoiceLine> getByCustomer(String nationalId) {
        return invoiceRepo.findByCustomerNationalId(nationalId);
    }

    public String deleteInvoice(UUID id) {
        if (!invoiceRepo.existsById(id)) {
            return "The invoice line with that id does not exist";
        }
        invoiceRepo.deleteById(id);
        return "Invoice line deleted successfully";
    }
}