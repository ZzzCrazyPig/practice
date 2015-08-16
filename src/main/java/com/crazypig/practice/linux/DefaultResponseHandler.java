package com.crazypig.practice.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 远程执行命令的默认响应处理器,将输出结果放入队列中
 * @author CrazyPig
 *
 */
public class DefaultResponseHandler implements ResponseHandler {
	
	private BlockingQueue<String> msgQueue;
	
	public DefaultResponseHandler(BlockingQueue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}

	public void handle(Session session) throws IOException {
		InputStream in = new StreamGobbler(session.getStdout());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while((line = reader.readLine()) != null) {
			msgQueue.offer(line);
		}
		reader.close();
	}

}
