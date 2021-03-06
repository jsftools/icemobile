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

package org.icemobile.samples.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import org.icemobile.samples.spring.NotificationsBean;
import org.icepush.PushContext;
import org.icepush.PushNotification;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.inject.Inject;

@Controller
@SessionAttributes("notificationsBean")
public class NotificationsController extends BaseController{

    Timer pushTimer = new Timer(true);

    @Inject
    private WebApplicationContext context;

    @RequestMapping(value = "/notifications", 
            headers="content-type=application/x-www-form-urlencoded")
    public void fileUploadFormPost(HttpServletRequest request, Model model) {
        fileUploadForm(request, model);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public void fileUploadForm(HttpServletRequest request, Model model) {
        model.addAttribute("isGET", Boolean.TRUE);
    }

    @RequestMapping(value = "/notificationsregion")
    public void notificationsRegion() {
    }

    @ModelAttribute("notificationsBean")
    public NotificationsBean createBean() {
        return new NotificationsBean();
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    public void process(
            @RequestParam(value = "pushType", required = false) String pushType,
            @ModelAttribute("notificationsBean") NotificationsBean model)
            throws IOException {
        
        final PushContext pushContext = PushContext.getInstance(
                context.getServletContext() );
        final String title = model.getTitle();
        final String message = model.getMessage();
        model.setTitle("");
        model.setMessage("");
        final NotificationsBean notificationsBean = model;
        final boolean isCloud = "Priority Push".equals(pushType);
        TimerTask pushTask = new TimerTask()  {
            public void run()  {
                notificationsBean.setTitle(title);
                notificationsBean.setMessage(message);
                if (isCloud)  {
                    pushContext.push("notifications",
                            new PushNotification(title, message));
                } else  {
                    pushContext.push("notifications");
                }
            }
        };
        pushTimer.schedule(pushTask, model.getDelay() * 1000);
    }

}
