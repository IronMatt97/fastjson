package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by wenshao on 2016/11/2.
 */
@RunWith(Parameterized.class)
public class FieldOrderTest extends TestCase {
    Person person;
    String result;
    public FieldOrderTest(TestInput t) {
        this.person = t.person;
        this.result = t.result;
    }
    public static Collection<TestInput[]> configure() {
        Collection<TestInput> inputs = new ArrayList<>();
        Collection<TestInput[]> result = new ArrayList<>();
        School validS = new School("Tor Vergata");
        School emptyS = new School("");
        School nullS = new School();
        String validN = "Mario";
        String emptyN = "";
        String nullN = null;

        //Istanza valida
        Person validP = new Person(validN,validS);
        //Combinazioni istanze con campi vuoti
        Person emptyP1 = new Person(emptyN,validS);
        Person emptyP2 = new Person(validN,emptyS);
        Person emptyP3 = new Person(emptyN,emptyS);
        //Combinazioni istanze nulle
        Person nullP1 = new Person(nullN,validS);
        Person nullP2 = new Person(validN,null);
        Person nullP3 = new Person(nullN,null);
        //Combinazioni istanze con il secondo parametro con il campo nullo
        //Person nullP4 = new Person(nullN,validS);
        Person nullP5 = new Person(validN,nullS);
        Person nullP6 = new Person(nullN,nullS);
        //Istanza nulla
        Person nullP7 = null;

        //Combinazioni dei vari test
        inputs.add(new TestInput(validP,"{\"name\":\""+validN+"\",\"school\":{\"name\":\""+validS.getName()+"\"}}"));
        inputs.add(new TestInput(emptyP1,"{\"name\":\"\",\"school\":{\"name\":\""+validS.getName()+"\"}}"));
        inputs.add(new TestInput(emptyP2,"{\"name\":\""+validN+"\",\"school\":{\"name\":\"\"}}"));
        inputs.add(new TestInput(emptyP3,"{\"name\":\"\",\"school\":{\"name\":\"\"}}"));
        inputs.add(new TestInput(nullP1,"{\"school\":{\"name\":\""+validS.getName()+"\"}}"));
        inputs.add(new TestInput(nullP2,"{\"name\":\""+validN+"\"}"));
        inputs.add(new TestInput(nullP3,"{}"));
        inputs.add(new TestInput(nullP5,"{\"name\":\""+validN+"\",\"school\":{}}"));
        inputs.add(new TestInput(nullP6,"{\"school\":{}}"));
        inputs.add(new TestInput(nullP7,"null"));

        for (TestInput e : inputs) {
            result.add(new TestInput[] { e });
        }
        return result;
    }
    @Parameters
    public static Collection<TestInput[]> getTestParameters() {
        return configure();
    }
    @Test
    public void test_field_order() {
        /*Person p = new Person();
        p.setName("njb");
        School s = new School();
        s.setName("llyz");
        p.setSchool(s);
        String json = JSON.toJSONString(p);
        assertEquals("{\"name\":\"njb\",\"school\":{\"name\":\"llyz\"}}", json);*/

        String json = JSON.toJSONString(this.person);
        assertEquals(this.result, json);
    }

    public static class Person {
        private String name;
        private School school;
        public Person(){}
        public Person(String name, School s){
            this.name = name;
            this.school = s;
        }
        public void setSchool(School school) {
            this.school = school;
        }
        public School getSchool(){
            return this.school;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    }
    public static class School {
        public School(){}
        public School(String s){
            this.name = s;
        }
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    private static class TestInput {
        Person person;
        String result;
        public TestInput(Person person, String result) {
            this.person = person;
            this.result = result;
        }
    }
}
