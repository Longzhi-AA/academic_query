package org.acq.lz.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class ProxyServerInfo
{
	public InetSocketAddress address;
	public Date lastTested;
	public boolean works = false;
	public long responseTime;
	public String websiteURL;
	
	public Proxy.Type type = Proxy.Type.HTTP;
	
	public ProxyServerInfo(String address)
	{
		int i1 = address.indexOf(":");
		String ip = address.substring(0, i1);
		int port = Integer.parseInt(address.substring(i1 + 1));
		
		this.address = new InetSocketAddress(ip, port);
		lastTested = new Date();
	}
	
	public ProxyServerInfo(String ip, int port, Proxy.Type type)
	{
		this.address = new InetSocketAddress(ip, port);
		this.type = type;
	}
	
	public static ProxyServerInfo createFromString(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ";");

		ProxyServerInfo p = new ProxyServerInfo(st.nextToken());
		p.lastTested = new Date(Long.parseLong(st.nextToken()));
		p.responseTime = Long.parseLong(st.nextToken());
		p.websiteURL = st.nextToken();
		return p;
	}
	
	public Proxy getProxy()
	{
		return new Proxy(type, address);
	}
	
	public String toString_address()
	{
		return address.getAddress().getHostAddress() + ":" + address.getPort();
	}

	public String toString()
	{
		return address.getAddress().getHostAddress() + ":" + address.getPort() + ";" + (lastTested == null ? 0 : lastTested.getTime()) + ";" + responseTime + ";" + websiteURL;
	}
}
