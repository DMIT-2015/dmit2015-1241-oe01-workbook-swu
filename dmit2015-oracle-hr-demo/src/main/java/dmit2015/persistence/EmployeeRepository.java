package dmit2015.persistence;

import dmit2015.entity.Employee;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository(dataStore = "oracle-jpa-hr-pu")
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}
