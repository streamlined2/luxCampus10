package org.training.campus.fileanalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAnalyzer {

	private static String getFileContent(InputStream is) throws IOException {
		return new String(is.readAllBytes());
	}

	public static int countWords(String word, InputStream is) throws IOException {
		return countWords(word, getFileContent(is));
	}

	public static int countWords(String word, String data) {
		Pattern pattern = Pattern.compile(word);
		Matcher matcher = pattern.matcher(data);
		int count = 0;
		int start = 0;
		while (matcher.find(start)) {
			count++;
			start = matcher.end();
		}
		return count;
	}

	public static List<String> findSentences(String word, InputStream is) throws IOException {
		return findSentences(word, getFileContent(is));
	}

	public static List<String> findSentences(String word, String data) {
		var list = new LinkedList<String>();
		Pattern pattern = Pattern.compile(String.format("[^.?!]*?%s.*?[.?!]{1}", word));
		Matcher matcher = pattern.matcher(data);
		int start = 0;
		while (matcher.find(start)) {
			list.add(matcher.group().trim());
			start = matcher.end();
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("please pass full file name and word to seek for as parameters");
		}
		final File source = new File(args[0]);
		if (!source.exists()) {
			throw new FileNotFoundException("can't find source file");
		}
		final String word = args[1];

		try (FileInputStream fis = new FileInputStream(source)) {
			byte[] data = fis.readAllBytes();
			String strData = new String(data);
			System.out.printf("Word '%s' encountered %d time(s) in file %s%n", word, countWords(word, strData),
					source.getCanonicalPath());
			System.out.printf("Sentences that include word '%s' are:%n", word);
			findSentences(word, strData).forEach(sentence -> System.out.println(sentence));
		}

	}

}
