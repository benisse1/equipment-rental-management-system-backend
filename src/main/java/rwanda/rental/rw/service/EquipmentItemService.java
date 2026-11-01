package rwanda.rental.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.EquipmentCategory;
import rwanda.rental.rw.domain.EquipmentItem;
import rwanda.rental.rw.domain.StorageSlot;
import rwanda.rental.rw.repository.EquipmentCategoryRepository;
import rwanda.rental.rw.repository.EquipmentItemRepository;
import rwanda.rental.rw.repository.StorageSlotRepository;

@Service
public class EquipmentItemService {

    @Autowired
    private EquipmentItemRepository itemRepo;

    @Autowired
    private StorageSlotRepository slotRepo;

    @Autowired
    private EquipmentCategoryRepository categoryRepo;

    public String saveItem(EquipmentItem item) {
        itemRepo.save(item);
        return "Equipment item saved successfully";
    }

    public List<EquipmentItem> getAllItems() {
        return itemRepo.findAll();
    }

    public Optional<EquipmentItem> getItemById(UUID id) {
        return itemRepo.findById(id);
    }

    public String updateItem(UUID id, EquipmentItem newData) {
        Optional<EquipmentItem> existing = itemRepo.findById(id);
        if (existing.isEmpty()) {
            return "not found";
        }
        EquipmentItem e = existing.get();
        e.setName(newData.getName());
        e.setBrand(newData.getBrand());
        e.setSerialNumber(newData.getSerialNumber());
        e.setDailyRate(newData.getDailyRate());
        e.setStorageSlot(newData.getStorageSlot());
        itemRepo.save(e);
        return "Equipment item updated successfully";
    }

    public String deleteItem(UUID id) {
        if (!itemRepo.existsById(id)) {
            return "not found";
        }
        itemRepo.deleteById(id);
        return "Equipment item deleted successfully";
    }

    public List<EquipmentItem> getByCategoryName(String name) {
        return itemRepo.findByCategoryName(name);
    }

    public String addCategoryToItem(UUID itemId, UUID categoryId) {
        Optional<EquipmentItem> itemOpt = itemRepo.findById(itemId);
        if (itemOpt.isEmpty()) {
            return "The equipment item with that id does not exist";
        }
        Optional<EquipmentCategory> catOpt = categoryRepo.findById(categoryId);
        if (catOpt.isEmpty()) {
            return "That equipment category does not exist";
        }

        EquipmentItem item = itemOpt.get();
        EquipmentCategory cat = catOpt.get();

        if (item.getEquipmentCategories().contains(cat)) {
            return "The equipment item already has that equipment category";
        }

        item.getEquipmentCategories().add(cat);
        itemRepo.save(item);
        return "Equipment category added to the equipment item";
    }

    public String moveItemToSlot(UUID itemId, int slotNumber) {
        Optional<EquipmentItem> itemOpt = itemRepo.findById(itemId);
        if (itemOpt.isEmpty()) {
            return "The equipment item with that id does not exist";
        }
        Optional<StorageSlot> slotOpt = slotRepo.findBySlotNumber(slotNumber);
        if (slotOpt.isEmpty()) {
            return "The storage slot with that slotNumber does not exist";
        }

        StorageSlot newSlot = slotOpt.get();
        EquipmentItem item = itemOpt.get();

        if (itemRepo.existsByStorageSlot(newSlot)) {
            if (item.getStorageSlot() == null || !item.getStorageSlot().getId().equals(newSlot.getId())) {
                return "That storage slot is taken by another equipment item";
            }
        }

        item.setStorageSlot(newSlot);
        itemRepo.save(item);
        return "Storage slot changed successfully";
    }
}