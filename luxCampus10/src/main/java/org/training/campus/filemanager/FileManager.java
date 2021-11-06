package org.training.campus.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

	private static final int BUFFER_SIZE = 10 * 1024;

	private static void checkIfExists(String path) {
		File file = new File(path);
		if (!file.exists())
			throw new IllegalArgumentException(String.format("%s doesn't exist", path));
	}
	
	private static void checkIfFolder(String path) {
		File file = new File(path);
		if (!file.isDirectory())
			throw new IllegalArgumentException(String.format("%s should be folder, not a file", path));
	}

	public static int countFiles(String path) {
		checkIfExists(path);
		checkIfFolder(path);
		return countFiles(new File(path));
	}

	private static int countFiles(File file) {
		int count = 0;
		for (File nestedFile : file.listFiles()) {
			if (nestedFile.isDirectory()) {
				count += countFiles(nestedFile);
			} else {
				count++;
			}
		}
		return count;
	}

	public static int countDirs(String path) {
		checkIfExists(path);
		checkIfFolder(path);
		return countDirs(new File(path));
	}

	private static int countDirs(File file) {
		int count = 0;
		for (File nestedFile : file.listFiles()) {
			if (nestedFile.isDirectory()) {
				count += countDirs(nestedFile) + 1;
			}
		}
		return count;
	}

	public static void copy(String from, String to) throws IOException {
		checkIfExists(from);
		checkIfExists(to);
		checkIfFolder(to);
		copy(new File(from), new File(to));
	}

	private static void copy(File source, File destDir) throws IOException {
		if (source.isDirectory()) {
			File targetDir = new File(destDir, source.getName());
			if (targetDir.mkdir()) {
				for (File nestedFile : source.listFiles()) {
					copy(nestedFile, targetDir);
				}
			}
		} else {
			copyFile(source, destDir);
		}
	}

	private static void copyFile(File source, File destDir) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		File destFile = new File(destDir, source.getName());
		try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(source))) {
			try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(destFile))) {
				int count = 0;
				while (true) {
					count = is.read(buffer);
					if (count == -1)
						break;
					os.write(buffer, 0, count);
				}
			}
		}
	}

	public static void move(String from, String to) throws IOException {
		copy(from, to);
		remove(from);
	}

	public static void remove(String location) throws IOException {
		remove(new File(location));
	}

	private static void remove(File source) throws IOException {
		if (source.isDirectory()) {
			for (File nestedFile : source.listFiles()) {
				remove(nestedFile);
			}
		}
		source.delete();
	}

}
