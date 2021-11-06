package org.training.campus.filemanager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class FileManagerTest {

	private static final String TEST_FILE_CONTENT = "TEST FILE CONTENT";
	private static final String TEST_FILE_NAME = "_file";
	private static final String TEST_FOLDER_NAME = "_folder";
	private static final String INITIAL_TEST_FOLDER = "_fsTest";
	private static final String COPY_TEST_FOLDER = "_fsCopy";

	private static void createTestFile(File folder, String path) throws IOException {
		File file = new File(folder, path);
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
			os.write(TEST_FILE_CONTENT.getBytes());
		}
	}

	private static boolean checkTestFile(File folder, String path) throws IOException {
		File file = new File(folder, path);
		if(file.exists()) {
			try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
				byte[] content = is.readAllBytes();
				return Arrays.equals(TEST_FILE_CONTENT.getBytes(), content);
			}
		}
		return false;
	}

	private static void buidTestData(String path) throws IOException {
		File root = new File(path);
		root.mkdir();
		createTestFile(root, TEST_FILE_NAME);
		File a = new File(root, TEST_FOLDER_NAME + "1");
		a.mkdir();
		createTestFile(a, TEST_FILE_NAME + "1");
		File b = new File(root, TEST_FOLDER_NAME + "2");
		b.mkdir();
		createTestFile(b, TEST_FILE_NAME + "2");
		File c = new File(root, TEST_FOLDER_NAME + "3");
		c.mkdir();
		createTestFile(c, TEST_FILE_NAME + "3");
	}

	private static boolean checkTestData(String path) throws IOException {
		boolean ok = true;
		File root = new File(path);
		ok = ok && root.exists() && root.isDirectory();
		ok = ok && checkTestFile(root,TEST_FILE_NAME);
		File a = new File(root, TEST_FOLDER_NAME + "1");
		ok = ok && a.exists() && a.isDirectory();
		ok = ok && checkTestFile(a,TEST_FILE_NAME + "1");
		File b = new File(root, TEST_FOLDER_NAME + "2");
		ok = ok && b.exists() && b.isDirectory();
		ok = ok && checkTestFile(b,TEST_FILE_NAME + "2");
		File c = new File(root, TEST_FOLDER_NAME + "3");
		ok = ok && c.exists() && c.isDirectory();
		ok = ok && checkTestFile(c,TEST_FILE_NAME + "3");
		return ok;
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		buidTestData(INITIAL_TEST_FOLDER);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		FileManager.remove(INITIAL_TEST_FOLDER);
	}
	
	@Test
	@Order(0)
	@DisplayName("testing if data prepared correctly")
	void testDataPrepared() throws IOException {
		assertTrue(checkTestData(INITIAL_TEST_FOLDER));
	}

	@Test
	@Order(1)
	@DisplayName("testing if file count is correct")
	void testCountFiles() {
		assertEquals(4,FileManager.countFiles(INITIAL_TEST_FOLDER));
	}

	@Test
	@Order(2)
	@DisplayName("testing if folder count is correct")
	void testCountDirs() {
		assertEquals(3,FileManager.countDirs(INITIAL_TEST_FOLDER));
	}

	@Test
	@Order(3)
	@DisplayName("testing if copy operation succeeded")
	void testCopy() throws IOException {
		File copy = new File(COPY_TEST_FOLDER);
		copy.mkdir();
		assertTrue(copy.exists());
		FileManager.copy(INITIAL_TEST_FOLDER,COPY_TEST_FOLDER);
		assertTrue(checkTestData(COPY_TEST_FOLDER+File.separator+INITIAL_TEST_FOLDER));
		FileManager.remove(COPY_TEST_FOLDER);
		assertFalse(copy.exists());
	}

	@Test
	@Order(4)
	@DisplayName("testing if move operation succeeded")
	void testMove() throws IOException {
		File copy = new File(COPY_TEST_FOLDER);
		copy.mkdir();
		assertTrue(copy.exists());
		FileManager.move(INITIAL_TEST_FOLDER,COPY_TEST_FOLDER);
		assertTrue(checkTestData(COPY_TEST_FOLDER+File.separator+INITIAL_TEST_FOLDER));
		assertFalse(new File(INITIAL_TEST_FOLDER).exists());
	}

	@Test
	@Order(Integer.MAX_VALUE)
	@DisplayName("testing if remove operation succeeded")
	void testRemove() throws IOException {
		FileManager.remove(COPY_TEST_FOLDER);
		assertFalse(new File(COPY_TEST_FOLDER).exists());
	}

}
