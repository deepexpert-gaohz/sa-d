package com.ideatech.ams.unifyLogin.socket;

import com.ideatech.ams.unifyLogin.CharsetEnum;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class ClientCallable implements Callable<String>
{
	private String ip;
	
	private int port;
	
	private String msg;
	
	private ClientCodecFactory clientCodecFactory;
	
	public ClientCallable(String ip, int port, String msg)
	{
		this.ip = ip;
		this.port = port;
		this.msg = msg;
		this.clientCodecFactory = new ClientCodecFactory();
	}
	
	public ClientCallable(String ip, int port, String msg, CharsetEnum charset)
	{
		this.ip = ip;
		this.port = port;
		this.msg = msg;
		this.clientCodecFactory = new ClientCodecFactory(Charset.forName(charset.getCode()));
	}
	
	public ClientCallable(String ip, int port, String msg, CharsetEnum charset, int headLength)
	{
		this.ip = ip;
		this.port = port;
		this.msg = msg;
		this.clientCodecFactory = new ClientCodecFactory(Charset.forName(charset.getCode()), headLength);
	}
	
	public String call() 
	{
		ClientHandler clientHandler = new ClientHandler(msg);
		NioSocketConnector connector = new NioSocketConnector();
		connector.setHandler(clientHandler);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(clientCodecFactory));
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
		return clientHandler.getReturnMsg();
	}
}
