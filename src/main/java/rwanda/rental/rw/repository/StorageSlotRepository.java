package rwanda.rental.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.StorageSlot;

@Repository
public interface StorageSlotRepository extends JpaRepository<StorageSlot, UUID> {

    Optional<StorageSlot> findBySlotNumber(int slotNumber);

    @Query("SELECT s FROM StorageSlot s WHERE NOT EXISTS (SELECT e FROM EquipmentItem e WHERE e.storageSlot = s) ORDER BY s.slotNumber")
    List<StorageSlot> findFreeSlots();
}