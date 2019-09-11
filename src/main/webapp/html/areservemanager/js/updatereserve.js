mini.parse();
// 初始化表单
var form = new mini.Form("#updateform");
// 修改
function update() {
	var f = new mini.Form("#updateform");
	f.validate();
	if (f.isValid()) {
		var data = f.getData("yyyy-MM-dd HH:mm");
		var jsonString = mini.encode(data);
		$.ajax({
			url : "/project/ReserveManagerServlet?method=update",
			type : "post",
			data : data,
			dataType : "json",
			success : function(text) {
				if (text.message) {
					mini.alert(text.data);
					CloseWindow("cancel");
				} else {
					mini.alert(text.data);
				}
			}
		})
	} else {
		mini.alert("请完善表单");
	}
}
// 限制日期
function onDrawDate(e) {
	var date = e.date;
	var d = new Date();
	if (date.getTime() > d.getTime()) {
		e.allowSelect = false;
	}
}
// 标准方法接口定义
function SetData(data) {
	if (data.action == "edit") {
		// 跨页面传递的数据对象，克隆后才可以安全使用
		data = mini.clone(data);
		$.ajax({
			url : "/project/ReserveManagerServlet?method=findById&reserveno="
					+ data.id,
			cache : false,
			success : function(text) {
				var o = mini.decode(text);
				var stime=new Date(o.data.starttime);
				var etime=new Date(o.data.endtime);
				o.data.starttime=mini.formatDate(stime,"yyyy-MM-dd HH:mm");
				o.data.endtime=mini.formatDate(etime,"yyyy-MM-dd HH:mm");
				form.setData(o.data);
			}
		});
	}
}
function GetData() {
	var o = form.getData();
	return o;
}
function CloseWindow(action) {
	if (action == "close" && form.isChanged()) {
		if (confirm("数据被修改了，是否先保存？")) {
			return false;
		}
	}
	if (window.CloseOwnerWindow)
		return window.CloseOwnerWindow(action);
	else
		window.close();
}
function onOk(e) {
	SaveData();
}
function onCancel(e) {
	CloseWindow("cancel");
}