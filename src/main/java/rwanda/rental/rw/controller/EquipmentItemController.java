package rwanda.rental.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.EquipmentItem;
import rwanda.rental.rw.service.EquipmentItemService;

@RestController
@RequestMapping("/api/equipment-items")
public class EquipmentItemController {

    @Autowired
    private EquipmentItemService itemService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EquipmentItem item) {
        String msg = itemService.saveItem(item);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EquipmentItem>> all() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/by-equipment-category")
    public ResponseEntity<List<EquipmentItem>> byCategory(@RequestParam String name) {
        return ResponseEntity.ok(itemService.getByCategoryName(name));
    }

    @PutMapping("/{itemId}/equipment-categories/{categoryId}")
    public ResponseEntity<?> addCategory(@PathVariable UUID itemId, @PathVariable UUID categoryId) {
        String msg = itemService.addCategoryToItem(itemId, categoryId);
        if (msg.equals("The equipment item with that id does not exist")
                || msg.equals("That equipment category does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("The equipment item already has that equipment category")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{itemId}/storage-slot")
    public ResponseEntity<?> moveSlot(@PathVariable UUID itemId, @RequestParam int slotNumber) {
        String msg = itemService.moveItemToSlot(itemId, slotNumber);
        if (msg.equals("The equipment item with that id does not exist")
                || msg.equals("The storage slot with that slotNumber does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("That storage slot is taken by another equipment item")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<EquipmentItem> e = itemService.getItemById(id);
        if (e.isEmpty()) {
            return new ResponseEntity<>("The equipment item with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(e.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody EquipmentItem e) {
        String msg = itemService.updateItem(id, e);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The equipment item with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = itemService.deleteItem(id);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The equipment item with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }
}