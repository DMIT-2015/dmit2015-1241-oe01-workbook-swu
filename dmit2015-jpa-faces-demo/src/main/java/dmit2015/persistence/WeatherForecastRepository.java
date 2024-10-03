package dmit2015.persistence;

import dmit2015.entity.WeatherForecast;
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
public class WeatherForecastRepository {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext (unitName="postgresql-jpa-pu")
    private EntityManager _entityManager;

    @Transactional
    public void add(@Valid WeatherForecast newWeatherForecast) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        _entityManager.persist(newWeatherForecast);
    }

    public Optional<WeatherForecast> findById(Long weatherForecastId) {
        try {
            WeatherForecast querySingleResult = _entityManager.find(WeatherForecast.class, weatherForecastId);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // weatherForecastId value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    public List<WeatherForecast> findAll() {
        return _entityManager.createQuery("SELECT o FROM WeatherForecast o ", WeatherForecast.class)
                .getResultList();
    }

    @Transactional
    public WeatherForecast update(Long id, @Valid WeatherForecast updatedWeatherForecast) {
        Optional<WeatherForecast> optionalWeatherForecast = findById(id);
        if (optionalWeatherForecast.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", id);
            throw new RuntimeException(errorMessage);
        } else {
            var existingWeatherForecast = optionalWeatherForecast.orElseThrow();
            // Update only properties that is editable by the end user
            existingWeatherForecast.setCity(updatedWeatherForecast.getCity());
            existingWeatherForecast.setTemperatureCelsius(updatedWeatherForecast.getTemperatureCelsius());
            existingWeatherForecast.setDate(updatedWeatherForecast.getDate());
            existingWeatherForecast.setDescription(updatedWeatherForecast.getDescription());
            updatedWeatherForecast = _entityManager.merge(existingWeatherForecast);
        }
        return updatedWeatherForecast;
    }

    @Transactional
    public void delete(WeatherForecast existingWeatherForecast) {
        // Write code to throw a RuntimeException if this entity contains child records

        if (_entityManager.contains(existingWeatherForecast)) {
            _entityManager.remove(existingWeatherForecast);
        } else {
            _entityManager.remove(_entityManager.merge(existingWeatherForecast));
        }
    }

    @Transactional
    public void deleteById(Long weatherForecastId) {
        Optional<WeatherForecast> optionalWeatherForecast = findById(weatherForecastId);
        if (optionalWeatherForecast.isPresent()) {
            WeatherForecast existingWeatherForecast = optionalWeatherForecast.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records

            _entityManager.remove(existingWeatherForecast);
        }
    }

    public long count() {
        return _entityManager.createQuery("SELECT COUNT(o) FROM WeatherForecast o", Long.class).getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        _entityManager.flush();
        _entityManager.clear();
        _entityManager.createQuery("DELETE FROM WeatherForecast").executeUpdate();
    }

}