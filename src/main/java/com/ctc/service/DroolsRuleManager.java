package com.ctc.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.drools.rule.Package;

public class DroolsRuleManager {

	private static DroolsRuleManager INSATNCE;

	private final WorkingMemory workingMemory;

	
	public static DroolsRuleManager getInstance() {
		if (INSATNCE == null) {
			try {
				INSATNCE = new DroolsRuleManager();
			} catch (DroolsParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return INSATNCE;
	}

	private DroolsRuleManager() throws DroolsParserException, IOException {
		RuleBase ruleBase = initialiseDrools();
		this.workingMemory = ruleBase.newStatefulSession();
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
			InputStream resourceAsStream = getClass().getResourceAsStream(
					ruleFile);
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

	public WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

}
