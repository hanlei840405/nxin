package com.nxin.framework.converter.kettle.transform.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.utils.DatabaseMetaUtils;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TableInputChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "TableInputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            int databaseId = (int) formAttributes.get("datasource");
            String sql = (String) formAttributes.get("sql");
            boolean lazyConversionActive = (boolean) formAttributes.get("lazyConversionActive");
            boolean executeEachInputRow = (boolean) formAttributes.get("executeEachInputRow");
            boolean variableReplacementActive = (boolean) formAttributes.get("variableReplacementActive");
            String lookupFromStepValue = (String) formAttributes.get("lookupFromStepValue");
            int rowLimit = (int) formAttributes.get("rowLimit");
            TableInputMeta tableInputMeta = new TableInputMeta();
            tableInputMeta.setSQL(sql);
            tableInputMeta.setLazyConversionActive(lazyConversionActive);
            tableInputMeta.setExecuteEachInputRow(executeEachInputRow);
            tableInputMeta.setVariableReplacementActive(variableReplacementActive);
            tableInputMeta.setRowLimit(String.valueOf(rowLimit));

            Datasource datasource = datasourceService.one((long) databaseId);
            DatabaseMeta databaseMeta = DatabaseMetaUtils.init(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(datasource.getPassword()), datasource.getUrl(), datasource.getDriver());
            databaseMeta.setStreamingResults(datasource.getUseCursor());
            Properties properties = new Properties();
            if (datasource.getUsePool()) {
                databaseMeta.setUsingConnectionPool(true);
                if (datasource.getPoolInitialSize() != null) {
                    databaseMeta.setInitialPoolSize(datasource.getPoolInitialSize());
                }
                if (datasource.getPoolMaxSize() != null) {
                    databaseMeta.setMaximumPoolSize(datasource.getPoolMaxSize());
                }
                if (datasource.getPoolInitial() != null) {
                    properties.put("initialSize", datasource.getPoolInitial());
                }
                if (datasource.getPoolMaxActive() != null) {
                    properties.put("maxActive", datasource.getPoolMaxActive());
                }
                if (datasource.getPoolMaxIdle() != null) {
                    properties.put("maxIdle", datasource.getPoolMaxIdle());
                }
                if (datasource.getPoolMinIdle() != null) {
                    properties.put("minIdle", datasource.getPoolMinIdle());
                }
                if (datasource.getPoolMaxWait() != null) {
                    properties.put("maxWait", datasource.getPoolMaxWait());
                }
            }
            String parameters = datasource.getParameter();

            if (StringUtils.hasLength(parameters)) {
                List<Map<String, Object>> params = objectMapper.readValue(parameters, new TypeReference<List<Map<String, Object>>>() {
                });
                for (Map<String, Object> param : params) {
                    if (StringUtils.hasLength((String) param.get("name"))) {
                        databaseMeta.addExtraOption(datasource.getCategory().toUpperCase(), (String) param.get("name"), (String) param.get("value"));
                        properties.putAll(param);
                    }
                }
                databaseMeta.setConnectionPoolingProperties(properties);
            }
            tableInputMeta.setDatabaseMeta(databaseMeta);
            if (StringUtils.hasLength(lookupFromStepValue)) {
                Map<String, Object> tableInputMetaMap = new HashMap<>(0);
                tableInputMetaMap.put("stepMetaInterface", tableInputMeta);
                tableInputMetaMap.put("previousStep", lookupFromStepValue);
                callbackMap.put(stepName, tableInputMetaMap);
            }
            StepMeta stepMeta = new StepMeta(stepName, tableInputMeta);
            TransformConvertFactory.getTransformConvertChains().add(this);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            return new ResponseMeta(cell.getId(), stepMeta, databaseMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {
        for (Map.Entry<String, Object> entry : callbackMap.entrySet()) {
            Map<String, Object> tableInputMetaMap = (Map<String, Object>) entry.getValue();
            if (tableInputMetaMap.get("stepMetaInterface") instanceof TableInputMeta) {
                TableInputMeta tableInputMeta = (TableInputMeta) tableInputMetaMap.get("stepMetaInterface");
                String previousStep = idNameMapping.get((String) tableInputMetaMap.get("previousStep"));
                StepIOMetaInterface stepIOMeta = new StepIOMeta(true, true, false, false, false, false);
                List<StreamInterface> infoStreams = tableInputMeta.getStepIOMeta().getInfoStreams();
                infoStreams.get(0).setSubject(previousStep);
                stepIOMeta.addStream(new Stream(infoStreams.get(0)));
                tableInputMeta.setStepIOMeta(stepIOMeta);
                tableInputMeta.searchInfoAndTargetSteps(transMeta.getSteps());
                callbackMap.remove(entry.getKey());
            }
        }
    }
}
