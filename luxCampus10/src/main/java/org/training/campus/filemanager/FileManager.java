package org.training.campus.filemanager;

import java.io.File;

public class FileManager {

	public static int countFiles(String path) {
		File root = new File(path);
		if (!root.exists())
			throw new IllegalArgumentException(String.format("wrong parameter: %s doesn't exist", path));
		if (!root.isDirectory())
			throw new IllegalArgumentException(String.format("wrong parameter: %s should be folder, not a file", path));
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

	public static void main(String[] args) {
		System.out.println(countFiles("C:/1"));
	}

}
