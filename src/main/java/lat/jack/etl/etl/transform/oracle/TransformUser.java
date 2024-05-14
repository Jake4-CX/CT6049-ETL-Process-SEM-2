package lat.jack.etl.etl.transform.oracle;

import lat.jack.etl.etl.extract.oracle.models.OracleUser;
import lat.jack.etl.models.DimUser;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransformUser {

    private static final Logger logger = Logger.getLogger(TransformUser.class.getName());
    private final List<DimUser> userCache = new ArrayList<>();

    public List<DimUser> transformUsers(List<OracleUser> users) {

        try (Connection connection = OracleDBUtil.getStgDataSource()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DIMUSERS");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("USERID");
                String userEmail = rs.getString("USEREMAIL");
                String userRole = rs.getString("USERROLE");

                userCache.add(new DimUser(userId, userEmail, userRole));
            }

        } catch (SQLException e) {
            logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
            return null;
        }

        users.forEach(this::insertUser);

        return userCache;
    }

    public void insertUser(OracleUser user) {
        int userId;
        String userEmail = user.getUserEmail();
        String userRole = user.getUserRole();

        if (userCache.stream().noneMatch(u -> u.getUserEmail().equals(userEmail))) {
            try (Connection connection = OracleDBUtil.getStgDataSource()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO DIMUSERS (USEREMAIL, USERROLE) VALUES (?, ?)", new String[] {"USERID"});
                statement.setString(1, userEmail);
                statement.setString(2, userRole);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    logger.warning("Inserting user failed, no rows affected.");
                    return;
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt(1);
                        userCache.add(new DimUser(userId, userEmail, userRole));
                        // logger.info("Inserted User: " + userEmail);
                    } else {
                        logger.warning("Failed to insert new user: " + userEmail);
                    }
                }


            } catch (SQLException e) {
                logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
            }
        }
    }
}
