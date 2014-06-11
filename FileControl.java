/**
 * 作者：罗欣然
 * 时间：2012.8.17
 * 功能：对文件进行相关操作
 * 流程：第一步：从HDFS下载生成的文件内容到本地temp文件中
 * 			 第二步：从本地文件temp中读取内容
 * 			 第三步：删除temp文件
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;


public class FileControl {
	public static void inputDataToLocalFile(String path,String[] Data) throws Exception
	{
		//读取文件中的数据
		BufferedWriter out = new BufferedWriter(new FileWriter(path));
		for(int i=0;i<Data.length;i++)
		{
			out.write(Data[i]);
			out.write("\r\n");
		}
		out.close();
	}
	
	//得到本地文件的数据
	public static String[] getLocalFileData(String path) throws Exception
	{
		String[] wssplit=null;
		File file = new File(path);
		if(!file.exists()){
			throw new IOException("file not exist!");
		}
		else{
			//读取文件中的数据
			BufferedReader bin = new BufferedReader(new FileReader(path));
			String t="";
			String temp="";
			//按行读取
			
			
			for(int i=0;(temp=bin.readLine()) != null&&i<1000;i++)
			{
				t+=temp+"\t";
			}
			bin.close();
			//分析数据，将词频和关键词分开
			Pattern p = Pattern.compile("\t");
			wssplit=p.split(t.toString());
	
			return wssplit;
		}
		
	}
	
	public static void deleteFile(String path) throws Exception{
		File file = new File(path);
		if(file.exists()){
			delFolder(file.getPath());
		}
	}
	
	public static void deleteAllDataFile() throws Exception{
		File file1 = new File("./TEMP");
		File file2 = new File("./RESULT");
		if(!file1.exists() && !file2.exists()){
//			System.out.println("file not exits!");
			throw new IOException("file not exits!");
		}
		else
		{
			delFolder(file1.getPath());
			delFolder(file2.getPath());

			System.out.println("delete file2 success!");
		}
		
		
		
	}
	
	
	//删除文件夹
	//param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        File myFilePath = new File(filePath);
	        myFilePath.delete(); //删除空文件夹
	    } catch (Exception e) {
	       e.printStackTrace(); 
	    }
	}
	
	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
		    } else {
		          temp = new File(path + File.separator + tempList[i]);
		    }
			if (temp.isFile()) {
	        	temp.delete();
			}
			if (temp.isDirectory()) {
	        	delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	        	delFolder(path + "/" + tempList[i]);//再删除空文件夹
	        	flag = true;
			}
		}
		return flag;
	}

	
	
	
	public static void main(String[] args) throws Exception
	{
		String[] wssplit=FileControl.getLocalFileData(".//amazon-meta.txt");

		for(int i=0;i<wssplit.length;i++)
		{
			if(i<(wssplit.length/2))
			{
				//奇数为ID，偶数为频率
//				int j=2*i+1;
				System.out.println(wssplit[i]);
			}
		}
	}
}
