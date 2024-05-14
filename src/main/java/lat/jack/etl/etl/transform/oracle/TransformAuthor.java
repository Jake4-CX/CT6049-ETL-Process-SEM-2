package lat.jack.etl.etl.transform.oracle;

import lat.jack.etl.etl.extract.oracle.models.OracleBookAuthor;
import lat.jack.etl.models.DimAuthor;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransformAuthor {

    private final List<DimAuthor> authorCache = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(TransformAuthor.class.getName());

    public List<DimAuthor> transformAuthors(List<OracleBookAuthor> authors) {

        try (Connection connection = OracleDBUtil.getStgDataSource()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DIMAUTHORS");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int authorId = resultSet.getInt("AUTHORID");
                String authorName = resultSet.getString("AUTHORNAME");

                authorCache.add(new DimAuthor(authorId, authorName));
            }

        } catch (SQLException e) {
            logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
            return null;
        }

        authors.forEach(this::insertAuthor);

        return authorCache;
    }

    public void insertAuthor(OracleBookAuthor author) {
        int authorId;
        String authorName = author.getAuthorFirstName() + " " + author.getAuthorLastName();

        if (authorCache.stream().noneMatch(a -> a.getAuthorName().equals(authorName))) {
            try (Connection connection = OracleDBUtil.getStgDataSource()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO DIMAUTHORS (AUTHORNAME) VALUES (?)", new String[] {"AUTHORID"});
                statement.setString(1, authorName);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    logger.warning("Inserting author failed, no rows affected.");
                    return;
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        authorId = resultSet.getInt(1);
                        authorCache.add(new DimAuthor(authorId, authorName));
                        // logger.info("Inserted Author: " + authorName);
                    } else {
                        logger.warning("Failed to Author Category: " + authorName);
                    }
                }

            } catch (SQLException e) {
                logger.warning("Oracle Database Error: " + e.getMessage());
            }
        }
    }
}
