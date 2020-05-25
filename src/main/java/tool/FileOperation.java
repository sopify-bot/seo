package tool;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileOperation {

	/***
	 * 根据参数路径遍历磁盘 获取文件列表
	 */
	private LinkedList<File> fileListG = new LinkedList<File>();

	public List<File> getFileList(String initialPath) {
		if (initialPath == null)
			throw new NullPointerException();
		File file = new File(initialPath);
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			if (fileList != null)
				for (File childF : fileList) {
					if (childF.isDirectory()) {
						getFileList(childF.getAbsolutePath());
					} else {
						if(!childF.isHidden())
						fileListG.add(childF);
					}
				}
		}
		return fileListG;
	}
	
	
	public List<File> getFileListDirectory(String initialPath) {
		if (initialPath == null)
			throw new NullPointerException();
		
		File file = new File(initialPath);
		if (file.isDirectory()) {
			File[] fileList = file.listFiles();
			if (fileList != null)
				for (File childF : fileList) {
					if (childF.isDirectory()) {
						fileListG.add(childF);
					} else {
						//
					}
				}
			File[] fileList1 = file.listFiles();
			if (fileList != null)
				for (File childF : fileList) {
					if (childF.isDirectory()) {
						fileListG.add(childF);
						fileListG.add(null);
						//if(File str:childF.listFiles()){
						//}
					} else {
						//
					}
				}
		}
		return fileListG;
	}

	public void serialization(String pathname, Object obj) {
		try {
			File file = new File(pathname);
			OutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		}
	}

	public Object deserialization(String pathname) {
		Object objChile = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(pathname));
			ObjectInputStream ois = new ObjectInputStream(fis);
			objChile = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();;
		}
		return objChile;
	}

	public  static void writeFile(String pathName, String text) {
		try {
			FileWriter fw = new FileWriter(new File(pathName));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.flush();
			bw.close();
			fw.close();
		} catch (Throwable e) {
			e.printStackTrace();;
		}
	}
	
	public static  void writeFile(String pathName, byte[] b) {
		try {
			FileOutputStream fos=new FileOutputStream(new File(pathName));
			fos.write(b);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();;
		}
	}
	

	public void serialization(File file, Object obj) {
		try {
			OutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		}
	}

	public Object deserialization(File file) {
		Object objChile = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			objChile = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();;
		}
		return objChile;
	}

	public Object ReadSerializableDateXML(String pathname) {
		File fin = new File(pathname);
		FileInputStream fis;
		Object obj = null;
		try {
			fis = new FileInputStream(fin);
			XMLDecoder decoder = new XMLDecoder(fis);
			obj = decoder.readObject();
			fis.close();
			decoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		}
		return obj;
	}

	public void WriteSerializableDateXML(String pathname, Object obj) {
		try {
			File fo = new File(pathname);
			FileOutputStream fos = new FileOutputStream(fo);
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(obj);
			encoder.flush();
			encoder.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		}
	}

	/****
	 * 获取classPath
	 */
	public String getClassPath() {
		URL url = FileOperation.class.getProtectionDomain().getCodeSource()
				.getLocation(); // Gets the path
		String jarPath = null;
		try {
			jarPath = URLDecoder.decode(url.getFile(), "UTF-8"); // Should fix
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String parentPath = new File(jarPath).getParentFile().getPath(); // Path
		parentPath = parentPath + File.separator;
		return parentPath;
	}

	/***
	 * 根据需要查找的文件名字 在path下查找配置文件路径
	 */
	public String queryFilePath(String path, String propertyFileName) {
		for (File file : new FileOperation().getFileList(path)) {
			if (file.getName().equals(propertyFileName))
				
				return file.getAbsolutePath();
		}
		return null;
	}

	public String readFile(String path, String charset) {
		if (path == null || charset == null)
			throw new NullPointerException();
		File file = new File(path);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charset);
			br = new BufferedReader(isr);
			while (br.ready())
				sb.append(br.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();;//
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();;
			}
		}
		return sb.toString();
	}



	
	/***
	 * 统一写文件
	 */
	public  static synchronized void  write(String str,String path) {
		if(str==null)
			return ;
		File  file=null;
		List<File> fileL=new FileOperation().getFileList(path);
		for(File eachF:fileL)
		{
			if((eachF.length()/1024/1024)<1024)
			{
				file=eachF;
				break;
			}
		}
		if(file==null)
			file=new File(path+File.separator+new java.util.Date().getTime());
		RandomAccessFile randomaccessfile=null;	
		try {
				 randomaccessfile = new RandomAccessFile(file, "rw");
				 {
					 	long lenth = randomaccessfile.length();
						randomaccessfile.seek(lenth);
						randomaccessfile.write("\r\n".getBytes());
						randomaccessfile.write(str.getBytes());
				 }
			} catch (FileNotFoundException e) {
				e.printStackTrace();;
			} catch (IOException e) {
				e.printStackTrace();;
			}finally{
				try {
					if(randomaccessfile!=null)
					randomaccessfile.close();
				} catch (IOException e) {
					e.printStackTrace();;
				}
			}
	}

	/****
	 * v7版本：
	 * 1 主要想猜测是否存在点击的refer漏洞，也就是并没有记录上下文；
	 * 这样我的点击，他就无法识别出到底是否是站内搜索；
	 * 2以此达到无需排名的点击；
	 * 1月24日21点，准备部署；3
	 */
	public  static final String  version="V10.0>-2yue-11ri";//月日时;
	public static void log(String showOrclick,String searchWordm,String doman,String engine,String path) {
		write("showOrclick<"+showOrclick+"> version:<"+version+"> engine:<"+engine+"> doman:<"+doman+"> searchWordm:<"+searchWordm+"> logT:<"+System.currentTimeMillis()+"> logD:"+ Monitor.getTime(),path);
		//System.out.println("为了空间..不写日志");
	}

	public static void main(String[] args) {
		//FileOperation.log("","dnf","baidu.com","360","/Users/jhy/Documents/Temp/doman");
		new FileOperation().readFileLine(new File("/Users/jhy/Desktop/back"),"utf-8").forEach(e->{
			System.out.println("-------------------------");
			System.out.print(e.split("\t")[0]);
			System.out.print(">>");
			System.out.print(e.split("\t")[1]);
			System.out.println();
		});
	}
	public ArrayList<String> readFileLine(File file, String charset) {
		ArrayList<String> al=new ArrayList<String>();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charset);
			br = new BufferedReader(isr);
			while (br.ready())
				al.add(br.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();;
		} catch (IOException e) {
			e.printStackTrace();;
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();;
			}
		}
		return al;
	}


}
