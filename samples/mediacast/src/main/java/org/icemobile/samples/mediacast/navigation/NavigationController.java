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

package org.icemobile.samples.mediacast.navigation;

import org.icefaces.util.JavaScriptRunner;
import org.icemobile.samples.util.FacesUtils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Simple controller to manage the navigation stack.  One can go forward
 * or back.  This is to facilitate the mobile stack navigation model.
 */
@ManagedBean(name = NavigationController.BEAN_NAME)
@ApplicationScoped
public class NavigationController implements Serializable {

    public static final String BEAN_NAME = "navigationController";

    public String navigateToPage(){
        String panelId = FacesUtils.getRequestParameter("panelId");
        navigateToPage(panelId);
        return null;
    }

    public String navigateToPage(String panelId) {
        NavigationModel navigationModel = getNavigationModel();
        navigationModel.goForward(panelId);
        scrollToTop();
        return null;
    }

    public String navigateBack() {
        NavigationModel navigationModel = getNavigationModel();
        navigationModel.goBack();
        scrollToTop();
        return null;
    }

    protected NavigationModel getNavigationModel() {
        return (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);
    }

    protected void scrollToTop(){
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),"javascript:scroll(0,0);");
    }
}
