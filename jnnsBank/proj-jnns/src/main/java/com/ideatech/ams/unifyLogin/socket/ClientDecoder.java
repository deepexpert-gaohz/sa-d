package com.ideatech.ams.unifyLogin.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

public class ClientDecoder extends CumulativeProtocolDecoder {

	private int headLength;

	private Charset charset;

	public ClientDecoder() {
		this(Charset.defaultCharset());
	}
	
	public ClientDecoder(Charset charset) {
		this(charset, 8);
	}

	public ClientDecoder(Charset charset, int headLength) {
		this.charset = charset;
		this.headLength = headLength;
	}

	protected boolean doDecode(IoSession session, IoBuffer in,ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() > headLength) {
			in.mark();
			byte[] headByte = new byte[headLength];
			in.get(headByte, 0, headLength);
			int messageLength = Integer.parseInt(new String(headByte).trim());

			if (messageLength > in.remaining()) {
				in.reset();
				return false;
			} else {
				in.reset();
				String s = in.getString(headLength + messageLength,charset.newDecoder());
				out.write(s);
				if (in.remaining() > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
