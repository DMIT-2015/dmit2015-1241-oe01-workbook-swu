package dmit2015.persistence;

import dmit2015.entity.Country;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository(dataStore = "oracle-jpa-hr-pu")
public interface CountryRepository extends CrudRepository<Country, String> {
}
