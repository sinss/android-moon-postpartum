package li.com.bobsonclinic.util;

import android.os.Environment;
import android.os.StatFs;
import androidx.core.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;


/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

public class FileUtil {

	public static void writeStreamToFile(InputStream is, String filename) {
		File f = new File(filename);
		writeStreamToFile(is, f);
	}
	
	public static void writeStreamToFile(InputStream is, File f){
		if(f.exists() == true)
			f.delete();
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buf = new byte[8192];
			int len;
			while((len = is.read(buf)) > 0)
				fos.write(buf, 0, len);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static boolean checkFileExist(String string) {
		File f = new File(string);
		return f.exists();
	}

//	public static void decryptStreamToFile(InputStream is, AESEncrypter aes,
//			String string) {
//		
//	}

	
	public static boolean copyFile(String source, String dest) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File f1 = new File(source);
	        File f2 = new File(dest);
	        in = new FileInputStream(f1);
	        out = new FileOutputStream(f2);

	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0)
	          out.write(buf, 0, len);
	        
	      }catch(FileNotFoundException ex){
	    	  	return false;
	      }catch(IOException e){
	    	  	return false;     
	      }finally{
	    	try{
	    		if(in != null)
	    			in.close();
	        }catch(Exception e){}
	        try{
	        	if(out != null)
	        		out.close();
	        }catch(Exception e){}
	      }
	      return true;
	}
	
	/**
	 * file can't over 2GB
	 */
	public static boolean copyFileFast(String source, String dest) {
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(source);
		    outStream = new FileOutputStream(dest);
		    FileChannel inChannel = inStream.getChannel();
		    FileChannel outChannel = outStream.getChannel();
		    inChannel.transferTo(0, inChannel.size(), outChannel);
	      }catch(FileNotFoundException ex){
	    	  return false;
	      }catch(IOException e){
	    	  	return false;     
	      }finally{
			try {
				if (inStream != null)
					inStream.close();
			} catch (Exception e) {}
			try {
				if (outStream != null)
					outStream.close();
			} catch (Exception e) {}
		  }
	      return true;
	}
	
	public static boolean deleteTree(String filePath) {
		return deleteTree(new File(filePath));
	}
	
	public static boolean deleteTree(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if(files != null && files.length > 0){
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteTree(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (path.delete());
	}
	
//	public static long getExternalStorageSpace() {
//
//		long space = 0;
//		try {
//			StatFs stat = new StatFs(UniSystemManager.getInstance().getSDCardPath());
//			space = (long)stat.getAvailableBlocks()*(long)stat.getBlockSize();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		return space;
//	}
	
	public static boolean isExternalStorageExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}
	
	public static float getLocalStorageSpace() {
		float space = 0;
		try {
			StatFs stat = new StatFs("/data/");
			space = stat.getAvailableBlocks()*(float)stat.getBlockSize();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return space;
	}
	
	/**
	 * @param file
	 * @param destPath
	 * @return
	 */
	public static boolean moveFile(File file, String destPath) {
		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));
		return success;
	}
	
	public static List<Pair<String, String>> getFileFromForder(File sdf[], List<Pair<String, String>> path){
		Pair<String,String> pathPair = null;
		for(File f:sdf){
			if(f.isDirectory()){
				getFileFromForder(f.listFiles(), path);
			}
			if(f.isFile()){
				pathPair = new Pair<String, String>(f.getName(), f.getParent());
				path.add(pathPair);
			}
	    }
		return path;
	}
}
