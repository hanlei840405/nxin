CREATE TABLE IF NOT EXISTS `${code}` (
<#if columns??>
    `id` bigint NOT NULL AUTO_INCREMENT,
    <#list columns as column>
    `${column.columnCode}` ${column.columnCategory}<#if column.columnLength??>(${column.columnLength}<#if column.columnDecimal??>,${column.columnDecimal}</#if>)</#if><#if column.columnNotNull> NOT NULL</#if> COMMENT '${column.columnName}',
    </#list>
    PRIMARY KEY (`id`)
</#if>
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='${name}';

<#if columns??>
    <#list columns as column>
ALTER TABLE `${code}` ADD COLUMN `${column.columnCode}` ${column.columnCategory}<#if column.columnLength??>(${column.columnLength}<#if column.columnDecimal??>,${column.columnDecimal}</#if>)</#if><#if column.columnNotNull> NOT NULL</#if> COMMENT '${column.columnName}';
    </#list>
</#if>