package com.ctc.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ctc.dao.AddressDao;

public class InitBean {

	private AddressDao dao;
	
	public void init(){
		DroolsRuleManager manager = DroolsRuleManager.getInstance();
		System.out.println("********** manager ************* "+ manager);
		
			try {
				dao.readAll(manager.getWorkingMemory(), Integer.MAX_VALUE);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//dao.listAll(1000);
		
		manager.getWorkingMemory().fireAllRules();
	}

	public AddressDao getDao() {
		return dao;
	}

	public void setDao(AddressDao dao) {
		this.dao = dao;
	}
	
	
}
