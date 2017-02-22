package com.show.service;


import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class DBConnect {
	protected Mongo mongo;

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public DBConnect(String hostName, Integer port) {
		try {
			mongo = new Mongo(hostName, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	
}
