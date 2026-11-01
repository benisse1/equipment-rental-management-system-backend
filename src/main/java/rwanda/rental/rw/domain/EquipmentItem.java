package rwanda.rental.rw.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipment_item")
public class EquipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "daily_rate")
    private double dailyRate;

    @ManyToOne
    @JoinColumn(name = "storage_slot_id")
    private StorageSlot storageSlot;

    @ManyToMany
    @JoinTable(name = "equipment_item_equipment_category", joinColumns = @JoinColumn(name = "equipment_item_id"), inverseJoinColumns = @JoinColumn(name = "equipment_category_id"))
    private List<EquipmentCategory> equipmentCategories = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public StorageSlot getStorageSlot() {
        return storageSlot;
    }

    public void setStorageSlot(StorageSlot storageSlot) {
        this.storageSlot = storageSlot;
    }

    public List<EquipmentCategory> getEquipmentCategories() {
        return equipmentCategories;
    }

    public void setEquipmentCategories(List<EquipmentCategory> equipmentCategories) {
        this.equipmentCategories = equipmentCategories;
    }
}