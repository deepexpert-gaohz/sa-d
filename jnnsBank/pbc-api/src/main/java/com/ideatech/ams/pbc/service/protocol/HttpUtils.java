package com.ideatech.ams.pbc.service.protocol;

import com.ideatech.ams.pbc.config.HttpConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.protocol.ExecutionContext.*;


public class HttpUtils {
	private static Pattern pattern = Pattern.compile("<meta[^>]*content=(['\"])?[^>]*charset=((gb2312)|(gbk)|(utf-8))\\1[^>]*>", Pattern.CASE_INSENSITIVE);
	private static Pattern charsetPattern = Pattern.compile("charset=((gb2312)|(gbk)|(utf-8))", Pattern.CASE_INSENSITIVE);

	public static HttpResult get(String url) throws Exception {
		return get(url, HttpConfig.HTTP_SOCKET_TIMEOUT, null, null, null);
	}

	public static HttpResult get(String url, CookieStore cookies, List<Header> headers) throws Exception {
		return get(url, HttpConfig.HTTP_SOCKET_TIMEOUT, cookies, headers, null);
	}

	public static HttpResult get(String url, int timeOut, CookieStore cookies, List<Header> headers, String localAddress) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		// TODO:创建代理设置
		HttpHost authedHost = getAuthed(client, url);
		client.getParams().setIntParameter("http.socket.timeout", timeOut * 1000);
		if (StringUtils.isNotEmpty(localAddress)) {
			InetAddress localBinding = InetAddress.getLocalHost();
			localBinding = InetAddress.getByName(localAddress);
			client.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS, localBinding);
		}
		HttpContext localContext = new BasicHttpContext();
		if (cookies != null && cookies.getCookies().size() > 0) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}
		HttpGet httpget = new HttpGet(url);
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				httpget.addHeader(headers.get(i));
			}
		}
		HttpResult httpResult = null;
		HttpResponse response = null;
		try {
			if (HttpConfig.HTTP_PROXY_ISON) {
				response = client.execute(authedHost, httpget, localContext);
			} else {
				response = client.execute(httpget, localContext);
			}
			cookies = client.getCookieStore();
			httpResult = readResponse(response, localContext);
			httpResult.setCookies(cookies);
		} catch (Exception e) {
			httpget.abort();
			throw e;
		} finally {
			if (client != null && client.getConnectionManager() != null) {
				client.getConnectionManager().shutdown();
			}
		}
		return httpResult;
	}

	private static HttpResult readResponse(HttpResponse response, HttpContext localContext) throws Exception {
		HttpResult httpResult = new HttpResult();
		HttpHost target = (HttpHost) localContext.getAttribute(HTTP_TARGET_HOST);
		HttpUriRequest req = (HttpUriRequest) localContext.getAttribute(HTTP_REQUEST);
		String uri = req.getURI().toString();
		String targetUrl = target.toString();
		if (uri.startsWith(targetUrl)) {
			uri = uri.substring(targetUrl.length());
		}
		httpResult.setResponseUrl(targetUrl + uri);
		httpResult.setStatuCode(response.getStatusLine().getStatusCode());
		Header[] rspHeaders = response.getAllHeaders();
		for (int i = 0; i < rspHeaders.length; i++) {
			Header header = rspHeaders[i];
			httpResult.addHeader(header);
		}
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			httpResult.setBuffer(HttpUtils.entityToByte(entity));
		}
		return httpResult;
	}

	public static String entityToString(HttpEntity entity, String encoding) throws Exception {
		StringBuilder sb = new StringBuilder();
		String lineSep = System.getProperty("line.separator");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(entity.getContent(), encoding));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append(lineSep);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return sb.toString();
	}

	public static String entityToStringAutoCharset(HttpEntity entity, String contentType) throws Exception {
		InputStream is = entity.getContent();
		if (is == null) {
			System.err.println("is == null");
			return "";
		}
		String ret = "";
		byte[] buffer = new byte[0];
		byte[] temp = new byte[1024];
		int count = is.read(temp);
		while (count > 0) {
			int length = buffer.length;
			buffer = Arrays.copyOf(buffer, length + count);
			System.arraycopy(temp, 0, buffer, length, count);
			count = is.read(temp);
		}
		StringBuilder builder = new StringBuilder();
		String charset = "";
		for (int i = 0; i < buffer.length && ("".equals(charset) == true); i++) {
			char c = (char) buffer[i];
			switch (c) {
			case '<':
				builder.delete(0, builder.length());
				builder.append(c);
				break;
			case '>':
				if (builder.length() > 0) {
					builder.append(c);
				}
				String meta = builder.toString();
				if (meta.toLowerCase().startsWith("<meta") == true) {
					charset = getCharsetFromMeta(meta);
				}
				break;
			default:
				if (builder.length() > 0) {
					builder.append(c);
				}
				break;
			}
		}
		if ("".equals(charset) == false) {
			ret = new String(buffer, charset);
		} else {
			Matcher m = charsetPattern.matcher(contentType);
			if (m.find() == true) {
				charset = m.group(1);
			} else {
				charset = "UTF-8";
			}
			ret = new String(buffer, charset);
		}
		return ret;
	}

	public static HttpResult post(String url, String data, String encoding, CookieStore cookies, List<Header> headers) throws Exception {
		return post(url, data, encoding, HttpConfig.HTTP_SOCKET_TIMEOUT, cookies, headers, null);
	}

	public static HttpResult post(String url, String data, int timeOut, String encoding, CookieStore cookies, List<Header> headers) throws Exception {
		return post(url, data, encoding, timeOut, cookies, headers, null);
	}

	/**
	 * 获取可信任https链接，以避免不受信任证书出现peer not authenticated异常
	 * 
	 * @param base
	 * @return
	 */
	public static DefaultHttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static HttpResult post(String url, String data, String encoding, int timeOut, CookieStore cookies, List<Header> headers, String localAddress) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// TODO:创建代理设置
		HttpHost authedHost = getAuthed(httpclient, url);
		if (timeOut == -1) {
			timeOut = 20;
		}
		httpclient.getParams().setIntParameter("http.socket.timeout", timeOut * 1000);
		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getLocalHost();
			localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS, localBinding);
		}
		// https
		if (url.contains("https")) {
			httpclient = wrapClient(httpclient);
		}
		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}
		HttpPost httppost = new HttpPost(url);
		String contentType = null;
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				Header h = headers.get(i);
				if (!h.getName().startsWith("$x-param")) {
					httppost.addHeader(h);
				}
				if ("Content-Type".equalsIgnoreCase(h.getName())) {
					contentType = h.getValue();
				}
			}
		}
		if (StringUtils.isNotEmpty(contentType)) {
			httppost.setHeader("Content-Type", contentType);
		} else {
			if (StringUtils.isNotEmpty(data)) {
				httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			}
		}
		HttpResult httpResult = new HttpResult();
		HttpResponse response;
		try {
			httppost.setEntity(new StringEntity(data, encoding));
			if (HttpConfig.HTTP_PROXY_ISON) {
				response = httpclient.execute(authedHost, httppost, localContext);
			} else {
				response = httpclient.execute(httppost, localContext);
			}
			httpResult = readResponse(response, localContext);
			cookies = httpclient.getCookieStore();
			httpResult.setCookies(cookies);
		} catch (Exception e) {
			httppost.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return httpResult;
	}

	public static List<NameValuePair> parsePostParams(byte[] data) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (data != null && data.length > 0) {
			int ix = 0;
			int ox = 0;
			String key = null;
			String value = null;
			while (ix < data.length) {
				byte c = data[ix++];
				switch ((char) c) {
				case '&':
					value = new String(data, 0, ox);
					if (key != null) {
						params.add(new BasicNameValuePair(key, value));
						key = null;
					}
					ox = 0;
					break;
				case '=':
					if (key == null) {
						key = new String(data, 0, ox);
						ox = 0;
					} else {
						data[ox++] = c;
					}
					break;
				default:
					data[ox++] = c;
				}
			}
			if (key != null) {
				value = new String(data, 0, ox);
				params.add(new BasicNameValuePair(key, value));
			}
		}
		return params;
	}

	public static String getCharsetFromMeta(String meta) {
		if (meta == null || "".equals(meta) == true) {
			return "";
		}
		Matcher m = pattern.matcher(meta);
		if (m.find() == true) {
			return m.group(2);
		}
		return "";
	}

	public static byte[] entityToByte(HttpEntity entity) throws Exception, SocketTimeoutException {
		return EntityUtils.toByteArray(entity);
		/**
		 * InputStream is = entity.getContent(); if (is == null) { System.err.println("is == null"); return null; } byte[] buffer = new byte[0]; byte[] temp = new byte[1024]; int count =
		 * is.read(temp); while (count > 0) { int length = buffer.length; buffer = Arrays.copyOf(buffer, length + count); System.arraycopy(temp, 0, buffer, length, count); count = is.read(temp); }
		 * return buffer;
		 */
	}

	private static HttpHost getAuthed(DefaultHttpClient client, String url) {
		if (!HttpConfig.HTTP_PROXY_ISON)
			return null;
		if (!HttpConfig.HTTP_PROXY_BASE) {
			// 设置代理认证空间
			AuthScope authScope = new AuthScope(HttpConfig.HTTP_PROXY_HOST, HttpConfig.HTTP_PROXY_PORT);
			// 设置用户和用户密码
			UsernamePasswordCredentials upc = new UsernamePasswordCredentials(HttpConfig.HTTP_PROXY_USERNAME, HttpConfig.HTTP_PROXY_PASSWORD);
			client.getCredentialsProvider().setCredentials(authScope, upc);
		}
		// 设置目标站点域名()
		HttpHost targetHost = null;
		String tarHostName = getFullDomain(url);
		if (HttpConfig.HTTP_USESSL) {
			targetHost = new HttpHost(tarHostName, 443, "https");
		} else {
			targetHost = new HttpHost(tarHostName);
		}
		// 设置代理的IP和端口
		HttpHost proxy = new HttpHost(HttpConfig.HTTP_PROXY_HOST, HttpConfig.HTTP_PROXY_PORT);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return targetHost;
	}

	/**
	 * 文件上传
	 * 
	 * @param url
	 * @param me
	 * @param encoding
	 * @param timeOut
	 * @param cookies
	 * @param headers
	 * @param localAddress
	 * @return
	 * @throws Exception
	 */
	public static HttpResult uploadFile(String url, HttpEntity me, String encoding, int timeOut, CookieStore cookies, List<Header> headers, String localAddress) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// TODO:创建代理设置
		HttpHost authedHost = getAuthed(httpclient, url);
		httpclient.getParams().setIntParameter("http.socket.timeout", timeOut * 1000);
		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getLocalHost();
			localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS, localBinding);
		}
		// https
		if (url.contains("https")) {
			httpclient = wrapClient(httpclient);
		}
		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}
		HttpPost httppost = new HttpPost(url);
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				Header h = headers.get(i);
				if (!h.getName().startsWith("$x-param")) {
					httppost.addHeader(h);
				}
			}
		}
		HttpResult httpResult = new HttpResult();
		HttpResponse response;
		try {
			httppost.setEntity(me);
			// httppost.setEntity(new StringEntity(data, encoding));
			if (HttpConfig.HTTP_PROXY_ISON) {
				response = httpclient.execute(authedHost, httppost, localContext);
			} else {
				response = httpclient.execute(httppost, localContext);
			}
			httpResult = readResponse(response, localContext);
			cookies = httpclient.getCookieStore();
			httpResult.setCookies(cookies);
		} catch (Exception e) {
			e.printStackTrace();
			httppost.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return httpResult;
	}

	public static HttpResult uploadFile(String url, HttpEntity me, String encoding, int timeOut, CookieStore cookies) throws Exception {
		return uploadFile(url, me, encoding, timeOut, cookies, null, null);
	}

	private static String getFullDomain(String url) {
		String temp = "";
		if (url.indexOf("http://") == 0) {
			temp = url.substring(7);
		} else if (url.indexOf("https://") == 0) {
			temp = url.substring(8);
		}
		if (temp.indexOf("/") > -1) {
			temp = temp.substring(0, temp.indexOf("/"));
		}
		return temp;
	}
}
