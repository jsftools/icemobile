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

package org.icemobile.component;

public interface IAccordion extends IMobiComponent{

    // ui-widget-header added here for borders due to implementation of li expansion
    public static final String ACCORDION_CLASS = "mobi-accordion ui-widget ui-widget-header";
    public static final String ACCORDION_RIGHT_POINTING_TRIANGLE = "&#9654;";
    public static final String ACCORDION_RIGHT_POINTING_POINTER= "&#9658;";
    public static final String ACCORDION_LEFT_POINTING_TRIANGLE = "&#9664;";
    public static final String ACCORDION_LEFT_POINTING_POINTER= "&#9668;";
    public static final String ACCORDION_DOWN_POINTING_ARROW="&#9660;";
    public static final String JS_NAME = "accordion.js";
    public static final String JS_MIN_NAME = "accordion-min.js";
    public static final String LIB_JSF = "org.icefaces.component.accordion";
    public static final String LIB_JSP = "javascript";
        
    public void setHeight(String fixedHeight);
    public String getHeight();
    public void setSelectedId(String currentId);
    public String getSelectedId();
    public void setAutoHeight(boolean autoHeight);
    public boolean isAutoHeight();
    public String getJavascriptFileRequestPath();
    public boolean isScriptLoaded();
    public void setScriptLoaded();
    public String getOpenedPaneClientId();
    public String getHashVal();
 //   public boolean isScrollablePaneContent();
}
