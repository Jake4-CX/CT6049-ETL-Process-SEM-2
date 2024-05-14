package lat.jack.etl.etl.transform;

import lat.jack.etl.etl.extract.oracle.models.OracleData;
import lat.jack.etl.etl.transform.oracle.*;
import lat.jack.etl.models.*;
import lat.jack.etl.utils.utils;

import java.util.List;
import java.util.logging.Logger;

public class DataTransformer {

    private final TransformAuthor transformAuthor = new TransformAuthor();
    private final TransformCategory transformCategory = new TransformCategory();
    private final TransformUser transformUser = new TransformUser();
    private final TransformBook transformBook = new TransformBook();
    private final TransformLoanedBook transformLoanedBook = new TransformLoanedBook();

    private static final Logger logger = Logger.getLogger(DataTransformer.class.getName());

    public void transformOracle(OracleData oracleData) {
        System.out.println("Transforming Oracle Data: " + oracleData);

        logger.info("Transforming Authors");
        List<DimAuthor> authors = transformAuthor.transformAuthors(oracleData.getBookAuthors());

        logger.info("Transforming Categories");
        List<DimCategory> categories = transformCategory.transformCategories(oracleData.getBookCategories());

        logger.info("Transforming Users");
        List<DimUser> users = transformUser.transformUsers(oracleData.getUsers());

        logger.info("Transforming Books");
        List<DimBook> books = transformBook.transformBooks(oracleData.getBooks(), authors, oracleData.getBookAuthors(), categories, oracleData.getBookCategories());

        logger.info("Transforming Loaned Books");
        List<FactLoanedBook> loanedBooks = transformLoanedBook.transformLoanedBooks(oracleData.getLoanedBooks(), oracleData.getLoanFines(), users, oracleData.getUsers(), books, oracleData.getBooks());

        System.out.println("Transformed Oracle Data: " + oracleData);
    }
}
