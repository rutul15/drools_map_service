package com.ctc.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.drools.FactHandle;
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

import com.ctc.address.Building;
import com.ctc.address.UserAddress;


public class TestOne {

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

        createHelloWorld(workingMemory);
        return workingMemory;
    }
    
    private void createHelloWorld(WorkingMemory workingMemory) {
        Building helloBuilding = new Building();

        helloBuilding.setDisplayAddress("123 Main Street");
        helloBuilding.setPlaceId("1");
        workingMemory.insert(helloBuilding);

        {
        	UserAddress ua1 = new UserAddress();
        	ua1.setOriginalAddress("123 Main Street");
        	workingMemory.insert(ua1);
        }
        {
        	UserAddress ua1 = new UserAddress();
        	ua1.setOriginalAddress("123 Main Avenue");
        	workingMemory.insert(ua1);
        }
    }

}
