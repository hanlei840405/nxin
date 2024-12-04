package com.nxin.framework.converter.kettle.transform;

import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.transform.connect.MergeJoinChain;
import com.nxin.framework.converter.kettle.transform.convert.*;
import com.nxin.framework.converter.kettle.transform.input.*;
import com.nxin.framework.converter.kettle.transform.lookup.DatabaseJoinChain;
import com.nxin.framework.converter.kettle.transform.lookup.DatabaseLookupChain;
import com.nxin.framework.converter.kettle.transform.connect.MultiMergeJoinChain;
import com.nxin.framework.converter.kettle.transform.lookup.RestChain;
import com.nxin.framework.converter.kettle.transform.output.*;
import com.nxin.framework.converter.kettle.transform.process.*;
import com.nxin.framework.converter.kettle.transform.shell.*;
import com.nxin.framework.converter.kettle.transform.streaming.*;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.kettle.ShellService;

import java.util.HashSet;
import java.util.Set;

public class TransformConvertFactory extends ConvertFactory {
    private static ThreadLocal<Set<TransformConvertChain>> transformConvertChains = ThreadLocal.withInitial(() -> new HashSet<>(0));

    private static TransformConvertChain beginChain;

    public static Set<TransformConvertChain> getTransformConvertChains() {
        return transformConvertChains.get();
    }

    public static void destroyTransformConvertChains() {
        transformConvertChains.remove();
    }

    public static void init(DatasourceService datasourceService, ShellService shellService) {
        TransformConvertChain beginChain = new BeginChain();
        TransformConvertChain tableInputChain = new TableInputChain();
        TransformConvertChain tableOutputChain = new TableOutputChain();
        TransformConvertChain tableInsertUpdateChain = new TableInsertUpdateChain();
        TransformConvertChain tableUpdateChain = new TableUpdateChain();
        TransformConvertChain tableDeleteChain = new TableDeleteChain();
        TransformConvertChain databaseLookupChain = new DatabaseLookupChain();
        TransformConvertChain databaseJoinChain = new DatabaseJoinChain();
        TransformConvertChain execSqlChain = new ExecSqlChain();
        TransformConvertChain javaScriptChain = new JavaScriptChain();
        TransformConvertChain dummyChain = new DummyChain();
        TransformConvertChain detectEmptyStreamChain = new DetectEmptyStreamChain();
        TransformConvertChain detectLastRowChain = new DetectLastRowChain();
        TransformConvertChain mailChain = new MailChain();
        TransformConvertChain writeToLogChain = new WriteToLogChain();
        TransformConvertChain dataGridChain = new DataGridChain();
        TransformConvertChain transHopChain = new TransHopChain();
        TransformConvertChain getVariableChain = new GetVariableChain();
        TransformConvertChain setVariableChain = new SetVariableChain();
        TransformConvertChain switchCaseChain = new SwitchCaseChain();
        TransformConvertChain constantChain = new ConstantChain();
        TransformConvertChain denormaliserChain = new DenormaliserChain();
        TransformConvertChain fieldSplitterChain = new FieldSplitterChain();
        TransformConvertChain flattenerChain = new FlattenerChain();
        TransformConvertChain normaliserChain = new NormaliserChain();
        TransformConvertChain replaceStringChain = new ReplaceStringChain();
        TransformConvertChain selectValuesChain = new SelectValuesChain();
        TransformConvertChain splitFieldToRowsChain = new SplitFieldToRowsChain();
        TransformConvertChain stringCutChain = new StringCutChain();
        TransformConvertChain uniqueRowsChain = new UniqueRowsChain();
        TransformConvertChain uniqueRowsByHashSetChain = new UniqueRowsByHashSetChain();
        TransformConvertChain valueMapperChain = new ValueMapperChain();
        TransformConvertChain jsonInputChain = new JsonInputChain();
        TransformConvertChain jsonOutputChain = new JsonOutputChain();
        TransformConvertChain rowGeneratorChain = new RowGeneratorChain();
        TransformConvertChain userDefinedJavaClassChain = new UserDefinedJavaClassChain();
        TransformConvertChain excelWriterChain = new ExcelWriterChain();
        TransformConvertChain elasticSearchBulkChain = new ElasticSearchBulkChain();
        TransformConvertChain randomValueChain = new RandomValueChain();
        TransformConvertChain concatFieldsChain = new ConcatFieldsChain();
        TransformConvertChain recordsFromStreamChain = new RecordsFromStreamChain();
        TransformConvertChain kafkaProducerOutputChain = new KafkaProducerOutputChain();
        TransformConvertChain kafkaConsumerInputChain = new KafkaConsumerInputChain();
        TransformConvertChain jmsProducerOutputChain = new JmsProducerChain();
        TransformConvertChain jmsConsumerInputChain = new JmsConsumerChain();
        TransformConvertChain setValueFieldChain = new SetValueFieldChain();
        TransformConvertChain mongodbOutputChain = new MongodbOutputChain();
        TransformConvertChain rowsToResultChain = new RowsToResultChain();
        TransformConvertChain rowsFromResultChain = new RowsFromResultChain();
        TransformConvertChain restChain = new RestChain();
        TransformConvertChain multiMergeJoinChain = new MultiMergeJoinChain();
        TransformConvertChain sortRowsChain = new SortRowsChain();
        TransformConvertChain mergeJoinChain = new MergeJoinChain();
        TransformConvertChain filterRowsChain = new FilterRowsChain();
        TransformConvertChain endChain = new EndChain();
        tableInputChain.setDatasourceService(datasourceService);
        tableOutputChain.setDatasourceService(datasourceService);
        tableInsertUpdateChain.setDatasourceService(datasourceService);
        tableUpdateChain.setDatasourceService(datasourceService);
        tableDeleteChain.setDatasourceService(datasourceService);
        databaseLookupChain.setDatasourceService(datasourceService);
        databaseJoinChain.setDatasourceService(datasourceService);
        execSqlChain.setDatasourceService(datasourceService);
        kafkaConsumerInputChain.setShellService(shellService);
        jmsConsumerInputChain.setShellService(shellService);
        beginChain.setNext(tableInputChain);
        tableInputChain.setNext(tableOutputChain);
        tableOutputChain.setNext(tableInsertUpdateChain);
        tableInsertUpdateChain.setNext(tableUpdateChain);
        tableUpdateChain.setNext(tableDeleteChain);
        tableDeleteChain.setNext(databaseLookupChain);
        databaseLookupChain.setNext(databaseJoinChain);
        databaseJoinChain.setNext(execSqlChain);
        execSqlChain.setNext(javaScriptChain);
        javaScriptChain.setNext(dummyChain);
        dummyChain.setNext(detectEmptyStreamChain);
        detectEmptyStreamChain.setNext(detectLastRowChain);
        detectLastRowChain.setNext(mailChain);
        mailChain.setNext(writeToLogChain);
        writeToLogChain.setNext(dataGridChain);
        dataGridChain.setNext(getVariableChain);
        getVariableChain.setNext(setVariableChain);
        setVariableChain.setNext(switchCaseChain);
        switchCaseChain.setNext(constantChain);
        constantChain.setNext(denormaliserChain);
        denormaliserChain.setNext(fieldSplitterChain);
        fieldSplitterChain.setNext(flattenerChain);
        flattenerChain.setNext(normaliserChain);
        normaliserChain.setNext(replaceStringChain);
        replaceStringChain.setNext(selectValuesChain);
        selectValuesChain.setNext(splitFieldToRowsChain);
        splitFieldToRowsChain.setNext(stringCutChain);
        stringCutChain.setNext(uniqueRowsChain);
        uniqueRowsChain.setNext(uniqueRowsByHashSetChain);
        uniqueRowsByHashSetChain.setNext(valueMapperChain);
        valueMapperChain.setNext(jsonInputChain);
        jsonInputChain.setNext(jsonOutputChain);
        jsonOutputChain.setNext(rowGeneratorChain);
        rowGeneratorChain.setNext(userDefinedJavaClassChain);
        userDefinedJavaClassChain.setNext(excelWriterChain);
        excelWriterChain.setNext(elasticSearchBulkChain);
        elasticSearchBulkChain.setNext(concatFieldsChain);
        concatFieldsChain.setNext(recordsFromStreamChain);
        recordsFromStreamChain.setNext(kafkaProducerOutputChain);
        kafkaProducerOutputChain.setNext(kafkaConsumerInputChain);
        kafkaConsumerInputChain.setNext(jmsProducerOutputChain);
        jmsProducerOutputChain.setNext(jmsConsumerInputChain);
        jmsConsumerInputChain.setNext(randomValueChain);
        randomValueChain.setNext(setValueFieldChain);
        setValueFieldChain.setNext(mongodbOutputChain);
        mongodbOutputChain.setNext(rowsToResultChain);
        rowsToResultChain.setNext(rowsFromResultChain);
        rowsFromResultChain.setNext(restChain);
        restChain.setNext(multiMergeJoinChain);
        multiMergeJoinChain.setNext(sortRowsChain);
        sortRowsChain.setNext(transHopChain);
        transHopChain.setNext(mergeJoinChain);
        mergeJoinChain.setNext(filterRowsChain);
        filterRowsChain.setNext(endChain);
        TransformConvertFactory.beginChain = beginChain;
    }

    public static TransformConvertChain getInstance() {
        return beginChain;
    }
}
