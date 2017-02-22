package com.show.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.show.service.DBConnect;
import com.show.service.DBManager;
import com.show.tool.MVC;

@Controller
@RequestMapping("/case")
public class CaseController {
	private DBConnect conn = new DBManager("127.0.0.1", 12345, "test", "blog");

	/**
	 * 获取所有数据
	 * 
	 * @return ModelAndView 所有数据
	 * @throws IOException
	 */
	@RequestMapping("/getList")
	public ModelAndView getList(Integer rows, Integer page) throws IOException {
		
		Map<String,Object> oldMap = new HashMap<String,Object>();
		oldMap = ((DBManager) conn).getList((page - 1) * rows,rows);
		return MVC.toString(oldMap);
	}

	/**
	 * 根据状态查询数据
	 * 
	 * @param status
	 *            status PASS/ERROR/ABORT
	 * @return ModelAndView
	 */
	@RequestMapping("/getListByStatus")
	public ModelAndView getListByStatus(String status, Integer rows,
			Integer page) {
		Map<String,Object> oldMap = new HashMap<String,Object>();
		oldMap = ((DBManager) conn).getListByStatus(status,(page - 1) * rows,rows);
		return MVC.toString(oldMap);
	}

	/**
	 * 根据模块名查询数据
	 * 
	 * @param moduleName
	 * @return ModelAndView
	 */
	@RequestMapping("/getListByModuleName")
	public ModelAndView getListByModuleName(String moduleName, Integer rows,
			Integer page) {
		Map<String,Object> oldMap = new HashMap<String,Object>();
		oldMap = ((DBManager) conn)
				.getListByModuleName(moduleName,(page - 1) * rows,rows);
		return MVC.toString(oldMap);
	}

	/**
	 * 增加数据
	 * 
	 * @param module
	 * @param function
	 * @param system
	 * @param platform
	 * @param time
	 * @param status
	 * @return ModelAndView
	 * @throws IOException
	 */
	@RequestMapping("/insertOne")
	public ModelAndView insertOne(HttpServletRequest req) throws IOException {
		ServletInputStream ris = req.getInputStream();
		StringBuilder content = new StringBuilder();
		byte[] b = new byte[1024];
		int lens = -1;
		while ((lens = ris.read(b)) > 0) {
			content.append(new String(b, 0, lens));
		}
		String strcont = content.toString();// 内容

		JSONObject jsonObj = JSONObject.fromObject(strcont);

		DBObject obj = new BasicDBObject();
		obj.put("module", jsonObj.getString("module"));
		obj.put("function", jsonObj.getString("function"));
		obj.put("system", jsonObj.getString("system"));
		obj.put("platform", jsonObj.getString("platform"));
		obj.put("time", jsonObj.getString("time"));
		obj.put("status", jsonObj.getString("status"));
		obj.put("IP", jsonObj.getString("IP"));
		
		Map<String, Object> map = new HashMap<String, Object>();

		int len = ((DBManager) conn).insertOne(obj);

		map.put("status", (len == 0) ? ("SUCCESS") : ("ERROR"));
		return MVC.toString(map);
	}

	/**
	 * 获取某键的值的不重复集合
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("/getModuleList")
	public ModelAndView getModuleList() {
		List<String> list = ((DBManager) conn).getModuleList();
		int len = list.size();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", (len > 0) ? ("SUCCESS") : ("ERROR"));
		map.put("data", list);
		return MVC.toString(map);
	}

	/**
	 * 删除所有数据
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping("/removeAllTest")
	public ModelAndView removeAllTest() {
		int len = ((DBManager) conn).removeAllTest();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", (len > 0) ? ("SUCCESS") : ("ERROR"));
		map.put("n", len);
		return MVC.toString(map);
	}
}
