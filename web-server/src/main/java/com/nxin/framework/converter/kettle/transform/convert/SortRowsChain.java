package com.nxin.framework.converter.kettle.transform.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.utils.BooleanUtils;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.sort.SortRowsMeta;
import org.springframework.util.ObjectUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SortRowsChain extends TransformConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException {
        if (cell.isVertex() && "SortRowsMeta".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            SortRowsMeta sortRowsMeta = new SortRowsMeta();
            sortRowsMeta.setPrefix("out");
            sortRowsMeta.setDirectory("%%java.io.tmpdir%%");
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String stepName = (String) formAttributes.get("name");
            String cacheSize = (String) formAttributes.get("cacheSize");
            sortRowsMeta.setSortSize(cacheSize);
            String freeMemory = (String) formAttributes.get("freeMemory");
            sortRowsMeta.setFreeMemoryLimit(freeMemory);
            boolean compressTempFile = (boolean) formAttributes.get("compressTempFile");
            sortRowsMeta.setCompressFiles(compressTempFile);
            boolean passUniqueRow = (boolean) formAttributes.get("passUniqueRow");
            sortRowsMeta.setOnlyPassingUniqueRows(passUniqueRow);
            List<String> fieldList = new ArrayList<>(0);
            List<Boolean> ascList = new ArrayList<>(0);
            List<Boolean> caseSensitiveList = new ArrayList<>(0);
            List<Boolean> sortBasedOnLocaleList = new ArrayList<>(0);
            List<Boolean> preSortedList = new ArrayList<>(0);
            List<Integer> collatorStrengthList = new ArrayList<>(0);
            List<Map<String, Object>> fieldMappingData = (List<Map<String, Object>>) formAttributes.get("parameters");
            for (Map<String, Object> fieldMapping : fieldMappingData) {
                fieldList.add((String) fieldMapping.get("field"));
                if ("Y".equals(fieldMapping.get("asc"))) {
                    ascList.add(true);
                } else {
                    ascList.add(false);
                }
                if ("Y".equals(fieldMapping.get("caseSensitive"))) {
                    caseSensitiveList.add(true);
                } else {
                    caseSensitiveList.add(false);
                }
                if ("Y".equals(fieldMapping.get("sortBasedOnLocale"))) {
                    sortBasedOnLocaleList.add(true);
                } else {
                    sortBasedOnLocaleList.add(false);
                }
                if ("Y".equals(fieldMapping.get("preSorted"))) {
                    preSortedList.add(true);
                } else {
                    preSortedList.add(false);
                }
                if (!ObjectUtils.isEmpty(fieldMapping.get("collatorStrength"))) {
                    collatorStrengthList.add((int) fieldMapping.get("collatorStrength"));
                } else {
                    collatorStrengthList.add(Collator.IDENTICAL);
                }
            }
            sortRowsMeta.setFieldName(fieldList.toArray(new String[0]));
            sortRowsMeta.setAscending(ascList.stream().collect(BooleanUtils.TO_BOOLEAN_ARRAY));
            sortRowsMeta.setCaseSensitive(caseSensitiveList.stream().collect(BooleanUtils.TO_BOOLEAN_ARRAY));
            sortRowsMeta.setCollatorEnabled(sortBasedOnLocaleList.stream().collect(BooleanUtils.TO_BOOLEAN_ARRAY));
            sortRowsMeta.setPreSortedField(preSortedList.stream().collect(BooleanUtils.TO_BOOLEAN_ARRAY));
            sortRowsMeta.setCollatorStrength(collatorStrengthList.stream().mapToInt(Integer::valueOf).toArray());
            int parallel = (int) formAttributes.get(Constant.ETL_PARALLEL);
            StepMeta stepMeta = new StepMeta(stepName, sortRowsMeta);
            if (formAttributes.containsKey("distribute")) {
                boolean distribute = (boolean) formAttributes.get("distribute");
                stepMeta.setDistributes(distribute);
            }
            stepMeta.setCopies(parallel);
            mxGeometry geometry = cell.getGeometry();
            stepMeta.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            stepMeta.setDraw(true);
            return new ResponseMeta(cell.getId(), stepMeta, null);
        } else {
            return next.parse(cell, transMeta);
        }
    }

//    @Override
//    public void parse(Long branchId, StepMeta stepMeta, mxGraph graph) throws JsonProcessingException {
//        if (stepMeta.getStepMetaInterface() instanceof TableInputMeta) {
//            String name = stepMeta.getName();
//            TableInputMeta tableInputMeta = (TableInputMeta) stepMeta.getStepMetaInterface();
//            String databaseName = tableInputMeta.getDatabaseMeta().getName();
//            Datasource datasource = datasourceService.one(branchId, databaseName);
//            String sql = tableInputMeta.getSQL();
//            boolean lazyConversionActive = tableInputMeta.isLazyConversionActive();
//            boolean executeEachInputRow = tableInputMeta.isExecuteEachInputRow();
//            boolean variableReplacementActive = tableInputMeta.isVariableReplacementActive();
//            String rowLimit = tableInputMeta.getRowLimit();
//            int x = stepMeta.getLocation().x;
//            int y = stepMeta.getLocation().y;
//            Document doc = mxDomUtils.createDocument();
//            Element node = doc.createElement("data");
//            Map<String, Object> formAttributes = new HashMap<>(0);
//            formAttributes.put("datasource", datasource.getId());
//            formAttributes.put("sql", sql);
//            formAttributes.put("lazyConversionActive", lazyConversionActive);
//            formAttributes.put("executeEachInputRow", executeEachInputRow);
//            formAttributes.put("variableReplacementActive", variableReplacementActive);
//            formAttributes.put("rowLimit", Integer.valueOf(rowLimit));
//            node.setAttribute("title", "表输入");
//            node.setAttribute("id", name);
//            node.setAttribute("form", objectMapper.writeValueAsString(formAttributes));
//            graph.insertVertex(graph.getDefaultParent(), null, node, x, y, 45, 45, "TableInputMeta");
//        } else {
//            next.parse(branchId, stepMeta, graph);
//        }
//    }
//
//    @Override
//    public void parse(TransHopMeta transHopMeta, mxGraph graph) throws JsonProcessingException {
//        next.parse(transHopMeta, graph);
//    }

    @Override
    public void callback(TransMeta transMeta, Map<String, String> idNameMapping) {

    }
}
