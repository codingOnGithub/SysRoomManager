package com.hotent.platform.service.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.hotent.core.bpm.model.FlowNode;
import com.hotent.core.bpm.model.NodeCache;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.XmlBeanUtil;
import com.hotent.platform.dao.bpm.BpmDefinitionDao;
import com.hotent.platform.dao.bpm.BpmNodeButtonDao;
import com.hotent.platform.model.bpm.BpmDefinition;
import com.hotent.platform.model.bpm.BpmNodeButton;
import com.hotent.platform.model.bpm.BpmButton;
import com.hotent.platform.model.bpm.BpmNodeButtonXml;
import com.hotent.platform.service.form.FormUtil;

/**
 * 对象功能:自定义工具条 Service类 开发公司:广州宏天软件有限公司 开发人员:ray 创建时间:2012-07-25 18:26:12
 */
@Service
public class BpmNodeButtonService extends BaseService<BpmNodeButton> {
	@Resource
	private BpmNodeButtonDao dao;

	@Resource
	private BpmDefinitionDao bpmDefinitionDao;

	@Resource
	private BpmService bpmService;

	public BpmNodeButtonService() {
	}
	@Override
	protected IEntityDao<BpmNodeButton, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 根据流程定义ID和节点ID获取操作按钮。
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<BpmNodeButton> getByDefNodeId(Long defId, String nodeId) {
		return dao.getByDefNodeId(defId, nodeId);
	}

	/**
	 * 根据流程定义ID获取起始表单的操作按钮数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	public List<BpmNodeButton> getByStartForm(Long defId) {
		List<BpmNodeButton> list = dao.getByStartForm(defId);
		return list;
	}

	/**
	 * 获取流程定义的按钮。
	 * 
	 * @param defId
	 * @return
	 */
	public Map<String, List<BpmNodeButton>> getMapByStartForm(Long defId) {
		Map<String, List<BpmNodeButton>> map = new HashMap<String, List<BpmNodeButton>>();
		List<BpmNodeButton> list = dao.getByStartForm(defId);
		if (BeanUtils.isEmpty(list)) {
			return map;
		}
		for (BpmNodeButton bpmNodeButton : list) {
			addItem(map, bpmNodeButton, "button");
		}
		return map;
	}

	/**
	 * 根据流程定义ID和流程节点ID获取按钮。
	 * 
	 * @param defId
	 *            流程定义id。
	 * @param nodeId
	 *            流程节点id。
	 * @return
	 */
	public Map<String, List<BpmNodeButton>> getMapByDefNodeId(Long defId,
			String nodeId) {
		Map<String, List<BpmNodeButton>> map = new HashMap<String, List<BpmNodeButton>>();
		List<BpmNodeButton> list = dao.getByDefNodeId(defId, nodeId);
		if (BeanUtils.isEmpty(list)) {
			return map;
		}
		for (BpmNodeButton bpmNodeButton : list) {
			addItem(map, bpmNodeButton, "button");
		}
		return map;
	}

	/**
	 * 根据流程定义ID获取获取节点和操作按钮的映射。
	 * 
	 * @param actDefId
	 * @return
	 */
	public Map<String, List<BpmNodeButton>> getMapByDefId(Long defId) {
		List<BpmNodeButton> list = dao.getByDefId(defId);
		Map<String, List<BpmNodeButton>> map = new HashMap<String, List<BpmNodeButton>>();
		if (BeanUtils.isEmpty(list))
			return map;
		for (BpmNodeButton bpmNodeButton : list) {
			if (bpmNodeButton.getIsstartform() == 1) {
				addItem(map, bpmNodeButton, "start");
			} else {
				addItem(map, bpmNodeButton, bpmNodeButton.getNodeid());
			}
		}
		return map;
	}

	/**
	 * 添加操作按钮到
	 * 
	 * @param map
	 * @param bpmNodeButton
	 * @param key
	 */
	private void addItem(Map<String, List<BpmNodeButton>> map,BpmNodeButton bpmNodeButton, String key)
	{
		if (map.containsKey(key)) {
			map.get(key).add(bpmNodeButton);
		} else {
			List<BpmNodeButton> list = new ArrayList<BpmNodeButton>();
			list.add(bpmNodeButton);
			map.put(key, list);
		}
	}

	/**
	 * 根据流程定义和节点操作类型判断是否已经存在。
	 * 
	 * @param bpmNodeButton
	 *            按钮操作。
	 * @return
	 */
	public boolean isOperatorExist(BpmNodeButton bpmNodeButton) {
		Long defId = bpmNodeButton.getDefId();
		Integer operatortype = bpmNodeButton.getOperatortype();
		if (bpmNodeButton.getIsstartform() == 1) {
			return dao.isStartFormExist(defId, operatortype) > 0;
		}
		String nodeId = bpmNodeButton.getNodeid();
		return dao.isExistByNodeId(defId, nodeId, operatortype) > 0;
	}

	/**
	 * 更新时根据流程定义和节点操作类型判断是否已经存在。
	 * 
	 * @param bpmNodeButton
	 * @return
	 */
	public boolean isOperatorExistForUpd(BpmNodeButton bpmNodeButton) {
		Long defId = bpmNodeButton.getDefId();
		Long id = bpmNodeButton.getId();
		Integer operatortype = bpmNodeButton.getOperatortype();
		if (bpmNodeButton.getIsstartform() == 1) {
			return dao.isStartFormExistForUpd(defId, operatortype, id) > 0;
		}
		String nodeId = bpmNodeButton.getNodeid();
		return dao.isExistByNodeIdForUpd(defId, nodeId, operatortype, id) > 0;
	}

	/**
	 * 更新排序字段。
	 * 
	 * @param ids
	 */
	public void sort(String ids) {
		String[] aryId = ids.split(",");
		for (int i = 0; i < aryId.length; i++) {
			Long id = Long.parseLong(aryId[i]);
			dao.updSn(id, (long) (i + 1));
		}
	}
	/**
	 * 初始化起始节点
	 * @param actDefId
	 * @param defId
	 * @throws Exception
	 */
	private void initStartForm(String actDefId, Long defId , List<BpmButton> list) throws Exception {
		for(BpmButton button:list){
			if(0==button.getType() && 1==button.getInit()){
				dao.add(new BpmNodeButton(actDefId, defId, button.getText(), button.getOperatortype()));
			}
		}
	}

	/**
	 * 初始化其它节点(不是起始节点)
	 * @param actDefId
	 * @param defId
	 * @param nodeId
	 * @param isSignTask
	 * @param isFirstNode 
	 * @throws Exception
	 */
	private void initNodeId(String actDefId, Long defId, String nodeId,
			boolean isSignTask,boolean isFirstNode, List<BpmButton> list) throws Exception {
		int nodetype = (isSignTask) ? 1 : 0;
		//是否第一个节点
		if(isFirstNode){
			for(BpmButton button:list){
				if(1==button.getType() && 1==button.getInit()){
					dao.add(new BpmNodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype));
				}
			}
		}
		else{
			if(isSignTask){
				for(BpmButton button:list){
					if((2==button.getType() || 4==button.getType()) && 1==button.getInit()){
						dao.add(new BpmNodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype));
					}
				}
			}
			else{
				for(BpmButton button:list){
					if((3==button.getType() || 4==button.getType()) && 1==button.getInit()){
						dao.add(new BpmNodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype));
					}
				}
			}
	    }
	}

	/**
	 * 初始化按钮。
	 * 
	 * @param defId
	 * @param nodeId
	 * @throws Exception
	 */
	public void init(Long defId, String nodeId) throws Exception {
		BpmDefinition bpmDefinition = bpmDefinitionDao.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		//是否是开始节点
		Boolean isStartForm = StringUtil.isEmpty(nodeId) ? true : false;
		//读button.xml
		String buttonPath = FormUtil.getDesignButtonPath();
		String xml = FileUtil.readFile(buttonPath + "button.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		BpmNodeButtonXml bpmButtonList = (BpmNodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, BpmNodeButtonXml.class);
		List<BpmButton> list = bpmButtonList.getButtons();
		if(BeanUtils.isEmpty(list)) return;
		if (isStartForm) {
			dao.delByStartForm(defId);
			initStartForm(actDefId, defId, list);

		}
		else {
			dao.delByNodeId(defId, nodeId);
			boolean isSignTask = bpmService.isSignTask(actDefId, nodeId);
			boolean isFirstNode = NodeCache.isFirstNode(actDefId, nodeId);
			initNodeId(actDefId, defId, nodeId, isSignTask, isFirstNode, list);
		}
	}
	/**
	 * 初始化流程的全部按钮。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @throws Exception
	 */
	public void initAll(Long defId) throws Exception {
		dao.delByDefId(defId);
		BpmDefinition bpmDefinition = bpmDefinitionDao.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		//读button.xml
		String buttonPath = FormUtil.getDesignButtonPath();
		String xml = FileUtil.readFile(buttonPath + "button.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		BpmNodeButtonXml bpmButtonList = (BpmNodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, BpmNodeButtonXml.class);
		List<BpmButton> list = bpmButtonList.getButtons();
		if(BeanUtils.isEmpty(list)) return;
		// 起始表单按钮。
		initStartForm(actDefId, defId, list);
		
		Map<String,FlowNode> map= NodeCache.getByActDefId(actDefId);

		
		Set<Map.Entry<String, FlowNode>> set = map.entrySet();
		for (Iterator<Map.Entry<String, FlowNode>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, FlowNode> entry = (Map.Entry<String, FlowNode>) it.next();
			FlowNode flowNode=entry.getValue();
			boolean isSignTask = flowNode.getIsSignNode();
			boolean isFirstNode = flowNode.getIsFirstNode();
			// 节点按钮。
			initNodeId(actDefId, defId, entry.getKey(), isSignTask,isFirstNode, list);
		}
	}

	/**
	 * 根据流程定义ID删除按钮。
	 * 
	 * @param defId
	 * @throws Exception
	 */
	public void delByDefId(Long defId) throws Exception {
		dao.delByDefId(defId);
	}

	/**
	 * 删除节点或开始表单的节点按钮定义。
	 * 
	 * @param defId
	 * @param nodeId
	 */
	public void delByDefNodeId(Long defId, String nodeId) {
		if (StringUtil.isEmpty(nodeId)) {
			dao.delByStartForm(defId);
		} else {
			dao.delByNodeId(defId, nodeId);
		}
	}

	/**
	 * 根据流程定义ID获得操作按钮。
	 * 
	 * @param defId
	 * @return
	 */
	public List<BpmNodeButton> getByDefId(Long defId) {
		return dao.getByDefId(defId);
	}

}
