function loadGrid(strurl) {
	$("#tableView").datagrid({
		pagination : true,
		pageList : [ 10, 20, 50, 100 ,250,500],
		pageSize : 50,
		fitColumns : true,
		rownumbers : true,
		singleSelect : true,
		url : strurl,

		columns : [ [{
			field : 'IP',
			title : 'IP',
			width : 10,
			align : "center"
		},  {
			field : 'module',
			title : '模块',
			width : 10,
			align : "center"
		}, {
			field : 'function',
			title : '方法',
			width : 10,
			align : "center"
		}, {
			field : 'system',
			title : '系统',
			width : 10,
			align : "center"
		}, {
			field : 'platform',
			title : '平台',
			width : 10,
			align : "center"
		}, {
			field : 'time',
			title : '时间',
			width : 10,
			align : "center"
		}, {
			field : 'status',
			title : '状态',
			width : 10,
			align : "center",
			styler : function(value, row, index) {
				var fontcolor = "";
				switch (value) {
				case "PASS":
					fontcolor = "green";
					break;
				case "ERROR":
					fontcolor = "red";
					break;
				case "ABORT":
					fontcolor = "#EEB422";
					break;
				default:
					break;
				}
				return "color:" + fontcolor + ";"
			}
		} ] ],
		onLoadSuccess : function() {
			console.log("success")
		},
		onLoadError : function() {
			console.log("error")
		}
	});

	var p = $("#tableView").datagrid("getPager");
	$(p).pagination({
		pagePosition : "bottom",
		beforePageText : '第',// 页数文本框前显示的汉字
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
	});
}

/** ************************************Module************************************* */

/**
 * 描述：从服务端获取数据
 */
(function() {
	window.DATABASE = {
		getModuleList : function(cb) {
			$.post('/ShowView/case/getModuleList', {}, function(msg, status) {
				cb(msg);
			})
		},
		getList : function() {
			var url = "/ShowView/case/getList";
			loadGrid(url);
		},
		getListByModuleName : function(moduleName) {
			var url = "/ShowView/case/getListByModuleName?moduleName="
					+ moduleName;

			loadGrid(url);
		},
		getListByStatus : function(status) {
			var url = "/ShowView/case/getListByStatus?status=" + status;
			loadGrid(url);
		},
		removeAllTest : function(cb) {
			$.post('/ShowView/case/removeAllTest', {}, function(msg, status) {
				cb(msg);
			})
		}
	};
})();

/** ************************************View************************************* */

(function() {
	window.VIEWS = {
		showModuleList : function(msg) {
			if (msg.status != "SUCCESS")
				return;
			var data = msg.data;
			var len = data.length;
			var html = "";
			for (var i = 0; i < len; i++) {
				html += "<li>" + data[i] + "</li>";
			}
			moduleList.innerHTML = html;
		},
		updateModuleListClass : function(elementNode) {
			var lightskyblueEle = document
					.getElementsByClassName("lightskyblue")[0];

			if (lightskyblueEle) {
				lightskyblueEle.classList.remove("lightskyblue");
			}
			if (elementNode && elementNode.nodeName.toLowerCase() == "li")
				elementNode.classList.add("lightskyblue");
		},
		updateTypeNavStatus : function() {
			$('input[name="type"]:checked').attr("checked", false);
		}
	}
})();

/** ************************************Controller************************************* */

/**
 * 描述：获取模块列表,刷新时自动加载
 */
(function() {
	setTimeout(function() {
		DATABASE.getModuleList(VIEWS.showModuleList);
		DATABASE.getList();
	}, 500)
})();

/**
 * 描述：为模块列表添加单击事件
 */
(function() {
	moduleList.addEventListener("click", function(e) {
		VIEWS.updateTypeNavStatus();
		var elementNode = e.target;
		var nodeName = elementNode.nodeName.toLowerCase();
		if (nodeName != "li")
			return;
		var moduleName = elementNode.innerHTML;
		DATABASE.getListByModuleName(moduleName, VIEWS.showContentList);
		VIEWS.updateModuleListClass(elementNode);
	})
})();

/**
 * 描述：为单选按钮添加单击事件
 */
(function() {
	typeNav.addEventListener("click", function(e) {
		VIEWS.updateModuleListClass();
		var elementNode = e.target;
		var nodeName = elementNode.nodeName.toLowerCase();
		if (nodeName != "input")
			return;
		var typeName = elementNode.value;
		switch (typeName) {
		case "ALL":
			DATABASE.getList();
			break;
		case "PASS":
			DATABASE.getListByStatus(typeName);
			break;
		case "ERROR":
			DATABASE.getListByStatus(typeName);
			break;
		case "ABORT":
			DATABASE.getListByStatus(typeName);
			break;
		default:
			break;
		}
	})
})();

/**
 * 描述：为移除按钮添加单击事件
 */
(function() {
	removeBtn.addEventListener('click', function() {
		var result = confirm("Do you really want to remove all?");
		if (result == true) {
			DATABASE.removeAllTest(function(msg) {
				console.log(msg)
				alert(msg.status + " , remove " + msg.n);
				//window.location.reload();
			})
		}
	})
})();
