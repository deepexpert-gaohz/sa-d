package com.ideatech.ams.unifyLogin.socket;

import com.ideatech.ams.unifyLogin.CharsetEnum;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientThreadExecutor 
{
	private static ExecutorService pool = Executors.newFixedThreadPool(Integer.parseInt("5"));
	 
	public static String send(String ip, int port, String msg)
	{
		try 
		{
			Callable<String> c = new ClientCallable(ip, port, msg);
			return pool.submit(c).get();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String send(String ip, int port, String msg, CharsetEnum charset)
	{
		try 
		{
			Callable<String> c = new ClientCallable(ip, port, msg, charset);
			return pool.submit(c).get();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String send(String ip, int port, String msg, CharsetEnum charset, int headLength)
	{
		try 
		{
			Callable<String> c = new ClientCallable(ip, port, msg, charset,headLength);
			return pool.submit(c).get();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
