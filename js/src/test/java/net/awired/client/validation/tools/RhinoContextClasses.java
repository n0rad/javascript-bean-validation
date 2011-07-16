package net.awired.client.validation.tools;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class RhinoContextClasses {

    public static class SandboxWrapFactory extends WrapFactory {
        @Override
        public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
            return new SandboxNativeJavaObject(scope, javaObject, staticType);
        }
    }

    public static class SandboxContextFactory extends ContextFactory {
        @Override
        protected Context makeContext() {
            Context cx = super.makeContext();
            cx.setWrapFactory(new SandboxWrapFactory());
            return cx;
        }
    }

    public static class SandboxNativeJavaObject extends NativeJavaObject {
        public SandboxNativeJavaObject(Scriptable scope, Object javaObject, Class staticType) {
            super(scope, javaObject, staticType);
        }

        @Override
        public Object get(String name, Scriptable start) {
            if (name.equals("getClass")) {
                return NOT_FOUND;
            }

            return super.get(name, start);
        }
    }

    public static class SandboxClassShutter implements ClassShutter {
        @Override
        public boolean visibleToScripts(String fullClassName) {
            // For the moment we dont allow to execute java code 
            if (fullClassName.startsWith("org.mozilla.javascript")) {
                return true;
            }
            if (fullClassName.startsWith("java.")) {
                return true;
            }
            return false;
        }
    }

}
