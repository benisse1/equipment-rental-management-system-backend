package rwanda.rental.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.Customer;
import rwanda.rental.rw.domain.EquipmentItem;
import rwanda.rental.rw.domain.RentalOrder;
import rwanda.rental.rw.domain.RentalOrderStatus;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, UUID> {

    long countByStatus(RentalOrderStatus status);

    long countByStartDate(LocalDate startDate);

    List<RentalOrder> findByStatusOrderByStartDateAsc(RentalOrderStatus status);

    List<RentalOrder> findByStartDateBetweenOrderByStartDateAsc(LocalDate start, LocalDate end);

    boolean existsByEquipmentItemAndStartDateAndStatusNot(EquipmentItem item, LocalDate date, RentalOrderStatus status);

    @Query("SELECT DISTINCT o.customer FROM RentalOrder o WHERE o.equipmentItem.id = :itemId")
    List<Customer> findCustomersByEquipmentItem(@Param("itemId") UUID itemId);

    @Query("SELECT o.status, COUNT(o) FROM RentalOrder o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    @Modifying
    @Query("UPDATE RentalOrder o SET o.status = :newStatus WHERE o.equipmentItem.id = :itemId AND o.startDate = :date AND o.status <> :excluded")
    int cancelOrdersForDay(@Param("itemId") UUID itemId, @Param("date") LocalDate date,
            @Param("newStatus") RentalOrderStatus newStatus, @Param("excluded") RentalOrderStatus excluded);
}