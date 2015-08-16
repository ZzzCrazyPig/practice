package com.crazypig.practice.linux;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

/**
 * 封装ganymed-ssh2连接远程linux的相关方法
 * 1.远程执行命令
 * 2.上传和下载
 * @author CrazyPig
 *
 */
public class SSH2Connector {
	
	private String hostIp;
	private String user;
	private String password;
	private static final int DEFAULT_PORT = 22;
	
	private Connection conn;
	private boolean authed;
	
	private static class ConnectionFactory {
		
		public static Connection makeConn(String hostIp, int port) {
			Connection conn = null;
			conn = new Connection(hostIp, port);
			return conn;
		}
		
	}
	
	public SSH2Connector(String hostIp, String user, String password) {
		this.hostIp = hostIp;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * 远程登陆linux
	 * @return
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
		conn = ConnectionFactory.makeConn(hostIp, DEFAULT_PORT);
		conn.connect();
		authed = conn.authenticateWithPassword(user, password);
		return authed;
	}
	
	/**
	 * 关闭远程连接
	 */
	public void close() {
		if(conn != null && authed) {
			conn.close();
		}
	}
	
	/**
	 * 打开一个会话
	 * @return
	 * @throws IOException
	 */
	public Session openSession() throws IOException {
		return conn.openSession();
	}
	
	/**
	 * 执行一个命令
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public ExecStatus execCmd(String cmd) throws IOException {
		Session session = this.openSession();
		ExecStatus execStatus = new ExecStatus();
		try {
			session.execCommand(cmd);
		} catch(IOException e) {
			throw e;
		} finally {
			session.close();
		}
		String exitSignal = session.getExitSignal();
		execStatus.setMsg(exitSignal);
		execStatus.setSucess(exitSignal == null);
		return execStatus;
	}
	
	/**
	 * 执行一个命令,并处理响应
	 * @param cmd
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	public ExecStatus execCmd(String cmd, ResponseHandler handler) throws IOException {
		Session session = this.openSession();
		ExecStatus execStatus = new ExecStatus();
		try {
			session.execCommand(cmd);
			handler.handle(session);
		} catch(IOException e) {
			throw e;
		} finally {
			session.close();
		}
		String exitSignal = session.getExitSignal();
		execStatus.setMsg(exitSignal);
		execStatus.setSucess(exitSignal == null);
		return execStatus;
	}
	
	/**
	 * 将远程文件下载到本地
	 * @param remoteFile
	 * @param localTargetDirectory
	 * @throws IOException
	 */
	public void download(String remoteFile, String localTargetDirectory) throws IOException {
		SCPClient scpClient = conn.createSCPClient();
		scpClient.get(remoteFile, localTargetDirectory);
	}
	
	/**
	 * 将多个远程文件下载到本地
	 * @param remoteFiles
	 * @param localTargetDirectory
	 * @throws IOException
	 */
	public void download(String[] remoteFiles, String localTargetDirectory) throws IOException {
		SCPClient scpClient = conn.createSCPClient();
		scpClient.get(remoteFiles, localTargetDirectory);
	}
	
	/**
	 * 将本地文件上传到远程目录
	 * @param localFile
	 * @param remoteTargetDirectory
	 * @throws IOException
	 */
	public void upload(String localFile, String remoteTargetDirectory) throws IOException {
		SCPClient scpClient = conn.createSCPClient();
		scpClient.put(localFile, remoteTargetDirectory);
	}
	
	/**
	 * 将多个本地文件上传到远程目录
	 * @param localFiles
	 * @param remoteTargetDirectory
	 * @throws IOException
	 */
	public void upload(String[] localFiles, String remoteTargetDirectory) throws IOException {
		SCPClient scpClient = conn.createSCPClient();
		scpClient.put(localFiles, remoteTargetDirectory);
	}
	
	/**
	 * 将字节数组写入远程文件
	 * @param data
	 * @param remoteFileName
	 * @param remoteTargetDirectory
	 * @throws IOException
	 */
	public void upload(byte[] data, String remoteFileName, String remoteTargetDirectory) throws IOException {
		SCPClient scpClient = conn.createSCPClient();
		scpClient.put(data, remoteFileName, remoteTargetDirectory);
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
