<#macro type datatype><#if datatype=="string">String<#elseif datatype=="int">Integer<#elseif datatype=="double">Double<#else>Long</#if></#macro>
package ${package};

import ${entity_package}.${table.code};
import java.util.List;

public interface ${table.code}Dao{

    int insert(${table.code} entity);

    List<${table.code}> selectAll(${table.code} where);

<#if (table.keys?size > 0)>
    ${table.code} selectById(<#list table.keys as column><@type datatype=column.datatype /> ${column.code}<#if column_has_next>, </#if></#list>);

    int deleteById(<#list table.keys as column><@type datatype=column.datatype /> ${column.code}<#if column_has_next>, </#if></#list>);

<#if (table.colsExceptKey?size > 0)>
    int updateById(${table.code} entity);
	
    int dynamicUpdateById(${table.code} entity);
</#if>
</#if>
}
