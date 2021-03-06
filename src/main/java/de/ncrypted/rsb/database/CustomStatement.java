/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.database;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

/**
 * @author ncrypted
 */
public class CustomStatement {

    private Connection con;
    private PreparedStatement ps;

    public CustomStatement(Connection con, String statement) {
        try {
            this.con = con;
            ps = con.prepareStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomStatement(Connection con, String statement, boolean autoGeneratedKeys) {
        try {
            this.con = con;
            ps = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomStatement setString(int column, String value) {
        try {
            ps.setString(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setInt(int column, int value) {
        try {
            ps.setInt(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setFloat(int column, float value) {
        try {
            ps.setFloat(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setDouble(int column, double value) {
        try {
            ps.setDouble(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setLong(int column, long value) {
        try {
            ps.setLong(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setArray(int column, Array value) {
        try {
            ps.setArray(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setBoolean(int column, boolean value) {
        try {
            ps.setBoolean(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setDate(int column, Date value) {
        try {
            ps.setDate(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setObject(int column, Object value) {
        try {
            ps.setObject(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setURL(int column, URL value) {
        try {
            ps.setURL(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setTimestamp(int column, Timestamp value) {
        try {
            ps.setTimestamp(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomStatement setTime(int column, Time value) {
        try {
            ps.setTime(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }


    public CustomStatement setBigDecimal(int column, BigDecimal value) {
        try {
            ps.setBigDecimal(column, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomResultSet getGeneratedKeys() {
        try {
            return new CustomResultSet(ps.getGeneratedKeys());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CustomStatement execUpd() {
        try {
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomResultSet execQuery() {
        try {
            return new CustomResultSet(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}