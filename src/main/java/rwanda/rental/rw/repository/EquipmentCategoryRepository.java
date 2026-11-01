package rwanda.rental.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.EquipmentCategory;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, UUID> {

    boolean existsByName(String name);

    @Query("SELECT c FROM EquipmentCategory c WHERE c.equipmentItems IS EMPTY")
    List<EquipmentCategory> findUnusedCategories();
}