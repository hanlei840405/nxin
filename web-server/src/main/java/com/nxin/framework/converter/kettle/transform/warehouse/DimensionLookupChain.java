package com.nxin.framework.converter.kettle.transform.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.enums.DatasourceType;
import com.nxin.framework.utils.DatabaseMetaUtils;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.dimensionlookup.DimensionLookupMeta;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DimensionLookupChain extends TransformConvertChain {
    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "DimensionLookupMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            DimensionLookupMeta dimensionLookupMeta = new DimensionLookupMeta();
            List<String> searchDbFields = new ArrayList<>(0);
            List<String> searchStreamFields = new ArrayList<>(0);
            List<String> dbFields = new ArrayList<>(0);
            List<String> streamFields = new ArrayList<>(0);
            String stepName = (String) formAttributes.get("name");
            boolean update = (boolean) formAttributes.get("update");
            String schemaName = (String) formAttributes.get("schemaName");
            String tableName = (String) formAttributes.get("tableName");
            int databaseId = (int) formAttributes.get("datasource");
            int commit;
            if (ObjectUtils.isEmpty(formAttributes.get("commit"))) {
                commit = 0;
            } else {
                commit = (int) formAttributes.get("commit");
            }
            boolean preloadingCache = (boolean) formAttributes.get("preloadingCache");
            int cacheSize;
            if (ObjectUtils.isEmpty(formAttributes.get("cacheSize"))) {
                cacheSize = -1;
            } else {
                cacheSize = (int) formAttributes.get("cacheSize");
            }
            String key = (String) formAttributes.get("key");
            String keyRename = (String) formAttributes.get("keyRename");
            String keyGenerateType = (String) formAttributes.get("keyGenerateType");
            String sequence = (String) formAttributes.get("key");
            String versionField = (String) formAttributes.get("replace");
            String dateField = (String) formAttributes.get("dateField");
            String dateFrom = (String) formAttributes.get("dateFrom");
            String dateTo = (String) formAttributes.get("dateTo");
            int minYear = (int) formAttributes.get("minYear");
            boolean useAltStartDate = (boolean) formAttributes.get("useAltStartDate");
            int altStartDate = (int) formAttributes.get("altStartDate");
            String altStartDateField = (String) formAttributes.get("altStartDateField");
            int maxYear = (int) formAttributes.get("maxYear");
            dimensionLookupMeta.setUpdate(update);
            dimensionLookupMeta.setPreloadingCache(preloadingCache);
            dimensionLookupMeta.setCacheSize(cacheSize);
            dimensionLookupMeta.setKeyField(key);
            dimensionLookupMeta.setKeyRename(keyRename);
            dimensionLookupMeta.setTechKeyCreation(keyGenerateType);
            if (DimensionLookupMeta.CREATION_METHOD_AUTOINC.equals(keyGenerateType)) {
                dimensionLookupMeta.setAutoIncrement(true);
                dimensionLookupMeta.setSequenceName(null);
            } else if (DimensionLookupMeta.CREATION_METHOD_SEQUENCE.equals(keyGenerateType)) {
                dimensionLookupMeta.setAutoIncrement(false);
                dimensionLookupMeta.setSequenceName(sequence);
            } else if (DimensionLookupMeta.CREATION_METHOD_TABLEMAX.equals(keyGenerateType)) {
                dimensionLookupMeta.setAutoIncrement(false);
                dimensionLookupMeta.setSequenceName(null);
            }
            dimensionLookupMeta.setVersionField(versionField);
            dimensionLookupMeta.setDateField(dateField);
            dimensionLookupMeta.setDateFrom(dateFrom);
            dimensionLookupMeta.setMinYear(minYear);
            dimensionLookupMeta.setUsingStartDateAlternative(useAltStartDate);
            dimensionLookupMeta.setStartDateAlternative(altStartDate);
            dimensionLookupMeta.setStartDateFieldName(altStartDateField);
            dimensionLookupMeta.setDateTo(dateTo);
            dimensionLookupMeta.setMaxYear(maxYear);

            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);
            Datasource datasource = datasourceService.one((long) databaseId);
            DatabaseMeta databaseMeta = DatabaseMetaUtils.init(datasource.getName(), datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(datasource.getPassword()), datasource.getUrl(), datasource.getDriver());
            databaseMeta.setStreamingResults(datasource.getUseCursor());
            String parameters = datasource.getParameter();
            if (StringUtils.hasLength(parameters)) {
                List<Map<String, Object>> params = objectMapper.readValue(parameters, new TypeReference<List<Map<String, Object>>>() {
                });
                Properties properties = new Properties();
                for (Map<String, Object> param : params) {
                    for (Map.Entry<String, Object> entry : param.entrySet()) {
                        if (StringUtils.hasLength(entry.getKey()) && !ObjectUtils.isEmpty(entry.getValue())) {
                            databaseMeta.addExtraOption(DatasourceType.getValue(datasource.getCategory()), entry.getKey(), (String) entry.getValue());
                            properties.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                databaseMeta.setConnectionPoolingProperties(properties);
            }
            if (datasource.getUsePool()) {
                databaseMeta.setUsingConnectionPool(true);
                if (datasource.getPoolInitialSize() != null) {
                    databaseMeta.setInitialPoolSize(datasource.getPoolInitialSize());
                }
                if (datasource.getPoolMaxSize() != null) {
                    databaseMeta.setMaximumPoolSize(datasource.getPoolMaxSize());
                }
                if (datasource.getPoolInitial() != null) {
                    databaseMeta.getConnectionPoolingProperties().put("initialSize", datasource.getPoolInitial());
                }
                if (datasource.getPoolMaxActive() != null) {
                    databaseMeta.getConnectionPoolingProperties().put("maxActive", datasource.getPoolMaxActive());
                }
                if (datasource.getPoolMaxIdle() != null) {
                    databaseMeta.getConnectionPoolingProperties().put("maxIdle", datasource.getPoolMaxIdle());
                }
                if (datasource.getPoolMinIdle() != null) {
                    databaseMeta.getConnectionPoolingProperties().put("minIdle", datasource.getPoolMinIdle());
                }
                if (datasource.getPoolMaxWait() != null) {
                    databaseMeta.getConnectionPoolingProperties().put("maxWait", datasource.getPoolMaxWait());
                }
            }
            dimensionLookupMeta.setTableName(tableName);
            dimensionLookupMeta.setSchemaName(schemaName);
            dimensionLookupMeta.setCommitSize(commit);
            dimensionLookupMeta.setDatabaseMeta(databaseMeta);
            List<Map<String, String>> searchMappingData = (List<Map<String, String>>) formAttributes.get("searchMappingData");
            for (Map<String, String> fieldMapping : searchMappingData) {
                searchDbFields.add(fieldMapping.get("target"));
                searchStreamFields.add(fieldMapping.get("source"));
            }
            dimensionLookupMeta.setKeyLookup(searchDbFields.toArray(new String[0]));
            dimensionLookupMeta.setKeyStream(searchStreamFields.toArray(new String[0]));
            List<Map<String, String>> fieldMappingData = (List<Map<String, String>>) formAttributes.get("fieldMappingData");
            for (Map<String, String> fieldMapping : fieldMappingData) {
                dbFields.add(fieldMapping.get("target"));
                streamFields.add(fieldMapping.get("source"));
            }
            dimensionLookupMeta.setFieldLookup(dbFields.toArray(new String[0]));
            dimensionLookupMeta.setFieldStream(streamFields.toArray(new String[0]));
            StepMeta stepMeta = new StepMeta(stepName, dimensionLookupMeta);
            mxGeometry geometry = cell.getGeometry();
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            stepMeta.setCopies(parallel);
            return new ResponseMeta(cell.getId(), stepMeta, databaseMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}
