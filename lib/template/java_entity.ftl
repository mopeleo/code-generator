<#macro type datatype><#if datatype=="string">String<#elseif datatype=="int">Integer<#elseif datatype=="double">Double<#else>Long</#if></#macro>
package ${package};

import com.dl.server.entity.DLEntity;

public class ${table.code} extends DLEntity{

<#list table.columns as column>
	private <@type datatype=column.datatype /> ${column.code};    //${column.comment}
</#list>

<#list table.columns as column>
	public <@type datatype=column.datatype /> get${column.code?cap_first}() {
		return this.${column.code};
	}

	public void set${column.code?cap_first}(<@type datatype=column.datatype /> ${column.code}) {
		this.${column.code} = ${column.code};
	}

</#list>
    public boolean existId(){
        return <#if (table.keys?size > 0)>true<#else>false</#if>;
    }
    
<#if (table.keys?size > 0)>
	public String getEntityKey(){
		StringBuilder build = new StringBuilder("${table.code}");
		return build<#list table.keys as column>.append(this.${column.code})</#list>.toString();
	}

    public static String buildEntityKey(<#list table.keys as column><@type datatype=column.datatype /> ${column.code}<#if column_has_next>, </#if></#list>){
        StringBuilder build = new StringBuilder("${table.code}");
        return build<#list table.keys as column>.append(${column.code})</#list>.toString();
    }
</#if>
	
	public void clear(){
<#list table.columns as column>
		this.${column.code} = null;
</#list>
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
<#list table.columns as column>
		sb.append(", ${column.code}=").append(${column.code});
</#list>
        sb.append("]");
        return sb.toString();
	}
}
