package net.awired.client.validation.tools;

import java.io.InputStreamReader;
import java.util.Set;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.bean.validation.js.service.ValidationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

public class ClientValidationTestHelper {

    private static Context cx;
    private static Scriptable scope;

    static {
        try {
            //JS
            Global global = new Global();
            //            ContextFactory.initGlobal(new SandboxContextFactory());
            cx = ContextFactory.getGlobal().enterContext();
            global.init(cx);
            //            cx.setClassShutter(new RhinoContextClasses.SandboxClassShutter());
            cx.setOptimizationLevel(-1);
            cx.setLanguageVersion(Context.VERSION_1_7);
            scope = cx.initStandardObjects(global);
            cx.evaluateReader(scope, new InputStreamReader(ClientValidationTestHelper.class.getClassLoader()
                    .getResourceAsStream("env.rhino.1.2.js")), "env.rhino.1.2.js", 1, null);
            cx.evaluateReader(scope, new InputStreamReader(ClientValidationTestHelper.class.getClassLoader()
                    .getResourceAsStream("jquery-1.6.2.min.js")), "jquery-1.6.2.min.js", 1, null);
            cx.evaluateReader(scope, new InputStreamReader(ClientValidationTestHelper.class.getClassLoader()
                    .getResourceAsStream("Validator.js")), "Validator.js", 1, null);
            cx.evaluateReader(scope, new InputStreamReader(ClientValidationTestHelper.class.getClassLoader()
                    .getResourceAsStream("retreaveConstrainViolations.js")), "retreaveConstrainViolations.js", 1,
                    null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //TODO manage groups
    public static <T> Set<ClientConstraintViolation> validateValue(ClientConstraintInfo info, Class<T> o,
            String propertyName, Object value, Class<?>... groups) {
        ValidationService validationService = new ValidationService();
        Object validationObject = validationService.getValidationObject(o, propertyName);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonContraints = mapper.writeValueAsString(validationObject);
            String jsonValue = "{\"" + propertyName + "\":" + mapper.writeValueAsString(value) + "}";
            cx.evaluateReader(scope, info.getJsConstraint(), "", 1, null);
            cx.evaluateString(scope, "validator = new Validator();", "", 1, null);
            cx.evaluateString(scope,
                    "validator.registerConstraint('" + info.getConstraintType() + "', " + info.getJsConstraintName()
                            + ")", "", 1, null);
            cx.evaluateString(scope, "cv = validator.validateValue(JSON.parse('" + jsonValue + "'), JSON.parse('"
                    + jsonContraints + "'), '" + propertyName + "')", "validator", 1, null);
            NativeJavaObject result = (NativeJavaObject) cx.evaluateString(scope, "retreaveConstrainViolations(cv);",
                    "retreave", 1, null);
            @SuppressWarnings("unchecked")
            Set<ClientConstraintViolation> constraintsViolations = (Set<ClientConstraintViolation>) result.unwrap();
            return constraintsViolations;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    List<ClientConstraintInfo> info,

    //TODO manage groups
    public static <T> Set<ClientConstraintViolation> validate(T o, Class<?>... groups) {
        ValidationService validationService = new ValidationService();
        Object validationObject = validationService.getValidationObject(o.getClass());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonContraints = mapper.writeValueAsString(validationObject);
            String jsonBean = mapper.writeValueAsString(o);
            cx.evaluateString(scope, "cv = new Validator().validate(JSON.parse('" + jsonBean + "'), JSON.parse('"
                    + jsonContraints + "'))", "validator", 1, null);
            NativeJavaObject result = (NativeJavaObject) cx.evaluateString(scope, "retreaveConstrainViolations(cv);",
                    "retreave", 1, null);
            @SuppressWarnings("unchecked")
            Set<ClientConstraintViolation> constraintsViolations = (Set<ClientConstraintViolation>) result.unwrap();
            return constraintsViolations;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
