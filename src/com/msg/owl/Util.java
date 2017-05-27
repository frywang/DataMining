package com.msg.owl;

import java.util.List;

import com.msg.file.FileIO;

public class Util
{
	public static void findDiff(String filename, String filepath)
	{
		List<String> words = FileIO.readLines(filename);
		OwlBase owl = new OwlBase(filepath);
		for (String word : words) {
			// System.out.println(word);
			String[] allWord = word.split("##");

			String word1 = allWord[1].replaceAll("name=", "");
			String word2 = allWord[2].replaceAll("synName=", "");
			String word3 = allWord[3].replaceAll("nickName=", "");

			List<OwlClassDict> dicts = owl.getClassDicts("生物");

			for (OwlClassDict dict : dicts) {
				String name = dict.getName();
				String synname = dict.getSynname();
				String nickname = dict.getNickname();

				if (name.equals(word1)) {
					String[] synnames = synname.split(" ");
					if (word2.contains("_")) {
						String[] words2 = word2.split("_");
						for (String w2 : words2) {
							for (int i = 0; i < synnames.length; i++) {
								if (synnames[i].equals(w2)) {
									synnames[i] = "";
								}
							}
						}
					} else {
						for (int i = 0; i < synnames.length; i++) {
							if (synnames[i].equals(word2)) {
								synnames[i] = "";
							}
						}
					}

					for (int i = 0; i < synnames.length; i++) {
						if (!synnames[i].equals("")) {
							System.out.print(synnames[i] + "\n");
						}
					}

					String[] nicknames = nickname.split(" ");

					if (word3.contains("_")) {
						String[] words3 = word3.split("_");
						for (String w3 : words3) {
							for (int i = 0; i < nicknames.length; i++) {
								if (nicknames[i].equals(w3)) {
									nicknames[i] = "";
								}
							}
						}
					} else {
						for (int i = 0; i < nicknames.length; i++) {
							if (nicknames[i].equals(word3)) {
								nicknames[i] = "";
							}
						}
					}

					for (int i = 0; i < nicknames.length; i++) {
						if (!nicknames[i].equals("")) {
							System.out.print(nicknames[i] + "\n");
						}
					}
					// System.out.println("");
				}
			}

		}
		owl.closeModel();
	}
}
