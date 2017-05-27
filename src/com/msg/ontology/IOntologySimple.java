package com.msg.ontology;

import org.apache.jena.ontology.OntModel;

public interface IOntologySimple
{
	/**
	 * 初始化OntModel 
	 * @return 初始化后的OntModel
	 */
	public OntModel initOntModel();

	/**
	 * 将OntModel 重新写入源文件中
	 */
	public void write2file();

	/**
	 * 关闭OntModel对象
	 */
	public void closeModel();
}
