package com.nxin.framework.converter.kettle.transform.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.blockuntilstepsfinish.BlockUntilStepsFinishMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BlockUntilStepsFinishChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "BlockUntilStepsFinishMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            BlockUntilStepsFinishMeta blockUntilStepsFinishMeta = new BlockUntilStepsFinishMeta();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            List<Map<String, Object>> steps = (List<Map<String, Object>>) formAttributes.get("steps");
            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);
            StepMeta stepMeta = new StepMeta(stepName, blockUntilStepsFinishMeta);
            TransformConvertFactory.getTransformConvertChains().add(this);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            callbackMap.put(stepName, blockUntilStepsFinishMeta);
            Map<String, Object> blockUntilStepsFinishMetaMap = new HashMap<>(0);
            blockUntilStepsFinishMetaMap.put("stepMetaInterface", blockUntilStepsFinishMeta);
            blockUntilStepsFinishMetaMap.put("steps", steps);
            callbackMap.put(stepName, blockUntilStepsFinishMetaMap);
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
        for (Map.Entry<String, Object> entry : callbackMap.entrySet()) {
            Map<String, Object> blockUntilStepsFinishMetaMap = (Map<String, Object>) entry.getValue();
            if (blockUntilStepsFinishMetaMap.get("stepMetaInterface") instanceof BlockUntilStepsFinishMeta) {
                BlockUntilStepsFinishMeta blockUntilStepsFinishMeta = (BlockUntilStepsFinishMeta) blockUntilStepsFinishMetaMap.get("stepMetaInterface");
                List<Map<String, Object>> steps = (List<Map<String, Object>>) blockUntilStepsFinishMetaMap.get("steps");
                List<String> stepNameList = new ArrayList<>(0);
                List<String> stepConyNrList = new ArrayList<>(0);
                for (Map<String, Object> step : steps) {
                    stepNameList.add(idNameMapping.get(String.valueOf(step.get("step"))));
                    stepConyNrList.add(String.valueOf(step.get("copyNr")));
                }
                blockUntilStepsFinishMeta.setStepName(stepNameList.toArray(new String[0]));
                blockUntilStepsFinishMeta.setStepCopyNr(stepConyNrList.toArray(new String[0]));
                callbackMap.remove(entry.getKey());
            }
        }
    }
}
