mini.parse();
var form = new mini.Form("#addform");
// 添加信息
function add() {
	var f = new mini.Form("#addform");
	f.validate();
	if (f.isValid()) {
		var data = f.getData("yyyy-MM-dd HH:mm:ss");
		var jsonString = mini.encode(data);
		var d1=mini.parseDate (data.starttime);
		var d2=mini.parseDate (data.endtime);
		if(d1.getTime()>d2.getTime()){
			mini.alert("用餐开始时间不能大于用餐结束时间");
		}else{
			$.ajax({
				url : "/project/ReserveManagerServlet?method=add",
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
		}
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
		// 跨页面传递的数据对象，克隆后才可以安全使用
		data = mini.clone(data);
		$.ajax({
			url : "/project/ReserveManagerServlet?method=getId",
			cache : false,
			success : function(text) {
				var o = mini.decode(text);
				mini.getbyName("reserveno").setValue(o.data);
			}
		});
	}
}
function changetable(e) {
	$.ajax({
		url : "/project/ReserveManagerServlet?method=findTable&peoplenum="
				+ e.value,
		cache : false,
		success : function(text) {
			var o = mini.decode(text);
			if (o.message) {
				var tn=mini.getByName("tableno");
				tn.setTextField("tableno");
				tn.setValueField("tableno");
				tn.setData(o.data);
				tn.setValue(o.data[0].tableno);
			} else {
				mini.getByName("tableno").setValue("");
				mini.alert("不存在对应的餐桌");
			}
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
