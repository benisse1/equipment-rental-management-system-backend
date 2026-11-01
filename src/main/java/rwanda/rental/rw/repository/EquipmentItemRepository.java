package rwanda.rental.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.EquipmentItem;
import rwanda.rental.rw.domain.StorageSlot;

@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, UUID> {

    boolean existsByStorageSlot(StorageSlot storageSlot);

    @Query("SELECT DISTINCT e FROM EquipmentItem e JOIN e.equipmentCategories c WHERE LOWER(c.name) = LOWER(:name)")
    List<EquipmentItem> findByCategoryName(@Param("name") String name);
}