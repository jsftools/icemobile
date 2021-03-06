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

package org.icemobile.jsp.tags;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.icemobile.jsp.tags.layout.AccordionTag;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class ContentTag extends TagSupport {

    private boolean isActive;

    private static final String MOBI_TABSET_CONTENT_CLASS = "mobi-tabset-content";
    private static final String CONTENT_WRAPPER_CLASS = ".mobi-panel-stack";


    private static Logger LOG = Logger.getLogger(ContentTag.class.getName());

    private TabSetTag mTabParent;

    private String mContentClass;
    private String mSelectedItem;


    public void setParent(Tag parent) {

        if (parent instanceof TabSetTag) {
            mContentClass = MOBI_TABSET_CONTENT_CLASS;
            mTabParent = (TabSetTag) parent;
            mSelectedItem = mTabParent.getSelectedTab();
        }  else {
            throw new IllegalArgumentException("ContentTag must have TabSet or Accordian parent");
        }
    }


    public boolean isTabParent() {
        return mTabParent != null;
    }

    public int doStartTag() {

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        Writer out = pageContext.getOut();

        if (!isTabParent()) {

            // There is no Content area wrapper <div> in accordion

            return EVAL_BODY_INCLUDE;
        }
        tag.append(" id=\"").append(mTabParent.getId()).append("_tabContent").append("\"");
        tag.append(" class=\"").append(mContentClass).append("\"");
        tag.append(">");

        try {

            out.write(tag.toString());

        } catch (IOException ioe) {
            LOG.severe("IOException starting ContentTag: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
     * @return
     */
    public int doEndTag() {

        Writer out = pageContext.getOut();
        try {
            if (mTabParent != null) {
                out.write(TagUtil.DIV_TAG_END);
            }

        } catch (IOException ioe) {
            LOG.severe("IOException closing content tag: " + ioe);
        } finally {
            if (mTabParent != null) {
                mTabParent.resetIndex();
            }
        }
        return EVAL_PAGE;
    }

    public String getSelectedItem() {
        return mSelectedItem;
    }

    public String getIndex() {
        if (mTabParent != null) {
            return mTabParent.getIndex();
        }
        throw new UnsupportedOperationException("getIndex() has no value");
    }

    public String getId() {
        if (mTabParent != null) {
            return mTabParent.getId();
        } else {
            throw new UnsupportedOperationException("getId() has no value");
        }
    }
}
