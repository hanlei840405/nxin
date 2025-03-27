package com.nxin.framework.converter.kettle.transform.output;

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
import org.pentaho.di.trans.steps.synchronizeaftermerge.SynchronizeAfterMergeMeta;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SynchronizeAfterMergeChain extends TransformConvertChain {
    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "SynchronizeAfterMergeMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            SynchronizeAfterMergeMeta synchronizeAfterMergeMeta = new SynchronizeAfterMergeMeta();
            List<String> searchDbFields = new ArrayList<>(0);
            List<String> searchStreamConditions = new ArrayList<>(0);
            List<String> searchStreamFields = new ArrayList<>(0);
            List<String> searchStreamFields2 = new ArrayList<>(0);
            List<String> dbFields = new ArrayList<>(0);
            List<String> streamFields = new ArrayList<>(0);
            List<Boolean> streamFieldUpdates = new ArrayList<>(0);
            String stepName = (String) formAttributes.get("name");
            String schemaName = (String) formAttributes.get("schemaName");
            String tableName = (String) formAttributes.get("tableName");
            int databaseId = (int) formAttributes.get("datasource");
            int commit = (int) formAttributes.get("commit");
            boolean batch = (boolean) formAttributes.get("batch");
            boolean tableNameInField = (boolean) formAttributes.get("tableNameInField");
            String tableField = (String) formAttributes.get("tableField");
            String operationField = (String) formAttributes.get("operationField");
            String orderInsert = (String) formAttributes.get("orderInsert");
            String orderUpdate = (String) formAttributes.get("orderUpdate");
            String orderDelete = (String) formAttributes.get("orderDelete");
            boolean performLookup = (boolean) formAttributes.get("performLookup");

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
            synchronizeAfterMergeMeta.setDatabaseMeta(databaseMeta);
            synchronizeAfterMergeMeta.setTableName(tableName);
            synchronizeAfterMergeMeta.setSchemaName(schemaName);
            synchronizeAfterMergeMeta.setCommitSize(commit);
            synchronizeAfterMergeMeta.setUseBatchUpdate(batch);
            synchronizeAfterMergeMeta.settablenameInField(tableNameInField);
            synchronizeAfterMergeMeta.settablenameField(tableField);
            synchronizeAfterMergeMeta.setOperationOrderField(operationField);
            synchronizeAfterMergeMeta.setOrderInsert(orderInsert);
            synchronizeAfterMergeMeta.setOrderUpdate(orderUpdate);
            synchronizeAfterMergeMeta.setOrderDelete(orderDelete);
            synchronizeAfterMergeMeta.setPerformLookup(performLookup);

            List<Map<String, String>> searchMappingData = (List<Map<String, String>>) formAttributes.get("searchMappingData");
            for (Map<String, String> fieldMapping : searchMappingData) {
                searchDbFields.add(fieldMapping.get("target"));
                searchStreamConditions.add(fieldMapping.get("condition"));
                searchStreamFields.add(fieldMapping.get("source"));
                searchStreamFields2.add(fieldMapping.get("source2"));
            }
            List<Map<String, String>> fieldMappingData = (List<Map<String, String>>) formAttributes.get("fieldMappingData");
            for (Map<String, String> fieldMapping : fieldMappingData) {
                dbFields.add(fieldMapping.get("target"));
                streamFields.add(fieldMapping.get("source"));
                if ("Y".equals(fieldMapping.get("update"))) {
                    streamFieldUpdates.add(true);
                } else {
                    streamFieldUpdates.add(false);
                }
            }
            synchronizeAfterMergeMeta.setKeyLookup(searchDbFields.toArray(new String[0]));
            synchronizeAfterMergeMeta.setKeyCondition(searchStreamConditions.toArray(new String[0]));
            synchronizeAfterMergeMeta.setKeyStream(searchStreamFields.toArray(new String[0]));
            synchronizeAfterMergeMeta.setKeyStream2(searchStreamFields2.toArray(new String[0]));

            synchronizeAfterMergeMeta.setUpdateLookup(dbFields.toArray(new String[0]));
            synchronizeAfterMergeMeta.setUpdateStream(streamFields.toArray(new String[0]));
            synchronizeAfterMergeMeta.setUpdate(streamFieldUpdates.toArray(new Boolean[0]));
            StepMeta stepMeta = new StepMeta(stepName, synchronizeAfterMergeMeta);
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
