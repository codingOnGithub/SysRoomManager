function TaskChangePathWindow(conf){
	var taskId=conf.taskId;
	var winArgs="dialogWidth=680px;dialogHeight=340px;help=0;status=0;scroll=1;center=1";
	url=__ctx + "/platform/bpm/task/changePath.ht?taskId="+ taskId;
	url=url.getNewUrl();
	
	var rtn=window.showModalDialog(url,"",winArgs);
	if(conf.callback)
	{
		if(rtn!=undefined){
			 conf.callback.call(this,rtn);
		}
	}
}