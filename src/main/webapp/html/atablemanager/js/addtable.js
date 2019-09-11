mini.parse();
var form = new mini.Form("#addform");
// 添加图书信息
function add() {
	var f = new mini.Form("#addform");
	f.validate();
	if (f.isValid()) {
		var data = f.getData("yyyy-MM-dd HH:mm:ss");
		var jsonString = mini.encode(data);
		$.ajax({
			url : "/project/TableManagerServlet?method=add",
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
	if (data.action == "new") {
		$.ajax({
			url : "/project/TableManagerServlet?method=getId&holdnum=" + 2,
			cache : false,
			success : function(text) {
				var o = mini.decode(text);
				mini.getByName("tableno").setValue(o.data);
			}
		});
	}
}
function getid(e) {
	$.ajax({
		url : "/project/TableManagerServlet?method=getId&holdnum="
				+ e.value,
		cache : false,
		success : function(text) {
			var o = mini.decode(text);
			mini.getByName("tableno").setValue(o.data);
		}
	});
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
