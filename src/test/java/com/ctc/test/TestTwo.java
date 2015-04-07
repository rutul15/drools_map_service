package com.ctc.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.drools.event.DebugAgendaEventListener;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.rule.Package;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ctc.address.Building;
import com.ctc.dao.AddressDao;
import com.ctc.model.AddressJoin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/test-application-context.xml" })
public class TestTwo {
	
	@Autowired
	private AddressDao	addressDao;

    @Test
    public void shouldFireHelloWorld() throws IOException, DroolsParserException {
    	
        RuleBase ruleBase = initialiseDrools();
        WorkingMemory workingMemory = initializeBuildingObjects(ruleBase);
        workingMemory.fireAllRules();
        
    }

    private RuleBase initialiseDrools() throws IOException, DroolsParserException {
        PackageBuilder packageBuilder = readRuleFiles();
        return addRulesToWorkingMemory(packageBuilder);
    }

    private PackageBuilder readRuleFiles() throws DroolsParserException, IOException {
        PackageBuilder packageBuilder = new PackageBuilder();

        String[] ruleFiles = {"/address.drl"};
       
        for (String ruleFile : ruleFiles) {
            Reader reader = getRuleFileAsReader(ruleFile);
            packageBuilder.addPackageFromDrl(reader);
        }

        assertNoRuleErrors(packageBuilder);

        return packageBuilder;
    }

    private Reader getRuleFileAsReader(String ruleFile) {
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

        return new InputStreamReader(resourceAsStream);
    }

    private RuleBase addRulesToWorkingMemory(PackageBuilder packageBuilder) {
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        Package rulesPackage = packageBuilder.getPackage();
        ruleBase.addPackage(rulesPackage);

        return ruleBase;
    }

    private void assertNoRuleErrors(PackageBuilder packageBuilder) {
        PackageBuilderErrors errors = packageBuilder.getErrors();

        if (errors.getErrors().length > 0) {
            StringBuilder errorBuildings = new StringBuilder();
            errorBuildings.append("Found errors in package builder\n");
            for (int i = 0; i < errors.getErrors().length; i++) {
                DroolsError errorBuilding = errors.getErrors()[i];
                errorBuildings.append(errorBuilding);
                errorBuildings.append("\n");
            }
            errorBuildings.append("Could not parse knowledge");

            throw new IllegalArgumentException(errorBuildings.toString());
        }
    }

    private WorkingMemory initializeBuildingObjects(RuleBase ruleBase) {
        WorkingMemory workingMemory = ruleBase.newStatefulSession();
        
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        workingMemory.addEventListener(new DebugAgendaEventListener()); 

        try {
			createHelloWorld(workingMemory);
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
        return workingMemory;
    }
    
    private void createHelloWorld(WorkingMemory workingMemory) throws SAXException, IOException, ParserConfigurationException {
    	
		List<AddressJoin> list = addressDao.listAll(Integer.MAX_VALUE); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		for(AddressJoin aj : list) {

			System.out.println(aj.toString());
			
			if(aj.getHttpStatus() != null && aj.getHttpStatus() == 200) {
				InputStream is = new ByteArrayInputStream(aj.getResult().getBytes());
				Document doc = dBuilder.parse(is);
				
				try {
					XPathFactory xPathfactory = XPathFactory.newInstance();
					XPath xpath = xPathfactory.newXPath();
					
					XPathExpression expr = xpath.compile("/searchresults/place/city");
					String city = (String) expr.evaluate(doc, XPathConstants.STRING);
					//System.out.println("City "+ city);
			      
					// need to get the value of <house_number> and <road> here
					XPathExpression houseNumberExpr = xpath.compile("/searchresults/place/house_number");
					String house_number = (String) houseNumberExpr.evaluate(doc, XPathConstants.STRING);
					
					XPathExpression roadExpr = xpath.compile("/searchresults/place/road");
					String road = (String) roadExpr.evaluate(doc, XPathConstants.STRING);
				

					Building b = new Building();
					b.setDisplayAddress(house_number + " " + road);
					
					XPathExpression placeExpr = xpath.compile("string(/*/place[1]/@place_id)");
					String placeId = (String) placeExpr.evaluate(doc, XPathConstants.STRING);
					//System.out.println("Place :"+ placeId);
					
					b.setPlaceId(placeId);
					workingMemory.insert(b);
				} catch (Exception e){
					e.printStackTrace();
					Assert.fail();
				}
			}
			
		}
    }
}
