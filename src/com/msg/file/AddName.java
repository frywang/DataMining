package com.msg.file;

import java.util.ArrayList;
import java.util.List;



public class AddName {
	private String filePath = "plant";



	public int addName(){

		List<String> filePaths = FilePath.getFileList(filePath);
		//System.out.println(filePaths);
		String file = "combine.txt";
		List<String> allLines = new ArrayList<>();
		for(String path:filePaths){
			if(path.contains("txt")){
				String filepath = path.substring(0, path.lastIndexOf("/"));
				//System.out.println(filepath);

				String filename = filepath.substring(filepath.lastIndexOf("/") + 1);

				//System.out.println(filename);

				List<String> lines = FileIO.readLines(path);

				//System.out.println(lines);
				for(String line:lines){
					String allLine = filename +":"+ line;
					allLines.add(allLine);
				}
			}


		}
		FileIO.writeFile(allLines, file);
		System.out.println("已写入文件：" + file+"    中");
		return 0;
	}

}
