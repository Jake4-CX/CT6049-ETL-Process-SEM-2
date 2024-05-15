package lat.jack.etl.utils;

import lat.jack.etl.models.DimTime;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InsertDimTime {

    public static DimTime insertDimTime(Connection connection, Date date) throws SQLException {
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
                return new DimTime(
                        resultSet.getInt(1),
                        date,
                        dateTime.getMinute(),
                        dateTime.getHour(),
                        dateTime.getDayOfWeek().getValue(),
                        dateTime.getMonthValue(),
                        (dateTime.getMonthValue() - 1) / 3 + 1,
                        dateTime.getYear(),
                        dateTime.getDayOfWeek().getValue() == 6 || dateTime.getDayOfWeek().getValue() == 7
                );
            } else {
                throw new SQLException("Failed to insert DimCategory record.");
            }
        }
    }

    public static DimTime insertDimTime(Connection connection, DimTime dimTime) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO DIMTIME (DATE_TIME, MINUTE, HOUR, DAYOFWEEK, MONTH, QUARTER, YEAR, ISWEEKEND) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new String[] {"DATEID"});

        statement.setTimestamp(1, new java.sql.Timestamp(dimTime.getDate().getTime()));
        statement.setInt(2, dimTime.getMinute());
        statement.setInt(3, dimTime.getHour());
        statement.setInt(4, dimTime.getDayOfWeek());
        statement.setInt(5, dimTime.getMonth());
        statement.setInt(6, dimTime.getQuarter());
        statement.setInt(7, dimTime.getYear());
        statement.setBoolean(8, dimTime.isWeekend());

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Failed to insert DimTime record.");
        }

        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                dimTime.setDateId(resultSet.getInt(1));
                return dimTime;
            } else {
                throw new SQLException("Failed to insert DimCategory record.");
            }
        }
    }
}
