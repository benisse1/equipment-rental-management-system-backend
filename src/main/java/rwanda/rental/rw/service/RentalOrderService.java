package rwanda.rental.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.rental.rw.domain.Customer;
import rwanda.rental.rw.domain.RentalOrder;
import rwanda.rental.rw.domain.RentalOrderStatus;
import rwanda.rental.rw.repository.InvoiceLineRepository;
import rwanda.rental.rw.repository.RentalOrderRepository;

@Service
public class RentalOrderService {

    @Autowired
    private RentalOrderRepository orderRepo;

    @Autowired
    private InvoiceLineRepository invoiceRepo;

    public String saveOrder(RentalOrder order) {
        if (order.getStartDate() != null && order.getStartDate().isBefore(LocalDate.now())) {
            return "Rental order date cannot be in the past";
        }

        if (order.getEquipmentItem() != null && order.getStartDate() != null) {
            boolean exists = orderRepo.existsByEquipmentItemAndStartDateAndStatusNot(
                    order.getEquipmentItem(), order.getStartDate(), RentalOrderStatus.VOIDED);
            if (exists) {
                return "Equipment item is already reserved on that date";
            }
        }

        order.setStatus(RentalOrderStatus.RESERVED);
        orderRepo.save(order);
        return "Rental order saved successfully";
    }

    public List<RentalOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    public Optional<RentalOrder> getOrderById(UUID id) {
        return orderRepo.findById(id);
    }

    public String updateOrder(UUID id, RentalOrder newData) {
        Optional<RentalOrder> existing = orderRepo.findById(id);
        if (existing.isEmpty()) {
            return "not found";
        }
        RentalOrder o = existing.get();
        o.setStartDate(newData.getStartDate());
        o.setDays(newData.getDays());
        o.setCustomer(newData.getCustomer());
        o.setEquipmentItem(newData.getEquipmentItem());
        orderRepo.save(o);
        return "Rental order updated successfully";
    }

    public String deleteOrder(UUID id) {
        if (!orderRepo.existsById(id)) {
            return "not found";
        }
        if (invoiceRepo.existsByRentalOrderId(id)) {
            return "That rental order has invoice lines, delete them first";
        }
        orderRepo.deleteById(id);
        return "Rental order deleted successfully";
    }

    public List<RentalOrder> getByStatus(RentalOrderStatus status) {
        return orderRepo.findByStatusOrderByStartDateAsc(status);
    }

    public List<RentalOrder> getBetween(LocalDate start, LocalDate end) {
        return orderRepo.findByStartDateBetweenOrderByStartDateAsc(start, end);
    }

    public List<Customer> getCustomersByEquipment(UUID itemId) {
        return orderRepo.findCustomersByEquipmentItem(itemId);
    }

    public List<Object[]> getStatusCount() {
        return orderRepo.countOrdersByStatus();
    }

    public String changeStatus(UUID id, RentalOrderStatus newStatus) {
        Optional<RentalOrder> existing = orderRepo.findById(id);
        if (existing.isEmpty()) {
            return "The rental order with that id does not exist";
        }
        RentalOrder o = existing.get();
        RentalOrderStatus current = o.getStatus();
        boolean allowed = false;

        if (current == RentalOrderStatus.RESERVED &&
                (newStatus == RentalOrderStatus.CHECKED_OUT || newStatus == RentalOrderStatus.VOIDED)) {
            allowed = true;
        } else if (current == RentalOrderStatus.CHECKED_OUT &&
                (newStatus == RentalOrderStatus.RETURNED || newStatus == RentalOrderStatus.VOIDED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        o.setStatus(newStatus);
        orderRepo.save(o);
        return "Rental order status updated successfully";
    }

    @Transactional
    public String cancelDay(UUID itemId, LocalDate date) {
        int count = orderRepo.cancelOrdersForDay(itemId, date, RentalOrderStatus.VOIDED, RentalOrderStatus.RETURNED);
        return count + " rental orders cancelled";
    }

    public Page<RentalOrder> getPage(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }
}