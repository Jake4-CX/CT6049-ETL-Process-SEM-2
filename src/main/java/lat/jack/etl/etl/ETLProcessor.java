package lat.jack.etl.etl;

import lat.jack.etl.etl.extract.oracle.models.OracleData;
import lat.jack.etl.etl.extract.oracle.service.OracleDB;

public class ETLProcessor {

    private final OracleDB oracleDB = new OracleDB();

    public void process() {

        OracleData oracleData = new OracleData(
                oracleDB.getBookAuthors(),
                oracleDB.getBookCategories(),
                oracleDB.getBooks(),
                oracleDB.getUsers(),
                oracleDB.getLoanedBooks(),
                oracleDB.getLoanFines()
        );

        System.out.println("Oracle Data: " + oracleData);
    }
}
