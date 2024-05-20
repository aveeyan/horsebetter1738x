import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HorseBettingDataFetcher {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HorseBettingDB";
    private static final String USERNAME = "horse_user";
    private static final String PASSWORD = "horse_password";
    private static final int NUM_HORSES_TO_SELECT = 6;

    public List<Horse> fetchHorseData() {
        List<Horse> horses = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            statement = connection.createStatement();
            String sql = "SELECT HorseID, HorseName, HorseNum, Color, OddBets FROM HorseRacer ORDER BY RAND() LIMIT " + (NUM_HORSES_TO_SELECT * 2);
            resultSet = statement.executeQuery(sql);

            Set<String> selectedColors = new HashSet<>();
            int selectedHorses = 0;

            while (resultSet.next() && selectedHorses < NUM_HORSES_TO_SELECT) {
                int horseID = resultSet.getInt("HorseID");
                String horseName = resultSet.getString("HorseName");
                int horseNum = resultSet.getInt("HorseNum");
                String color = resultSet.getString("Color");
                float oddBets = resultSet.getFloat("OddBets");

                if (!selectedColors.contains(color)) {
                    selectedColors.add(color);
                    selectedHorses++;

                    horses.add(new Horse(horseID, horseName, horseNum, color, oddBets));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return horses;
    }
}
