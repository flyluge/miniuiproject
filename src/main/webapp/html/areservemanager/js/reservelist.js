mini.parse();
var grid = mini.get("datagrid1");
grid.load();
// 添加一条记录
function add() {
	mini.open({
		targetWindow : window,
		url : "addreserve.html",
		title : "新增记录",
		width : 600,
		height : 400,
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
		url : "updatereserve.html",
		title : "修改记录",
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
// 条件查询
function search() {
	grid.load({
		stuname : mini.get("key").getValue(),
		profession : mini.get("profession").getValue()
	});
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
		url : "/project/ReserveManagerServlet?method=deleteById&reserveno="
				+ id,
		success : function(text) {
			grid.reload();
		},
		error : function() {
		}
	});
}
// 回车效果
function onKeyEnter(e) {
	search();
}
// 转换日期
function changetoadddate(e) {
	var date = new Date(e.value);
	return mini.formatDate(date, "yyyy-MM-dd HH:mm");
}
// 状态
function changetoreservestatus(e) {
	switch (e.value) {
		case 1:
			return "用餐中";
		case 2:
			return "取消";
		case 0:
			return "预约中";
	}
}
// 根据借阅次数修改颜色
function changeColor(e) {
	if (e.value > 10) {
		e.cellStyle = "color:red";
	} else {
		e.cellStyle = "color:green";
	}
	return e.value;
}
// 更新和删除按钮
function updateAnddelete(e) {
	var temp = $("#addanddeletebutton").html();
	var items = Mustache.render(temp, {
		id : e.row.reserveno
	});
	return items;
}
//刷新
function reloadgrid(){
	var data=grid.getData();
	$.each(data,function(i,n){
		if(n.reservestatus==0){
			var time=(new Date().getTime()-n.starttime)/(1000*60);
			if(time>15.0){
				var row=grid.getRow(i);
				grid.addRowCls(row,"yellowrow");
			}
		}
	})
}
