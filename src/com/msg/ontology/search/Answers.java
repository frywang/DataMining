package com.msg.ontology.search;

import java.util.HashMap;
import java.util.Map;

import com.msg.ontology.dict.TripleDict;

public class Answers
{
	
	public String getAnswerTemplate(String[] result, TripleDict td)
	{
		//System.out.println(td);
		String subject = td.getSubject();		//主语
		String predicate = td.getPredicate();	//谓语
		String object = td.getObject();			//宾语
		boolean flag = td.isFlag();				//判断对错
		if(null == predicate)
		{
			return td.getObject();
		}
		
		String type = result[2];
		Map<String,String> temp = new HashMap<String,String>();
		temp.put("特指", "###的@@@是&&&哦");
		temp.put("判断", "###是有@@@的，###的@@@是&&&哦");
		temp.put("宾语", "是的,###的@@@是&&&哦");
		String answer = null;
		if(flag){
			answer = temp.get(type).replaceAll("###", subject).replaceAll("@@@", predicate).replaceAll("&&&", object);
		}else{
			answer = temp.get(type).replaceAll("是", "不是").replaceAll("###", subject).replaceAll("@@@", predicate).replaceAll("&&&", object);
		}
		return answer;
	}

}
