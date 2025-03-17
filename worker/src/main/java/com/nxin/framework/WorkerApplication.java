package com.nxin.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.pentaho.big.data.kettle.plugins.kafka.KafkaConsumerInputMeta;
import org.pentaho.big.data.kettle.plugins.kafka.KafkaProducerOutputMeta;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.steps.concatfields.ConcatFieldsMeta;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.pentaho.di.trans.steps.excelwriter.ExcelWriterStepMeta;
import org.pentaho.di.trans.steps.jsoninput.JsonInputMeta;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;

@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@MapperScan("com.nxin.framework.mapper")
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class WorkerApplication {

    public static void main(String[] args) throws KettleException {
        // StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("classpath:plugins", false, true));
        KettleEnvironment.init();
        PluginRegistry.addPluginType(StepPluginType.getInstance());
        PluginRegistry.init();
        if (!Props.isInitialized()) {
            Props.init(0);
        }
        StepPluginType.getInstance().handlePluginAnnotation(
                ConcatFieldsMeta.class,
                ConcatFieldsMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
        StepPluginType.getInstance().handlePluginAnnotation(
                JsonInputMeta.class,
                JsonInputMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
        StepPluginType.getInstance().handlePluginAnnotation(
                KafkaConsumerInputMeta.class,
                KafkaConsumerInputMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
        StepPluginType.getInstance().handlePluginAnnotation(
                KafkaProducerOutputMeta.class,
                KafkaProducerOutputMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
        StepPluginType.getInstance().handlePluginAnnotation(
                ExcelWriterStepMeta.class,
                ExcelWriterStepMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
        StepPluginType.getInstance().handlePluginAnnotation(
                ExcelInputMeta.class,
                ExcelInputMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
                Collections.emptyList(), false, null);
//        StepPluginType.getInstance().handlePluginAnnotation(
//                JmsProducerMeta.class,
//                JmsProducerMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
//                Collections.emptyList(), true, null);
//        StepPluginType.getInstance().handlePluginAnnotation(
//                JmsConsumerMeta.class,
//                JmsConsumerMeta.class.getAnnotation(org.pentaho.di.core.annotations.Step.class),
//                Collections.emptyList(), true, null);
        SpringApplication.run(WorkerApplication.class, args);
    }

}
