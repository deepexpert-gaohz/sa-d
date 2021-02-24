package com.ideatech.ams.unifyLogin.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class ClientEncoder extends ProtocolEncoderAdapter
{
	
	private Charset charset;
	
	public ClientEncoder()
	{
		this.charset = Charset.defaultCharset();
	}
	
	public ClientEncoder(Charset charset)
	{
		this.charset = charset;
	}
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception 
	{
		String msg = (String)message;
		IoBuffer buffer = IoBuffer.allocate(msg.getBytes(charset.displayName()).length);
		buffer.setAutoExpand(true);
		if(msg!=null)
		{
			buffer.put(msg.getBytes(charset.displayName()));
		}
		buffer.flip();
		out.write(buffer);
	}

}
