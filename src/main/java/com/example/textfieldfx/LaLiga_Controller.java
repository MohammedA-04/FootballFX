package com.example.textfieldfx;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LaLiga_Controller {

    // Labels
    @FXML
    public Label season_Label;
    public Label topScorerAssists_Label;
    public Label topScorerGoals_Label;
    public Label topScorerGA_Label;
    public Label topScorerPlayer_Label;
    public Label topScorers_Label;

    // Buttons
    public Button getLaLiga_Button;
    public Button topScorer_Button;

    // ImageView
    public ImageView topScorer_ImageView;

    // etc components
    public ChoiceBox<Integer> season_choice;
    public TableView<PremLeagueController.Standing> LaLigaTable_TableView;

    // Table
    @FXML
    public TableColumn<PremLeagueController.Standing, String> LogoColumn;
    public TableColumn<PremLeagueController.Standing, Integer> rankColumn;
    public TableColumn<PremLeagueController.Standing, String> teamColumn;
    public TableColumn<PremLeagueController.Standing, Integer> winColumn;
    public TableColumn<PremLeagueController.Standing, Integer> drawColumn;
    public TableColumn<PremLeagueController.Standing, Integer> lossColumn;
    public TableColumn<PremLeagueController.Standing, Integer> ptsColumn;
    public TableColumn<PremLeagueController.Standing, String> formColumn;
    public TableColumn<PremLeagueController.Standing, String> noteColumn;
    public TableColumn<PremLeagueController.Standing, String> statusColumn;


    // Vars
    public static Integer usrLeagueSeason;
    private static final String APIKEY = APIConfig.getAPIKEY();
    private static final Gson gson = new Gson(); //obj

    // static
    static PremLeagueController.Standing standing;
    static JsonArray standingsArray_0;
    static JsonObject standingTeams;
    static PremLeagueController.topScorers topScorersObj;




    // #1 Load LigLiga
    public void initialize() {

        for (int i = 2023; i >= 2010; i--) {
            season_choice.getItems().add(i);
        }

        // Lambda Exp which waits for a selection event -> stores choice in Integer vars
        season_choice.setOnAction(event -> {
            usrLeagueSeason = season_choice.getValue();
        });

        getLaLiga_Button.setOnMouseClicked(event -> {

            if (usrLeagueSeason != null) {
                Standings_API(usrLeagueSeason); // Call API
            }
        });

    }

    // La Liga is #140
    public void Standings_API (Integer usrLeagueSeason){

        OkHttpClient standings_client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url("https://api-football-v1.p.rapidapi.com/v3/standings?season=" + usrLeagueSeason + "&league=140")
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

        } catch (IOException IOE) {
            System.out.println("Input Out Exception" + IOE.getCause());
        }


    }

    // Parse Information
    public void parse_APICall (String responseBody){

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

    private void printToTable (JsonArray standingsArray0){

        LaLigaTable_TableView.getItems().clear();

        for (int i = 0; i < standingsArray_0.size(); i++) {

            // standingTeam is Json_object{} representing i^th Json object
            // Standing class represents all key,value pairs
            standingTeams = standingsArray_0.get(i).getAsJsonObject();
            standing = gson.fromJson(standingTeams, PremLeagueController.Standing.class);

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

            LaLigaTable_TableView.getItems().add(standing);

        }
    }

    // ------------ API Call for Top Scorer using {league, year} <La Liga>-----
    public void topScorer_API() throws IOException{

        OkHttpClient topScorers_client = new OkHttpClient();

        Request topScorers_request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/players/topscorers?league=140&season="+ usrLeagueSeason)
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
        topScorersObj = gson.fromJson(topScorers_firstResponse, PremLeagueController.topScorers.class);

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

    // @backButton - To Premier League from La Liga
    public void laligaToPL(MouseEvent e){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/textfieldfx/PremierLeague_Football.fxml"));
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

    // @forwardButton - null
    public void laligaToLigue1(MouseEvent e){

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
}


