package moviesapp.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JSONReaderTest {

    private static JSONReader jsonReader;

    @BeforeAll
    static void setupBeforeAll(){
        jsonReader = new JSONReader(System.getProperty("user.dir")+"/src/test/java/moviesapp/model/data_example.json");
    }

    @Test
    void testFindMovies(){

        List<Movie> movieList1 = jsonReader.findMovies("Monkeyshines" , null) ;
        List<Movie> movieList2= jsonReader.findMovies(null, "1890") ;
        List<Movie> movieList3 = jsonReader.findMovies(null , null) ;
        List<Movie> movieList4 = jsonReader.findMovies("Monkeyshines" , "1890") ;

        for (Movie movie : movieList1){
            assertThat(movie.title().contains("Monkeyshines")).isTrue() ;
        }
        for (Movie movie : movieList2){
            assertThat(movie.releaseDate().contains("1890")).isTrue();
        }
        assertThat(movieList3 == null).isTrue() ;

        for (Movie movie : movieList4){
            assertThat(movie.releaseDate().contains("1890")).isTrue();
            assertThat(movie.title().contains("Monkeyshines")).isTrue() ;
        }

    }

    @Test
    void testFindAllMovies(){
        List<Movie> movieList = jsonReader.findAllMovies();
        assertThat(movieList.get(0).title().equals("Monkeyshines, No. 1")).isTrue();
        assertThat(movieList.get(0).voteCount() == 90).isTrue();
        assertThat(movieList.get(1).title().equals("Monkeyshines, No. 2")).isTrue();
        assertThat(movieList.get(1).voteCount() == 47).isTrue();
        assertThat(movieList.get(9).title().equals("Escrime")).isTrue();
        assertThat(movieList.get(9).voteCount() == 4).isTrue();
        assertThat(movieList.size() == 10).isTrue();
    }
}
