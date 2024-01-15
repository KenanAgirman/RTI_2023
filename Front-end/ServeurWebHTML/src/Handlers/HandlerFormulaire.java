package Handlers;

import BD.DatabaseConnection;
import Model.Article;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandlerFormulaire implements HttpHandler {

    private final DatabaseConnection databaseConnection;


    public HandlerFormulaire(DatabaseConnection con) {
        this.databaseConnection = con;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String data = bufferedReader.readLine();
            System.out.print(data);
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            System.out.print("HandlerFormulaire (methode " + requestMethod + ") = " + requestPath + " --> ");

            try {
                JSONObject json = new JSONObject(data);

                int id = json.getInt("id");
                float price = json.getFloat("price");
                int quantity = json.getInt("quantity");

                String updateQuery = "UPDATE articles SET prix = " + price + ", stock = " + quantity + " WHERE id = " + id + ";";

                System.out.print(updateQuery);

                System.out.print("ID : " + id);
                System.out.print("Prix : " + price);
                System.out.print("Quantité : " + quantity);

                databaseConnection.executeUpdate(updateQuery);

            } catch (JSONException e) {
                System.err.println("Erreur JSON : " + e.getMessage());
                exchange.sendResponseHeaders(400, 0);
                OutputStream os = exchange.getResponseBody();
                os.close();
            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
                exchange.sendResponseHeaders(500, 0);
                OutputStream os = exchange.getResponseBody();
                os.close();
            }
        } else {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String requestPath = exchange.getRequestURI().getPath();
                String requestMethod = exchange.getRequestMethod();
                System.out.print("HandlerFormulaire (methode " + requestMethod + ") = " + requestPath + " --> ");
                try {
                    ResultSet resultSet = databaseConnection.executeQuery("SELECT * FROM articles");

                    ArrayList<Article> articlesList = new ArrayList<>();

                    while (resultSet.next()) {
                        Article article = new Article();
                        article.setId(resultSet.getInt("id"));
                        article.setNom(resultSet.getString("intitule"));
                        System.out.println("NOM INTITULE " + article.getNom());
                        article.setUnitPrice(resultSet.getFloat("prix"));
                        article.setQuantite(resultSet.getInt("stock"));
                        article.setImage(resultSet.getString("image"));
                        articlesList.add(article);
                    }

                   Gson gson = new Gson();
                    JsonArray jsonArray = gson.toJsonTree(articlesList).getAsJsonArray();

                    exchange.sendResponseHeaders(200, jsonArray.toString().length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonArray.toString().getBytes());

                    os.close();
                } catch (SQLException e) {
                    System.err.println("erreur de SQL : " + e.getMessage());
                    exchange.sendResponseHeaders(500, 0);
                    OutputStream os = exchange.getResponseBody();
                    os.close();
                }

            }
        }
    }
}
