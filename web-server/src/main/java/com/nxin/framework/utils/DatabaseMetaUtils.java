package com.nxin.framework.utils;

import com.nxin.framework.enums.Constant;
import com.nxin.framework.enums.DatasourceType;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.GenericDatabaseMeta;

import java.util.Properties;

public final class DatabaseMetaUtils {

    private DatabaseMetaUtils() {}

    public static DatabaseMeta init(String name, String category, String host, String db, String port, String user, String pass, String url, String driver) {
        String dbType = DatasourceType.getValue(category);
        DatabaseMeta databaseMeta = new DatabaseMeta(name, dbType, null, host, db, port, user, pass);
        if (Constant.GENERIC.equals(dbType)) {
            Properties properties = new Properties();
            properties.put( GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, url);
            properties.put( GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, driver);
            databaseMeta.getDatabaseInterface().setAttributes(properties);
        }
        return databaseMeta;
    }
}
