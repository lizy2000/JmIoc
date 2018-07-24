/*
 * Copyright (C) Chris Liao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmin.ioc.impl.config.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom.Element;

import org.jmin.ioc.impl.exception.BeanConfiguerationFileException;
import org.jmin.ioc.impl.util.StringUtil;
import org.jmin.ioc.impl.util.Symbols;

/**
 * 文件查找
 * 
 * @author chris liao
 */
public class BeanXMLFileFinder {
	
	/**
	 * 查找文件
	 */
	public URL find(String filename,BeanXMLConstants tags)throws BeanConfiguerationFileException{
		return find(System.getProperty(tags.FILE_USERDIR),filename,tags);
	}
	
	/**
	 * 查找文件
	 */
	public URL find(String currentFolder,String filename,BeanXMLConstants tags)throws BeanConfiguerationFileException{
		if(StringUtil.isNull(filename))
		 throw new NullPointerException("File name can;t be null");
		
		filename=filename.trim();
		if(!filename.toLowerCase().endsWith(tags.XML_FILE_EXTEND))
			throw new BeanConfiguerationFileException("File["+ filename +"] is not a valid xml file");
		
		filename = StringUtil.getFilterValue(filename);//过滤环境变量
		if(filename.toLowerCase().startsWith(tags.FILE_CP)){//需要从classpath中查找
			return findFromClassPath(filename.substring(tags.FILE_CP.length()).trim());
		}else if(filename.toLowerCase().startsWith(tags.FILE_CLASSPATH)){//需要从classpath中查找
			return findFromClassPath(filename.substring(tags.FILE_CLASSPATH.length()).trim());
		}else{//无法确定文件是是一个完整的路径还是需要从classpath中寻找，因此需要尝试不同的寻找方法
			
			URL fileURL = null;
			try{
				fileURL = findFromClassPath(filename);
			}catch(Exception e){}
			
			try{
				if(fileURL==null)
				 fileURL = findFromSytemFolder(filename);
			}catch(Exception e){}
			
			try{
			  if(fileURL == null && !StringUtil.isNull(currentFolder))//文件没有被找到
				 fileURL = findFromSytemFolder(currentFolder+File.separatorChar+filename);
			}catch(Exception e){}

			
			if(fileURL==null)
				throw new BeanConfiguerationFileException("Not found ioc file:"+filename);
			else
				return fileURL;
		}
	}
	
	/**
	 * 从类路径中查找
	 */
	private URL findFromClassPath(String filename)throws BeanConfiguerationFileException{
		return (this.getClass().getClassLoader()).getResource(filename);
	}
	
	/**
	 * 直接从目录中匹配
	 */
	private URL findFromSytemFolder(String filename)throws BeanConfiguerationFileException{
		try {
			File file = new File(filename);
			if(file.exists()&& file.isFile())
				return new URL(file.getAbsolutePath());
			else
				return null;
		} catch (MalformedURLException e) {
		 throw new BeanConfiguerationFileException(e);
		}
	}
	
	/**
	 * 验证XML文件
	 */
	public void validateXMLFile(URL url,BeanXMLConstants tags)throws BeanConfiguerationFileException {
		if (url == null)
			throw new BeanConfiguerationFileException("File URL can't be null");
		
		if(!url.getFile().toLowerCase().endsWith(tags.XML_FILE_EXTEND))
			throw new BeanConfiguerationFileException("File["+url+"]is not a valid XML file");
	}

	/**
	 * 验证顶级节点
	 */
	public void validateXMLRoot(Element rootElement, String rootName,String filename)throws BeanConfiguerationFileException {
		if (rootElement == null)
			throw new BeanConfiguerationFileException("Missed root node in file:"+filename);
		if (!rootElement.getName().equalsIgnoreCase(rootName))
			throw new BeanConfiguerationFileException("Missed root node<"+ rootName +"> in file:"+filename);
	}
	
	/**
	 * 获得文件的路径
	 */
	public String getFilePath(String fileName) {
		int point = fileName.lastIndexOf(Symbols.RIGHT_FILE_SEP_CHAR);
		if (point == -1)
			point = fileName.lastIndexOf(Symbols.LEFT_FILE_SEP_CHAR);
		String folder = fileName;
		if (point > 0) {
			folder = folder.substring(0, point);
		}

		if (folder.startsWith(Symbols.RIGHT_FILE_SEP_CHAR)) {
			folder = folder.substring(1);
		}
		return folder;
	}
}
