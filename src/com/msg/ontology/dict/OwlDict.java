package com.msg.ontology.dict;

public class OwlDict
{
	private String name; 			// 概念名
	private String id; 				// ID
	private String path; 			// 路径
	private String parentname; 		// 父类
	private String synname; 		// 同义词
	private String nickname; 		// 昵称
	private String imp; 			// 重要等级
	private String altername; 		// 指代：目前几乎没有用到
	private String level; 			// 层级
	private String parent_id;		//父类id

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

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getFathername()
	{
		return parentname;
	}

	public void setParentname(String parentname)
	{
		this.parentname = parentname;
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

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getParent_id()
	{
		return parent_id;
	}

	public void setParent_id(String parent_id)
	{
		this.parent_id = parent_id;
	}

	public String getParentname()
	{
		return parentname;
	}

	@Override
	public String toString()
	{
		return toString(false);
	}
	
	public String toString(boolean bool)
	{
		if(bool){
			return "id=" + id + "##name=" + name + "##synName=" + synname
					+ "##nickName=" + nickname + "##imp=" + imp;
		}else{
			return "id=" + id + "##name=" + name + "##path=" + path + "##parent=" + parentname + "##parentId=" + parent_id + "##synName=" + synname
					+ "##nickName=" + nickname + "##level=" + level + "##imp=" + imp + "##alterName= ";
		}
	}
}
