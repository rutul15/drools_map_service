package com.ctc.dao;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.drools.WorkingMemory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.ctc.address.Building;
import com.ctc.address.UserAddress;
import com.ctc.model.AddressJoin;

@Repository
@Transactional
public class AddressDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<AddressJoin> listAll(int maxResults) {
		Query query = sessionFactory.getCurrentSession().createQuery("from AddressJoin");
		query.setMaxResults(maxResults);
		return query.list();
	}

	public FactBook readAll(WorkingMemory workingMemory, int limit)
			throws SAXException, IOException, ParserConfigurationException {
	
		List<AddressJoin> list = this.listAll(limit);
		BuildingParser bp = new BuildingParser();
		//Set<String> dedup = new HashSet<String>(); 
		FactBook fb = new FactBook();
	
		for (AddressJoin aj : list) {
	
			Building b = bp.parseBuilding(aj);
			
			if (b == null) {
				// if it's not a Building, it's a UserAddress
				UserAddress ua = new UserAddress();
				ua.setOriginalAddress(aj.getUncleanedAddress());
				workingMemory.insert(ua);
				fb.getUserAddresses().add(ua);
			}
			else {
				workingMemory.insert(b);
				fb.getBuildings().add(b);
			}
		}
		return fb;
	}
}
