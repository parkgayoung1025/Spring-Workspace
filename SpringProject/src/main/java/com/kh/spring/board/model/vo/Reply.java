package com.kh.spring.board.model.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Reply {

	private int replyNo;
	private String replyContent;
	private int refBno;
	private String replyWriter;
	private int userNo;
	private Date createDate;
	private String status;
}
