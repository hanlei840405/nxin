package com.nxin.framework.converter.kettle.transform.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.utils.DatabaseMetaUtils;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.combinationlookup.CombinationLookupMeta;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CombinationLookupChain extends TransformConvertChain {
    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "CombinationLookupMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            CombinationLookupMeta combinationLookupMeta = new CombinationLookupMeta();
            List<String> dbFields = new ArrayList<>(0);
            List<String> streamFields = new ArrayList<>(0);
            String stepName = (String) formAttributes.get("name");
            String schemaName = (String) formAttributes.get("schemaName");
            String tableName = (String) formAttributes.get("tableName");
            int databaseId = (int) formAttributes.get("datasource");
            int commit;
            if (ObjectUtils.isEmpty(formAttributes.get("commit"))) {
                commit = 0;
            } else {
                commit = (int) formAttributes.get("commit");
            }
            int cacheSize;
            if (ObjectUtils.isEmpty(formAttributes.get("cacheSize"))) {
                cacheSize = 0;
            } else {
                cacheSize = (int) formAttributes.get("cacheSize");
            }
            boolean preloadCache = (boolean) formAttributes.get("preloadCache");
            String key = (String) formAttributes.get("key");
            String keyGenerateType = (String) formAttributes.get("keyGenerateType");
            String sequence = (String) formAttributes.get("key");
            boolean replace = (boolean) formAttributes.get("replace");
            boolean useHash = (boolean) formAttributes.get("useHash");
            String hashField = (String) formAttributes.get("hashField");
            String lastUpdateField = (String) formAttributes.get("lastUpdateField");
            combinationLookupMeta.setCacheSize(cacheSize);
            combinationLookupMeta.setPreloadCache(preloadCache);
            combinationLookupMeta.setTechnicalKeyField(key);
            combinationLookupMeta.setTechKeyCreation(keyGenerateType);
            if (CombinationLookupMeta.CREATION_METHOD_AUTOINC.equals(keyGenerateType)) {
                combinationLookupMeta.setUseAutoinc(true);
                combinationLookupMeta.setSequenceFrom(null);
            } else if (CombinationLookupMeta.CREATION_METHOD_SEQUENCE.equals(keyGenerateType)) {
                combinationLookupMeta.setUseAutoinc(false);
                combinationLookupMeta.setSequenceFrom(sequence);
            } else if (CombinationLookupMeta.CREATION_METHOD_TABLEMAX.equals(keyGenerateType)) {
                combinationLookupMeta.setUseAutoinc(false);
                combinationLookupMeta.setSequenceFrom(null);
            }
            combinationLookupMeta.setReplaceFields(replace);
            combinationLookupMeta.setUseHash(useHash);
            combinationLookupMeta.setHashField(hashField);
            combinationLookupMeta.setLastUpdateField(lastUpdateField);

            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);
            Datasource datasource = datasourceService.one((long) databaseId);
            DatabaseMeta databaseMeta = DatabaseMetaUtils.init(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(datasource.getPassword()), datasource.getUrl(), datasource.getDriver());
            databaseMeta.setStreamingResults(datasource.getUseCursor());
            String parameters = datasource.getParameter();
            if (StringUtils.hasLength(parameters)) {
                List<Map<String, Object>> params = objectMapper.readValue(parameters, new TypeReference<List<Map<String, Object>>>() {
                });
                Properties properties = new Properties();
                for (Map<String, Object> param : params) {
                    if (StringUtils.hasLength((String) param.get("name"))) {
                        databaseMeta.addExtraOption(datasource.getCategory().toUpperCase(), (String) param.get("name"), (String) param.get("value"));
                        properties.putAll(param);
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
            combinationLookupMeta.setTablename(tableName);
            combinationLookupMeta.setSchemaName(schemaName);
            combinationLookupMeta.setCommitSize(commit);
            combinationLookupMeta.setDatabaseMeta(databaseMeta);
            List<Map<String, String>> fieldMappingData = (List<Map<String, String>>) formAttributes.get("fieldMappingData");
            for (Map<String, String> fieldMapping : fieldMappingData) {
                dbFields.add(fieldMapping.get("target"));
                streamFields.add(fieldMapping.get("source"));
            }
            combinationLookupMeta.setKeyLookup(streamFields.toArray(new String[0]));
            combinationLookupMeta.setKeyField(dbFields.toArray(new String[0]));
            StepMeta stepMeta = new StepMeta(stepName, combinationLookupMeta);
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