package com.kh.spring.board.model.vo;

import lombok.Data;

@Data
public class BoardImg {

	private int boardImgNo;
	private String originName;
	private String changeName;
	private int imgLevel;
	private int refBno;
}
