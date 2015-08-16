package com.crazypig.practice.linux;

import java.io.IOException;

import ch.ethz.ssh2.Session;

public interface ResponseHandler {
	void handle(Session session) throws IOException;
}
