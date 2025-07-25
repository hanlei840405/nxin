package com.nxin.framework.service;

import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.utils.DatabaseMetaUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseMetaInformation;
import org.pentaho.di.core.database.SqlScriptStatement;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.util.*;

@Slf4j
@Service
public class DynamicQueryDataService {
    @Autowired
    private DatasourceService datasourceService;

    public Map<String, Object> preview(String name, String category, String host, String schemaName, String port, String username, String password, String url, String driver, String sql) throws Exception {
        Map<String, Object> result = new HashMap<>(0);
        List<String> headers = new ArrayList<>(0);
        List<Map<String, String>> records = new ArrayList<>(0);
        DatabaseMeta databaseMeta = DatabaseMetaUtils.init(name, category, host, schemaName, port, username, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(password), url, driver);
//        DatabaseMeta databaseMeta = new DatabaseMeta(name, DatasourceType.getValue(category), "JDBC", host, schemaName, port, username, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(password));
        List<SqlScriptStatement> statements = databaseMeta.getDatabaseInterface().getSqlScriptStatements(sql + Const.CR);
        Database db = new Database(new SimpleLoggingObject("Spoon", LoggingObjectType.SPOON, null), databaseMeta);
        try {
            db.connect();
            for (SqlScriptStatement sss : statements) {
                List<Object[]> rows = db.getRows(sss.getStatement(), 20);
                RowMetaInterface rowMeta = db.getReturnRowMeta();
                for (int i = 0; i < rowMeta.size(); i++) {
                    ValueMetaInterface v = rowMeta.getValueMeta(i);
                    headers.add(v.getName());
                }
                for (Object[] row : rows) {
                    Map<String, String> record = new HashMap<>(0);
                    for (int c = 0; c < rowMeta.size(); c++) {
                        ValueMetaInterface v = rowMeta.getValueMeta(c);
                        record.put(v.getName(), v.getString(row[c]));
                    }
                    records.add(record);
                }
            }
            result.put("headers", headers);
            result.put("records", records);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            db.close();
        }
    }

    public List<Map<String, Object>> structure(String name, String category, String host, String schemaName, String port, String username, String password, String url, String driver, String type, String tableName) throws Exception {
        List<Map<String, Object>> response = new ArrayList<>(0);
        DatabaseMeta databaseMeta = DatabaseMetaUtils.init(name, category, host, schemaName, port, username, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(password), url, driver);
//        DatabaseMeta databaseMeta = new DatabaseMeta(name, DatasourceType.getValue(category), "JDBC", host, schemaName, port, username, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(password));
        Database db = new Database(new SimpleLoggingObject("Spoon", LoggingObjectType.SPOON, null), databaseMeta);
        try {
            db.connect();
            if ("db".equals(type)) {
                String key;
                if (Arrays.asList(db.getCatalogs()).contains(schemaName)) {
                    key = schemaName;
                } else if (Arrays.asList(db.getSchemas()).contains(schemaName)) {
                    key = schemaName;
                } else {
                    key = null;
                }
                DatabaseMetaInformation databaseMetaInformation = new DatabaseMetaInformation(databaseMeta);
                databaseMetaInformation.setDbInfo(db.getDatabaseMeta());
                databaseMetaInformation.getData(new SimpleLoggingObject("Spoon", LoggingObjectType.SPOON, null), null);
                Map<String, Collection<String>> tableMap = databaseMetaInformation.getTableMap();
                if (!CollectionUtils.isEmpty(tableMap)) {
                    List<String> tableKeys = new ArrayList<>(0);
                    if (tableMap.containsKey(key)) {
                        tableKeys.add(key);
                    } else {
                        tableKeys.addAll(tableMap.keySet());
                    }
                    Collections.sort(tableKeys);
                    for (String schema : tableKeys) {
                        if (tableMap.containsKey(schema)) {
                            List<String> tables = new ArrayList<>(tableMap.get(schema));
                            Collections.sort(tables);
                            for (String table : tables) {
                                Map<String, Object> result = new HashMap<>(0);
                                result.put("name", schema.concat(".").concat(table));
                                result.put("type", "TABLE");
                                response.add(result);
                            }
                        }
                    }
                }
                Map<String, Collection<String>> viewMap = databaseMetaInformation.getViewMap();
                if (!CollectionUtils.isEmpty(viewMap)) {
                    List<String> viewKeys = new ArrayList<>(0);
                    if (tableMap.containsKey(key)) {
                        viewKeys.add(key);
                    } else {
                        viewKeys.addAll(tableMap.keySet());
                    }
                    Collections.sort(viewKeys);
                    for (String schema : viewKeys) {
                        if (viewMap.containsKey(schema)) {
                            List<String> views = new ArrayList<>(viewMap.get(schema));
                            Collections.sort(views);
                            for (String view : views) {
                                Map<String, Object> result = new HashMap<>(0);
                                result.put("name", schema.concat(".").concat(view));
                                result.put("type", "VIEW");
                                response.add(result);
                            }
                        }
                    }
                }
            } else {
                db.getFirstRows(tableName, 1);
                RowMetaInterface rowMeta = db.getReturnRowMeta();
                for (int i = 0; i < rowMeta.size(); i++) {
                    Map<String, Object> meta = new HashMap<>(2);
                    ValueMetaInterface v = rowMeta.getValueMeta(i);
                    meta.put("name", v.getName());
                    meta.put("info", v.toStringMeta());
                    response.add(meta);
                }
            }
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            db.close();
        }
    }

    public Map<String, Object> query(String name, String category, String host, String schemaName, String port, String username, String password, String url, String driver, String sql) throws Exception {
        Map<String, Object> result = new HashMap<>(0);
        List<String> headers = new ArrayList<>(0);
        List<Map<String, Object>> records = new ArrayList<>(0);
        DatabaseMeta databaseMeta = DatabaseMetaUtils.init(name, category, host, schemaName, port, username, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(password), url, driver);
        Database db = new Database(new SimpleLoggingObject("Spoon", LoggingObjectType.SPOON, null), databaseMeta);
        try {
            db.connect();
            ResultSet rs = db.openQuery(sql + Const.CR);
            RowMetaInterface rowMeta = db.getReturnRowMeta();
            for (int i = 0; i < rowMeta.size(); i++) {
                ValueMetaInterface v = rowMeta.getValueMeta(i);
                headers.add(v.getName());
            }
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>(0);
                for (String header : headers) {
                    record.put(header, rs.getObject(header));
                }
                records.add(record);
            }
            result.put("headers", headers);
            result.put("records", records);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            db.close();
        }
    }
}
