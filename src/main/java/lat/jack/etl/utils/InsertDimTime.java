package lat.jack.etl.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InsertDimTime {

    public static int insertDimTime(Connection connection, Date date) throws SQLException {
        java.util.Date utilDate = new java.util.Date(date.getTime());
        LocalDateTime dateTime = LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());


        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO DIMTIME (DATE_TIME, MINUTE, HOUR, DAYOFWEEK, MONTH, QUARTER, YEAR, ISWEEKEND) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new String[] {"DATEID"});

        statement.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
        statement.setInt(2, dateTime.getMinute());
        statement.setInt(3, dateTime.getHour());
        statement.setInt(4, dateTime.getDayOfWeek().getValue());
        statement.setInt(5, dateTime.getMonthValue());
        statement.setInt(6, (dateTime.getMonthValue() - 1) / 3 + 1);
        statement.setInt(7, dateTime.getYear());
        statement.setBoolean(8, dateTime.getDayOfWeek().getValue() == 6 || dateTime.getDayOfWeek().getValue() == 7);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Failed to insert DimTime record.");
        }

        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Failed to insert DimCategory record.");
            }
        }
    }
}
