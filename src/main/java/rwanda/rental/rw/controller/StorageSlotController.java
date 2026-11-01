package rwanda.rental.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.StorageSlot;
import rwanda.rental.rw.service.StorageSlotService;

@RestController
@RequestMapping("/api/storage-slots")
public class StorageSlotController {

    @Autowired
    private StorageSlotService slotService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody StorageSlot slot) {
        String msg = slotService.saveSlot(slot);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StorageSlot>> all() {
        return ResponseEntity.ok(slotService.getAllSlots());
    }

    @GetMapping("/free")
    public ResponseEntity<List<StorageSlot>> free() {
        return ResponseEntity.ok(slotService.getFreeSlots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<StorageSlot> s = slotService.getSlotById(id);
        if (s.isEmpty()) {
            return new ResponseEntity<>("The storage slot with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(s.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody StorageSlot s) {
        String msg = slotService.updateSlot(id, s);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The storage slot with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = slotService.deleteSlot(id);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The storage slot with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }
}