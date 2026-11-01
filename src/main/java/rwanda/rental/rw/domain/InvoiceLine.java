package rwanda.rental.rw.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_line")
public class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;

    @Column(name = "amount")
    private int amount;

    @Column(name = "billed_date")
    private LocalDate billedDate;

    @ManyToOne
    @JoinColumn(name = "rental_order_id")
    private RentalOrder rentalOrder;

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getBilledDate() {
        return billedDate;
    }

    public void setBilledDate(LocalDate billedDate) {
        this.billedDate = billedDate;
    }

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }
}