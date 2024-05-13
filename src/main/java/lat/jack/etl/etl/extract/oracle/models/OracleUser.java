package lat.jack.etl.etl.extract.oracle.models;

import java.sql.Date;

public class OracleUser {

    Integer id;
    String userEmail;
    String userPassword;
    Date userCreatedDate;
    Date userUpdatedDate;
    String userRole;

    public OracleUser(Integer id, String userEmail, String userPassword, Date userCreatedDate, Date userUpdatedDate, String userRole) {
        this.id = id;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userCreatedDate = userCreatedDate;
        this.userUpdatedDate = userUpdatedDate;
        this.userRole = userRole;
    }

    public OracleUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Date getUserCreatedDate() {
        return userCreatedDate;
    }

    public void setUserCreatedDate(Date userCreatedDate) {
        this.userCreatedDate = userCreatedDate;
    }

    public Date getUserUpdatedDate() {
        return userUpdatedDate;
    }

    public void setUserUpdatedDate(Date userUpdatedDate) {
        this.userUpdatedDate = userUpdatedDate;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
