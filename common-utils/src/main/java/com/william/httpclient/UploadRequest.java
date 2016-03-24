package com.william.httpclient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.william.io.FileUtil;

public class UploadRequest extends HttpRequest{
	
	private List<File> fileList;

	public List<File> getFileList() {
		return fileList;
	}
	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}
	public void addFile(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			if(fileList == null)
				fileList = new ArrayList<File>();
			if(file.isFile()){
				fileList.add(file);
			}else if(file.isDirectory()){
				List<File> files = FileUtil.getFiles(file);
				if(files != null){
					fileList.addAll(files);
				}
			}
		}
	}
}
