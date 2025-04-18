package it.polimi.tiw25.pure_html.DAO;

import it.polimi.tiw25.pure_html.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Checks if there is a user with matching credentials is in the database. If true, then returns a User object with the data retrieved.
     *
     * @param nickname nickname to check
     * @param password password to check
     * @return If the user exists, a User; else null
     * @see User
     * @throws SQLException
     */
    public User checkUser(String nickname, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.CHECK_USER);

        statement.setString(1, nickname);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        if (result.isBeforeFirst()) {
            // the user exists
            result.next();
            return new User(
                    result.getString("nickname"),
                    result.getString("password"),
                    result.getString("name"),
                    result.getString("surname")
            );
        } else {
            // the user does not exist
            return null;
        }
    }
}
