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
import org.pentaho.di.trans.step.StepIOMeta;
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.errorhandling.Stream;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.mergejoin.MergeJoinMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MergeJoinChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "MergeJoinMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            MergeJoinMeta mergeJoinMeta = new MergeJoinMeta();
            mergeJoinMeta.setDefault();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String mergeType = (String) formAttributes.get("join_type");
            String step1 = (String) formAttributes.get("step1");
            String step2 = (String) formAttributes.get("step2");
            List<Map<String, Object>> fieldMappingData = (List<Map<String, Object>>) formAttributes.get("fieldMappingData");
            List<String> keyFields1 = new ArrayList<>();
            List<String> keyFields2 = new ArrayList<>();
            fieldMappingData.forEach(item -> {
                for (String s : item.keySet()) {
                    if (s.equalsIgnoreCase("target")) {
                        keyFields1.add(item.get(s).toString());
                    }
                    if (s.equalsIgnoreCase("source")) {
                        keyFields2.add(item.get(s).toString());
                    }
                }
            });

            mergeJoinMeta.setJoinType(mergeType);
            mergeJoinMeta.setKeyFields1(keyFields1.toArray(new String[0]));
            mergeJoinMeta.setKeyFields2(keyFields2.toArray(new String[0]));


            Map<String, Object> mergeJointMetaMap = new HashMap<>(0);
            mergeJointMetaMap.put("stepMetaInterface",mergeJoinMeta);
            mergeJointMetaMap.put("step1", step1);
            mergeJointMetaMap.put("step2", step2);
            callbackMap.put(stepName,mergeJointMetaMap);

            TransformConvertFactory.getTransformConvertChains().add(this);

            StepMeta stepMeta = new StepMeta(stepName, mergeJoinMeta);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            return new ResponseMeta(cell.getId(), stepMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {
        for (Map.Entry<String, Object> entry : callbackMap.entrySet()) {
            Map<String, Object> mergeJointMetaMap = (Map<String, Object>) entry.getValue();
            if (mergeJointMetaMap.get("stepMetaInterface") instanceof MergeJoinMeta) {
                MergeJoinMeta mergeJoinMeta = (MergeJoinMeta) mergeJointMetaMap.get("stepMetaInterface");
                String step1Name = idNameMapping.get((String) mergeJointMetaMap.get("step1"));
                String step2Name = idNameMapping.get((String) mergeJointMetaMap.get("step2"));

                StepIOMetaInterface stepIOMeta = new StepIOMeta( true, true, false, false, false, false );
                List<StreamInterface> infoStreams = mergeJoinMeta.getStepIOMeta().getInfoStreams();
                infoStreams.get(0).setSubject(step1Name);
                infoStreams.get(1).setSubject(step2Name);
                for ( StreamInterface infoStream : infoStreams ) {
                    stepIOMeta.addStream( new Stream( infoStream ) );
                }
                mergeJoinMeta.setStepIOMeta(stepIOMeta);
                mergeJoinMeta.searchInfoAndTargetSteps(transMeta.getSteps());
                callbackMap.remove(entry.getKey());
            }
        }
    }
}
