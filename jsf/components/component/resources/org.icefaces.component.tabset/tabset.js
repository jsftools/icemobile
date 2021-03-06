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
(function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables
    //---------------------------------------
    function updateHidden(clientId, value) {
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }

    /* taken from accordion with slight modifications */
    function calcMaxChildHeight(containerEl) {
        var mxht = 0;
        if( containerEl ){
          //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
            var children = containerEl.getElementsByTagName('div');
            for (var i = 0; i < children.length; i++) {
                if (children[i].scrollHeight > mxht) {
                    mxht = children[i].scrollHeight;
                }
            }
        }
        return mxht;
    }

    function hasClass(ele, cls) {
        return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
    }

    function addClass(ele, cls) {
        if (hasClass(ele, cls)) {
            ele.className = cls;
        }
    }

    function removeClass(ele, cls) {
        if (hasClass(ele, cls)) {
            // var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');  don't need if we don't allow to skin?
            ele.className = " ";
        }
    }

    function setWidthStyle(root){
        if( root ){
            var nodes = root.getElementsByTagName('ul');
            var ul = nodes[0];
            var children = ul.getElementsByTagName('li');
            var containerWidth = root.clientWidth;        
            var width = Math.floor(containerWidth/children.length);
            var remainingPixels = containerWidth - (children.length*width);
            var percentageWidth = Math.floor(100/children.length);
            var remainingPercentage = 100 - (children.length*percentageWidth);
            
            for (var i = 0; i < children.length; i++){
                if( i < (children.length -1 )){
                    children[i].style.width = width+"px";
                    children[i].style.maxWidth = percentageWidth+"%";
                }
                else{
                    children[i].style.width = (width+remainingPixels)+"px";
                    children[i].style.maxWidth = (percentageWidth+remainingPercentage)+"%";
                }
                
            }
        }
    }

    function setTabActive(id, cls) {
        var curTab = document.getElementById(id);
        if (curTab) {
            curTab.setAttribute("class", cls);
        }  else {
            console.log("PROBLEM SETTING ATIVE TAB FOR id="+id);
        }
    }

    //declare functions who creates object with methods that have access to the local variables of the function
    //so in effect the returned object can operate on the local state declared in the function ...
    //think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
    //-------------------------------------
    function TabSet(clientId, cfgIn) {
        // setup tabContainer
        var tabContainer = document.getElementById(clientId);
        var contentId = clientId+"_tabContent";
        var tabContent = document.getElementById(contentId);
        var classHid = "mobi-tabpage-hidden";
        var classVis = "mobi-tabpage";
        var clsActiveTab = "activeTab ui-state-active";
        var tabCtrl = clientId + "tab_";
        var tabIndex = cfgIn.tIndex;
        var autoWidth = cfgIn.autoWidth;
        if (autoWidth){
            setTimeout( function(){
                setWidthStyle(tabContainer);
            }, 10);
            var setWidthStyleListener = function(){ 
                if( !tabContainer ){
                    ice.mobi.removeListener(window,"resize", this);
                    return;
                };
                setWidthStyle(tabContainer); 
            }
            ice.mobi.addListener(window, 'resize', setWidthStyleListener);
        }
        var lastServerIndex = tabIndex;
        var height = cfgIn.height || -1;
        var disabled = cfgIn.disabled;
        var autoheight = cfgIn.autoheight || false;
        var cntr = 0;
        if (height !== -1) {
            tabContent.style.maxHeight = height;
            tabContent.style.height = height;
        } else if (autoheight == true){
            var ht = calcMaxChildHeight(tabContent);
            if (ht > 0) {
                tabContent.style.height = ht + "px";
            }
        }
        setTabActive(tabCtrl + tabIndex, clsActiveTab);
        updateHidden(clientId, tabIndex);
        var contents = tabContent.childNodes;
        var length = contents.length;
        var newPage = contents[tabIndex];
        newPage.className = classVis;
        for (i = 0; i < length; i++) {
            if (i != tabIndex) {
               contents[i].className = classHid;
            }
        }

        return {
            showContent: function(clientId, el, cfgIn) {
                if ( disabled == true) {
                    return;
                }
                if (!contentId){
                    contentId = clientId+"_tabContent";
                }
         //       tabContent = document.getElementById(contentId);
                contents = this.getContents(clientId);
                var parent = el.parentNode;
                if (!parent) {
                    parent = el.parentElement;
                }
                var old = tabIndex;
                var oldPage = contents[old];
                oldPage.className = classHid;
                var currCtrl = tabCtrl + old;
                var oldCtrl = document.getElementById(currCtrl);
                removeClass(oldCtrl, clsActiveTab);
                var isClient = cfgIn.client || false;
                tabIndex = cfgIn.tIndex;
             //   console.log("SHOWCONTENT tabIndex="+tabIndex+" old="+old);
                if (lastServerIndex==tabIndex){
                    cntr= cntr + 1;
                } else {
                    cntr = 0;
                    lastServerIndex = tabIndex;
                }
                var submitted = tabIndex +","+cntr;
                  //  console.log(" submitted="+submitted);
                updateHidden(clientId, submitted);
                contents[old].setAttribute("class", classHid);
                if (!isClient) {
                    ice.se(null, clientId);
                } else {
                    contents[tabIndex].setAttribute("class", classVis);
                }
                el.setAttribute("class", clsActiveTab);
             //   console.log("end of SHOW CONTENTS:-") ;
            },
            updateProperties: function (clientId, cfgUpd) {
                var newHt = cfgUpd.height || -1;
                if (newHt !== -1 && newHt !== height ){
                    tabContainer.style.maxHeight = newHt;
                    tabContainer.style.height = newHt;
                }
                var autoWidth = cfgUpd.autoWidth;
                if (autoWidth){
                    setTimeout( function(){
                        setWidthStyle(document.getElementById(clientId));
                    }, 1);
                    var setWidthStyleListener = function(){ 
                        if( !document.getElementById(clientId) ){
                            ice.mobi.removeListener(window,"resize", this);
                            return;
                        };
                        setWidthStyle(document.getElementById(clientId)); 
                    }
                    ice.mobi.addListener(window, 'resize', setWidthStyleListener);
                }
                var oldIdx = tabIndex;
                tabIndex = cfgUpd.tIndex;
            //    console.log ('UPDATEPROPS>>>> old ='+oldIdx+" UPDATED TabINDEX="+tabIndex);
                var oldCtrl = document.getElementById(tabCtrl + oldIdx);
                if (oldCtrl) {
                    removeClass(oldCtrl, clsActiveTab);
                }
                //check to see if pages have been added or removed
                if (!contentId){
                    contentId = clientId+"_tabContent";
                }
           //     tabContent = document.getElementById(contentId);
                contents = this.getContents(clientId);
                var newCtrl = tabCtrl+tabIndex;
                setTabActive(newCtrl, clsActiveTab);
                if (oldIdx != tabIndex){
                    contents[oldIdx].setAttribute("class", classHid);
                    contents[tabIndex].setAttribute("class", classVis);
                }
              //  console.log("end of UPDATE PROPS:-") ;
            },
            setDisabled: function(disabledIn){
                disabled = disabledIn;
            },
            getContents: function(clientId){
                contentId = clientId+"_tabContent";
                tabContent = document.getElementById(contentId);
                return tabContent.childNodes;
            }
        }
    }

    ice.mobi.tabsetController = {
        tabsets: {},
        initClient: function(clientId, cfg) {
            if (!this.tabsets[clientId]) {
                this.tabsets[clientId] = TabSet(clientId, cfg);
            } else {
                this.tabsets[clientId].setDisabled(cfg.disabled);
                this.tabsets[clientId].updateProperties(clientId, cfg);
            }
        },
        showContent: function(clientId, el, cfgIn) {
            if (this.tabsets[clientId]) {
                this.tabsets[clientId].showContent(clientId, el, cfgIn);
            }
        }
    }

})();