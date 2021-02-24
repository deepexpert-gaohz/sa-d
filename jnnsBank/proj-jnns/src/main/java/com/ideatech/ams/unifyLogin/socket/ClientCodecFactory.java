package com.ideatech.ams.unifyLogin.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class ClientCodecFactory implements ProtocolCodecFactory 
{
	private final ClientDecoder decoder;
	
	private final ClientEncoder encoder;
	
	public ClientCodecFactory() 
	{
		this.decoder = new ClientDecoder();
		this.encoder = new ClientEncoder();
	}
	
	public ClientCodecFactory(Charset charset) 
	{
		this.decoder = new ClientDecoder(charset);
		this.encoder = new ClientEncoder(charset);
	}
	
	public ClientCodecFactory(Charset charset, int headLength) 
	{
		this.decoder = new ClientDecoder(charset, headLength);
		this.encoder = new ClientEncoder(charset);
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception 
	{
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception 
	{
		return encoder;
	}

}
