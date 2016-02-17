package com.william.file;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;

/**
 * @DateTime 2015年1月6日 下午5:13:42
 * @Desc 文件处理类
 */
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * @DateTime 2015年1月6日 下午5:13:54
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 获取文件后缀名
	 * @param fileName
	 * @return String
	 */
	public static String getFileNameSuffix(String fileName) {

		if (fileName.indexOf(".") == -1) {
			return "";
		}

		String suffix = fileName.substring(fileName.lastIndexOf("."));
		return suffix;
	}

	/**
	 * @DateTime 2015年4月24日 上午11:17:32
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 写文件
	 * @param in
	 *            输入流
	 * @param outFilePath
	 *            输出文件路径 void
	 */
	public static void writeFile(InputStream in, String outFilePath) throws Exception{

		OutputStream out = null;
		BufferedInputStream fis = null;
		BufferedOutputStream fos = null;
		try {

			fis = new BufferedInputStream(in);

			out = new FileOutputStream(outFilePath);
			fos = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int length = 0;

			while ((length = fis.read(b)) != -1) {
				fos.write(b, 0, length);
			}

			fos.flush();

			fis.close();
			in.close();
			fos.close();
			out.close();

		} catch (Exception e) {
			try {
				fis.close();
				in.close();
				fos.close();
				out.close();
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * @DateTime 2015年4月24日 上午11:17:32
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 写文件
	 * @param in
	 *            输入流
	 * @param out
	 *            输出文件路径 void
	 */
	public static void inToOut(InputStream in, OutputStream out) {

		BufferedInputStream fis = null;
		BufferedOutputStream fos = null;
		try {

			fis = new BufferedInputStream(in);

			fos = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int length = 0;

			while ((length = fis.read(b)) != -1) {
				fos.write(b, 0, length);
			}

			fos.flush();

			fis.close();
			in.close();
			fos.close();
			out.close();

		} catch (Exception e) {
			try {
				fis.close();
				in.close();
				fos.close();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public static void tarExtract(String filePath, String desPath) {

		InputStream inputstream = null;
		OutputStream outputstream = null;
		TarInputStream zis = null;
		try {
			File file = new File(filePath);
			File tempFile = null;
			inputstream = new FileInputStream(file);
			zis = new TarInputStream(inputstream);
			/*
			 * 关键在于这个TarEntry 的理解， 实际你的tar包里有多少文件就有多 少TarEntry
			 */
			TarEntry tarEntry = null;
			while ((tarEntry = zis.getNextEntry()) != null) {
				tempFile = new File(desPath + tarEntry.getName());
				// tempFile.createNewFile();
				createFile(tempFile.getAbsolutePath());
				outputstream = new FileOutputStream(tempFile);
				// 定一个缓存池 可以根据实际情况调整大小（事实证
				// 明很有用）
				byte[] buffer = new byte[1024 * 50];
				while (true) {
					int readsize = zis.read(buffer);
					outputstream.write(buffer, 0, readsize);
					if (readsize < 1024 * 50) {
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputstream.flush();
				inputstream.close();
				zis.close();
				outputstream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * @DateTime 2015年4月24日 下午6:36:38
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 创建文件
	 * @param filePath
	 *            void
	 */
	public static void createFile(String filePath) {
		filePath = filePath.replaceAll("\\\\", "/");
		String parentPath = filePath.substring(0, filePath.lastIndexOf("/"));
		File parentFile = new File(parentPath);
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("createFile:" + filePath, e);
			}
		}
		file = null;
	}

	public static String readFile(String filePath) {

		InputStream in = null;
		String temp = "";
		try {

			in = new FileInputStream(filePath);
			byte[] b = new byte[8192];
			int length = 0;

			while ((length = in.read(b)) != -1) {
				temp += new String(b, 0, length);
			}

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;

	}

	/**
	 * @DateTime 2015年5月20日 下午2:08:47
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 删除某个文件夹下所有文件
	 * @param filePath
	 * @return
	 * @throws Exception
	 * boolean
	 */
	public static boolean deletefile(String filePath) throws Exception {
		try {

			File file = new File(filePath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(filePath + File.separator + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(filePath + File.separator + filelist[i]);
					}
				}
				file.delete();
			}

		} catch (FileNotFoundException e) {
			logger.error("deletefile() Exception:" + filePath, e);
		}
		return true;
	}
	
	/**
	 * @DateTime 2015年7月10日 上午11:10:02
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 创建文件夹 
	 * @param path
	 * void
	 */
    public static void createDir(String path){
		
		File file = new File(path);
		if (! file.exists()) {
			try {
				file.mkdirs();				
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
    }
    
    /**
     * @DateTime 2015年7月20日 下午2:10:33
     * @Author 刘兴密
     * @QQ 63972012
     * @Desc 将字符串写入文件
     * @param data
     * @param outFilePath
     * void
     */
	public static void dataToFile(String data, String outFilePath) {

		OutputStream out = null;
		BufferedOutputStream fos = null;
		try {

			out = new FileOutputStream(outFilePath);
			fos = new BufferedOutputStream(out);

			fos.write(data.getBytes());
			fos.flush();
			
			fos.close();
			out.close();

		} catch (Exception e) {
			try {
				fos.close();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	/**
	 * @DateTime 2015年7月23日 下午5:51:44
	 * @Author 刘兴密
	 * @QQ 63972012
	 * @Desc 获取文件MD5
	 * @param filePath
	 * @return
	 * @throws Exception
	 * String
	 */
	public static String getFileMD5(String filePath) throws Exception{
		FileInputStream fis= new FileInputStream(filePath);    
        String md5 = DigestUtils.md5DigestAsHex(IOUtils.toByteArray(fis));    
        IOUtils.closeQuietly(fis);
        return md5;
	}

}
