package com.arits.docRepo.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class   Node implements Comparable<Node>{

    private String nodeId; // node id
    private String pid; // parent id
    private String text;
    private String href;
    private List<Node> subNodes;

    public Node(String nodeId, String pId, String text, String href) {
        this.nodeId = nodeId;
        this.pid = pId;
        this.text = text;
        this.href = href;
    }

	/**
	 * @param string
	 * @param string2
	 * @param folderName
	 */
	public Node(String nodeId, String pId, String text) {
		 this.nodeId = nodeId;
	        this.pid = pId;
	        this.text = text;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	/**
	  *<b>getSubNodes</b>
	  *<p>Getter method for getting the value of subNodes</p>
	  * 
	  * @return the subNodes
	  */
	public List<Node> getSubNodes() {
		return subNodes;
	}

	 /**
	   * <b>setSubNodes</b>
	   * <p>Setter method for setting the value of subNodes</p>
	   * 
	   * subNodes the subNodes to set
	   *            
	   */
	public void setSubNodes(List<Node> subNodes) {
		this.subNodes = subNodes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Node node) {
		return text.compareTo(node.text);
	}

	
    
}
