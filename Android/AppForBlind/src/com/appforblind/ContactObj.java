package com.appforblind;

import java.util.ArrayList;

public class ContactObj {

	String name;
	String p1,p2,p3;
	
	public ContactObj(String a, String b , String c , String d){
		name = a;
		p1 = b;
		p2 = c;
		p3 = d;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}
	
}
