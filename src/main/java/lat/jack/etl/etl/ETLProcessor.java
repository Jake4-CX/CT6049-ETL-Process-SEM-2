package lat.jack.etl.etl;

import lat.jack.etl.etl.extract.oracle.models.OracleData;
import lat.jack.etl.etl.extract.oracle.service.OracleDB;
import lat.jack.etl.etl.transform.DataTransformer;

public class ETLProcessor {

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

        // Transform

        // ToDo: Wipe Staging Database

        DataTransformer dataTransformer = new DataTransformer();

        dataTransformer.transformOracle(oracleData);

        // Load

        // Load to Data Warehouse
    }
}
