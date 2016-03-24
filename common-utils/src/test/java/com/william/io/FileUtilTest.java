package com.william.io;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class FileUtilTest {
	
	@Test
	public void testGetFiles(){
		List<File> files = FileUtil.getFiles(new File("F:"));
		System.out.println(files.size());
	}

}
