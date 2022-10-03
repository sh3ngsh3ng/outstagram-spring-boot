package com.dxc.Outstagram.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;

public class Image{
	public byte[] content;
	
//	public Image(byte[] byteArray) {
//		this(byteArray, "resource loaded from byte array");
//	}



	public byte[] getContent() {
		return content;
	}

	public Image() {
		super();
	}

	public Image(byte[] content) {
		super();
		this.content = content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.content);
	}
	
}
