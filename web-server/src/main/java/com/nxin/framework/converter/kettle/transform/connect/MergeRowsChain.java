package com.nxin.framework.converter.kettle.transform.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.mergerows.MergeRowsMeta;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MergeRowsChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "MergeRowsMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            MergeRowsMeta mergeRowsMeta = new MergeRowsMeta();
            mergeRowsMeta.setDefault();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String oldDatasource = (String) formAttributes.get("oldDatasource");
            String newDatasource = (String) formAttributes.get("newDatasource");
            String flagField = (String) formAttributes.get("flagField");
            List<Map<String, String>> keys = (List<Map<String, String>>) formAttributes.get("keys");
            List<Map<String, String>> values = (List<Map<String, String>>) formAttributes.get("values");
            List<String> keyFields = new ArrayList<>();
            List<String> valueFields = new ArrayList<>();
            for (Map<String, String> keyField : keys) {
                keyFields.add(keyField.get("field"));
            }
            for (Map<String, String> valueField : values) {
                valueFields.add(valueField.get("field"));
            }
            mergeRowsMeta.setFlagField(flagField);
            mergeRowsMeta.allocate(keyFields.size(), valueFields.size());
            mergeRowsMeta.setKeyFields(keyFields.toArray(new String[0]));
            mergeRowsMeta.setValueFields(valueFields.toArray(new String[0]));

            if (StringUtils.hasLength(oldDatasource) && StringUtils.hasLength(newDatasource)) {
                Map<String, Object> mergeRowsMetaMap = new HashMap<>(0);
                mergeRowsMetaMap.put("stepMetaInterface", mergeRowsMeta);
                mergeRowsMetaMap.put("oldDatasourceStep", oldDatasource);
                mergeRowsMetaMap.put("newDatasourceStep", newDatasource);
                callbackMap.put(stepName, mergeRowsMetaMap);
                TransformConvertFactory.getTransformConvertChains().add(this);
            }
            StepMeta stepMeta = new StepMeta(stepName, mergeRowsMeta);
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
        for (Map.Entry<String, Object> entry : callbackMap.entrySet()) {
            Map<String, Object> mergeRowsMetaMap = (Map<String, Object>) entry.getValue();
            if (mergeRowsMetaMap.get("stepMetaInterface") instanceof MergeRowsMeta) {
                MergeRowsMeta mergeRowsMeta = (MergeRowsMeta) mergeRowsMetaMap.get("stepMetaInterface");
                String oldDatasourceStep = idNameMapping.get((String) mergeRowsMetaMap.get("oldDatasourceStep"));
                String newDatasourceStep = idNameMapping.get((String) mergeRowsMetaMap.get("newDatasourceStep"));
                mergeRowsMeta.getStepIOMeta().getInfoStreams().get(0).setSubject(oldDatasourceStep);
                mergeRowsMeta.getStepIOMeta().getInfoStreams().get(1).setSubject(newDatasourceStep);
                mergeRowsMeta.searchInfoAndTargetSteps(transMeta.getSteps());
                callbackMap.remove(entry.getKey());
            }
        }
    }
}
