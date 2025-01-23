package com.nxin.framework.converter.kettle.transform.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.nxin.framework.exception.ConvertException;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.filterrows.FilterRowsMeta;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            List<Map<String, Object>> conditions = (List<Map<String, Object>>) formAttributes.get("conditions");
            Map<String, Object> root = conditions.get(0); // 根节点，无逻辑意义
            Condition condition = new Condition();
            deepLoop(condition, root);
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
                StepIOMetaInterface stepIOMeta = new StepIOMeta(true, true, false, false, false, false);
                List<StreamInterface> infoStreams = filterRowsMeta.getStepIOMeta().getTargetStreams();
                infoStreams.get(0).setSubject(trueStepName);
                infoStreams.get(1).setSubject(falseStepName);
                for (StreamInterface infoStream : infoStreams) {
                    stepIOMeta.addStream(new Stream(infoStream));
                }
                filterRowsMeta.setStepIOMeta(stepIOMeta);
                filterRowsMeta.searchInfoAndTargetSteps(transMeta.getSteps());
                callbackMap.remove(entry.getKey());
            }
        }
    }

    private void deepLoop(Condition condition, Map<String, Object> node) {
        List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
        if (children.size() == 0) {
            return;
        }
//        Condition groupCondition = null;
//        if (children.size() > 1) {
//            groupCondition = new Condition();
//        }
//        if (condition.getChildren().size() > 1) {
//            groupCondition.setOperator(Condition.OPERATOR_AND);
//        }
        for (int i = 0; i < children.size(); i++) {
            Map<String, Object> child = children.get(i);
            Condition childCondition = new Condition();
            if (i > 0) {
                childCondition.setOperator(Condition.OPERATOR_OR);
            }
//            else if (groupCondition != null && groupCondition.getChildren().size() > 0) {
//                childCondition.setOperator(Condition.OPERATOR_AND);
//            } else if (groupCondition == null && condition.getChildren().size() > 0) {
//                childCondition.setOperator(Condition.OPERATOR_AND);
//            }
            build(childCondition, child);
            Condition emptyCondition = new Condition();
            deepLoop(emptyCondition, child);
            if (!emptyCondition.isEmpty()) {
                emptyCondition.setOperator(Condition.OPERATOR_AND);
                childCondition.addCondition(emptyCondition);
            }
            condition.getChildren().add(childCondition);

//            if (groupCondition != null) {
//                groupCondition.addCondition(childCondition);
//                deepLoop(groupCondition, child);
//            } else {
//                condition.getChildren().add(childCondition);
//                deepLoop(condition, child);
//            }
        }
//        if (groupCondition != null) {
//            condition.getChildren().add(groupCondition);
//        }
    }

    private void build(Condition condition, Map<String, Object> child) {
        Map<String, Object> config = (Map<String, Object>) child.get("config");
        if (config == null) {
            return;
        }
        String negate = (String) config.get("negate");
        String leftValuename = (String) config.get("leftValuename");
        String function = (String) config.get("function");
        String rightValuename = (String) config.get("rightValuename");
        Object value = config.get("value");
        String type = (String) config.get("category");
        int length = (int) config.get("lengthValue");
        int accuracy = (int) config.get("accuracy");
        String format = (String) config.get("format");
        condition.setNegated(negate.equals("Y"));
        condition.setLeftValuename(leftValuename);
        condition.setFunction(Condition.getFunction(function));
        condition.setRightValuename(rightValuename);
        if (value != null) {
//            int id = ValueMetaFactory.getIdForValueMeta(type);
//            ValueMetaBase valueMetaBase = new ValueMetaBase("constant", id, length, accuracy);
            try {
                if ("Number".equals(type)) {
                    DecimalFormat decimalFormat;
                    if (StringUtils.hasLength(format)) {
                        decimalFormat = new DecimalFormat(format);
                    } else {
                        decimalFormat = new DecimalFormat();
                    }
                    value = decimalFormat.parse((String) value).doubleValue();
                } else if ("Integer".equals(type)) {
                    DecimalFormat decimalFormat;
                    if (StringUtils.hasLength(format)) {
                        decimalFormat = new DecimalFormat(format);
                    } else {
                        decimalFormat = new DecimalFormat();
                    }
                    value = decimalFormat.parse((String) value).longValue();
                } else if ("Date".equals(type)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    value = sdf.parse((String) value);
                } else if ("Timestamp".equals(type)) {
                    value = Timestamp.valueOf((String) value);
                } else if ("BigNumber".equals(type)) {
                    DecimalFormat decimalFormat;
                    if (StringUtils.hasLength(format)) {
                        decimalFormat = new DecimalFormat(format);
                    } else {
                        decimalFormat = new DecimalFormat();
                    }
                    decimalFormat.setParseBigDecimal(true);
                    value = decimalFormat.parse((String) value);
                } else if ("Boolean".equals(type)) {
                    value = new Boolean((String) value);
                }
                ValueMetaAndData valueMetaAndData = new ValueMetaAndData("constant", value);

                valueMetaAndData.getValueMeta().setLength(length);
                valueMetaAndData.getValueMeta().setPrecision(accuracy);
                if (StringUtils.hasLength(format)) {
                    valueMetaAndData.getValueMeta().setConversionMask(format);
                }
                condition.setRightExact(valueMetaAndData);
            } catch (ParseException | KettleValueException e) {
                throw new ConvertException(e.getMessage());
            }
//            valueMetaBase.setConversionMask(format);
//            ValueMetaAndData valueMetaAndData = new ValueMetaAndData(valueMetaBase, value);
//            condition.setRightExact(valueMetaAndData);
        } else {

        }
    }
}
