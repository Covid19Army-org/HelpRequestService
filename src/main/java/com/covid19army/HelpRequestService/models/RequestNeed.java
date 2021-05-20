package com.covid19army.HelpRequestService.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the requestneeds database table.
 * 
 */
@Entity
@Table(name="requestneeds")
@NamedQuery(name="RequestNeed.findAll", query="SELECT r FROM RequestNeed r")
public class RequestNeed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long requestneedid;

	private int needid;

	//bi-directional many-to-one association to HelpRequest
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="requestid")
	private HelpRequest helprequest;

	public RequestNeed() {
	}
	
	public RequestNeed(int needId, HelpRequest helpRequest) {
		this.needid = needId;
		this.helprequest = helpRequest;
	}

	public long getRequestneedid() {
		return this.requestneedid;
	}

	public void setRequestneedid(long requestneedid) {
		this.requestneedid = requestneedid;
	}

	public int getNeedid() {
		return this.needid;
	}

	public void setNeedid(int needid) {
		this.needid = needid;
	}

	public HelpRequest getHelprequest() {
		return this.helprequest;
	}

	public void setHelprequest(HelpRequest helprequest) {
		this.helprequest = helprequest;
	}

}