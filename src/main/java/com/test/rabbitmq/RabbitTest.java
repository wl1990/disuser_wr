package com.test.rabbitmq;

import java.io.Serializable;

public class RabbitTest implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9211737369307038616L;
	private String name;
	private String pass;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}
