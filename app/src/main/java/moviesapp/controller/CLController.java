package moviesapp.controller;

import moviesapp.model.*;

import java.util.*;

public final class CLController {
    private final List<String> commands;
    private final Scanner scanner;
    private JsonReader jsonReader;
    public final static String apiFilePath = System.getProperty("user.dir")+"/src/main/java/moviesapp/model/json/api-results.json";
    public final static String favoritesFilePath = System.getProperty("user.dir")+"/src/main/java/moviesapp/model/json/favorites.json";
    private final TmdbAPI apiObject;

    public CLController() {
        commands = new ArrayList<>();
        setupCommands();
        scanner = new Scanner(System.in);
        jsonReaderUpdate();
        apiObject = new TmdbAPI();
    }

    /**
     * Update the file read by the jsonReader
     */
    private void jsonReaderUpdate(){
        jsonReader = new JsonReader(apiFilePath);
    }

    /**
     * Add elements to the command list
     */
    private void setupCommands(){
        commands.add("[1] catalog: see popular movies at the moment");
        commands.add("[2] search: show specific movies based on your criteria");
        commands.add("[3] details: see detailed information about one movie of your precedent research");
        commands.add("[4] add: add one movie to your favorite list");
        commands.add("[5] remove: remove one movie to your favorite list");
        commands.add("[6] favorites: see movies in your favorite list");
        commands.add("[7] clear: remove all the movies from your favorite list");
        commands.add("[8] exit: leave the application");
    }

    /**
     * Print the list of commands available
     */
    private void help(){
        System.out.println("\nCommands available: ");
        for (String command : commands){
            System.out.println("•" + command);
        }
    }

    /**
     * Display only the title, the year of release and the average note of every film in the catalog (popular films generated by API)
     */
    private void displayCatalog(){
        apiObject.displayCatalog(1);
        do{
            jsonReaderUpdate();
            System.out.println("The most popular movies at the moment are listed below: \n" + jsonReader.findAllMovies());
        } while(askPreviousOrNextPage(messageOfAskPreviousOrNextPage()));
    }

    /**
     * Ask the user to select an interaction with page management system (previous, next, stop)
     * @param message the message to print to the user to interact with page management system
     * @return {@code true} if the user select something else than stopping the page management system
     */
    private boolean askPreviousOrNextPage(String message){
        String response = askValue(message);

        switch (response) {
            case "3" -> {
                int pageNumber = Integer.parseInt(askValue("Enter page number: "));
                if (pageNumber >= 1 && pageNumber <= jsonReader.numberOfPagesOfMoviesInJson()){
                    apiObject.displayCatalog(pageNumber);
                    System.out.println();
                    return true;
                }
                System.out.println("Page number unavailable.");
            }
            case "2" -> {
                if(jsonReader.getPageInJson() < jsonReader.numberOfPagesOfMoviesInJson()){
                    apiObject.displayCatalog(jsonReader.getPageInJson() + 1);
                    System.out.println();
                    return true;
                }
                System.out.println("\nThere is no next page.");
            }
            case "1" -> {
                if(jsonReader.getPageInJson() > 1){
                    apiObject.displayCatalog(jsonReader.getPageInJson() - 1);
                    System.out.println();
                    return true;
                }
                System.out.println("\nThere is no precedent page.");
            }
            case "0" -> {
                return false;
            }
            default -> System.out.println("Please enter a valid option.");
        }

        return askPreviousOrNextPage(message);
    }

    /**
     * Ask the user to select an interaction with page management system (previous, next, stop)
     * @param title from the precedent search
     * @param releaseYear from the precedent search
     * @param genres from the precedent search
     * @param voteAverage from the precedent search
     * @param message the message to print to the user to interact with page management system
     * @return the user's answer
     */
    private boolean askPreviousOrNextPage(String title, String releaseYear, List<String> genres, String voteAverage , String message){
        String response = askValue(message);

        switch (response) {
            case "3" -> {
                int pageNumber = Integer.parseInt(askValue("Enter page number: "));
                if (pageNumber >= 1 && pageNumber <= jsonReader.numberOfPagesOfMoviesInJson()){
                    apiObject.searchMovies(title, releaseYear, genres, voteAverage, String.valueOf(pageNumber));
                    return true;
                }
                System.out.println("\nPage number unavailable.");
            }
            case "2" -> {
                if(jsonReader.getPageInJson() < jsonReader.numberOfPagesOfMoviesInJson()){
                    apiObject.searchMovies(title, releaseYear, genres, voteAverage, String.valueOf(jsonReader.getPageInJson() + 1));
                    return true;
                }
                System.out.println("\nThere is no next page.");
            }
            case "1" -> {
                if(jsonReader.getPageInJson() > 1){
                    apiObject.searchMovies(title, releaseYear, genres, voteAverage, String.valueOf(jsonReader.getPageInJson() - 1));
                    return true;
                }
                System.out.println("\nThere is no precedent page.");
            }
            case "0" -> {
                return false;
            }
            default -> System.out.println("Please enter a valid option.");
        }

        return askPreviousOrNextPage(title, releaseYear, genres, voteAverage , message);
    }

    /**
     * Search a specific group of movies and print their detailed information
     */
    private void details(){
        jsonReaderUpdate();
        Movies movieList= jsonReader.findAllMovies();
        System.out.println("Below the movies from your precedent search: \n" + movieList);
        if(movieList != null) {
            int index = Integer.parseInt(askValue("Enter the index of the movie: ")) - 1;
            System.out.println(movieList.get(index).details());
        }
        else {
            System.out.println("There was no movie.");
        }
    }

    /**
     * Ask title, release year, vote average and genres information to the user to select a specific group of movies
     * @return the group of movies found
     */
    private Movies searchMoviesToReturn(){
        searchMovies();
        return jsonReader.findAllMovies();
    }

    /**
     * Ask title, release year, vote average and genres information to the user to select a specific group of movies
     */
    private void searchMovies(){
        String title = askValue("Title of the movie: ");
        String releaseYear = askValue("Year of release: ");
        String voteAverage = askValue("Movie's minimum rate: ");
        List<String> genres = specifiedGenres(apiObject);

        if(title.isEmpty() && releaseYear.isEmpty() && voteAverage.isEmpty() && genres.isEmpty()){
            System.out.println("No information sent. \nPlease give me more details for your next search.");
        }
        else{
            apiObject.searchMovies(title, releaseYear, genres, voteAverage , "1");
            do{
                jsonReaderUpdate();
                System.out.println("\nYour list of movies found in your search: \n" + jsonReader.findAllMovies());
            } while(askPreviousOrNextPage(title,releaseYear,genres,voteAverage, messageOfAskPreviousOrNextPage()));
        }
    }

    /**
     * Return the message corresponding to the page management user interactive
     * @return the message corresponding to the page management user interactive
     */
    private String messageOfAskPreviousOrNextPage(){
        return "Choose your action: [0] Continue/Leave command, [1] Previous Page, [2] Next Page, [3] Specify Page | page ("
                + jsonReader.getPageInJson()
                + "/"
                + jsonReader.numberOfPagesOfMoviesInJson() +")";
    }

    /**
     * Return the user's specified genres
     * @param apiObject the api that contains all the genres available
     * @return the user's specified genres
     */
    private List<String> specifiedGenres(TmdbAPI apiObject){
        List<String> genres = new ArrayList<>();

        if(askToConfirm("Do you want to specify one or more genres?")){
            System.out.println("\nList of genres: \n" + apiObject.genreList());

            do{
                String genreName = askValue("Enter genre name: ").trim().toLowerCase();
                genreName = genreName.substring(0,1).toUpperCase() + genreName.substring(1);
                if (TmdbAPI.GENRE_NAME_ID_MAP.containsKey(genreName)) {
                    genres.add(genreName);
                }
                else {
                    System.out.println("Genre not found. Please enter a valid genre.");
                }
            } while(askToConfirm("Do you want to add more genres?"));
        }
        return genres;
    }

    /**
     * Print a message to the user and return its input.
     * @param message to print to the user
     * @return its input
     */
    private String askValue(String message){
        System.out.println(message);
        return scanner.nextLine();
    }

    /**
     * Command that print all movies stored in user favorite list
     */
    private void displayFavorites(){
        System.out.println(Favorites.instance);
    }

    /**
     * Test if askToConfirm is true ,and if it is, clear the favourite list
     */
    private void clear() {
        if (askToConfirm("Are you sure that you want to delete your favourites?")){
            Favorites.instance.clear();
            new JsonWriter(favoritesFilePath).clean();
            System.out.println("Your favorite list has been cleared.");
        }
    }

    /**
     * Exit command that asks user if he is sure that he wants to leave the application, exit it if yes
     */
    private void exit(){
        if(askToConfirm("Are you sure that you want to leave the application?")){
            System.exit(0);
        }
    }

    /**
     * Add a specific movie chosen by the user with a number to the favorite list
     * @param movies chosen to browse
     */
    private void addMovieByIndex(Movies movies){
        Favorites.instance.add(selectMovieByIndex(movies, "Index of the movie to add to your favorites: "));
    }

    /**
     * Remove a specific movie chosen by the user with the index of the movie to the favorite list
     * @param movies chosen to browse
     */
    private void removeMovieByIndex(Movies movies){
        Favorites.instance.remove(selectMovieByIndex(movies, "Index of the movie to remove from your favorites: "));
    }

    /**
     * Ask the user the index of the movie that he wants to select in a Movies object
     * @param movies to browse
     * @return movie selected in a Movies object
     */
    private Movies selectMovieByIndex(Movies movies, String message){
        int index = Integer.parseInt(askValue(message));
        Movies movieSelected = new Movies();
        movieSelected.add(movies.findMovieByIndex(index - 1));
        return movieSelected ;
    }

    /**
     * add to the favorites one or several movies
     */
    private void add(){
        do{
            Movies movies = searchMoviesToReturn();
            if (!Movies.noMovieFound(movies)) {
                if (movies.size() > 1){
                    addMovieByIndex(movies);
                }
                else{
                    Favorites.instance.add(movies);
                }
                new JsonWriter(favoritesFilePath).saveFavorites(Favorites.instance.getFavorites());
            }
        }
        while(askToConfirm("Do you want to add another movie?"));
        printFavoritesUpdate() ;
    }

    /**
     * Remove to the favorites the movie chosen by the user
     */
    private void remove(){
        do{
            if(Favorites.instance.isEmpty()){
                break;
            }
            System.out.println("Your actual favorites list: ");
            displayFavorites() ;
            Movies movies = searchFavoritesToRemove() ;
            if (!Movies.noMovieFound(movies)) {
                if (movies.size() > 1){
                    removeMovieByIndex(movies);
                }
                else{
                    Favorites.instance.remove(movies);
                }
            }
        }
        while(askToConfirm("Do you want to remove another movie?"));
        printFavoritesUpdate() ;
    }

    /**
     * Ask title and release year information to the user to select a specific group of favorite movies
     * @return the group of movies found
     */
    private Movies searchFavoritesToRemove(){
        String title = askValue("Title of the movie to remove: ");
        String releaseYear = askValue("Year of release: ");
        return Favorites.instance.findMovies(title, releaseYear, null, null);
    }

    /**
     * Print the favorites list modified
     */
    private void printFavoritesUpdate(){
        System.out.println("\nYour favorites list updated: ");
        displayFavorites();
    }

    /**
     * Print a terminal message with choice (yes or no) and return true if yes, false if no
     * @param string the message to print
     * @return true if yes, false if no
     */
    private boolean askToConfirm(String string){
        String answer;
        do{
            answer = askValue(string + " [Y/n]: ").trim().toLowerCase();
        }while(!answer.equals("y") && !answer.equals("n"));

        return answer.equals("y");
    }

    /**
     * Select a method to execute based on user input and execute it
     */
    public void select(){
        new JsonWriter(apiFilePath).clean();

        for (;;) {
            help();
            System.out.println("\nInput your command: ");
            String command = scanner.nextLine().toLowerCase(Locale.ROOT).trim();
            System.out.println();

            switch(command){
                case "7":
                    clear();
                    break;

                case "8":
                    exit();
                    break;

                case "1":
                    displayCatalog();
                    break;

                case "3":
                    details();
                    break;

                case "2":
                    searchMovies();
                    break;

                case "6":
                    displayFavorites();
                    break;

                case "4":
                    add();
                    break;

                case "5":
                    remove();
                    break;

                default :
                    System.out.println("*** Command '" + command +  "' doesn't exist. ***");
                    break;
            }
        }
    }
}