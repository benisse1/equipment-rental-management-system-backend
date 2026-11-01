package rwanda.rental.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rwanda.rental.rw.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByNationalId(String nationalId);

    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
}