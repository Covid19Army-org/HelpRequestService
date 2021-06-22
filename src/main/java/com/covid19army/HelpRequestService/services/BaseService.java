package com.covid19army.HelpRequestService.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.covid19army.core.dtos.ActivityLogDto;
import com.covid19army.core.extensions.HttpServletRequestExtension;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;
import com.covid19army.core.utilities.Helper;

public abstract class BaseService {

	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	@Qualifier("activitylogExchangeSender")
	RabbitMQSender _activitylogExchangeSender;
	
	
	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	protected void publishActivity(long entityid, String entity, String action, long fromuserid, long touserid) {
		var dto = Helper.createActivityLogDto(entityid, entity, action, fromuserid, touserid);
		_activitylogExchangeSender.<ActivityLogDto>send(dto);
	}
}
