package ${package};

import java.util.List;
import ${dto_package}.${table.code}DTO;

public interface ${table.code}Service{

	public ResultDTO<${table.code}DTO> query(${table.code}DTO request);
	
	public ResultDTO<${table.code}DTO> getById(String id);
	
    public ResultDTO<${table.code}DTO> save(${table.code}DTO dto);

	public ResultDTO<${table.code}DTO> delete(String id);
}
