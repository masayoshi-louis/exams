package com.iconic.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*

---------- Q3 ----------

Using corpus.txt as input again, write a program that takes a string (word) as an argument and returns 2 values: 
a) the word (string) it occurs most frequently BEFORE. 
b) the word (string) in occurs most frequently AFTER.

For example:
input string: "the"
returns: "of" and "course"

[hint: consider creating a class]

*/

public class Q3 {

	
	public static void main(String[] args) {
		String arg = "you";
		
		Map<String, Argument> wordsMap;
		wordsMap = initFile("src//main//resources//corpus.txt");
		
		if (wordsMap.containsKey(arg)) {
			// The occur most frequently word may have several, because they may have the same frequency, but I only return one in result.
			
			// I'm not quite understand the meaning of question (b), is it the same as question (a)?
			// So I return the occurs most frequently AFTER of argument.
			String after = wordsMap.get(arg.toLowerCase()).findMostAfter();
			String before = wordsMap.get(arg.toLowerCase()).findMostBefore();
			
			System.out.println("input string: " + arg);
			System.out.println("occurs most frequently BEFORE: " + before);
			System.out.println("occurs most frequently AFTER: " + after);
		} else {
			System.out.println("Does not contain the word: " + arg);
		}
	}

	private static Map<String, Argument> initFile(String path) {
		Map<String, Argument> wordsMap = new HashMap<String, Argument>();
		try {
			FileReader reader = new FileReader(new File(path));
			BufferedReader br = new BufferedReader(reader);
			
			String line;
			while ((line = br.readLine()) != null) {
				// I see the sentences in corpus.txt are already been tokenized, so I split the sentence by space.
				String[] words = line.toLowerCase().split(" ");
				
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					String before = null, after = null;
					if (i != 0) {
						before = words[i - 1];
					}
					if (i != words.length - 1) {
						after = words[i + 1];
					}
					if (wordsMap.containsKey(word)) {
						wordsMap.get(word).update(after, before);
					} else {
						Argument arg = new Argument(word, after, before);
						wordsMap.put(word, arg);
					}
				}
				
			}
			br.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordsMap;
	}
	
}

class Argument {
	private String word;
	private Map<String, Integer> before, after;

	public Argument(String word, String afterw, String beforew) {
		this.word = word;
		before = new HashMap<String, Integer>();
		if (beforew != null) {
			before.put(beforew, 1);
		}
		after = new HashMap<String, Integer>();
		if (afterw != null) {
			after.put(afterw, 1);
		}
	}
	public void update(String afterw, String beforew) {
		if (afterw != null) {
			if (after.containsKey(afterw)) {
				int count = after.get(afterw) + 1;
				after.put(afterw, count);
			} else {
				after.put(afterw, 1);
			}
		}
		
		if (beforew != null) {
			if (before.containsKey(beforew)) {
				int count = before.get(beforew) + 1;
				before.put(beforew, count);
			} else {
				before.put(beforew, 1);
			}
		}
	}
	public String findMostAfter() {
		String w = null;
		w = findWord(after);
		return w;
	}
	public String findMostBefore() {
		String w = null;
		w = findWord(before);
		return w;
	}
	private String findWord(Map<String, Integer> map) {
		String w = null;
		int count = -1;
		for (Entry<String, Integer> ent : map.entrySet()) {
			int val = ent.getValue();
			if (val > count) {
				w = ent.getKey();
				count = val;
			}
		}
		return w;
	}
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Map<String, Integer> getBefore() {
		return before;
	}

	public void setBefore(Map<String, Integer> before) {
		this.before = before;
	}

	public Map<String, Integer> getAfter() {
		return after;
	}

	public void setAfter(Map<String, Integer> after) {
		this.after = after;
	}
}