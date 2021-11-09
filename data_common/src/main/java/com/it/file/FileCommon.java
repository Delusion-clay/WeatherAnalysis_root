package com.it.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FileCommon {
	
	private FileCommon(){}

	/**
	 * 判断文件是否存在
	 * @param name
	 * @return
	 */
	public static boolean exist(String name){
		return exist(new File(name));
	}
	
	public static boolean exist(File file){
		return file.exists();
	}

	/**
	 * 创建文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(String file) throws IOException {
		return createFile(new File(file));
	}

	public static boolean createFile(File file) throws IOException {
		if(!file.exists()){
			if(file.isDirectory()){
				return file.mkdirs();
			}else{
				File parentDir = file.getParentFile();
				if(!parentDir.exists()) {
					if (parentDir.mkdirs()) {
						return file.createNewFile();
					}
				}else{
					return file.createNewFile();
				}
			}
		}
		return true;
	}

	/**
	 * 读取文件内容 按行
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(String file) throws IOException{
		return readLines(new File(file), "UTF-8");
	}
	public static List<String> readLines(String file, String encording) throws IOException{
		return readLines(new File(file), encording);
	}
	public static List<String> readLines(File file, String encording) throws IOException {

		List<String> lines = null;
		if(FileCommon.exist(file)) {
			FileInputStream fileInputStream = new FileInputStream(file);
			lines = IOUtils.readLines(fileInputStream, encording);
			fileInputStream.close();
		}
		return lines;
	}


	/**
	 * 获取文件前缀
	 * @param fileName
	 * @return
	 */
	public static String getPrefix(String fileName){
		String prefix = fileName;
		int pos = fileName.lastIndexOf(".");
		if (pos != -1){
			prefix = fileName.substring(0,pos);
		}
		return prefix;
	}

	/**
	 * 获取文件名后缀
	 * @param fileName
	 * @return
	 */
	public static String getFilePostfix(String fileName){
		String filePostfix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return filePostfix.toLowerCase();
	}

	/**
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static boolean delFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			flag = file.delete();
		}
		return flag;
	}

	/**
	 * 移动文件
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean mvFile(String oldPath,String newPath){
		boolean flag = false;
		File oldfile = new File(oldPath);
		File newfile = new File(newPath);
		if(oldfile.isFile() && oldfile.exists()){
			if(newfile.exists()){
				delFile(newfile.getAbsolutePath());
			}
			flag = oldfile.renameTo(newfile);
		}
		return flag;
	}


	/**
	 * 删除目录
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir){
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			if(children!=null){
				for (int i=0; i<children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}


	//递归建立目录，解压缩相关类中使用
	public static void mkdirs(File file) {
		File parent = file.getParentFile();
		if (parent != null && (!parent.exists())) {
			parent.mkdirs();
		}
	}


	public static String getJarFilePathByClass(String clazz) throws ClassNotFoundException {
		return getJarFilePathByClass(Class.forName(clazz));
	}

	public static String getJarFileDirByClass(String clazz) throws ClassNotFoundException {
		return getJarFileDirByClass(Class.forName(clazz));
	}

	public static String getJarFilePathByClass(Class<?> clazz){
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile()).getAbsolutePath();
	}

	public static String getJarFileDirByClass(Class<?> clazz){
		return new File(getJarFilePathByClass(clazz)).getParent();
	}




	public static String getAbstractPath(String abstractPath) throws Exception{
		URL url = FileCommon.class.getClassLoader().getResource(abstractPath);
		System.out.println("配置文件路径为" + url);
		File file = new File(url.getFile());
		String content= FileUtils.readFileToString(file,"UTF-8");
		return content;
	}

	public static String getAbstractPath111(String abstractPath) throws Exception{
		File file = new File(abstractPath);
		String content= FileUtils.readFileToString(file,"UTF-8");
		return content;
	}



}
