import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class Test {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/nxin_etl_2024_001_mybatis?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&failOverReadOnly=false&maxReconnects=10", "root", "1qaz@WSX")
                .globalConfig(builder -> {
                    builder.author("jesse han") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("D:/coding/"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.nxin.framework.kettle") // 设置父包名
                            .moduleName("kettle") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:/coding/xml/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("basic_dictionary", "basic_dictionary_item") // 设置需要生成的表名
                            .addTablePrefix("basic_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
