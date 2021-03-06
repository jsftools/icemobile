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
 *
 */

package org.icemobile.samples.spring.controllers;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.samples.spring.AccordionBean;
import org.icemobile.samples.spring.CarouselBean;
import org.icemobile.samples.spring.DateTimeSpinnerBean;
import org.icemobile.samples.spring.FlipSwitchBean;
import org.icemobile.samples.spring.GeolocationBean;
import org.icemobile.samples.spring.InputTextBean;
import org.icemobile.samples.spring.ListBean;
import org.icemobile.samples.spring.TabsetBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * General Controller for echoing simple input pages
 */
@Controller
@SessionAttributes({"geolocationBean", "carouselBean"})
public class EchoController extends BaseController{

    @RequestMapping(value = "/menu")
    public void doMenuRequest() {
    }
    
    @RequestMapping(value = "/accordion")
    public void doRequest(
        @ModelAttribute("accordionBean") AccordionBean model) {
    }

    @RequestMapping(value = "/buttons")
    public void doRequest(Model model,
                          @RequestParam(value = "submitB", required = false)
                          String submitted) {
        if (submitted != null) {
            if( submitted.indexOf(",") > -1 ){
                submitted = submitted.substring(submitted.indexOf(",")+1).trim();
            }
            model.addAttribute("pressed", "[" + submitted + "]");
        }
    }

    @RequestMapping(value = "/carousel")
    public void doRequest(
        @ModelAttribute("carouselBean") CarouselBean model) {
    }
    
    @ModelAttribute("carouselBean")
    public CarouselBean createBean() {
        return new CarouselBean();
    }

    @RequestMapping(value = "/datetime")
    public void doRequest(
        @ModelAttribute("dateTimeSpinnerBean")
        DateTimeSpinnerBean model) {
    }
    
    @RequestMapping(value = "/fieldset")
    public void doFieldsetRequest() {
    }

    @RequestMapping(value = "/flipswitch")
    public void doRequest(
        @ModelAttribute("flipSwitchBean") FlipSwitchBean model) {
    }

    @RequestMapping(value = "/geolocation")
    public void doRequest(@ModelAttribute("geolocationBean") GeolocationBean model) {
    }
    
    @ModelAttribute("geolocationBean")
    public GeolocationBean createGeolocationBean(HttpServletRequest request) {
        GeolocationBean bean = new GeolocationBean();
        return bean;
    }

    @RequestMapping(value = "/inputtext")
    public void doRequest(
        @ModelAttribute("inputTextBean") InputTextBean model) {
    }

    @RequestMapping(value = "/list")
    public void doRequest(@ModelAttribute("listBean") ListBean model) {
    }

    @RequestMapping(value = "/tabset")
    public void doRequest(@ModelAttribute("tabsetBean") TabsetBean model) {
    }

    @RequestMapping(value = "/panelPopup")
    public void doPanelPopupRequest() {
    }

}

