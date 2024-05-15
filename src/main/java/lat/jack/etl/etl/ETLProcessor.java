package lat.jack.etl.etl;

import lat.jack.etl.etl.extract.oracle.models.OracleData;
import lat.jack.etl.etl.extract.oracle.service.OracleDB;
import lat.jack.etl.etl.load.DataWarehouseLoader;
import lat.jack.etl.etl.transform.DataTransformer;
import lat.jack.etl.models.TransformedCollection;
import lat.jack.etl.utils.OracleDBUtil;
import lat.jack.etl.utils.OracleDatabaseManager;

import java.util.logging.Logger;

public class ETLProcessor {

    private static final Logger logger = Logger.getLogger(ETLProcessor.class.getName());
    private final OracleDB oracleDB = new OracleDB();

    public void process() {

        // Extract

        OracleData oracleData = new OracleData(
                oracleDB.getBookAuthors(),
                oracleDB.getBookCategories(),
                oracleDB.getBooks(),
                oracleDB.getUsers(),
                oracleDB.getLoanedBooks(),
                oracleDB.getLoanFines()
        );

        OracleDatabaseManager oracleDatabaseManager = new OracleDatabaseManager();



        // Transform
        logger.info("Resetting Staging Database");

        try {
            oracleDatabaseManager.resetDatabase(OracleDBUtil.getStgDataSource());
            logger.info("Staging Database Reset");
        } catch (Exception e) {
            logger.warning("Failed to reset staging database: " + e.getMessage());
            return;
        }

        logger.info("Transforming Oracle Data");

        DataTransformer dataTransformer = new DataTransformer();
        TransformedCollection transformedCollection = dataTransformer.transformOracle(oracleData);
        logger.info("Oracle Data Transformed");



        // Load
        logger.info("Resetting Data Warehouse Database");

        try {
            oracleDatabaseManager.resetDatabase(OracleDBUtil.getDwDataSource());
            logger.info("Data Warehouse Database Reset");
        } catch (Exception e) {
            logger.warning("Failed to reset staging database: " + e.getMessage());
            return;
        }

        // Load to Data Warehouse
        logger.info("Loading Data to Data Warehouse");

        DataWarehouseLoader dataWarehouseLoader = new DataWarehouseLoader();

        dataWarehouseLoader.loadAuthors(transformedCollection.getAuthors());
        dataWarehouseLoader.loadCategories(transformedCollection.getCategories());
        dataWarehouseLoader.loadUsers(transformedCollection.getUsers());
        dataWarehouseLoader.loadBooks(transformedCollection.getBooks());
        dataWarehouseLoader.loadLoanedBooks(transformedCollection.getLoanedBooks());

        logger.info("Data Loaded to Data Warehouse");
    }
}
