package rwanda.rental.rw.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rental_order")
public class RentalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RentalOrderStatus status;

    @Column(name = "days")
    private int days;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "equipment_item_id")
    private EquipmentItem equipmentItem;

    public UUID getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public RentalOrderStatus getStatus() {
        return status;
    }

    public void setStatus(RentalOrderStatus status) {
        this.status = status;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public EquipmentItem getEquipmentItem() {
        return equipmentItem;
    }

    public void setEquipmentItem(EquipmentItem equipmentItem) {
        this.equipmentItem = equipmentItem;
    }
}