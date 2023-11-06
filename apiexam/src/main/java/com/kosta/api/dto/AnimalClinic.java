package com.kosta.api.dto;

public class AnimalClinic {
	private String trdStateNm; // 영업상태명
	private String siteTel; // 전화번호
	private String rdNwhlAddr; // 도로주소
	private String bplcNm; // 사업장명
	private String x; // 좌표 x
	private String y; // 좌표 y

	public AnimalClinic(String trdStateNm, String siteTel, String rdNwhlAddr, String bplcNm, String x, String y) {
		this.trdStateNm = trdStateNm;
		this.siteTel = siteTel;
		this.rdNwhlAddr = rdNwhlAddr;
		this.bplcNm = bplcNm;
		this.x = x;
		this.y = y;
	}

	public String getTrdStateNm() {
		return trdStateNm;
	}

	public void setTrdStateNm(String trdStateNm) {
		this.trdStateNm = trdStateNm;
	}

	public String getSiteTel() {
		return siteTel;
	}

	public void setSiteTel(String siteTel) {
		this.siteTel = siteTel;
	}

	public String getRdNwhlAddr() {
		return rdNwhlAddr;
	}

	public void setRdNwhlAddr(String rdNwhlAddr) {
		this.rdNwhlAddr = rdNwhlAddr;
	}

	public String getBplcNm() {
		return bplcNm;
	}

	public void setBplcNm(String bplcNm) {
		this.bplcNm = bplcNm;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

}
