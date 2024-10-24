package dmit2015.service;

import dmit2015.entity.Movie;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initialize the movies database table with sample data if none exists.
 */
@ApplicationScoped
public class MovieInitializer {
    private final Logger _logger = Logger.getLogger(MovieInitializer.class.getName());

    @Inject
    private MovieService _movieService;

    @PostConstruct
    public void onStartup() {
        if (_movieService.countMovies() == 0) {
            _logger.info("Using csv file data to create initial movie data.");
            try {
                try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/csv/movies.csv")))) ) {
                    String line;
                    final var delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    // Skip the first line as it is containing column headings
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        Optional<Movie> optionalMovie = Movie.parseCsv(line);
                        if (optionalMovie.isPresent()) {
                            Movie csvMovie = optionalMovie.orElseThrow();
                            _movieService.addMovie(csvMovie);
                        }
                    }
                }

            } catch (Exception ex) {
                _logger.log(Level.WARNING,"Exception occurred while processing csv file ",ex);
            }
        }
    }
}
