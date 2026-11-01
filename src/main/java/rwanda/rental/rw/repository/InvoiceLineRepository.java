package rwanda.rental.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.InvoiceLine;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, UUID> {

    boolean existsByRentalOrderId(UUID rentalOrderId);

    @Query("SELECT l FROM InvoiceLine l WHERE l.rentalOrder.customer.nationalId = :nationalId ORDER BY l.billedDate DESC")
    List<InvoiceLine> findByCustomerNationalId(@Param("nationalId") String nationalId);
}