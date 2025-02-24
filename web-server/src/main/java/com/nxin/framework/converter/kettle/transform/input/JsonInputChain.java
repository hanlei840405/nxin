package com.nxin.framework.converter.kettle.transform.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.jsoninput.JsonInputField;
import org.pentaho.di.trans.steps.jsoninput.JsonInputMeta;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonInputChain extends TransformConvertChain {

    private static final String SOURCE_FROM_FILE = "file";

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "JsonInputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            JsonInputMeta jsonInputMeta = new JsonInputMeta();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String sourceFrom = (String) formAttributes.get("sourceFrom");
            String downloadDir = (String) formAttributes.get("downloadDir");
            String wildcard = (String) formAttributes.get("wildcard");
            String valueField = (String) formAttributes.get("valueField");
            boolean ignoreEmptyFile = (boolean) formAttributes.get("ignoreEmptyFile");
            boolean notFailIfNoFile = (boolean) formAttributes.get("notFailIfNoFile");
            boolean ignoreMissingPath = (boolean) formAttributes.get("ignoreMissingPath");
            boolean defaultPathLeafToNull = (boolean) formAttributes.get("defaultPathLeafToNull");
            int rowLimit = (int) formAttributes.get("rowLimit");
            jsonInputMeta.setFieldValue(valueField);
            jsonInputMeta.setIgnoreEmptyFile(ignoreEmptyFile);
            jsonInputMeta.setDoNotFailIfNoFile(notFailIfNoFile);
            jsonInputMeta.setIgnoreMissingPath(ignoreMissingPath);
            jsonInputMeta.setDefaultPathLeafToNull(defaultPathLeafToNull);
            jsonInputMeta.setRowLimit(rowLimit);
            if (SOURCE_FROM_FILE.equals(sourceFrom)) {
                jsonInputMeta.setInFields(false);
                jsonInputMeta.inputFiles = new JsonInputMeta.InputFiles();
                jsonInputMeta.inputFiles.allocate(1);
                jsonInputMeta.setFileName(new String[]{downloadDir});
                jsonInputMeta.setFileMask(new String[]{wildcard});
                jsonInputMeta.setExcludeFileMask(new String[]{null});
                jsonInputMeta.setFileRequired(new String[]{"Y"});
                jsonInputMeta.setIncludeSubFolders(new String[]{null});
            } else {
                jsonInputMeta.setInFields(true);
            }
            List<Map<String, Object>> parameters = (List<Map<String, Object>>) formAttributes.get("parameters");
            List<JsonInputField> jsonInputFields = new ArrayList<>(0);
            for (Map<String, Object> fieldMapping : parameters) {
                JsonInputField jsonInputField = new JsonInputField();
                jsonInputField.setName((String) fieldMapping.get("field"));
                jsonInputField.setPath((String) fieldMapping.get("path"));
                jsonInputField.setType((String) fieldMapping.get("category"));
                jsonInputField.setFormat((String) fieldMapping.get("formatValue"));
                if (!ObjectUtils.isEmpty(fieldMapping.get("lengthValue"))) {
                    jsonInputField.setLength((int) fieldMapping.get("lengthValue"));
                } else {
                    jsonInputField.setLength(-1);
                }
                if (!ObjectUtils.isEmpty(fieldMapping.get("accuracy"))) {
                    jsonInputField.setPrecision((int) fieldMapping.get("accuracy"));
                } else {
                    jsonInputField.setPrecision(-1);
                }
                jsonInputField.setCurrencySymbol((String) fieldMapping.get("currency"));
                jsonInputField.setDecimalSymbol((String) fieldMapping.get("decimal"));
                jsonInputField.setGroupSymbol((String) fieldMapping.get("groupBy"));
                jsonInputField.setTrimType((String) fieldMapping.get("removeBlank"));
                if ("Y".equals(fieldMapping.get("repeat"))) {
                    jsonInputField.setRepeated(true);
                } else {
                    jsonInputField.setRepeated(false);
                }
                jsonInputFields.add(jsonInputField);
            }
            jsonInputMeta.setInputFields(jsonInputFields.toArray(new JsonInputField[0]));
            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);
            StepMeta stepMeta = new StepMeta(stepName, jsonInputMeta);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            stepMeta.setCopies(parallel);
            return new ResponseMeta(cell.getId(), stepMeta, null, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}
