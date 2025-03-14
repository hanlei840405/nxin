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
import org.pentaho.di.trans.steps.parallelgzipcsv.ParGzipCsvInputMeta;
import org.pentaho.di.trans.steps.textfileinput.TextFileInputField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ParGzipCsvInputChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "ParGzipCsvInputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            ParGzipCsvInputMeta parGzipCsvInputMeta = new ParGzipCsvInputMeta();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String filename = (String) formAttributes.get("filename");
            String downloadDir = (String) formAttributes.get("downloadDir");
            String delimiter = (String) formAttributes.get("delimiter");
            String enclosure = (String) formAttributes.get("enclosure");
            String bufferSize = (String) formAttributes.get("bufferSize");
            boolean lazyConversion = (boolean) formAttributes.get("lazyConversion");
            boolean header = (boolean) formAttributes.get("header");
            String rowNumField = (String) formAttributes.get("rowNumField");
            boolean runningInParallel = (boolean) formAttributes.get("runningInParallel");
            String encoding = (String) formAttributes.get("encoding");
            parGzipCsvInputMeta.setFilename(downloadDir.concat(Constant.FILE_SEPARATOR).concat(filename));
            parGzipCsvInputMeta.setDelimiter(delimiter);
            parGzipCsvInputMeta.setEnclosure(enclosure);
            parGzipCsvInputMeta.setBufferSize(bufferSize);
            parGzipCsvInputMeta.setLazyConversionActive(lazyConversion);
            parGzipCsvInputMeta.setHeaderPresent(header);
            parGzipCsvInputMeta.setRowNumField(rowNumField);
            parGzipCsvInputMeta.setRunningInParallel(runningInParallel);
            parGzipCsvInputMeta.setEncoding(encoding);
            List<Map<String, String>> fieldMappingData = (List<Map<String, String>>) formAttributes.get("parameters");
            List<TextFileInputField> textFileInputFields = new ArrayList<>();
            for (Map<String, String> fieldMapping : fieldMappingData) {
                TextFileInputField textFileInputField = new TextFileInputField();
                textFileInputField.setName(fieldMapping.get("field"));
                textFileInputField.setType(ValueMetaFactory.getIdForValueMeta(fieldMapping.get("category")));
                textFileInputField.setFormat(fieldMapping.get("formatValue"));
                textFileInputField.setLength(Const.toInt(fieldMapping.get("lengthValue"), -1));
                textFileInputField.setPrecision(Const.toInt(fieldMapping.get("accuracy"), -1));
                textFileInputField.setCurrencySymbol(fieldMapping.get("accuracy"));
                textFileInputField.setDecimalSymbol(fieldMapping.get("decimal"));
                textFileInputField.setGroupSymbol(fieldMapping.get("groupBy"));
                textFileInputField.setTrimType(ValueMetaString.getTrimTypeByDesc(fieldMapping.get("trimType")));
                textFileInputFields.add(textFileInputField);
            }
            parGzipCsvInputMeta.setInputFields(textFileInputFields.toArray(new TextFileInputField[0]));
            StepMeta stepMeta = new StepMeta(stepName, parGzipCsvInputMeta);
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
