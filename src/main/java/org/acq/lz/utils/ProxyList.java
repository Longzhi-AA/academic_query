package org.acq.lz.utils;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyList
{
	private static final File allProxiesFile = new File("data/proxies/proxies_working_20131126.txt");
	private static final File workingProxiesFile = new File("data/proxies/proxies_working_20131126.txt"); // proxies_working.txt
	
	public List<ProxyServerInfo> servers = new ArrayList<ProxyServerInfo>();
	private Map<String, ProxyServerInfo> serverMap = new HashMap<String, ProxyServerInfo>();
	private Set<String> ips = new HashSet<String>();
	



	public static void createWorkingProxiesList() throws Exception
	{
		ProxyList proxyList = new ProxyList();
		BufferedReader reader = new BufferedReader(new FileReader(allProxiesFile));
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			if (line.length() < 1) continue;
			proxyList.servers.add(ProxyServerInfo.createFromString(line));
		}
		reader.close();
		
		proxyList.testProxies();
	}
	
	public static ProxyList load() throws Exception
	{
		return load(workingProxiesFile);
	}
	
	public static ProxyList load(File file) throws Exception
	{
		return load(new FileInputStream(file));
	}
	
	public static ProxyList load(InputStream is) throws Exception
	{
		ProxyList proxyList = new ProxyList();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			if (line.length() < 1) continue;
			proxyList.servers.add(ProxyServerInfo.createFromString(line));
		}
		reader.close();
		
		return proxyList;
	}
	
	private volatile int threadcount = 0;
	
	public void testProxies() throws Exception
	{
		System.out.println("testing " + servers.size() + " proxy servers.");
		final ExecutorService exec = Executors.newFixedThreadPool(10);
		
		for (int i=0; i<servers.size(); i++)
		{
			final ProxyServerInfo proxy = servers.get(i);
			
			while (threadcount > 70)
			{
				Thread.sleep(50);
			}
			
			
			exec.execute(new Runnable()
			{
//				private int trials = 0;
				
				public void run()
				{
					threadcount++;
//					trials++;
					
					try
					{
						addProgress(true);
						threadcount--;
						return;
					}
					catch (Exception ex)
					{
//						if (trials < 2) exec.execute(this);
					}
					
//					if (trials >= 2) 
						addProgress(false);
					
					threadcount--;
				}
			});
		}
		
		while (count + countFailed < servers.size() * 0.94f)
		{
			Thread.sleep(30000);
		}
		
//		System.out.println("before shutdown");
//		exec.awaitTermination(1, TimeUnit.HOURS);
//		System.out.println("awaitTermination.");
//		exec.shutdown();
		System.out.println("after shutdown");
//		exec.awaitTermination(10, TimeUnit.SECONDS);
		
		
		
		
//		final int chunks = 100;
//		final int elementsPerChunk = servers.size()/chunks + 1; 
//		Thread threads[] = new Thread[chunks];
//		for (int i=0; i<chunks; i++)
//		{
//			final int j=i*elementsPerChunk;
//			threads[i] = new Thread(new Runnable()
//			{
//				public void run()
//				{
//					doPartWork(j, Math.min(servers.size(), j + elementsPerChunk));
//				}
//			});
//			threads[i].start();
//		}
		
//		for (int i=0; i<chunks; i++)
//		{
//			threads[i].join();
//		}
		
//		while (true)
//		{
//			Thread.sleep(1000);
//			if (workDone >= servers.size() - 300) break;
//		}
//		System.out.println("after termination");
		
		class WebsiteInfo
		{
			int count;
			int sumResponseTime;
		}
		
		Map<String, WebsiteInfo> usedWebsitesMap = new HashMap<String, WebsiteInfo>();
		
		List<ProxyServerInfo> workingList = new ArrayList<ProxyServerInfo>();
		for (ProxyServerInfo proxy : servers)
		{
			if (proxy.works)
			{
				workingList.add(proxy);
				
				WebsiteInfo info = usedWebsitesMap.get(proxy.websiteURL);
				if (info == null)
				{
					info = new WebsiteInfo();
					usedWebsitesMap.put(proxy.websiteURL, info);
				}
				info.count++;
				info.sumResponseTime += proxy.responseTime;
			}
		}
		Collections.sort(workingList, new Comparator<ProxyServerInfo>()
		{
			public int compare(ProxyServerInfo o1, ProxyServerInfo o2)
			{
				return (int) (o1.responseTime - o2.responseTime);
			}
		});
		
		PrintWriter writer = new PrintWriter(new FileOutputStream(workingProxiesFile));
		for (ProxyServerInfo proxy : workingList)
		{
			writer.println(proxy);
		}
		writer.close();
		
		List<Entry<String, WebsiteInfo>> usedWebsitesList = new ArrayList<Entry<String, WebsiteInfo>>(usedWebsitesMap.entrySet());
		Collections.sort(usedWebsitesList, new Comparator<Entry<String, WebsiteInfo>>() 
		{
			public int compare(Entry<String, WebsiteInfo> o1, Entry<String, WebsiteInfo> o2)
			{
				return o2.getValue().count - o1.getValue().count;
			}
		});
		
		System.out.println("--------------------");
		for (Entry<String, WebsiteInfo> entry : usedWebsitesList)
		{
			WebsiteInfo info = entry.getValue();
			if (info.count < 2) continue;
			System.out.println(info.count + " (" + info.sumResponseTime / info.count + " ms.) " + entry.getKey());
		}
		
		System.exit(0);
		
	}
	
//	private void doPartWork(int fromIndex, int exToIndex)
//	{
//		for (int i=fromIndex; i<exToIndex; i++)
//		{
//			final ProxyServerInfo proxy = servers.get(i);
//
//				Thread thread = new Thread(new Runnable()
//				{
//					public void run()
//					{
//						try
//						{
//							proxy.test();
//							addProgress(true);
//							return;
//						}
//						catch (Exception ex)
//						{
//						}
//						addProgress(false);	
//					}
//					
//				});
//				thread.start();
//				try 
//				{
//					thread.join(300);
//					if (thread.isAlive())
//					{
//						thread.interrupt();
//						addProgress(false);
//					}
//				}
//				catch (InterruptedException e) 
//				{
//					e.printStackTrace();
//				}
//		}
//	}
	
//	private int workDone = 0;
	private int count = 0;
	private int countFailed = 0;
	private synchronized void addProgress(boolean success)
	{
//		workDone++;
		
		if (success) count++;
		else countFailed++;
		
		System.out.println("ok: " + count + "  f: " + countFailed + " t" + threadcount);
	}
	
	private boolean addProxy(String address, String websiteURL, PrintWriter writer)
	{
    	ProxyServerInfo proxy = serverMap.get(address);
    	if (proxy == null)
    	{
    		proxy = new ProxyServerInfo(address);
    		proxy.websiteURL = websiteURL;
    		serverMap.put(address, proxy);
    		servers.add(proxy);
    		ips.add(proxy.address.getAddress().getHostAddress());
    		writer.println(proxy);
    		return true;
    	}
    	return false;
	}
	
}
