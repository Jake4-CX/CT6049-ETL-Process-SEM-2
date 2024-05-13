package lat.jack.etl.etl.extract.oracle.models;

public class OracleBookAuthor {

    Integer id;
    String authorFirstName;
    String authorLastName;

    public OracleBookAuthor(Integer id, String authorFirstName, String authorLastName) {
        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }

    public OracleBookAuthor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }
}
