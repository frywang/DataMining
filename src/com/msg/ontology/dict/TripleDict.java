package com.msg.ontology.dict;

public class TripleDict
{
	private String subject;		//subject
	private String predicate;	//predicate
	private String object;		//object
	private boolean flag;		//true or false
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	public String getPredicate()
	{
		return predicate;
	}
	public void setPredicate(String predicate)
	{
		this.predicate = predicate;
	}
	public String getObject()
	{
		return object;
	}
	public void setObject(String object)
	{
		this.object = object;
	}
	public boolean isFlag()
	{
		return flag;
	}
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	@Override
	public String toString()
	{
		return "subject=" + subject + ", predicate=" + predicate + ", object=" + object + ",flag=" + flag;
	}
	
}
