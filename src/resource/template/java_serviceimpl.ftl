package ${package};

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gurms.entity.PageRequest;
import org.gurms.entity.PageResult;
import org.gurms.entity.PropertyFilter;
import ${dto_package}.${table.code}DTO;
import ${entity_package}.${table.code};
import ${service_package}.${table.code}Service;
import ${dao_package}.${table.code}Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#assign dao = table.code?uncap_first + "Dao">
@Service
@Transactional
public class ${table.code}ServiceImpl implements ${table.code}Service {

	@Autowired
	private ${table.code}Dao ${dao};
	
	public ${table.code}Dao get${table.code}Dao() {
		return ${dao};
	}

	public void set${table.code}Dao(${table.code}Dao ${dao}) {
		this.${dao} = ${dao};
	}

	@Override
	@Transactional(readOnly = true)
	public List<${table.code}> query(Map<String, Object> request){
		return ${dao}.find(PropertyFilter.buildFromRequestMap(request));
	}
	
	@Override
	@Transactional(readOnly = true)
	public PageResult<${table.code}> query(Map<String, Object> request, PageRequest page){
		return ${dao}.findPage(page, PropertyFilter.buildFromRequestMap(request));
	}
	
	@Override
	@Transactional(readOnly = true)
	public ${table.code} get(String id){
		return ${dao}.get(id);
	}
	
	@Override
	public PageResult<${table.code}> save(${table.code} entity){
		PageResult<${table.code}> result = new PageResult<${table.code}>();
		${dao}.save(entity);
		return result;
	}

	@Override
	public PageResult<${table.code}> delete(String id){
		PageResult<${table.code}> result = new PageResult<${table.code}>();
		if(StringUtils.isBlank(id)){
			result.setSuccess(false);
			result.setReturnmsg("ID不能为空");
		}else{
			${dao}.delete(id);
		}
		return result;
	}
}
