package moviesapp.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MoviesTest {
    private final Movie movie1 = new Movie(true,null,null,"1",null,
            null, null,0,null,"2023",null,true,
            0, 0);
    private final Movie movie2 = new Movie(true,null,null,"2",null,
            null, null,0,null,"2023",null,true,
            0, 0);
    private final Movie movie3 = new Movie(true,null,null,"3",null,
            null, null,0,null,"2023",null,true,
            0, 0);
    private final Movie movie4 = new Movie(true,null,null,"4",null,
            null, null,0,null,"2023",null,true,
            0, 0);
    Movies movies = new Movies();
    List<Movie> moviesToAddToMovies = new ArrayList<>();
    @Test
    void testToString(){
        moviesToAddToMovies.add(movie2);
        moviesToAddToMovies.add(movie1);
        moviesToAddToMovies.add(movie3);
        Movies moviesFull = new Movies(moviesToAddToMovies);
        assertThat(moviesFull.toString().equals(
                movie2 + "\n" + movie1 + "\n" + movie3 + "\n")).isTrue();
        assertThat(movies.toString().equals("Your list of movies is empty.")).isTrue();
    }
    @Test
    void testToStringWithID(){
        moviesToAddToMovies.add(movie2);
        moviesToAddToMovies.add(movie1);
        moviesToAddToMovies.add(movie3);
        Movies moviesFull = new Movies(moviesToAddToMovies);
        System.out.println(moviesFull.toStringWithID());
        assertThat(moviesFull.toStringWithID().equals(
                movie2.toStringWithID() + "\n" + movie1.toStringWithID() + "\n" +
                        movie3.toStringWithID() + "\n")).isTrue();
        assertThat(movies.toStringWithID().equals("Your list of movies is empty.")).isTrue();
    }
    @Test
    void testAdd(){
        moviesToAddToMovies.add(movie2);
        moviesToAddToMovies.add(movie1);
        moviesToAddToMovies.add(movie3);
        Movies moviesFull = new Movies(moviesToAddToMovies);
        moviesFull.add(movie4);
        Boolean truth = moviesFull.toString().equals(movie2 + "\n" + movie1 + "\n" + movie3 + "\n" + movie4 +"\n");
        assertThat(truth).isTrue();
        moviesFull.add(null);
        assertThat(truth).isTrue();
    }
    @Test
    void testIsEmpty(){
        assertThat(movies.isEmpty()).isTrue();
        moviesToAddToMovies.add(movie3);
        Movies moviesFull = new Movies(moviesToAddToMovies);
        assertThat(moviesFull.isEmpty()).isFalse();
    }
}
