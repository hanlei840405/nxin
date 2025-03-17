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
import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.pentaho.di.trans.steps.excelinput.SpreadSheetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelInputChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "ExcelInputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            ExcelInputMeta excelInputMeta = new ExcelInputMeta();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String spreadSheetType = (String) formAttributes.get("spreadSheetType");
            String downloadDir = (String) formAttributes.get("downloadDir");
            String filename = (String) formAttributes.get("filename");
            String wildcard = (String) formAttributes.get("wildcard");
            boolean header = (boolean) formAttributes.get("header");
            boolean noEmpty = (boolean) formAttributes.get("noEmpty");
            boolean stopOnEmpty = (boolean) formAttributes.get("stopOnEmpty");
            String limit = (String) formAttributes.get("limit");
            String encoding = (String) formAttributes.get("encoding");
            List<Map<String, String>> sheetInfoData = (List<Map<String, String>>) formAttributes.get("sheetInfos");
            List<Map<String, String>> fieldMappingData = (List<Map<String, String>>) formAttributes.get("parameters");

            excelInputMeta.setSpreadSheetType(SpreadSheetType.getStpreadSheetTypeByDescription(spreadSheetType));
            excelInputMeta.setFileName(new String[]{downloadDir.concat(Constant.FILE_SEPARATOR).concat(filename)});
            excelInputMeta.setFileMask(new String[]{wildcard});
            excelInputMeta.setExcludeFileMask(new String[]{null});
            excelInputMeta.setFileRequired(new String[]{"Y"});
            excelInputMeta.setIncludeSubFolders(new String[]{null});
            excelInputMeta.setStartsWithHeader(header);
            excelInputMeta.setIgnoreEmptyRows(noEmpty);
            excelInputMeta.setStopOnEmpty(stopOnEmpty);
            excelInputMeta.setRowLimit(Const.toLong(limit, 0));
            excelInputMeta.setEncoding(encoding);
            List<String> sheetNames = new ArrayList<>();
            List<Integer> startRows = new ArrayList<>();
            List<Integer> startColumns = new ArrayList<>();
            for (Map<String, String> sheetInfo : sheetInfoData) {
                sheetNames.add(sheetInfo.get("sheetName"));
                startRows.add(Const.toInt(sheetInfo.get("startRow"), 0));
                startColumns.add(Const.toInt(sheetInfo.get("startColumn"), 0));
            }
            excelInputMeta.setSheetName(sheetNames.toArray(new String[0]));
            excelInputMeta.setStartRow(startRows.stream().mapToInt(Integer::intValue).toArray());
            excelInputMeta.setStartColumn(startColumns.stream().mapToInt(Integer::intValue).toArray());

            List<ExcelInputField> excelInputFields = new ArrayList<>();
            for (Map<String, String> fieldMapping : fieldMappingData) {
                ExcelInputField excelInputField = new ExcelInputField();
                excelInputField.setName(fieldMapping.get("field"));
                excelInputField.setType(ValueMetaFactory.getIdForValueMeta(fieldMapping.get("category")));
                excelInputField.setFormat(fieldMapping.get("formatValue"));
                excelInputField.setLength(Const.toInt(fieldMapping.get("lengthValue"), -1));
                excelInputField.setPrecision(Const.toInt(fieldMapping.get("accuracy"), -1));
                excelInputField.setCurrencySymbol(fieldMapping.get("accuracy"));
                excelInputField.setDecimalSymbol(fieldMapping.get("decimal"));
                excelInputField.setGroupSymbol(fieldMapping.get("groupBy"));
                excelInputField.setTrimType(ValueMetaString.getTrimTypeByDesc(fieldMapping.get("trimType")));
                excelInputField.setRepeated(Boolean.getBoolean(fieldMapping.get("repeated")));
                excelInputFields.add(excelInputField);
            }
            excelInputMeta.setField(excelInputFields.toArray(new ExcelInputField[0]));
            StepMeta stepMeta = new StepMeta(stepName, excelInputMeta);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            return new ResponseMeta(cell.getId(), stepMeta, null, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}
