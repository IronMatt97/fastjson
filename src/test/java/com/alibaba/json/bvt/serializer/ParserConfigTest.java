package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IdentityHashMap;
import org.junit.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@RunWith(Parameterized.class)
public class ParserConfigTest {
    String input;
    Type clazz;
    ParserConfig config;
    Object expectedParam;
    public ParserConfigTest(TestInput t) {
        this.input = t.input;
        this.clazz = t.clazz;
        this.config = t.config;
        this.expectedParam = t.expectedParam;
    }

    public static Collection<TestInput[]> configure() {
        Collection<TestInput> inputs = new ArrayList<>();
        Collection<TestInput[]> result = new ArrayList<>();
        ParserConfig config0 = new ParserConfig();
        ParserConfig config1 = new ParserConfig(Thread.currentThread().getContextClassLoader());
        //Combinazioni di stringa [valida-non valida-vuota-nulla], Type [valido-nullo] e ParserConfig [valido-unconfigured-nullo]
        //Combinazioni con stringhe valide
        inputs.add(new TestInput("{\"value\":123}",Model.class ,config0,123));
        inputs.add(new TestInput("{\"value\":123}",Model.class ,config1,123));
        inputs.add(new TestInput("{\"value\":123}",Model.class,null,null));
        inputs.add(new TestInput("{\"value\":123}",null,config0,null));
        inputs.add(new TestInput("{\"value\":123}",null,config1,null));
        inputs.add(new TestInput("{\"value\":123}",null,null,null));

        //Combinazioni con stringhe non valide
        inputs.add(new TestInput("prova",Model.class,config0,null));
        inputs.add(new TestInput("prova",Model.class,config1,null));
        inputs.add(new TestInput("prova",Model.class,null,null));
        inputs.add(new TestInput("prova",null,config0,null));
        inputs.add(new TestInput("prova",null,config1,null));
        inputs.add(new TestInput("prova",null,null,null));

        //Combinazioni con stringhe vuote
        inputs.add(new TestInput("",Model.class,config0,null));
        inputs.add(new TestInput("",Model.class,config1,null));
        inputs.add(new TestInput("",Model.class,null,null));
        inputs.add(new TestInput("",null,config0,null));
        inputs.add(new TestInput("",null,config1,null));
        inputs.add(new TestInput("",null,null,null));

        //Combinazioni con stringhe nulle
        inputs.add(new TestInput(null,Model.class,config0,null));
        inputs.add(new TestInput(null,Model.class,config1,null));
        inputs.add(new TestInput(null,Model.class,null,null));
        inputs.add(new TestInput(null,null,config0,null));
        inputs.add(new TestInput(null,null,config1,null));
        inputs.add(new TestInput(null,null,null,null));

        for (TestInput e : inputs) {
            result.add(new TestInput[] { e });
        }
        return result;
    }
    private static class TestInput {
        String input;
        Type clazz;
        ParserConfig config;
        Object expectedParam;
        public TestInput(String input, Type clazz, ParserConfig config, Object e) {
            this.input = input;
            this.clazz = clazz;
            this.config = config;
            this.expectedParam = e;
        }
    }
    @Parameters
    public static Collection<TestInput[]> getTestParameters() {
        return configure();
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void test_0() {
        //ParserConfig config = new ParserConfig();
        //config.getDeserializers();
        if (Objects.isNull(this.config))
            thrown.expect(NullPointerException.class);
        IdentityHashMap<Type, ObjectDeserializer> deserializers = this.config.getDeserializers();
        if (Objects.isNull(this.config))
            Assert.assertNull(deserializers);
        else
            Assert.assertNotNull(deserializers);
    }
    @Test
    public void test_1() {
        //ParserConfig config = new ParserConfig(Thread.currentThread().getContextClassLoader());
        //Model model = JSON.parseObject("{\"value\":123}", Model.class, 123);
        //Assert.assertEquals(123, model.value);

        boolean nullConfig =Objects.isNull(this.config);
        boolean nullClazz =Objects.isNull(this.clazz);
        boolean nullInput =Objects.isNull(this.input);
        int nullParams = (nullClazz ?1:0) + (nullConfig?1:0) + (nullInput?1:0);
        boolean invalidFormat=false;
        if(!nullInput)
            invalidFormat=!(this.input.startsWith("{\"value\":") && this.input.endsWith("}"));
        //Nel caso in cui sia il formato che config che clazz sono null basta aspettarsi JSONException, whitebox è il primo che viene sollevato

        if(!nullInput && !this.input.isEmpty() && nullConfig)
            thrown.expect(NullPointerException.class);
        else if (!nullInput && !this.input.isEmpty() && invalidFormat)
            thrown.expect(JSONException.class);
        else if(!nullInput && !this.input.isEmpty() && nullClazz)
            thrown.expect(ClassCastException.class);
        else if(nullInput){
            if(nullParams <2)
                thrown.expect(NullPointerException.class);
        }
        Model model = JSON.parseObject(this.input, this.clazz, this.config);

        if(invalidFormat || nullClazz || nullConfig)
            Assert.assertNull(model);
        else
            Assert.assertEquals(this.expectedParam, model.value);
    }
    public static class Model {
        public int value;
    }
}
