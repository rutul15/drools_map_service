package com.ctc.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ctc.address.UserAddress;
import com.ctc.service.DroolsRuleManager;



/**
 * The Class ChicagoMapController.
 */
@Controller
public class AddressResolverController {

	

	/** The logger. */
	private final Logger			logger	= LoggerFactory.getLogger(getClass());

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String locationPage(Model model) {
		
		return "home";
	}

	

	

	

	@RequestMapping(value = "/parse/{street}", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public UserAddress getData(@PathVariable("street") String street) {

		logger.debug("* Inputs: [" + street + "]");
		
		if(StringUtils.isBlank(street)){
			return new UserAddress();
		}
		
		DroolsRuleManager manager = DroolsRuleManager.getInstance();
		System.out.println("********** Starts ************* "+ new Date());
		
		UserAddress ua = new UserAddress();
		ua.setOriginalAddress(street);
		manager.getWorkingMemory().insert(ua);
		manager.getWorkingMemory().fireAllRules();
		System.out.println("resolved address: "+ua.getResolvedAddress());
		logger.info("********** Ends ************* "+ new Date());

		return ua;

	}

	

}
