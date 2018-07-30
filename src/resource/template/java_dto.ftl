<#macro type datatype><#if datatype=="string">String<#elseif datatype=="int">Integer<#elseif datatype=="double">Double<#else>Long</#if></#macro>
package ${package};

import com.dl.server.dto.BaseDTO;

public class ${table.code}DTO extends BaseDTO {

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
