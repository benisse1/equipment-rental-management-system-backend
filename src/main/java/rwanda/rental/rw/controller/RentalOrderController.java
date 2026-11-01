package rwanda.rental.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.RentalOrder;
import rwanda.rental.rw.domain.RentalOrderStatus;
import rwanda.rental.rw.service.RentalOrderService;

@RestController
@RequestMapping("/api/rental-orders")
public class RentalOrderController {

    @Autowired
    private RentalOrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody RentalOrder order) {
        String msg = orderService.saveOrder(order);
        if (msg.equals("Rental order date cannot be in the past")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        if (msg.equals("Equipment item is already reserved on that date")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalOrder>> all() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<RentalOrder>> byStatus(@RequestParam RentalOrderStatus status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<RentalOrder>> between(@RequestParam String start, @RequestParam String end) {
        LocalDate d1 = LocalDate.parse(start);
        LocalDate d2 = LocalDate.parse(end);
        return ResponseEntity.ok(orderService.getBetween(d1, d2));
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> statsByStatus() {
        return ResponseEntity.ok(orderService.getStatusCount());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam RentalOrderStatus status) {
        String msg = orderService.changeStatus(id, status);
        if (msg.equals("The rental order with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.startsWith("Cannot change status")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID equipmentItemId, @RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(orderService.cancelDay(equipmentItemId, d));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<RentalOrder>> page(@RequestParam int page, @RequestParam int size,
            @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));
        return ResponseEntity.ok(orderService.getPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<RentalOrder> o = orderService.getOrderById(id);
        if (o.isEmpty()) {
            return new ResponseEntity<>("The rental order with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(o.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody RentalOrder o) {
        String msg = orderService.updateOrder(id, o);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The rental order with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = orderService.deleteOrder(id);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The rental order with that id does not exist", HttpStatus.NOT_FOUND);
        }
        if (msg.equals("That rental order has invoice lines, delete them first")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }
}