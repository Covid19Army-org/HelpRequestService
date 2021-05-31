package com.covid19army.HelpRequestService.models;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.covid19army.core.enums.NeedsEnum;


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

	@Enumerated(EnumType.ORDINAL)
	private NeedsEnum needid;

	//bi-directional many-to-one association to HelpRequest	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)	
	@JoinColumn(name="requestid")
	private HelpRequest helprequest;

	public RequestNeed() {
	}
	
	public RequestNeed(NeedsEnum needId, HelpRequest helpRequest) {
		this.needid = needId;
		this.helprequest = helpRequest;
	}

	public long getRequestneedid() {
		return this.requestneedid;
	}

	public void setRequestneedid(long requestneedid) {
		this.requestneedid = requestneedid;
	}

	public NeedsEnum getNeedid() {
		return this.needid;
	}

	public void setNeedid(NeedsEnum needid) {
		this.needid = needid;
	}

	public HelpRequest getHelprequest() {
		return this.helprequest;
	}

	public void setHelprequest(HelpRequest helprequest) {
		this.helprequest = helprequest;
	}

}