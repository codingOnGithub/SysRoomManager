if (typeof FlowUtil == 'undefined') {
	FlowUtil = {};
}

/**
 * 启动流程。
 * @param defId 流程定义ID。
 */
FlowUtil.startFlow=function(defId,actDefId){
	var url= __ctx +"/platform/bpm/bpmDefinition/getCanDirectStart.ht";
	var params={defId:defId};
	$.post(url,params,function(data){
		if(data){
			var callBack=function(rtn){
				if(!rtn) return;
				var flowUrl= __ctx +"/platform/bpm/task/startFlow.ht";
				var parameters={actDefId:actDefId};
				$.post(flowUrl,parameters,function(responseText){
					var obj=new com.hotent.form.ResultMessage(responseText);
					if(obj.isSuccess()){//成功
						$.ligerDialog.success("启动流程成功!",'提示信息');
					}
					else{
						$.ligerDialog.error("启动流程失败!",'出错了');
					}
				});
			};
			$.ligerDialog.confirm("需要启动流程吗?",'提示信息',callBack);
		}else{
			var url=__ctx +"/platform/bpm/task/startFlowForm.ht?defId="+defId;
			jQuery.openFullWindow(url);
		}
	});
};



/**
 * 流程追回。
 * @param runId
 * @param memo
 */
FlowUtil.recover=function(conf){
	if(!conf) conf={};	
	var url= __ctx +"/platform/bpm/processRun/checkRecover.ht";
	if(conf.backToStart==0){
		url= __ctx +"/platform/bpm/processRun/checkRedo.ht";
	}	
	var params={runId:conf.runId ,backToStart: conf.backToStart};
	$.post(url,params,function(data){
		 var obj=new com.hotent.form.ResultMessage(data);
		 if(obj.isSuccess()){
				var url=__ctx + '/platform/bpm/processRun/recoverDialog.ht?runId=' + conf.runId +'&backToStart=' + conf.backToStart;
				if(conf.backToStart==0){
					url=__ctx + '/platform/bpm/processRun/redoDialog.ht?runId=' + conf.runId +'&backToStart=' + conf.backToStart;
				}
				var dialogWidth=500;
				var dialogHeight=300;
				conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1,reload:true},conf);
				var winArgs="dialogWidth="+conf.dialogWidth+"px;dialogHeight="+conf.dialogHeight +"px;help=" + conf.help +";status=" + conf.status +";scroll=" + conf.scroll +";center=" +conf.center;
				url=url.getNewUrl();
				var rtn=window.showModalDialog(url,"",winArgs);
				if(rtn && conf.callback){
					conf.callback(this);
				}
		 }
		 else{
			 $.ligerDialog.err($lang.tip.msg,$lang_js.bpm.flowUtil.recover.failure,obj.getMessage());
		 }
	});
};

