package com.hotent.platform.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.core.bpm.model.ProcessTask;
import com.hotent.core.page.PageBean;
import com.hotent.core.util.ContextUtil;
import com.hotent.platform.dao.bpm.ProcessRunDao;
import com.hotent.platform.dao.bpm.TaskDao;
import com.hotent.platform.dao.mail.OutMailDao;
import com.hotent.platform.dao.system.MessageSendDao;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.model.mail.OutMail;
import com.hotent.platform.model.system.MessageSend;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.bpm.BpmDefinitionService;

public class DesktopService {
	@Resource
	private MessageSendDao messageSendDao;
	@Resource
	private TaskDao taskDao;
	@Resource
	private ProcessRunDao processRunDao;
	@Resource
	private OutMailDao outMailDao;
	@Resource
	private BpmDefinitionService bpmDefinitionService;
	
	//@Resource
	//private StaffInfoMainDao staffInfoMaonDao;
	
	/**
	 * <p>Description: 获取个人信息<p>
	 * @return
	 */
	/*public StaffInfoMain getStaffInfoMain(){
		StaffInfoMain staffInfoMain = null;
		SysUser u = ContextUtil.getCurrentUser();
		if (u != null) {
			String account = u.getAccount();
			staffInfoMain = staffInfoMaonDao.getByPortalID(account);
		}
		
		//如果从SM_STAFFINFO_MAIN中获取不到数据时　实例化一个为空的对象
		if (staffInfoMain == null) {
			staffInfoMain = new StaffInfoMain();
		}
		
		return staffInfoMain;
	}*/

	/**
	 * 个人信息
	 * 
	 * @return
	 */
	public SysUser getUser() {
		SysUser u = ContextUtil.getCurrentUser();
		return u;

	}

	/**
	 * 内部消息
	 * 
	 * @return
	 */
	public List<?> getMessage() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		List<MessageSend> list = messageSendDao.getNotReadMsgByUserId(
				ContextUtil.getCurrentUserId(), pb);
		return list;
	}

	/**
	 * 待办任务
	 * 
	 * @return
	 */
	public List<ProcessTask> forMe() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		List<ProcessTask> list = new ArrayList<ProcessTask>();
		list = taskDao.getTasks(ContextUtil.getCurrentUserId(), null, null,
				null, null, "desc", pb);
		return list;
	}

	/**
	 * 我的审批的流程
	 * 
	 * @return
	 */
	public List<ProcessRun> myAttend() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		//去掉进行分页的总记录数的查询
		pb.setShowTotal(false);
		List<ProcessRun> list = processRunDao.getMyAttend(
				ContextUtil.getCurrentUserId(), null, pb);
		return list;
	}

	/**
	 * 我发起的流程
	 * 
	 * @return
	 */
	public List<ProcessRun> myStart() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		List<ProcessRun> list = processRunDao.myStart(
				ContextUtil.getCurrentUserId(), pb);
		return list;
	}

	/**
	 * 获取用户未读邮件。
	 * 以时间降序排序，最多取10条。
	 * @return 用户未读邮件对象列表
	 */
	public List<OutMail> myNewMail() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		List<OutMail> list = outMailDao.getMailByUserId(
				ContextUtil.getCurrentUserId(), pb);
		return list;

	}
	
	/**
	 * 获取用户可以访问的流程定义
	 * @return
	 */
	public List<BpmDefinition> myProcess(){
		
		Long curUserId=ContextUtil.getCurrentUserId();
		List<BpmDefinition> list=bpmDefinitionService.getMyList(curUserId);
		return list;
	}
	
	
	/**
	 * 我的办结
	 * @return
	 */
	public List<ProcessRun> myCompleted() {
		long curUserId=ContextUtil.getCurrentUserId();
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		//去掉进行分页的总记录数的查询
		pb.setShowTotal(false);
		List<ProcessRun> list=processRunDao.myCompleted(curUserId,pb);
		return list;
	}
	
	/**
	 * 已办事宜
	 * @return
	 */
	public List<ProcessRun> alreadyMatters() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		//去掉进行分页的总记录数的查询
		pb.setShowTotal(false);
		List<ProcessRun> list=processRunDao.Myalready(ContextUtil.getCurrentUserId(),pb);
		return list;
	}
	
	/**
	 * 办结事宜
	 * @return
	 */
	public List<ProcessRun> completedMatters() {
		PageBean pb = new PageBean();
		pb.setCurrentPage(1);
		pb.setPagesize(10);
		//去掉进行分页的总记录数的查询
		pb.setShowTotal(false);
		List<ProcessRun> list=processRunDao.completedMatters(ContextUtil.getCurrentUserId(),pb);
		return list;
	}

	
	
}
