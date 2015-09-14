package com.iconic.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* 
 ---------- Q2 ----------

 ----(2a)
 An n-gram is a contiguous sequence of n words from a given sequence of text.

 For the text file provided (resources/corpus.txt), for each value of n (where 1<=n<=3), write a function which outputs a list of all 
 n-grams	and their frequency, sorted by frequency. You can assume a word is any token (including punctuation) surrounded by whitespace. 

 [hint: java StringTokenizer or String.split()]

 The output should be in the format "{n-gram>}\t{count}". Sample output and values from the corpus.txt are shown below:

 == Sample output ==

 (for n=1)
 the	108
 is	35		
 we	18

 (for n=2)
 Madam President	15
 I would	11
 on the	8

 (for n=3)
 I would like	6
 the principle of	3
 =====================

 ----(2b) 
 Extend the previous function to display and sort all unigrams (a unigram is an n-gram where n = 1 i.e. a single word or token) 
 by their RELATIVE frequency (this is: number of occurrences of a word / total number of words).

 e.g. "the" occurs 108 times and there are 2108 words overall so the relative frequency of "the" = 108/2108 = 0.051 (to 3 decimal places) 

 */

public class Q2 {


	public static void main(String[] args) throws IOException {
		System.out.println("(for n=1)");
		unigram();
		System.out.println("\n(for n=2)");
		bigram();
		System.out.println("\n(for n=3)");
		trigram();
		System.out.println("\n(for Q2b)");
		unigramEx();
	}
	
	public static void unigram() throws IOException {
		Result res = ngrams(1);
		res.print(false);
	}
	
	public static void bigram() throws IOException {
		Result res = ngrams(2);
		res.print(false);
	}
	
	public static void trigram() throws IOException {
		Result res = ngrams(3);
		res.print(false);
	}
	
	public static void unigramEx() throws IOException {
		Result res = ngrams(1);
		res.print(true);
	}

	private static class Result {
		
		private ConcurrentHashMap<String, AtomicInteger> wordFreq = new ConcurrentHashMap<String, AtomicInteger>();
		
		public ConcurrentHashMap<String, AtomicInteger> getWordFreq() {
			return wordFreq;
		}
		
		public void addCount(String ngram, int times) {
			if (!wordFreq.containsKey(ngram)) {
				wordFreq.putIfAbsent(ngram, new AtomicInteger(0));
			}
			wordFreq.get(ngram).addAndGet(times);
		}
		
		public int getTotalWords() {
			int sum = 0;
			for (AtomicInteger v : getWordFreq().values()) {
				sum += v.get();
			}
			return sum;
		}
		
		public void print(boolean rel) {
			TreeMap<Integer, List<String>> sorted = sort();
			for (Entry<Integer, List<String>> e : sorted.entrySet()) {
				int count = e.getKey();
				if (rel) {
					double total = getTotalWords();
					for (String i : e.getValue()) {
						System.out.printf("%s\t%.3f%n", i, count / total);
					}
				} else {
					for (String i : e.getValue()) {
						System.out.println(i + "\t" + count);
					}
				}
			}
		}
		
		private TreeMap<Integer, List<String>> sort() {
			TreeMap<Integer, List<String>> sorted = new TreeMap<Integer, List<String>>(
					Collections.reverseOrder());
			for (Entry<String, AtomicInteger> e : wordFreq.entrySet()) {
				int count = e.getValue().get();
				String ngram = e.getKey();
				List<String> list = sorted.get(count);
				if (list == null) {
					list = new LinkedList<String>();
					sorted.put(count, list);
				}
				list.add(ngram);
			}
			return sorted;
		}
		
	}
	
	public static Result ngrams(final int n) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"src/main/resources/corpus.txt"));
		final Result res = new Result();
		
		class ProcSentence implements Runnable {
			private String sent;
			
			ProcSentence(String sent) {
				this.sent = sent;
			}
			
			@Override
			public void run() {
				String[] toks = sent.trim().split("\\s{1,}");
				for (int i = 0; i < toks.length - (n - 1); i++) {
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < n; j++) {
						sb.append(toks[i + j]);
						sb.append(' ');
					}
					String ngram = sb.toString().trim();
					res.addCount(ngram, 1);
				}
			}
			
		}
		
		ExecutorService exec = Executors.newFixedThreadPool(4);
		try {
			String sent = null;
			while ((sent = br.readLine()) != null) {
				exec.execute(new ProcSentence(sent));
			}
		} finally {
			br.close();
		}
		exec.shutdown();
		try {
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return res;
	}
}
