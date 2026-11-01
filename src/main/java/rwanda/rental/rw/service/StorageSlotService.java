package rwanda.rental.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.StorageSlot;
import rwanda.rental.rw.repository.StorageSlotRepository;

@Service
public class StorageSlotService {

    @Autowired
    private StorageSlotRepository slotRepo;

    public String saveSlot(StorageSlot slot) {
        slotRepo.save(slot);
        return "Storage slot saved successfully";
    }

    public List<StorageSlot> getAllSlots() {
        return slotRepo.findAll();
    }

    public Optional<StorageSlot> getSlotById(UUID id) {
        return slotRepo.findById(id);
    }

    public String updateSlot(UUID id, StorageSlot newData) {
        Optional<StorageSlot> existing = slotRepo.findById(id);
        if (existing.isEmpty()) {
            return "not found";
        }
        StorageSlot s = existing.get();
        s.setSlotNumber(newData.getSlotNumber());
        s.setName(newData.getName());
        slotRepo.save(s);
        return "Storage slot updated successfully";
    }

    public String deleteSlot(UUID id) {
        if (!slotRepo.existsById(id)) {
            return "not found";
        }
        slotRepo.deleteById(id);
        return "Storage slot deleted successfully";
    }

    public List<StorageSlot> getFreeSlots() {
        return slotRepo.findFreeSlots();
    }
}