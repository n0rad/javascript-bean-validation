/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.client.validation.tools;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class RhinoContextClasses {

    public static class SandboxWrapFactory extends WrapFactory {
        @Override
        public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
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
        public SandboxNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
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
