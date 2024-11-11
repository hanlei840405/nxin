package com.nxin.framework.converter.kettle.transform.streaming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.big.data.kettle.plugins.kafka.KafkaConsumerField;
import org.pentaho.big.data.kettle.plugins.kafka.KafkaConsumerInputMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class KafkaConsumerInputChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "KafkaConsumerInputMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            KafkaConsumerInputMeta kafkaConsumerInputMeta = new KafkaConsumerInputMeta();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String servers = (String) formAttributes.get("servers");
            Number shellId = (Number) formAttributes.get("shellId");
            List<Map<String, String>> topics = (List<Map<String, String>>) formAttributes.get("topics");
            String consumerGroup = (String) formAttributes.get("consumerGroup");
            String commitMode = (String) formAttributes.get("commitMode");
            int duration = 0;
            if (!ObjectUtils.isEmpty(formAttributes.get("duration"))) {
                duration = (int) formAttributes.get("duration");
            }
            int records = 0;
            if (!ObjectUtils.isEmpty(formAttributes.get("records"))) {
                records = (int) formAttributes.get("records");
            }
            int batches = 1;
            if (!ObjectUtils.isEmpty(formAttributes.get("batches"))) {
                batches = (int) formAttributes.get("batches");
            }
            String returnFieldByStep = (String) formAttributes.get("returnFieldByStep");
            List<Map<String, String>> fields = (List<Map<String, String>>) formAttributes.get("fields");
            for (Map<String, String> field : fields) {
                if ("key".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setKeyField(new KafkaConsumerField(KafkaConsumerField.Name.KEY, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                } else if ("message".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setMessageField(new KafkaConsumerField(KafkaConsumerField.Name.MESSAGE, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                } else if ("topic".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setTopicField(new KafkaConsumerField(KafkaConsumerField.Name.TOPIC, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                } else if ("partition".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setPartitionField(new KafkaConsumerField(KafkaConsumerField.Name.PARTITION, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                } else if ("offset".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setOffsetField(new KafkaConsumerField(KafkaConsumerField.Name.OFFSET, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                } else if ("timestamp".equals(field.get("inputName"))) {
                    kafkaConsumerInputMeta.setTimestampField(new KafkaConsumerField(KafkaConsumerField.Name.TIMESTAMP, field.get("outputName"), KafkaConsumerField.Type.valueOf(field.get("category"))));
                }
            }
            List<Map<String, String>> options = (List<Map<String, String>>) formAttributes.get("options");
            Map<String, String> config = new HashMap<>(0);
            for (Map<String, String> option : options) {
                String v = "";
                if (StringUtils.hasLength(option.get("value"))) {
                    v = option.get("value");
                }
                config.put(option.get("name"), v);
            }
            kafkaConsumerInputMeta.setConfig(config);
            Shell transformShell = getShellService().one(shellId.longValue());
            if (transformShell.getExecutable()) {
                String dir = (String) ConvertFactory.getVariable().get("dir");
//                kafkaConsumerInputMeta.setFileName(transformShell.getProjectId() + File.separator + transformShell.getParentId() + File.separator + transformShell.getId() + Constant.DOT + Constant.TRANS_SUFFIX);
                kafkaConsumerInputMeta.setTransformationPath(dir + transformShell.getProjectId() + File.separator + transformShell.getParentId() + File.separator + transformShell.getId() + Constant.DOT + Constant.TRANS_SUFFIX);
            }
            kafkaConsumerInputMeta.setDirectBootstrapServers(servers);
            for (Map<String, String> topic : topics) {
                kafkaConsumerInputMeta.addTopic(topic.get("topic"));
            }
            kafkaConsumerInputMeta.setConnectionType(KafkaConsumerInputMeta.ConnectionType.DIRECT);
            kafkaConsumerInputMeta.setConsumerGroup(consumerGroup);
            kafkaConsumerInputMeta.setAutoCommit("record".equals(commitMode));
            kafkaConsumerInputMeta.setBatchDuration("" + duration);
            kafkaConsumerInputMeta.setBatchSize("" + records);
            kafkaConsumerInputMeta.setParallelism("" + batches);
            kafkaConsumerInputMeta.setSubStep(returnFieldByStep);
            StepMeta stepMeta = new StepMeta(stepName, kafkaConsumerInputMeta);
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            stepMeta.setDraw(true);
            Set<Long> references = new HashSet<>(0);
            if (StringUtils.hasLength(transformShell.getReference())) {
                String[] ids = transformShell.getReference().split(",");
                for (String id : ids) {
                    references.add(Long.parseLong(id));
                }
            }
            references.add(transformShell.getId());
            if (ConvertFactory.getVariable().containsKey(Constant.VAR_REFERENCES)) {
                references.addAll((Set<Long>) ConvertFactory.getVariable().get(Constant.VAR_REFERENCES));
            }
            ConvertFactory.getVariable().put(Constant.VAR_REFERENCES, references);
            return new ResponseMeta(cell.getId(), stepMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}
