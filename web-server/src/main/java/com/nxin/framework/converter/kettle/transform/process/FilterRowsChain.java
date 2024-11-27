package com.nxin.framework.converter.kettle.transform.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.value.ValueMetaBase;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.filterrows.FilterRowsMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilterRowsChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "FilterRowsMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            FilterRowsMeta filterRowsMeta = new FilterRowsMeta();
            filterRowsMeta.setDefault();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String sendTrueTo = (String) formAttributes.get("sendTrueTo");
            String sendFalseTo = (String) formAttributes.get("sendFalseTo");
            List<Map<String, Object>> fieldMappingData = (List<Map<String, Object>>) formAttributes.get("fieldMappingData");
            Condition condition = new Condition();
            for (int i = 0; i < fieldMappingData.size(); i++) {
                Map<String, Object> mapping = fieldMappingData.get(i);
                condition.addCondition(this.build(new Condition(), mapping));
            }
            filterRowsMeta.setCondition(condition);
            Map<String, Object> filterRowsMetaMap = new HashMap<>(0);
            filterRowsMetaMap.put("sendTrueTo", sendTrueTo);
            filterRowsMetaMap.put("sendFalseTo", sendFalseTo);
            filterRowsMetaMap.put("stepMetaInterface", filterRowsMeta);
            callbackMap.put(stepName, filterRowsMetaMap);
            StepMeta stepMeta = new StepMeta(stepName, filterRowsMeta);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            TransformConvertFactory.getTransformConvertChains().add(this);
            return new ResponseMeta(cell.getId(), stepMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {
        for (Map.Entry<String, Object> entry : callbackMap.entrySet()) {
            Map<String, Object> filterRowsMetaMap = (Map<String, Object>) entry.getValue();
            if (filterRowsMetaMap.get("stepMetaInterface") instanceof FilterRowsMeta) {
                FilterRowsMeta filterRowsMeta = (FilterRowsMeta) filterRowsMetaMap.get("stepMetaInterface");
                String trueStepName = idNameMapping.get((String) filterRowsMetaMap.get("sendTrueTo"));
                String falseStepName = idNameMapping.get((String) filterRowsMetaMap.get("sendFalseTo"));
                StepIOMetaInterface stepIOMeta = new StepIOMeta( true, true, false, false, false, false );
                List<StreamInterface> infoStreams = filterRowsMeta.getStepIOMeta().getTargetStreams();
                infoStreams.get(0).setSubject(trueStepName);
                infoStreams.get(1).setSubject(falseStepName);
                for ( StreamInterface stream : infoStreams ) {
                    stream.setStepMeta( StepMeta.findStep( transMeta.getSteps(), (String) stream.getSubject() ) );
                }
                for ( StreamInterface infoStream : infoStreams ) {
                    stepIOMeta.addStream( new Stream( infoStream ) );
                }
                filterRowsMeta.setStepIOMeta(stepIOMeta);
                callbackMap.remove(entry.getKey());
            }
        }
    }

    private Condition build(Condition condition, Map<String, Object> fieldMapping) {
        String negate = fieldMapping.get("negate").toString();
        if (!condition.getChildren().isEmpty()) {
            String operates = fieldMapping.get("operates").toString();
            condition.setOperator(Condition.getOperator(operates));
        }
        String leftValuename = fieldMapping.get("leftValuename").toString();
        String function = fieldMapping.get("function").toString();
        String rightValuename = fieldMapping.get("rightValuename").toString();
        String text = fieldMapping.get("value").toString();
        String type = fieldMapping.get("type").toString();
        condition.setNegated(negate.equals("Y"));
        condition.setLeftValuename(leftValuename);
        condition.setFunction(Condition.getFunction(function));
        condition.setRightValuename(rightValuename);
        if (StringUtils.hasLength(text)) {
            int id = ValueMetaFactory.getIdForValueMeta(type);
            // todo length,precision暂时为默认值-1与0，后面迭代
            ValueMetaBase valueMetaBase = new ValueMetaBase("constant", id, -1, 0, null);
            ValueMetaAndData valueMetaAndData = new ValueMetaAndData(valueMetaBase, text);

            condition.setRightExact(valueMetaAndData);
        }
        return condition;
    }
}
