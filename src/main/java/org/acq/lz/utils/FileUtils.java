package org.acq.lz.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.*;

public class FileUtils
{
	public static byte[] getFileData(File file) throws IOException
	{
		byte data[] = new byte[(int) file.length()];
//		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		if (in.read(data) != data.length)
		{
			in.close();
			throw new IOException("file length != read length for " + file.getAbsolutePath());
		}
		in.close();
		return data;
	}
	
	public static int[] getFileDataInt(File file) throws IOException
	{
		int data[] = new int[(int) (file.length()/4)];
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		for (int i=0; i<data.length; i++)
		{
			data[i] = dis.readInt();
		}
		dis.close();
		return data;
	}
	
	public static long[] getFileDataLong(File file) throws IOException
	{
		long data[] = new long[(int) (file.length()/8)];
		// use with BufferedInputStream, otherwise very slow!
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		for (int i=0; i<data.length; i++)
		{
			data[i] = dis.readLong();
		}
		dis.close();
		return data;
	}
	
	public static float[] getFileDataFloat(File file) throws IOException
	{
		return getFileDataFloat(new FileInputStream(file), (int) file.length());
	}
	
	public static float[] getFileDataFloat(InputStream is, int streamLength) throws IOException
	{
		float data[] = new float[(int) (streamLength/4)];
		// use with BufferedInputStream, otherwise very slow!
		DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
		for (int i=0; i<data.length; i++)
		{
			data[i] = dis.readFloat();
		}
		dis.close();
		return data;
	}
	
	public static double[] getFileDataDouble(File file) throws IOException
	{
		return getFileDataDouble(new FileInputStream(file), (int) file.length());
	}
	
	public static double[] getFileDataDouble(InputStream is, int streamLength) throws IOException
	{
		double data[] = new double[(int) (streamLength/8)];
		// use with BufferedInputStream, otherwise very slow!
		DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
		for (int i=0; i<data.length; i++)
		{
			data[i] = dis.readDouble();
		}
		dis.close();
		return data;
	}
	
	public static void writeToFile(byte[] data, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
	}
	
	public static void writeToFile(long[] data, File file) throws IOException
	{
		// use with BufferedOutputStream, otherwise very slow!
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		for (int i=0; i<data.length; i++) dos.writeLong(data[i]);
		dos.close();
	}
	
	public static void writeToFile(float[] data, File file) throws IOException
	{
		// use with BufferedOutputStream, otherwise very slow!
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		for (int i=0; i<data.length; i++) dos.writeFloat(data[i]);
		dos.close();
	}
	
	public static void writeToFile(double[] data, File file) throws IOException
	{
		// use with BufferedOutputStream, otherwise very slow!
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		for (int i=0; i<data.length; i++) dos.writeDouble(data[i]);
		dos.close();
	}
	
	public static void writeToFile(int[] data, File file) throws IOException
	{
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		for (int i=0; i<data.length; i++) dos.writeInt(data[i]);
		dos.close();
	}
	
	public static void writeToZipFile(byte[] data, File file) throws IOException
	{
		GZIPOutputStream fos = new GZIPOutputStream(new FileOutputStream(file));
		fos.write(data);
		fos.close();
	}
	
	public static long copy(File in, File out) throws IOException
	{
		return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
	}
	
	public static byte[] copy(InputStream in) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(16384);
		copy(in, bos);
		return bos.toByteArray();
	}

	public static long copy(InputStream in, OutputStream out, boolean closeInputStream, boolean closeOutputStream) throws IOException
	{
		try 
		{
			long byteCount = 0;
			byte[] buffer = new byte[16384];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) 
			{
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally 
		{
			try 
			{
				if (closeInputStream) in.close();
			} 
			catch (IOException localIOException3) 
			{
			}
			try 
			{
				if (closeOutputStream) out.close();
			} 
			catch (IOException localIOException4) 
			{
			}
		}
	}
	
	public static long copy_log(InputStream in, OutputStream out, long estimatedMBytes) throws IOException
	{
		try 
		{
			long byteCount = 0;
			byte[] buffer = new byte[16384];
			
			
			int bytesRead = -1;
			long time = System.currentTimeMillis();
			
			int i = 0;
			while ((bytesRead = in.read(buffer)) != -1) 
			{
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
				i++;
				if (i % 5000 == 0) 
				{
					long mbytes = (byteCount / 1000 / 1000);
					double mbytespersec = (double) byteCount / (System.currentTimeMillis() - time) / 1000;
					mbytespersec = (double) (int) (mbytespersec * 100.0) / 100.0;
					
					long mbytesLeft = estimatedMBytes - mbytes;
					int secsLeft = (int) (mbytesLeft / mbytespersec);
					
					System.out.println(mbytes + " MByte. " + mbytespersec + " MBytes/sec. " + secsLeft + " secs left.");
				}
			}
			out.flush();
			return byteCount;
		} 
		finally 
		{
			try 
			{
				 in.close();
			} 
			catch (IOException localIOException3) 
			{
			}
			try 
			{
				out.close();
			} 
			catch (IOException localIOException4) 
			{
			}
		}
	}
	
	public static long copy(InputStream in, OutputStream out) throws IOException
	{
		return copy(in, out, true, true);
	}
	

	public static File[] listFiles(File dir, final String endsWith)
	{
		return dir.listFiles(new FilenameFilter() 
		{
			public boolean accept(File dir, String name) 
			{
				return name.endsWith(endsWith);
			}
		});
	}
	
	public static void compareDirsRecursively(File dir1, File dir2)
	{
		long time = System.currentTimeMillis();
		List<File> files1 = listFilesRecursively(dir1, null);
		System.out.println((System.currentTimeMillis() - time) + " ms. " + files1.size() + " files in " + dir1.getAbsolutePath());
		
		time = System.currentTimeMillis();
		List<File> files2 = listFilesRecursively(dir2, null);
		System.out.println((System.currentTimeMillis() - time) + " ms. " + files2.size() + " files in " + dir2.getAbsolutePath());
		
		String dir2String = dir2.getAbsolutePath();
		Set<String> files2set = new HashSet<String>();
		for (File file : files2)
		{
			if (file.getName().startsWith("._")) continue;
			files2set.add(file.getAbsolutePath().substring(dir2String.length()));
		}
		
		String dir1String = dir1.getAbsolutePath();
		int count = 0;
		for (File file : files1)
		{
			if (file.getName().startsWith("._")) continue;
			String fileName = file.getAbsolutePath().substring(dir1String.length());
			if (!files2set.contains(fileName))
			{
				System.out.println(fileName);
				count++;
			}
		}
		System.out.println(count + " of " + files1.size() + " files in " + dir1.getAbsolutePath() + " were not found in the " + files2.size() + " files of " + dir2.getAbsolutePath());
	}
	
	public static List<File> listFilesRecursively(File dir,  String endsWith)
	{
		List<File> acceptedFiles = new ArrayList<File>();
		listFilesRecursively(dir, endsWith, acceptedFiles);
		return acceptedFiles;
	}
	
	static void listFilesRecursively(File dir, String endsWith, List<File> acceptedFiles)
	{
		for (File file : dir.listFiles())
		{
			if (file.isFile())
			{
				if (endsWith == null || file.getName().endsWith(endsWith)) 
				{
					acceptedFiles.add(file);
					if (acceptedFiles.size() % 1000 == 0) System.out.print(".");
				}
			}
			else if (file.isDirectory())
			{
				listFilesRecursively(file, endsWith, acceptedFiles);
			}
		}
	}
	

	public static void unzip(File zipFile, File targetDir) throws IOException
	{
		unzip(zipFile, targetDir, null);
	}
	

	public static void unzip(File zipFile, File targetDir, FilenameFilter filter) throws IOException
	{
	      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
	      ZipEntry entry;
	      byte[] buffer = new byte[16384];
	      while ((entry = zin.getNextEntry()) != null)
	      {
	    	  File file = new File(targetDir, entry.getName());
	    	  if (entry.isDirectory()) 
	    	  {
	    		  if (!file.exists()) file.mkdirs();
	    	  }
	    	  else
	    	  {
	    		  File dir = file.getParentFile();
	    		  if (filter != null && !filter.accept(dir, file.getName())) continue;
	    		  
	    		  if (!dir.exists()) dir.mkdirs();

	    		  BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
	    		  int bytesRead = -1;
	    		  while ((bytesRead = zin.read(buffer)) != -1) 
	    		  {
	    			  bos.write(buffer, 0, bytesRead);
	    		  }
	    		  bos.close();
	    	  }
	      }
	      zin.close();
	}
	
	
	public static File findDir(File start, String name) throws IOException
	{
		if (start.isDirectory())
		{
			if (name.equals(start.getName())) return start;
			File[] children = start.listFiles();
			if (children != null)
			{
				for (File f : children)
				{
					File dir = findDir(f, name);
					if (dir != null) return dir;
				}	
			}
		}
		return null;
	}
	
	
	
	public static long dirSize(File dir) throws Exception
	{
		if (dir.isDirectory())
		{
			long byteCount = 0;
			for (File file : dir.listFiles()) byteCount += dirSize(file);
			return byteCount;
		}
		return dir.length();
	}
	
}
