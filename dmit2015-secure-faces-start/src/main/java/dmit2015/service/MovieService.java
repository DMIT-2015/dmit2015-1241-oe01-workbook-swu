package dmit2015.service;

import dmit2015.entity.Movie;
import dmit2015.repository.MovieRepository;
import jakarta.data.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieService implements Serializable {

    @Inject
    private SecurityContext _securityContext;

    @Inject
    private MovieRepository _movieRepository;

    public void addMovie(Movie newMovie) {
        // Assign the username of this entity to the current authenticated username
        String username = _securityContext.getCallerPrincipal().getName();
        newMovie.setUsername(username);

        newMovie.setCreateTime(LocalDateTime.now());
        _movieRepository.add(newMovie);
    }

    @Transactional
    public void updateMovie(Movie updatedMovie) {
        updatedMovie.setUpdateTime(LocalDateTime.now());
        _movieRepository.update(updatedMovie);
    }

    public void deleteMovie(Movie existingMovie) {
        _movieRepository.delete(existingMovie);
    }

//    public void deleteMovieById(Long id) {
//        Optional<Movie> optionalMovie = _movieRepository.find(id);
//        if (optionalMovie.isPresent()) {
//            Movie existingMovie = optionalMovie.orElseThrow();
//            deleteMovie(existingMovie);
//        }
//    }

    public Optional<Movie> findMovieById(Long id) {
        return _movieRepository.find(id);
    }

    public List<Movie> findAllMovies(Sort<Movie> sort) {
//        return _movieRepository.findAll(sort);
        if (_securityContext.getCallerPrincipal().getName().equalsIgnoreCase("anonymous") ) {
            throw new RuntimeException("Access Denied. Anonymous users do not have permission to access this method.");
        }
        if (_securityContext.isCallerInRole("Executive") ||
                _securityContext.isCallerInRole("Administration") ) {
            return _movieRepository.findAll(Sort.asc("title"));
        }
        String username = _securityContext.getCallerPrincipal().getName();
        return _movieRepository.findAllBy(username, Sort.desc("releaseDate"));
    }

    public long countMovies() {
        return _movieRepository.count();
    }

    public void deleteAllMovies() {
        _movieRepository.deleteAll();
    }

}
