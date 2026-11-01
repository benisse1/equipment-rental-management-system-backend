package rwanda.rental.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.Customer;
import rwanda.rental.rw.repository.EquipmentItemRepository;
import rwanda.rental.rw.service.CustomerService;
import rwanda.rental.rw.service.RentalOrderService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RentalOrderService orderService;

    @Autowired
    private EquipmentItemRepository equipmentItemRepo;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Customer customer) {
        String msg = customerService.saveCustomer(customer);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> all() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Customer>> byLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(customerService.getByLastName(lastName));
    }

    @GetMapping("/of-equipment-item/{equipmentItemId}")
    public ResponseEntity<?> ofEquipmentItem(@PathVariable UUID equipmentItemId) {
        if (!equipmentItemRepo.existsById(equipmentItemId)) {
            return new ResponseEntity<>("The equipment item with that id does not exist", HttpStatus.NOT_FOUND);
        }
        List<Customer> customers = orderService.getCustomersByEquipment(equipmentItemId);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Customer> c = customerService.getCustomerById(id);
        if (c.isEmpty()) {
            return new ResponseEntity<>("The customer with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(c.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Customer c) {
        String msg = customerService.updateCustomer(id, c);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The customer with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = customerService.deleteCustomer(id);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The customer with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }
}