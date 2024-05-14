package lat.jack.etl.etl.transform.oracle;

import lat.jack.etl.etl.extract.oracle.models.OracleBookCategory;
import lat.jack.etl.models.DimCategory;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransformCategory {

    private final List<DimCategory> categoryCache = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(TransformCategory.class.getName());

    public List<DimCategory> transformCategories(List<OracleBookCategory> categories) {

        try (Connection connection = OracleDBUtil.getStgDataSource()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DIMCATEGORIES");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("CATEGORYID");
                String categoryName = resultSet.getString("CATEGORYNAME");

                categoryCache.add(new DimCategory(categoryId, categoryName));
            }

        } catch (SQLException e) {
            logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
            return null;
        }

        categories.forEach(this::insertCategory);

        return categoryCache;
    }

    public void insertCategory(OracleBookCategory category) {
        String categoryName = category.getCategoryName();

        if (categoryCache.stream().noneMatch(c -> c.getCategoryName().equals(categoryName))) {
            try (Connection connection = OracleDBUtil.getStgDataSource()) {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO DIMCATEGORIES (CATEGORYNAME) VALUES (?)", new String[] {"CATEGORYID"});
                statement.setString(1, categoryName);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    logger.warning("Inserting category failed, no rows affected.");
                    return;
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int categoryId = resultSet.getInt(1);
                        categoryCache.add(new DimCategory(categoryId, categoryName));
                        // logger.info("Inserted Category: " + categoryName);
                    } else {
                        logger.warning("Failed to insert Category: " + categoryName);
                    }
                }
            } catch (SQLException e) {
                logger.warning("Oracle Database Error: " + e.getMessage());
            }
        } else {
            logger.info("Category already exists in cache: " + categoryName);
        }
    }
}
