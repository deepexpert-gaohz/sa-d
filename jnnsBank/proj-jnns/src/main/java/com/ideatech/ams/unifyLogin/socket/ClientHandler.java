package com.ideatech.ams.unifyLogin.socket;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter 
{
	private static final Logger logger = Logger.getLogger(ClientHandler.class);
	
	private String msg;
	
	private String returnMsg;
	
	public ClientHandler(String msg)
	{
		this.msg = msg;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception 
	{
		if(logger.isDebugEnabled()){
			logger.debug("client send message:"+msg);
		}
		session.write(msg);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
	{
		String msg = message.toString();
		if(logger.isDebugEnabled()){
			logger.debug("client received message:"+msg);
		}
		this.returnMsg = msg;
		session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception 
	{
		cause.printStackTrace();
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
}
