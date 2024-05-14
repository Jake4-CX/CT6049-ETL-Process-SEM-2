package lat.jack.etl.models;

public class DimUser {

    private int userId;
    private String userEmail;
    private String userRole;

    public DimUser(int userId, String userEmail, String userRole) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }

    public DimUser() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
