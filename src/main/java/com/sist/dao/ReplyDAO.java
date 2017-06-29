package com.sist.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDAO {
	@Autowired
	private MongoTemplate mt;
	
	public void replyInsert(ReplyVO vo){
		mt.insert(vo, "movie");
		
	}
	
	public List<ReplyVO> replyAllData(){
		Query query=new Query();
		
		return mt.find(query, ReplyVO.class, "movie");
	}
	
}
