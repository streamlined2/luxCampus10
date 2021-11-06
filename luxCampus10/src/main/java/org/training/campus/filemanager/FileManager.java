package org.training.campus.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

	private static final int BUFFER_SIZE = 10 * 1024;

	public static int countFiles(String path) {
		File root = new File(path);
		if (!root.exists())
			throw new IllegalArgumentException(String.format("folder %s doesn't exist", path));
		if (!root.isDirectory())
			throw new IllegalArgumentException(String.format("%s should be folder, not a file", path));
		return countFiles(root);
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
		File root = new File(path);
		if (!root.exists())
			throw new IllegalArgumentException(String.format("folder %s doesn't exist", path));
		if (!root.isDirectory())
			throw new IllegalArgumentException(String.format("%s should be folder, not a file", path));
		return countDirs(root);
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
		File source = new File(from);
		if (!source.exists())
			throw new IllegalArgumentException(String.format("file or folder %s doesn't exist", from));
		File dest = new File(to);
		if (!dest.exists())
			throw new IllegalArgumentException(String.format("folder %s doesn't exist", to));
		if (!dest.isDirectory())
			throw new IllegalArgumentException(String.format("%s is not a folder", to));
		copy(source, dest);
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
		File file = new File(location);
		if (!file.exists())
			throw new IllegalArgumentException(String.format("file or folder %s doesn't exist", location));
		remove(file);
	}

	private static void remove(File source) throws IOException {
		if (source.isDirectory()) {
			for (File nestedFile : source.listFiles()) {
				remove(nestedFile);
			}
		}
		source.delete();
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(countFiles("C:/1"));
		// System.out.println(countDirs("C:/1"));
		// copy("C:/1", "D:/1");
		// remove("D:/1");
		// move("C:/1", "D:/1");
	}

}
