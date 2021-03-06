/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobi.component.fieldset;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "fieldsetGroup",
        componentClass = "org.icefaces.mobi.component.fieldset.FieldSetGroup",
        rendererClass = "org.icefaces.mobi.component.fieldset.FieldSetGroupRenderer",
        generatedClass = "org.icefaces.mobi.component.fieldset.FieldSetGroupBase",
        componentType = "org.icefaces.FieldSetGroup",
        rendererType = "org.icefaces.FieldSetGroupRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.FieldSetGroup",
        tlddoc = "fieldSetGroup organizes a grouping of unordered list items. The immediate children " +
                "must be fieldsetRow components. "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class FieldSetGroupMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="true",
    		  tlddoc = "When \"true\", renders padding around list group. ")
    private boolean inset;
 
    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

}
