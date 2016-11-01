package com.yulin.applib.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Environment;

/**
 * 日志记录类
 * 记录日志，并生成txt文件，输出到存储设备
 * emoney
 * @version 1.0
 *
 */
public class Logger {
	private StringBuffer mMsgBuffser = new StringBuffer();
	private String mPrefixName = null;
	public Logger(String name)
	{
		mPrefixName = name;
	}
	public Logger()
	{

	}
	public static Logger create(String name)
	{
		return new Logger(name);
	}
	public static Logger create()
	{
		return new Logger();
	}
	public Logger append(String msg)
	{
		mMsgBuffser.append(msg);
		return this;
	}
	public Logger appendln(String msg)
	{
		return append(msg).appendln();
	}
	public Logger append(String key, String val)
	{
		mMsgBuffser.append(key+" = "+val);
		return this;
	}
	public Logger append(String key,Object val)
	{
		return append(key,String.valueOf(val));
	}
	public Logger appendln(String key,Object val)
	{
		return append(key,val).appendln();
	}
	public Logger appendln(String key, String val)
	{
		return append(key,val).appendln();
	}
	public Logger append(Map<String,String> map)
	{
		for(Entry<String,String> entry:map.entrySet())
		{
			mMsgBuffser.append(entry.getKey()+" = "+entry.getValue());
			appendln();
		}
		return this;
	}
	public Logger appendln()
	{
		mMsgBuffser.append("\n");
		return this;
	}
	/**
	 * 输出日志
	 * @param dirName 日志文件夹
	 */
	public void output(String dirName)
	{
		output(dirName,true);
	}
	/**
	 * 输出日志
	 * @param dirName 日志文件夹
	 * @param append 是否追加日志
	 */
	public void output(String dirName,boolean append) {
		if(checkSDCard())
		{
			String logDir = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+dirName;
			File dir = new File(logDir);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String strDate = format.format(date);
			String fileName = null;
			if(mPrefixName != null)
			{
				fileName = mPrefixName + "_" + strDate;
			}
			else
			{
				fileName = strDate;
			}
			String logFile = logDir + File.separator + fileName+".txt";
			File file = new File(logFile);
			if(!file.exists())
			{
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Writer w = new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8");
				PrintWriter pw = new PrintWriter(w);
				pw.println(mMsgBuffser.toString());
				pw.flush();
				pw.close();
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public static boolean checkSDCard()
	{
	    if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	       return true;
	   else
	       return false;
	}
}
