package com.kh.spring.member.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository
public class MemberDao {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public Member loginMember(Member inputMember) {
		
		return sqlSession.selectOne("memberMapper.loginMember", inputMember);
	}
	
	public int insertMember(Member inputMember) {
		
		return sqlSession.insert("memberMapper.insertMember", inputMember);
	}
	
	public ArrayList<Member> selectAll() {
		
		return (ArrayList)sqlSession.selectList("memberMapper.selectAll");
	}
}
