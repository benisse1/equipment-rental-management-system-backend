package rwanda.rental.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.InvoiceLine;
import rwanda.rental.rw.service.InvoiceLineService;

@RestController
@RequestMapping("/api/invoice-lines")
public class InvoiceLineController {

    @Autowired
    private InvoiceLineService invoiceService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID rentalOrderId, @RequestBody InvoiceLine line) {
        String msg = invoiceService.saveInvoice(rentalOrderId, line);
        if (msg.equals("The rental order with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("Invoice lines are only for RETURNED rental orders")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{nationalId}")
    public ResponseEntity<List<InvoiceLine>> byCustomer(@PathVariable String nationalId) {
        return ResponseEntity.ok(invoiceService.getByCustomer(nationalId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = invoiceService.deleteInvoice(id);
        if (msg.equals("The invoice line with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }
}