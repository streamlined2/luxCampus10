package org.training.campus.fileanalyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileAnalyzerTest {

	private final static String WORD = "word";
	private static final String SAMPLE = """
			1 word! 2 word? 3 word. 4 word ! 5 word ?
			6 word.	7 word 8 word 9 word 10 word? 11 word! 12 word.
			13 word 14? word 15 word
			16 .word?
			17 .word! 18 .word. 19 .word! 20 .word
			""";

	@Test
	void testCountWords() {
		try {
			assertEquals(20, FileAnalyzer.countWords(WORD, new ByteArrayInputStream(SAMPLE.getBytes())));
		} catch (IOException e) {
			fail("unexpected io exception in test");
		}
	}

	@Test
	void testFindSentences() {
		final Object[] expected = new String[] { "1 word!", "2 word?", "3 word.", "4 word !", "5 word ?", "6 word.",
				"7 word 8 word 9 word 10 word?", "11 word!", "12 word.", "13 word 14?", "word?", "word!", "word.",
				"word!" };
		try {
			final Object[] result = FileAnalyzer.findSentences(WORD, new ByteArrayInputStream(SAMPLE.getBytes()))
					.toArray();
			assertArrayEquals(expected, result);
		} catch (IOException e) {
			fail("unexpected io exception in test");
		}
	}

}
