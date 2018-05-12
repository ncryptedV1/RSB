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
public class CustomResultSet {

    private ResultSet rs;

    public CustomResultSet(ResultSet rs) {
        this.rs = rs;
    }

    public boolean next() {
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getString(String value) {
        try {
            return rs.getString(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(int column) {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getInt(String value) {
        try {
            return rs.getInt(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getInt(int column) {
        try {
            return rs.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Float getFloat(String value) {
        try {
            return rs.getFloat(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Float getFloat(int column) {
        try {
            return rs.getFloat(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getDouble(String value) {
        try {
            return rs.getDouble(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getDouble(int column) {
        try {
            return rs.getDouble(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getLong(String value) {
        try {
            return rs.getLong(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getLong(int column) {
        try {
            return rs.getLong(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Array getArray(String value) {
        try {
            return rs.getArray(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Array getArray(int column) {
        try {
            return rs.getArray(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean getBoolean(String value) {
        try {
            return rs.getBoolean(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean getBoolean(int column) {
        try {
            return rs.getBoolean(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate(String value) {
        try {
            return rs.getDate(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate(int column) {
        try {
            return rs.getDate(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(String value) {
        try {
            return rs.getObject(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(int column) {
        try {
            return rs.getObject(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public URL getURL(String value) {
        try {
            return rs.getURL(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public URL getURL(int column) {
        try {
            return rs.getURL(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getTimestamp(String value) {
        try {
            return rs.getTimestamp(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getTimestamp(int column) {
        try {
            return rs.getTimestamp(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Time getTime(String value) {
        try {
            return rs.getTime(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Time getTime(int column) {
        try {
            return rs.getTime(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal getBigDecimal(String value) {
        try {
            return rs.getBigDecimal(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal getBigDecimal(int column) {
        try {
            return rs.getBigDecimal(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
