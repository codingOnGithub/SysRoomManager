<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>条件脚本编辑</title>
<f:js pre="js/lang/view/platform/system" ></f:js>
<style type="text/css">
	html,body{
		overflow:auto;
	}
	.para-info-table thead tr th{
		text-align:center;
	}
	.para-info-table tbody tr td{
		padding:5px;
	}
	input{
		width:180px;
		height:21px;
	}
</style>
<script type="text/javascript" src="${ctx}/js/util/easyTemplate.js" ></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=conditionScript"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/system/ScriptEdit.js"></script>
<script type="text/javascript">

	$(function(){
		$("select[name='className']").change(classNameChange).trigger("change");
		$('#conditionScriptEdit').ajaxForm({success:showResponse });	
	});
	
	//类名选择事件
	function classNameChange(){
		var className = $(this).val();
		if(!className)return;
		
		var match = /^.*\.(\w*)$/.exec(className),
			name = '';
		if(match!=null){
			name = match[1];
		}
		if(!name)return;
		name = name.toLowerCase().split("",1) + name.slice(1);
		$("input[name='classInsName']").val(name);
		var url = __ctx + '/platform/system/conditionScript/getMethodsByName.ht?name='+className;
		getMethods(url);
	};
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">条件脚本编辑</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" onclick="save('conditionScriptEdit')"><span></span>保存</a>
					</div>
					<div class="l-bar-separator"></div>
					<div class="group">
						<a class="link back" href="list.ht"><span></span>返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
		<form id="conditionScriptEdit" method="post" action="save.ht">
			<div class="panel-detail">
			<input type="hidden" name="id" value="${conditionScript.id}"/>
			<input type="hidden" id="methodName" value="${conditionScript.methodName}"/>
			<textarea class="hidden" name="argument">${conditionScript.argument}</textarea>
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0">				 
				<tr>
					<th width="20%">脚本所在类的类名:</th>
					<td>
						<select name="className">
							<c:forEach items="${implClasses}" var="implClass">
								<option value="${implClass.name}"<c:if test="${conditionScript.className eq implClass.name}"> selected="selected"</c:if>>${implClass.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th width="20%">类实例名:</th>
					<td>
						<input type="text" readonly="readonly" value="${conditionScript.classInsName}" name="classInsName" />
					</td>
				</tr>
				<tr>
					<th width="20%">方法名:</th>
					<td>
						<select name="methodName">
						</select>
					</td>
				</tr>
				<tr>
					<th width="20%">方法描述:</th>
					<td>
						<input type="text" name="methodDesc" value="${conditionScript.methodDesc}"/>
					</td>
				</tr>
				<tr>
					<th width="20%">返回值类型:</th>
					<td>
						<input type="text" readonly="readonly" name="returnType" value="${conditionScript.returnType}"/>
					</td>
				</tr>
				<tr>
					<th width="20%">参数信息:</th>
					<td id="paraInfo">
					
					</td>
				</tr> 
				<tr>
					<th width="20%">是否有效:</th>
					<td>
						<select name="enable">
							<option value="0"<c:if test="${conditionScript.enable eq 0}"> selected="selected"</c:if>>是</option>
							<option value="1"<c:if test="${conditionScript.enable eq 1}"> selected="selected"</c:if>>否</option>
						</select>
					</td>
				</tr>
			</table>
			</div>
		</form>
		</div>
	</div>
	
	<div class="hidden">
		<div id="para-txt">
			<table class="table-detail para-info-table" cellpadding="0" cellspacing="0" border="0">
				<thead>
					<tr>
						<th width="10%">参数信息</th>
						<th width="25%">参数类型</th>
						<th width="25%">参数说明</th>
						<th width="35%">控件类型</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><span name="paraName"></span></td>
						<td><span name="paraType"></span></td>
						<td><input type="text" name="paraDesc"/></td>
					<%--
						<td>
							<div class="valueFrom-div">
								<select  name="paraVf" onchange="valueFormChange(this)">
									<option value="-1">--无--</option>
									<option value="0">输入</option>
									<option value="1">脚本运算</option>
								</select>
							</div>
						</td>
					--%>
						<td>
							<div class="controlType-div">
								<select name="paraCt">
									<option value="1">单行文本框</option>
									<option value="4">人员选择器(单选)</option> 
									<option value="8">人员选择器(多选)</option> 
									<option value="17">角色选择器(单选)</option>
									<option value="5">角色选择器(多选)</option> 
									<option value="18">组织选择器(单选)</option>
									<option value="6">组织选择器(多选)</option> 
									<option value="19">岗位选择器(单选)</option> 
									<option value="7">岗位选择器(多选)</option>
									<option value="0">自定义对话框</option>
<!--									<option value="11">下拉选项</option> -->
								</select>
								<span style="display: none;" id="settingSpan">
									<select id="dialog-type" name="dialog-type" onchange="dialogChange(this)" style="width:100px;">
										<option value="">请选择对话框……</option>
									</select>
									<select id="dialog-param" name="dialog-param" style="width:100px;">
										<option value="">请选择返回值……</option>
									</select>
								</span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div style="display: none;" id="template">
		<textarea name="script"></textarea>
		<select id="paraCt-temp" name="paraCt"></select>
	</div>
</body>
</html>

