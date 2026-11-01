package rwanda.rental.rw.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.RentalOrderStatus;
import rwanda.rental.rw.repository.CustomerRepository;
import rwanda.rental.rw.repository.EquipmentItemRepository;
import rwanda.rental.rw.repository.InvoiceLineRepository;
import rwanda.rental.rw.repository.RentalOrderRepository;
import rwanda.rental.rw.repository.StorageSlotRepository;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private EquipmentItemRepository equipmentItemRepo;

    @Autowired
    private StorageSlotRepository slotRepo;

    @Autowired
    private RentalOrderRepository orderRepo;

    @Autowired
    private InvoiceLineRepository invoiceRepo;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("customers", customerRepo.count());
        summary.put("equipmentItems", equipmentItemRepo.count());
        summary.put("freeStorageSlots", (long) slotRepo.findFreeSlots().size());
        summary.put("ordersToday", orderRepo.countByStartDate(LocalDate.now()));
        summary.put("reserved", orderRepo.countByStatus(RentalOrderStatus.RESERVED));
        summary.put("invoiceLines", invoiceRepo.count());

        return summary;
    }
}