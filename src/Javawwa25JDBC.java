import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;

class Javawwa25JDBC {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/moviesrental";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static Connection DB_CONNECTION;

    private static final String QUERY = "select title, releaseDate from moviesinfo";
    private static final String QUERY_PARAMETRIZED = "select title, releaseDate " +
            "from moviesinfo where releaseDate " +
            "between ? and ?";
    private static final String INSERT_MOVIE = "insert into moviesinfo (title, genre, releaseDate, description) " +
            "values (?, ?, ?, ?)";

    public static void main(String[] args) {

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", DB_USER);
        connectionProperties.put("password", DB_PASSWORD);

        try {
            DB_CONNECTION = DriverManager.getConnection(DB_URL, connectionProperties);
            //            Statement statement = connection.createStatement();
//            // ResultSet resultSet = statement.executeQuery("select title, releaseDate from moviesinfo");
//            ResultSet resultSet = statement.executeQuery(QUERY);
//
//            System.out.println("Filmy w bazie");
//            System.out.println("Tytuł\t|\tData produkcji");
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("title") + "\t|\t" +
//                        resultSet.getDate(2));
//            }
//            statement.close();
//            LocalDate[] dates = getDateFromUser();
//
//            printMoviesReleasedBetween(dates[0], dates[1]);

            addMovie("Greenbook", "dramat", LocalDate.of(2018,03,15),
                    "jakiś opis");

            DB_CONNECTION.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMovies(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("title") + "\t|\t" +
                        resultSet.getDate(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMoviesReleasedBetween(LocalDate from, LocalDate to) {
        try (PreparedStatement preparedStatement = DB_CONNECTION.prepareStatement(QUERY_PARAMETRIZED)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(from));
            preparedStatement.setDate(2, java.sql.Date.valueOf(to));

            printMovies(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static LocalDate[] getDateFromUser() {
        Scanner scanner = new Scanner(System.in);
        LocalDate[] dates = new LocalDate[2];

        System.out.println("Podaj datę początkową: ");
        dates[0] = LocalDate.parse(scanner.nextLine());

        System.out.println("Podaj datę końcową: ");
        dates[1] = LocalDate.parse(scanner.nextLine());

        return dates;
    }

    private static void addMovie(String title, String genre, LocalDate releaseDate, String description) {
        try (PreparedStatement preparedStatement = DB_CONNECTION.prepareStatement(INSERT_MOVIE)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, genre);
            preparedStatement.setDate(3, java.sql.Date.valueOf(releaseDate));
            preparedStatement.setString(4, description);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}













