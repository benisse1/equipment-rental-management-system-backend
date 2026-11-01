package rwanda.rental.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.rental.rw.domain.Customer;
import rwanda.rental.rw.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    public String saveCustomer(Customer customer) {
        customerRepo.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepo.findById(id);
    }

    public List<Customer> getByLastName(String lastName) {
        return customerRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public String updateCustomer(UUID id, Customer newData) {
        Optional<Customer> existing = customerRepo.findById(id);
        if (existing.isEmpty()) {
            return "not found";
        }
        Customer c = existing.get();
        c.setFirstName(newData.getFirstName());
        c.setLastName(newData.getLastName());
        c.setNationalId(newData.getNationalId());
        c.setPhone(newData.getPhone());
        customerRepo.save(c);
        return "Customer updated successfully";
    }

    public String deleteCustomer(UUID id) {
        if (!customerRepo.existsById(id)) {
            return "not found";
        }
        customerRepo.deleteById(id);
        return "Customer deleted successfully";
    }
}