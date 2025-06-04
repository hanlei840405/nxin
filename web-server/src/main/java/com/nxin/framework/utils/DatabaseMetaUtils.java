package com.nxin.framework.utils;

import com.nxin.framework.enums.Constant;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.GenericDatabaseMeta;

import java.util.Properties;

public final class DatabaseMetaUtils {

    private DatabaseMetaUtils() {
    }

    public static DatabaseMeta init(String name, String category, String host, String db, String port, String user, String pass, String url, String driver) {
        DatabaseMeta databaseMeta = new DatabaseMeta(name, category.toUpperCase(), null, host, db, port, user, pass);
        if (Constant.GENERIC.equals(category)) {
            Properties properties = new Properties();
            properties.put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, url);
            properties.put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, driver);
            databaseMeta.getDatabaseInterface().setAttributes(properties);
        }
        return databaseMeta;
    }
}
