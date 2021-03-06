/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

public interface ISplitPane extends IMobiComponent{
    
    public static final String SPLITPANE_BASE_CSS = "mobi-splitpane" ;
    public static final String SPLITPANE_NONSCROLL_CSS = "mobi-splitpane-nonScrollable";
    public static final String SPLITPANE_SCROLLABLE_CSS = "mobi-splitpane-scrollable";
    public static final String SPLITPANE_DIVIDER_CSS = "mobi-splitpane-divider";
    /**
    * <p>the component id that this thumbnail represents, either camera or camcorder.</p>
    */
    public int getColumnDivider();

    /**
     * <p> the thumb base class depends on if an upload has been detected (jsf only)</p>
     */
    public boolean isScrollable();


}
