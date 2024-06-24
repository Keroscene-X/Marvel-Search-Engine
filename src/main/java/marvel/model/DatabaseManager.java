package marvel.model;

import javax.imageio.ImageIO;
import java.io.File;
import java.sql.*;

public class DatabaseManager {
    private static final String dbName = "marvel.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            // If we get here that means no exception raised from getConnection - meaning it worked
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void setupDB() {
        String createMarvelTableSQL =
                """
                CREATE TABLE IF NOT EXISTS marvel (
                    request text PRIMARY KEY,
                    json_object text NOT NULL,
                    search_frequency integer
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createMarvelTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void clearDB(){
        String clearMarvelTableSQL =
                """
                DELETE FROM marvel;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(clearMarvelTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void saveData(String request, String cache){
        String addSingleRecordWithParametersSQL =
                """
                INSERT INTO marvel VALUES
                    (?,?,1);
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addSingleRecordWithParametersSQL)) {
            preparedStatement.setString(1, request);
            preparedStatement.setString(2, cache);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateData(String request, String cache, Integer search_frequency){
        String addSingleRecordWithParametersSQL =
                """
                UPDATE marvel
                SET json_object = ?, search_frequency = ?
                WHERE request = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addSingleRecordWithParametersSQL)) {
            preparedStatement.setString(1, cache);
            preparedStatement.setInt(2, search_frequency);
            preparedStatement.setString(3, request);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public boolean cacheHit(String request){
        boolean cacheHit = false;
        String cacheCheckSQL =
                """
                SELECT (count(*) > 0) AS found
                FROM marvel
                WHERE request = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(cacheCheckSQL)) {
            preparedStatement.setString(1, request);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                cacheHit = results.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        return cacheHit;
    }

    public String achieveData(String request){
        String cache = "";
        String marvelCacheSQL =
                """
                SELECT json_object
                FROM marvel
                WHERE request = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(marvelCacheSQL)) {
            preparedStatement.setString(1, request);
            ResultSet results = preparedStatement.executeQuery();

            if(results.next()){
                cache = results.getString("json_object");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return cache;
    }

    public Integer achieveFrequency(String request){
        Integer cache = 0;
        String marvelCacheSQL =
                """
                SELECT search_frequency
                FROM marvel
                WHERE request = ?;
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(marvelCacheSQL)) {
            preparedStatement.setString(1, request);
            ResultSet results = preparedStatement.executeQuery();

            if(results.next()){
                cache = Integer.parseInt(results.getString("search_frequency"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return cache;
    }




}
