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

package org.icemobile.client.android.video;
import android.util.Log;

import org.icemobile.client.android.util.JavascriptInterface;
import org.icemobile.client.android.util.AttributeExtractor;

public class VideoInterface implements JavascriptInterface {

    private VideoHandler handler;
    private String result;

    public VideoInterface (VideoHandler handler) {
	this.handler = handler;
    }

    public String shootVideo(String thumbId, String attr) { 

	AttributeExtractor attributes = new AttributeExtractor(attr);
	int width = Integer.parseInt(attributes.getAttribute("maxwidth", "640"));
	int height = Integer.parseInt(attributes.getAttribute("maxheight", "480"));
	int thumbWidth = Integer.parseInt(attributes.getAttribute("thumbWidth", "64"));
	int thumbHeight = Integer.parseInt(attributes.getAttribute("thumbHeight", "64"));
	int maxDuration = Integer.parseInt(attributes.getAttribute("maxtime", "-1"));
	int quality;
	if (width > 640 || height >480) {
	    quality = 1; // High res
	} else {
	    quality = 0; // Low res
	}
        return handler.shootVideo(quality, thumbId, thumbWidth, thumbHeight, maxDuration);
    }
}
