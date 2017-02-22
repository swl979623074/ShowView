package com.show.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class DBManager extends DBConnect {
	/**
	 * DBManager的构造函数 初始化db、collectionName
	 * @param hostName 父类的构造函数的参数 主机名
	 * @param port 父类的构造函数的参数 端口
	 * @param DBName 数据库名称
	 * @param collectionName 集合名称
	 */
	public DBManager(String hostName, Integer port,String DBName,String collectionName){
		super(hostName, port);
		setDb(mongo.getDB(DBName));
		setCollectionName(collectionName);
	}
	
	private DB db;	
	
	private String collectionName;
	
	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}
	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	/**
	 * 获取所有数据
	 * @return List<DBObject> DBObject的集合
	 */
	public Map<String,Object> getList(int from,int rows) {
		Map<String,Object> map = new HashMap<String,Object>();
		DBObject filter = new BasicDBObject("_id",0);
		DBCollection collection = db.getCollection(getCollectionName());
		DBCursor cur = collection.find(null,filter).sort(new BasicDBObject("time",-1)).skip(from).limit(rows);
		int num = collection.find(null,filter).count();
		map.put("total", num);
		map.put("rows", cur.toArray());
		return map;
	}
	
	/**
	 * 根据状态查询数据
	 * @param status PASS/ERROR/ABORT
	 * @return List<DBObject> DBObject的集合
	 */
	public Map<String,Object> getListByStatus(String status,int from,int rows){
		Map<String,Object> map = new HashMap<String,Object>();
		DBObject find = new BasicDBObject("status",status);
		DBObject filter = new BasicDBObject("_id",0);
		
		DBCollection collection = db.getCollection(getCollectionName());
		DBCursor cur = collection.find(find,filter).sort(new BasicDBObject("time",-1)).skip(from).limit(rows);
		int num = collection.find(find,filter).count();
		map.put("total", num);
		map.put("rows", cur.toArray());
		return map;
	}
	
	/**
	 * 根据模块名查询数据
	 * @param moduleName
	 * @return List<DBObject> DBObject的集合
	 */
	public Map<String,Object> getListByModuleName(String moduleName,int from,int rows){
		Map<String,Object> map = new HashMap<String,Object>();
		DBObject find = new BasicDBObject("module",moduleName);
		DBObject filter = new BasicDBObject("_id",0);
		
		DBCollection collection = db.getCollection(getCollectionName());
		DBCursor cur = collection.find(find,filter).sort(new BasicDBObject("time",-1)).skip(from).limit(rows);
		int num = collection.find(find,filter).count();
		map.put("total", num);
		map.put("rows", cur.toArray());
		return map;
	}
	
	/**
	 * 增加数据
	 * @return Internet 添加的条目数
	 */
	public int insertOne(DBObject data) {
		DBCollection collection = db.getCollection(getCollectionName());
	    return collection.insert(data).getN();
	}
	
	/**
	 * 获取某键的值的不重复集合
	 * @return List<String> 键key的值的集合
	 */
	public List<String> getModuleList(){
		DBCollection collection = db.getCollection(getCollectionName());
		List<String> list= collection.distinct("module");
		return list;
	}
	
	/**
	 * 删除所有数据
	 * @return Internet 删除的条目数
	 */
	public int removeAllTest(){
		DBCollection collection = db.getCollection(getCollectionName());
		return collection.remove(new BasicDBObject()).getN();
	}
}
