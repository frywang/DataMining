package com.msg.owl;

public class OwlClassDict
{

	private String name; // 概念名
	private String id; // ID
	private String synname; // 同义词
	private String nickname; // 昵称
	private String imp; // 重要等级
	private String altername; // 指代：几乎没有用处

	public String getName()
	{
		return name;
	}

	public void setName(String cls)
	{
		this.name = cls;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSynname()
	{
		return synname;
	}

	public void setSynname(String synname)
	{
		this.synname = synname;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getImp()
	{
		return imp;
	}

	public void setImp(String imp)
	{
		this.imp = imp;
	}

	public void setAltername(String altername)
	{
		this.altername = altername;
	}

	public String getAltername()
	{
		return altername;
	}

	@Override
	public String toString()
	{
		return name + ": [id=" + id + ", synname=" + synname + ", nickname=" + nickname + ", imp=" + imp + "]";
	}
}
