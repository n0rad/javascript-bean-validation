package net.awired.client.validation.tools;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.bean.validation.js.service.ValidationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;

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

    ////

    public static <T> Set<ClientConstraintViolation> validateValue(ArrayList<ClientConstraintInfo> infos,
            Class<T> oClass, String propertyName, Object value, Class<?>... groups) {
        return rhinoValidate(infos, new Scenario<T>(oClass, propertyName, value), groups);
    }

    public static <T> Set<ClientConstraintViolation> validate(ArrayList<ClientConstraintInfo> infos, T obj,
            Class<?>... groups) {
        return rhinoValidate(infos, new Scenario<T>(obj), groups);
    }

    ////

    public static <T> Set<ClientConstraintViolation> validateValue(ClientConstraintInfo info, Class<T> oClass,
            String propertyName, Object value, Class<?>... groups) {
        ArrayList<ClientConstraintInfo> constraintInfos = new ArrayList<ClientConstraintInfo>();
        constraintInfos.add(info);
        return rhinoValidate(constraintInfos, new Scenario<T>(oClass, propertyName, value), groups);
    }

    public static <T> Set<ClientConstraintViolation> validate(ClientConstraintInfo info, T obj, Class<?>... groups) {
        ArrayList<ClientConstraintInfo> constraintInfos = new ArrayList<ClientConstraintInfo>();
        constraintInfos.add(info);
        return rhinoValidate(constraintInfos, new Scenario<T>(obj), groups);
    }

    ////

    public static <T> Set<ClientConstraintViolation> validateValue(Class<T> oClass, String propertyName,
            Object value, Class<?>... groups) {
        return rhinoValidate(new ArrayList<ClientConstraintInfo>(), new Scenario<T>(oClass, propertyName, value),
                groups);
    }

    public static <T> Set<ClientConstraintViolation> validate(T obj, Class<?>... groups) {
        return rhinoValidate(new ArrayList<ClientConstraintInfo>(), new Scenario<T>(obj), groups);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //TODO manage groups
    private static <T> Set<ClientConstraintViolation> rhinoValidate(List<ClientConstraintInfo> info,
            Scenario<T> scenario, Class<?>... groups) {
        Preconditions.checkNotNull(info);
        ObjectMapper mapper = new ObjectMapper();
        ValidationService validationService = new ValidationService();
        try {
            StringBuilder jsScript = new StringBuilder();
            jsScript.append("validator = new Validator();").append("\n");
            //            cx.evaluateString(scope, "validator = new Validator();", "", 1, null);

            for (ClientConstraintInfo clientConstraint : info) {
                //                cx.evaluateReader(scope, clientConstraint.getJsConstraint(), "", 1, null);
                jsScript.append(CharStreams.toString(clientConstraint.getJsConstraint())).append("\n");
                //                cx.evaluateString(scope, "validator.registerConstraint('" + clientConstraint.getConstraintType()
                //                        + "', " + clientConstraint.getJsConstraintName() + ")", "", 1, null);
                jsScript.append(
                        "validator.registerConstraint('" + clientConstraint.getConstraintType() + "', "
                                + clientConstraint.getJsConstraintName() + ")").append("\n");
            }

            String query;
            if (scenario.scenario == ScenarioEnum.PROPERTY) {
                Object validationObject = validationService.getValidationObject(scenario.objClass,
                        scenario.propertyName);
                String jsonContraints = mapper.writeValueAsString(validationObject);
                String jsonValue = "{\"" + scenario.propertyName + "\":" + mapper.writeValueAsString(scenario.value)
                        + "}";
                query = "cv = validator.validateValue(JSON.parse('" + jsonValue + "'), JSON.parse('" + jsonContraints
                        + "'), '" + scenario.propertyName + "')";
            } else if (scenario.scenario == ScenarioEnum.OBJECT) {
                Object validationObject = validationService.getValidationObject(scenario.obj.getClass());
                String jsonContraints = mapper.writeValueAsString(validationObject);
                String jsonBean = mapper.writeValueAsString(scenario.obj);
                query = "cv = validator.validate(JSON.parse('" + jsonBean + "'), JSON.parse('" + jsonContraints
                        + "'))";
            } else {
                throw new RuntimeException("unknow scenario type");
            }
            //            cx.evaluateString(scope, query, "validator", 1, null);
            jsScript.append(query).append("\n");

            System.out.println(jsScript.toString());
            cx.evaluateString(scope, jsScript.toString(), "validator", 1, null);

            NativeJavaObject result = (NativeJavaObject) cx.evaluateString(scope, "retreaveConstrainViolations(cv);",
                    "retreave", 1, null);
            @SuppressWarnings("unchecked")
            Set<ClientConstraintViolation> constraintsViolations = (Set<ClientConstraintViolation>) result.unwrap();
            return constraintsViolations;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    enum ScenarioEnum {
        OBJECT, PROPERTY;
    }

    static class Scenario<T> {

        private T obj;

        private Class<T> objClass;
        private String propertyName;
        private Object value;
        private ScenarioEnum scenario;

        public Scenario(T o) {
            Preconditions.checkNotNull(o);
            this.obj = o;
            this.scenario = ScenarioEnum.OBJECT;
        }

        public Scenario(Class<T> oClass, String propertyName, Object value) {
            Preconditions.checkNotNull(oClass);
            Preconditions.checkNotNull(propertyName);
            this.objClass = oClass;
            this.propertyName = propertyName;
            this.value = value;
            this.scenario = ScenarioEnum.PROPERTY;
        }
    }

}
