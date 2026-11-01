package rwanda.rental.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rwanda.rental.rw.domain.EquipmentCategory;
import rwanda.rental.rw.service.EquipmentCategoryService;

@RestController
@RequestMapping("/api/equipment-categories")
public class EquipmentCategoryController {

    @Autowired
    private EquipmentCategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EquipmentCategory category) {
        String msg = categoryService.saveCategory(category);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EquipmentCategory>> all() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/unused")
    public ResponseEntity<List<EquipmentCategory>> unused() {
        return ResponseEntity.ok(categoryService.getUnusedCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<EquipmentCategory> c = categoryService.getCategoryById(id);
        if (c.isEmpty()) {
            return new ResponseEntity<>("The equipment category with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(c.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody EquipmentCategory c) {
        String msg = categoryService.updateCategory(id, c);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The equipment category with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = categoryService.deleteCategory(id);
        if (msg.equals("not found")) {
            return new ResponseEntity<>("The equipment category with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }
}