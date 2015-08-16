package com.crazypig.practice.linux;

/**
 * 封装执行结果
 * @author CrazyPig
 *
 */
public class ExecStatus {
	
	private boolean sucess;
	private String msg;
	public boolean isSucess() {
		return sucess;
	}
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
