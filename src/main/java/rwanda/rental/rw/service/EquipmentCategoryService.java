package rwanda.rental.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.EquipmentCategory;
import rwanda.rental.rw.repository.EquipmentCategoryRepository;

@Service
public class EquipmentCategoryService {

    @Autowired
    private EquipmentCategoryRepository categoryRepo;

    public String saveCategory(EquipmentCategory category) {
        categoryRepo.save(category);
        return "Equipment category saved successfully";
    }

    public List<EquipmentCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Optional<EquipmentCategory> getCategoryById(UUID id) {
        return categoryRepo.findById(id);
    }

    public String updateCategory(UUID id, EquipmentCategory newData) {
        Optional<EquipmentCategory> existing = categoryRepo.findById(id);
        if (existing.isEmpty()) {
            return "not found";
        }
        EquipmentCategory c = existing.get();
        c.setName(newData.getName());
        categoryRepo.save(c);
        return "Equipment category updated successfully";
    }

    public String deleteCategory(UUID id) {
        if (!categoryRepo.existsById(id)) {
            return "not found";
        }
        categoryRepo.deleteById(id);
        return "Equipment category deleted successfully";
    }

    public List<EquipmentCategory> getUnusedCategories() {
        return categoryRepo.findUnusedCategories();
    }
}