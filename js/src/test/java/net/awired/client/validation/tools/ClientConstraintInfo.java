/**
 *
 *     Copyright (C) Awired.net
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
package net.awired.client.validation.tools;

import java.io.Reader;

public class ClientConstraintInfo {
    private Reader jsConstraint;
    private String jsConstraintName;
    private String constraintType;

    public ClientConstraintInfo(Reader jsConstraint, String jsConstraintName, String constraintType) {
        this.constraintType = constraintType;
        this.jsConstraintName = jsConstraintName;
        this.jsConstraint = jsConstraint;
    }

    public Reader getJsConstraint() {
        return jsConstraint;
    }

    public void setJsConstraint(Reader jsConstraint) {
        this.jsConstraint = jsConstraint;
    }

    public String getJsConstraintName() {
        return jsConstraintName;
    }

    public void setJsConstraintName(String jsConstraintName) {
        this.jsConstraintName = jsConstraintName;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }
}
