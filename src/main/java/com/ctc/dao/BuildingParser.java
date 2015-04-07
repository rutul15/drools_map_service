package com.ctc.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ctc.address.Building;
import com.ctc.model.AddressJoin;

public class BuildingParser {
	
	private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private XPathFactory xPathfactory = XPathFactory.newInstance();

	public Building parseBuilding(AddressJoin aj) throws SAXException,
			IOException, ParserConfigurationException {
		
		if(aj.getHttpStatus() == null || aj.getHttpStatus() != 200 || aj.getResult() == null) {
			return null;
		}
		
		InputStream is = new ByteArrayInputStream(aj.getResult().getBytes("UTF-8"));
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		
		try {
			XPath xpath = xPathfactory.newXPath();
			
			XPathExpression expr = xpath.compile("/searchresults/place[city='Chicago']");
			Node place= (Node) expr.evaluate(doc, XPathConstants.NODE);
			if(place == null) {
				return null;
			}
			//			System.out.println("City "+ place.toString());
	
			// need to get the value of <house_number> and <road> here
			XPathExpression houseNumberExpr = xpath.compile("/searchresults/place/house_number");
			String house_number = (String) houseNumberExpr.evaluate(place, XPathConstants.STRING);
	
			XPathExpression roadExpr = xpath.compile("/searchresults/place/road");
			String road = (String) roadExpr.evaluate(place, XPathConstants.STRING);
	
	
			Building b = new Building();
			b.setDisplayAddress(house_number + " " + road);
	
			XPathExpression placeExpr = xpath.compile("string(/searchresults/place/@place_id)");
			String placeId = (String) placeExpr.evaluate(doc, XPathConstants.STRING);
			//System.out.println("Place :"+ placeId);
	
			b.setPlaceId(placeId);
			return b;
		} catch (Exception e){
			//e.printStackTrace();
			return null;
		}
	}

}
