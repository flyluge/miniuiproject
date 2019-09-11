mini.parse();
var grid = mini.get("datagrid1");
grid.load();
//回车效果
function onKeyEnter(e) {
	search();
}
//条件查询
function search() {
	grid.load({
		isuse : mini.get("isuse").getValue(),
		holdnum : mini.get("holdnum").getValue()
	});
}
// 添加一条记录
function add() {
	mini.open({
		targetWindow : window,
		url : "addtable.html",
		title : "新增记录",
		width : 600,
		height : 350,
		allowResize : false,
		onload : function() {
			var iframe = this.getIFrameEl();
			var data = {
				action : "new"
			};
			iframe.contentWindow.SetData(data);
		},
		ondestroy : function(action) {
			grid.reload();
		}
	});
}
// 选中行修改
function edit() {
	var row = grid.getSelected();
	if (row) {
		openEditWindow(row.stuid);
	} else {
		mini.alert("请选中一条记录");
	}
}
// 打开编辑窗口 修改单条数据
function openEditWindow(id) {
	mini.open({
		targetWindow : window,
		url : "updatestu.html",
		title : "修改学生记录",
		width : 600,
		height : 300,
		allowResize : false,
		onload : function() {
			var iframe = this.getIFrameEl();
			var data = {
				action : "edit",
				id : id
			};
			iframe.contentWindow.SetData(data);
		},
		ondestroy : function(action) {
			grid.reload();
		}
	});
}
// 删除多条数据
function remove() {
	var rows = grid.getSelecteds();
	if (rows.length > 0) {
		mini.confirm("确定删除选中记录？", "", function(action) {
			if (action == "ok") {
				var ids = [];
				for (var i = 0; i < rows.length; i++) {
					var r = rows[i];
					ids.push(r.tableno);
				}
				var id = ids.join(',');
				grid.loading("操作中，请稍后......");
				deleteById(id);
			}
		})
	} else {
		mini.alert("请选中一条记录");
	}
}
// 删除单条数据
function removeOne(id) {
	mini.confirm("确定删除该条记录吗?", "", function(action) {
		if (action == "ok") {
			deleteById(id);
		}
	})
}
// 通过id删除数据
function deleteById(id) {
	$.ajax({
		url : "/project/TableManagerServlet?method=deleteById&tableno="
				+ id,
		success : function(text) {
			var data=mini.decode(text);
			if(data.message){
				mini.alert("删除成功");
				grid.reload();
			}else{
				mini.alert(data.data);
			}
		},
		error : function() {
		}
	});
}
// 餐桌类型
function changetoholdnum(e) {
	switch (e.value) {
	case 2:
		return "2人桌";
	case 4:
		return "4人桌";
	case 6:
		return "6人桌";
	}
}
// 使用状态
function changetoisuse(e) {
	switch (e.value) {
	case 1:
		return "是";
	case 0:
		e.cellStyle = "background:green;color:white";
		return "否";
	}
}
// 更新和删除按钮
function updateAnddelete(e) {
	var temp = $("#addanddeletebutton").html();
	var items = Mustache.render(temp, {
		id : e.row.tableno
	});
	return items;
}
// 汇总行
function onDrawSummaryCell(e) {
	// var s="总工资: 100$";
	var s = e.result.message;
	if (e.field == "count")
		e.cellHtml = "是否查询到:" + s;
}
