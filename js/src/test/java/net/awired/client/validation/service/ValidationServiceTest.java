package net.awired.client.validation.service;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import net.awired.client.bean.validation.js.service.ValidationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

public class ValidationServiceTest {

    public class Bean {

        public Long id;

        @NotNull
        public String firstname;

        @NotNull
        public String lastname;

        @Past
        public Date birthday;

        @Pattern(regexp = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}")
        public String email;

    }

    @Test
    public void should() throws Exception {
        ValidationService validationService = new ValidationService();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Bean b = new Bean();
        b.email = "genere";
        Validator v = validatorFactory.getValidator();
        Set<ConstraintViolation<Bean>> violation = v.validate(b);

        Object validationObject = validationService.getValidationObject(Bean.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonContraints = mapper.writeValueAsString(validationObject);
        String jsonBean = mapper.writeValueAsString(b);
        System.out.println(jsonContraints);

        //JS
        Global global = new Global();
        Context cx = ContextFactory.getGlobal().enterContext();
        global.init(cx);
        cx.setClassShutter(new ClassShutterImpl());
        cx.setOptimizationLevel(-1);
        cx.setLanguageVersion(Context.VERSION_1_5);

        Scriptable scope = cx.initStandardObjects(global);
        cx.evaluateReader(scope, new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream("Validator.js")), "Validator.js", 1, null);

        StringBuilder sb = new StringBuilder();
        sb.append("new Validator().validate(" + jsonBean + "," + jsonContraints + ");");

        Object o1 = cx.evaluateString(scope, sb.toString(), "validator", 1, null);
        System.out.println(o1);
    }

    public class ClassShutterImpl implements ClassShutter {
        @Override
        public boolean visibleToScripts(String fullClassName) {
            // For the moment we dont allow to execute java code 
            return fullClassName.startsWith("org.mozilla.javascript");
        }
    }

}
