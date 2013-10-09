package com.sporthenon.web;

public class RenderOptions {

	private boolean headerDisabled;
	
	private boolean picturesDisabled;
	
	private String style;

	public boolean isHeaderDisabled() {
		return headerDisabled;
	}

	public void setHeaderDisabled(boolean headerDisabled) {
		this.headerDisabled = headerDisabled;
	}

	public boolean isPicturesDisabled() {
		return picturesDisabled;
	}

	public void setPicturesDisabled(boolean picturesDisabled) {
		this.picturesDisabled = picturesDisabled;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
}
