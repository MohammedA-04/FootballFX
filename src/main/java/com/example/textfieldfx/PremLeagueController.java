package com.example.textfieldfx;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PremLeagueController {


    @FXML
    public ImageView backToFootballHome_ImageView;
    public ChoiceBox<Integer> season_choice;
    public Button getPL_Button;
    public TableView<Standing> PLTable_TableView;

    // Table Columns
    @FXML
    public TableColumn<Standing, String> LogoColumn;
    public TableColumn<Standing, Integer> rankColumn;
    public TableColumn<Standing, String> teamColumn;
    public TableColumn<Standing, Integer> winColumn;
    public TableColumn<Standing, Integer> drawColumn;
    public TableColumn<Standing, Integer> lossColumn;
    public TableColumn<Standing, Integer> ptsColumn;
    public TableColumn<Standing, String> formColumn;
    public TableColumn<Standing, String> noteColumn;
    public TableColumn<Standing, String> statusColumn;



    private static final String APIKEY = APIConfig.getAPIKEY();
    public static Integer usrLeagueSeason;

    static JsonArray standingsArray_0;
    static JsonObject standingTeams;
    static Standing standing;
    static topScorers topScorersObj;

    private static final Gson gson = new Gson(); // GSON Object
    private final ObservableList<Standing> standingsList = FXCollections.observableArrayList();
    public Label PL_Label;
    public Label season_Label;

    public AnchorPane footballHome_AnchorPane;
    public ImageView topScorer_ImageView;

    // Custom @FXML
    private final ImageTableCell imageTableCell = new ImageTableCell();
    public Button topScorer_Button;
    public Label topScorerGA_Label;
    public Label topScorerGoals_Label;
    public Label topScorerAssists_Label;
    public Label topScorerPlayer_Label;

    // ----- Vertical Bar Images
    public ImageView PL_ImageView;
    public ImageView Laliga_ImageView;
    public ImageView Ligue1_ImageView;
    public ImageView Bundesliga_ImageView;
    public ImageView SerieA_ImageView;


    // #1 On Load
    // ERROR: Found when using throws Exception
    public void initialize() {

        for (int i = 2023; i >= 2010; i--) {
            season_choice.getItems().add(i);
        }

        // Lambda Exp which waits for a selection event -> stores choice in Integer vars
        season_choice.setOnAction(event -> {
            usrLeagueSeason = season_choice.getValue();
        });

        getPL_Button.setOnMouseClicked(event -> {

            if (usrLeagueSeason != null){
               Standings_API(usrLeagueSeason); // Call API
            }
        });



        // ----- if button clicked get top scorer photo -----
        season_choice.setOnAction(event -> {
            usrLeagueSeason = season_choice.getValue();
        });

        topScorer_Button.setOnMouseClicked(event -> {

            if (usrLeagueSeason != null){
                try {
                    topScorer_API();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

    }



    /**  Plan

     User
     * Select Season from ChoiceBox
     * Clicks GET Button
        > Calls API using selected season
        > Parse Information
        > Populate Table and View it

     * ? Make Class to represent JSON now GSON elements
     * ? Some-how link class to table/row
     * ? Learn how to populate tables manually
     * **/

/* There is an error somewhere here that blocks the scenebuilder from loading  {standing_api, parse_APICall, printToTable} */


    // ------------  API Call for Standings using {league, year} -----
    public void Standings_API(Integer usrLeagueSeason){

        OkHttpClient standings_client = new OkHttpClient();

        try{
            Request request = new Request.Builder()
                    .url("https://api-football-v1.p.rapidapi.com/v3/standings?season="+ PremLeagueController.usrLeagueSeason +"&league=39")
                    .get()
                    .addHeader("X-RapidAPI-Key", APIKEY)
                    .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                    .build();

            Response response = standings_client.newCall(request).execute();
            String responseBody = response.body().string();  // Correct way to get response body as string
            System.out.println("\nResponse Body:\n" + responseBody);

            String remainingCalls = response.header("x-ratelimit-requests-remaining");
            System.out.println("x-ratelimit-requests-remaining: " + remainingCalls);

            parse_APICall(responseBody);

        }catch (IOException IOE){
            System.out.println("Input Out Exception" + IOE.getCause());
        }


    }

    // Parse Information
    public void parse_APICall(String responseBody){

        // Parse JSON response using Gson library
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();

        // Get the "response" array
        JsonArray responseArray = jsonObject.getAsJsonArray("response");

        // Assuming there's only one element in the "response" array (index 0)
        JsonObject firstResponse = responseArray.get(0).getAsJsonObject();

        // Get the "league" object from the first response
        JsonObject leagueObject = firstResponse.getAsJsonObject("league");

        JsonArray standingsArray = leagueObject.getAsJsonArray("standings");
        standingsArray_0 = standingsArray.get(0).getAsJsonArray();

        printToTable(standingsArray_0);



    }

    private void printToTable(JsonArray standingsArray0) {

        PLTable_TableView.getItems().clear();

        for (int i=0; i < standingsArray_0.size(); i++){

            // standingTeam is Json_object{} representing i^th Json object
            // Standing class represents all key,value pairs
            standingTeams =  standingsArray_0.get(i).getAsJsonObject();
            standing = gson.fromJson(standingTeams, Standing.class);

            // Bind columns to corresponding properties

            // Need way of get an image to display in a table
            // Find how to do with string representation

            // @Undone Comments When UI is designed
            // @Problem: LogoColumn.setCellFactory(col -> new ImageTableCell());
            // @Problem: LogoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeam().getLogo()));

            // ***********   @Warning - Comment out this code bellow in-order to access SceneBuilder ********

            /*

            {
                LogoColumn.setCellFactory(col -> new ImageTableCell());
                LogoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeam().getLogo()));
            }
            */

              LogoColumn.setCellFactory(col -> new ImageTableCell());
              LogoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeam().getLogo()));

            rankColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRank()).asObject());
            teamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeam().getName()));
            winColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAll().getWin()).asObject());
            drawColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAll().getDraw()).asObject());
            lossColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAll().getLose()).asObject());
            ptsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPoints()).asObject());
            teamColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeam().getName()));
            formColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getForm()));
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
            noteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

            // Add the Standing object to the TableView
            PLTable_TableView.getItems().add(standing);

        }
    }


    // ------------ API Call for Top Scorer using {league, year} -----
    public void topScorer_API() throws IOException{

        OkHttpClient topScorers_client = new OkHttpClient();

        Request topScorers_request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/players/topscorers?league=39&season="+PremLeagueController.usrLeagueSeason)
                .get()
                .addHeader("X-RapidAPI-Key", APIKEY)
                .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .build();

        Response topScorers_response = topScorers_client.newCall(topScorers_request).execute();
        String topScorers_responseBody = topScorers_response.body().string();  // Correct way to get response body as string

        String remainingCalls = topScorers_response.header("x-ratelimit-requests-remaining");
        System.out.println("x-ratelimit-requests-remaining: " + remainingCalls);

        parse_topScorers_APICall(topScorers_responseBody);

    }

    private void parse_topScorers_APICall(String responseBody) {


        // Parse JSON response using Gson library
        JsonParser topScorers_jsonParser = new JsonParser();
        JsonObject topScorers_jsonObject = topScorers_jsonParser.parse(responseBody).getAsJsonObject();

        // Get the "response" array
        JsonArray topScorers_responseArray = topScorers_jsonObject.getAsJsonArray("response");

        // 1st instance in topScorers_responseArray
        JsonObject topScorers_firstResponse = topScorers_responseArray.get(0).getAsJsonObject();

        // Class class = gson.fromJson(standingTeams, {class}.class);
        topScorersObj = gson.fromJson(topScorers_firstResponse, topScorers.class);

        String imageUrl = topScorersObj.player.photo;
        Image image = loadImageFromUrl(imageUrl);

        // Set the image to your ImageView
        topScorer_ImageView.setImage(null);
        topScorer_ImageView.setImage(image);

        {
            // Clearing items before its clicked again
            topScorerGoals_Label.setText("");
            topScorerAssists_Label.setText("");
            topScorerGA_Label.setText("");
            topScorerPlayer_Label.setText("");

        }


        Integer g = topScorersObj.statistics.get(0).getGoals().getTotal();
        Integer a = topScorersObj.statistics.get(0).getGoals().getAssists();
        Integer ga = g + a;

        String ts_goals = String.valueOf(g);
        String ts_assists = String.valueOf(a);
        String ts_ga = String.valueOf(ga);
        String ts_player = topScorersObj.player.name;

        // Append the new values to the existing text
        topScorerGoals_Label.setText("Goals: " + ts_goals);
        topScorerAssists_Label.setText("Assists: " + ts_assists);
        topScorerGA_Label.setText("G/A: " + ts_ga);
        topScorerPlayer_Label.setText("Player: " + ts_player);

    }


    // --- Image Setter ---
    private Image loadImageFromUrl(String imageUrl) {
        try (InputStream stream = new URL(imageUrl).openStream()) {
            return new Image(stream);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    // ---------------  Image View Directors ---------------------------
    // @backButton - To Football Home from PL
    public void plToFootballHome(MouseEvent e){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void plToLaliga(MouseEvent e){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/LaLiga_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void plToLigue1(MouseEvent e){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/Ligue1_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void plToBunda(MouseEvent e){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/Bundesliga_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void plToSerieA(MouseEvent e){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/SerieA_Football.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            applySceneTransition(root); // Apply transition || @note may change to after Parent root = fxml.load();
            stage.show();

        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    // Animation Applier()
    private void applySceneTransition(Parent root) {
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), root);
        scaleTransition.setFromX(0.5); // starting scale factor
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);   // ending scale factor
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    // --------------- GSON Representation in Class Form ----------------
    // @Class Contains Standings Array 20 Teams Representation

    public static class Standing {
        int rank;
        standingTeam team;
        int points;
        int goalsDiff;
        String group; // gets League Name
        String form;
        String status; // e.g., same
        String description; //
        standingAll all;
        JsonObject home; // @WIP
        JsonObject away; // @WIP
        String update;

        public void setRank(int rank) {
            this.rank = rank;
        }
        public standingTeam getTeam() {
            return team;
        }

        public standingAll getAll() {
            return all;
        }

        public int getGoalsDiff() {
            return goalsDiff;
        }

        public int getPoints() {
            return points;
        }

        public String getForm() {
            return form;
        }

        public String getDescription(){
            return description;
        }

        public int getRank() {
            return rank;
        }
        public String getGroup() {
            return group;
        }
        public String getStatus() {
            return status;
        }
        public String getUpdate() {
            return update;
        }
    }
    public static class standingTeam{
        int id ;
        String name;
        String logo;

        public String getName() {
            return name;
        }
        public String getLogo() {
            return logo;
        }


    }
    public static class standingAll{
        int played;
        int win;
        int draw;
        int lose;

        public int getPlayed() {
            return played;
        }

        public int getWin() {
            return win;
        }

        public int getDraw() {
            return draw;
        }

        public int getLose() {
            return lose;
        }
    }


    // --------------- GSON Representation in Class Form for topScorers ----------------
    // Statistics is [] JSON
    // |- has "0:" {} JSON
    //    |- goals {} JSON
    //       |- int total
    // if array or object then Class class


    public static class topScorers{
        Player player;
        List<Statistics> statistics;
    }

    public static class Player{
        String name;
        int age;
        String photo;
    }

    public static class Statistics{
        Goals goals;

        public Goals getGoals() {
            return goals;
        }
    }

    public static class Goals{
        int total;
        int assists;

        public int getTotal() {
            return total;
        }

        public int getAssists() {
            return assists;
        }
    }


}

