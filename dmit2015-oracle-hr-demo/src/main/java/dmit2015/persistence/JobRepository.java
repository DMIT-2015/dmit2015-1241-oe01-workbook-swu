package dmit2015.persistence;

import dmit2015.entity.Job;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * This Jakarta Persistence class contains methods for performing CRUD operations on a
 * Jakarta Persistence managed entity.
 */
@ApplicationScoped
public class JobRepository {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager _entityManager;

    @Transactional
    public void add(@Valid Job newJob) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        _entityManager.persist(newJob);
    }

    public Optional<Job> findById(String jobId) {
        try {
            Job querySingleResult = _entityManager.find(Job.class, jobId);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // jobId value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    public List<Job> findAll() {
        return _entityManager.createQuery("SELECT o FROM Job o ", Job.class)
                .getResultList();
    }

    @Transactional
    public Job update(String id, @Valid Job updatedJob) {
        Optional<Job> optionalJob = findById(id);
        if (optionalJob.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", id);
            throw new RuntimeException(errorMessage);
        } else {
             var existingJob = optionalJob.orElseThrow();
             // Update only properties that is editable by the end user
            existingJob.setJobTitle(updatedJob.getJobTitle());
            existingJob.setMinSalary(updatedJob.getMinSalary());
            existingJob.setMaxSalary(updatedJob.getMaxSalary());

            updatedJob = _entityManager.merge(existingJob);
        }
        return updatedJob;
    }

    @Transactional
    public void delete(Job existingJob) {
         // Write code to throw a RuntimeException if this entity contains child records

        if (_entityManager.contains(existingJob)) {
            _entityManager.remove(existingJob);
        } else {
            _entityManager.remove(_entityManager.merge(existingJob));
        }
    }

    @Transactional
    public void deleteById(String jobId) {
        Optional<Job> optionalJob = findById(jobId);
        if (optionalJob.isPresent()) {
            Job existingJob = optionalJob.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records

            _entityManager.remove(existingJob);
        }
    }

    public long count() {
        return _entityManager.createQuery("SELECT COUNT(o) FROM Job o", Long.class).getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        _entityManager.flush();
        _entityManager.clear();
        _entityManager.createQuery("DELETE FROM Job").executeUpdate();
    }

}