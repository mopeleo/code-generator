package org.oversky.code.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.oversky.code.model.Column;
import org.oversky.code.model.Model;
import org.oversky.code.model.Table;

public class CDMParser {

    private static final String CODE_SEP = "_";
	private static final String ATTR_ID = "Id";
	private static final String ATTR_REF = "Ref";
    private static final String ELEMENT_NAME = "Name";
    private static final String ELEMENT_CODE = "Code";
    private static final String ELEMENT_COMMENT = "Comment";
    private static final String ELEMENT_DATATYPE = "DataType";
    private static final String ELEMENT_LENGTH = "Length";
    private static final String ELEMENT_PRECISION = "Precision";
    private static final String ELEMENT_DEFAULTVALUE = "DefaultValue";
    private static final String ELEMENT_COLUMNS = "Attributes";
    private static final String ELEMENT_DATAITEM = "DataItem";
    private static final String ELEMENT_IDENTIFIERS = "Identifiers";
    private static final String ELEMENT_PRIMARYIDENTIFIER = "PrimaryIdentifier";
    private static final String ELEMENT_IDENTIFIER = "Identifier";
    private static final String ELEMENT_IDENTIFIER_ATTR = "Identifier.Attributes";
    private static final String ELEMENT_ATTRIBUTE_MANDATORY = "BaseAttribute.Mandatory";
    private static final String ELEMENT_IDENTIFIER_ENTITY_ATTR = "EntityAttribute";
    private static final String NODE_ROOT = "//Model";
    private static final String NODE_TABLE = NODE_ROOT + "/o:RootObject/c:Children/o:Model/c:Entities/o:Entity";
    private static final String NODE_COLUMN = NODE_ROOT + "/o:RootObject/c:Children/o:Model/c:DataItems/o:DataItem";
    
	private CDMParser(){}
	
	public static Model parse(File cdm){
		Model model = new Model();
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(cdm);
			//直接跳到o:DataItem节点解析
			List colItems = document.selectNodes(NODE_COLUMN);
			if(colItems == null || colItems.size() == 0){
			    throw new NullPointerException("找不到列属性");
			}
			Map<String, Column> colMap = new HashMap<String, Column>();
			for(int i = 0; i < colItems.size(); i++){
			    Column column = new Column();
			    Element colItem = (Element)colItems.get(i);
 
			    Attribute attrId = colItem.attribute(ATTR_ID);
                column.setId(attrId.getValue());
                
                Element elementName = colItem.element(ELEMENT_NAME);
                column.setName(elementName.getTextTrim());

                Element elementCode = colItem.element(ELEMENT_CODE);
                String origincode = elementCode.getTextTrim().toLowerCase();
                column.setOriginCode(origincode);
                String code = "";
                if(origincode.indexOf(CODE_SEP) > 0){
                    String[] parts = origincode.split(CODE_SEP);
                    code = parts[0];
                    for(int p = 1; p < parts.length; p++){
                        code += parts[p].substring(0, 1).toUpperCase() + parts[p].substring(1);
                    }
                }else{
                    code = origincode;
                }
                column.setCode(code);
                System.out.println("column : id = " + column.getId() + ", name = " + column.getName() + ", code = " + column.getCode());

                Element elementCommont = colItem.element(ELEMENT_COMMENT);
                if(elementCommont == null){
                    column.setComment(elementName.getTextTrim());
                }else{
                    column.setComment(elementCommont.getTextTrim());
                }

                Element elementDataType = colItem.element(ELEMENT_DATATYPE);
                String origintype = elementDataType.getTextTrim().toUpperCase();
                column.setOriginDatatype(origintype);
                column.setDatatype(DataType.convertType(origintype, DataType.SourceType.CDM));

                Element elementLength = colItem.element(ELEMENT_LENGTH);
                if(elementLength == null){
                    column.setLength("");
                }else{
                    column.setLength(elementLength.getTextTrim());
                }

                Element elementPrecision = colItem.element(ELEMENT_PRECISION);
                if(elementPrecision == null){
                    column.setPrecision("");
                }else{
                    column.setPrecision(elementPrecision.getTextTrim());
                }

                Element elementDefaultValue = colItem.element(ELEMENT_DEFAULTVALUE);
                if(elementDefaultValue != null){
                    column.setDefaultValue(elementDefaultValue.getTextTrim());
                }
                
                colMap.put(column.getId(), column);
			}
			
			//直接跳到o:Entity节点解析
			List tables = document.selectNodes(NODE_TABLE);
			if(tables == null || tables.size() == 0){
                throw new NullPointerException("找不到表属性");
			}

			for(int i = 0; i < tables.size(); i++){
				Table table = new Table();
				Element elementTable=(Element)tables.get(i);
				
				Attribute attrId = elementTable.attribute(ATTR_ID);
				table.setId(attrId.getValue());
				
				Element elementName = elementTable.element(ELEMENT_NAME);
				table.setName(elementName.getTextTrim());

				Element elementCode = elementTable.element(ELEMENT_CODE);
				String origincode = elementCode.getTextTrim().toLowerCase();
				table.setOriginCode(origincode);
                String code = "";
                if(origincode.indexOf(CODE_SEP) > 0){
                    String[] parts = origincode.split(CODE_SEP);
                    table.setGroup(parts[0]);
                    for(int p = 0; p < parts.length; p++){
                        code += parts[p].substring(0, 1).toUpperCase() + parts[p].substring(1);
                    }
                }else{
                    code = origincode;
                }
                table.setCode(code);

				Element elementCommont = elementTable.element(ELEMENT_COMMENT);
				if(elementCommont == null){
					table.setComment(elementName.getTextTrim());
				}else{
					table.setComment(elementCommont.getTextTrim());
				}
                //解析主键节点
                Element primaryIndentifierNode = elementTable.element(ELEMENT_PRIMARYIDENTIFIER);
                List<String> keyList = new ArrayList<String>();
                if(primaryIndentifierNode != null){
                    Element primaryKeyRef = primaryIndentifierNode.element(ELEMENT_IDENTIFIER);
                    String refId = primaryKeyRef.attribute(ATTR_REF).getValue();
                    
                    //解析 Identifiers
                    Element identifiersNode = elementTable.element(ELEMENT_IDENTIFIERS);
                    if(identifiersNode != null){
                        List identifier = identifiersNode.elements(ELEMENT_IDENTIFIER);
                        for(int j = 0; j < identifier.size(); j++){
                            Element elementIdentifier = (Element)identifier.get(j);
                            String identifierId = elementIdentifier.attribute(ATTR_ID).getValue();
                            if(!refId.equals(identifierId)){
                                continue;
                            }
                            List idenAttributes = elementIdentifier.element(ELEMENT_IDENTIFIER_ATTR).elements(ELEMENT_IDENTIFIER_ENTITY_ATTR);
                            for(int k = 0; k < idenAttributes.size(); k++){
                                Element elementKey = (Element)idenAttributes.get(k);
                                String keyRef = elementKey.attribute(ATTR_REF).getValue();
                                keyList.add(keyRef);
                            }
                            break;
                        }
                    }                    
                }

				//解析c:Attributes节点 ,即table的column
				List columns = elementTable.element(ELEMENT_COLUMNS).elements();
				for(int j = 0; j < columns.size(); j++){
					Element elementColumn = (Element)columns.get(j);
					String id = elementColumn.attribute(ATTR_ID).getValue();
					String refId = elementColumn.element(ELEMENT_DATAITEM).element(ELEMENT_DATAITEM).attribute(ATTR_REF).getValue();
					Column refCol = (Column)colMap.get(refId);
                    Element mandatory = elementColumn.element(ELEMENT_ATTRIBUTE_MANDATORY);
                    if(mandatory != null){
                        String man = mandatory.getTextTrim();
                        refCol.setMandatory(man);
                    }else{
                        refCol.setMandatory("0");
                    }
					if(colMap.containsKey(refId)){
					    table.addColumn(refCol);
					}
					
					if(keyList.contains(id)){
					    table.addKey(refCol);
					}else{
					    table.addColsExceptKey(refCol);
					}
				}

				model.addTable(table);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return model;
	}	
}
