package luxCampus10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAnalyzer {

	private static int countWords(String word, String data) {
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
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("please pass full file name and word to seek for as parameters");
		}
		File source = new File(args[0]);
		if (!source.exists()) {
			throw new FileNotFoundException("can't find source file");
		}
		final String word = args[1];
		try (FileInputStream fis = new FileInputStream(source)) {
			byte[] data = fis.readAllBytes();
			String strData = new String(data);
			System.out.printf("Word '%s' encountered %d time(s) in file %s%n", word, countWords(word, strData),
					source.getCanonicalPath());
		}
	}

}
