package com.ctc.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.ctc.address.UserAddress;
import com.ctc.dao.AddressDao;
import com.ctc.dao.FactBook;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/test-application-context.xml" })
public class Test3 {

	@Autowired
	private AddressDao addressDao;

	@Test
	public void shouldFireHelloWorld() throws IOException,
			DroolsParserException, SAXException, ParserConfigurationException {

		RuleBase ruleBase = initialiseDrools();
		WorkingMemory wm = ruleBase.newStatefulSession();
		
		// uncomment for verbose output
		wm.addEventListener(new DebugWorkingMemoryEventListener());
		wm.addEventListener(new DebugAgendaEventListener());
		
		FactBook fb = addressDao.readAll(wm, 1000);
		//addressDao.listAll(1000);
		wm.fireAllRules();

		// uncomment for verbose output
//		wm.addEventListener(new DebugWorkingMemoryEventListener());
//		wm.addEventListener(new DebugAgendaEventListener());

		// here's actual test of how to resolve an address once all rules are in memory
		UserAddress ua = new UserAddress();
		ua.setOriginalAddress("1234 SOUTH MAY");
		wm.insert(ua);
		wm.fireAllRules();
		assertEquals(ua.getResolvedAddress(), "1234 SOUTH MAY STREET");

		System.out.println("Statistics");
		/*System.out.println("Building count = " + fb.getBuildings().size());
		System.out.println("UserAddress count = " + fb.getUserAddresses().size());
		
		int countUnresolved = 0;
		for(UserAddress u : fb.getUserAddresses()) {
			if(u.getResolvedAddress() == null) {
				++countUnresolved;
				System.out.println("Unresolved: " + u.getOriginalAddress());
			}
		}*/
		//System.out.println("UserAddress count [unresolved] = " + countUnresolved);
		
	}

	private RuleBase initialiseDrools() throws IOException,
			DroolsParserException {
		PackageBuilder packageBuilder = readRuleFiles();
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		Package rulesPackage = packageBuilder.getPackage();
		ruleBase.addPackage(rulesPackage);
		return ruleBase;
	}

	private PackageBuilder readRuleFiles() throws DroolsParserException,
			IOException {
		PackageBuilder packageBuilder = new PackageBuilder();

		String[] ruleFiles = { "/address.drl" };

		for (String ruleFile : ruleFiles) {
			InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);
			Reader reader = new InputStreamReader(resourceAsStream);
			packageBuilder.addPackageFromDrl(reader);
		}

		assertNoRuleErrors(packageBuilder);

		return packageBuilder;
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
}
