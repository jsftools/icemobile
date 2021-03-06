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
        var hidden = document.getElementById(clientId+"_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }
    //-------------------------------------
    function PanelPopup(clientId, cfgIn) {
        var cId = clientId;
        var myId = cfgIn.id || null;
        var client = cfgIn.client;
        var visible = cfgIn.visible || false;
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var autocenter = cfgIn.autocenter || true;
        var centerCalculation = {};
        var baseopenClass = "mobi-panelpopup-container ";
        var openbgClass= "mobi-panelpopup-bg ";
        var closebgClass = "mobi-panelpopup-bg-hide ";
        var basecloseClass = "mobi-panelpopup-container-hide ";
        var opencontClass;
        var closecontClass;
        var origClass = cfgIn.sclass || null;
        if (origClass){
            opencontClass=baseopenClass+ cfgIn.sclass;
            closecontClass=basecloseClass +cfgIn.sclass;
        } else {
            opencontClass=  baseopenClass;
            closecontClass= basecloseClass;
        }
        ice.mobi.panelPopup.visible[clientId]= visible;
        return {
            openPopup: function(clientId, cfg) {
                autocenter = cfg.autocenter || true;
                if (cfg.sclass && origClass!==cfg.sclass){  //so can update dynamically
                    opencontClass=baseopenClass + cfg.sclass;
                    closecontClass=basecloseClass + cfg.sclass;
                }
                if (cfg.disabled){
                    return;//no opening
                }
                var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
                var fadedDiv = document.getElementById(idPanel);
                if (fadedDiv){
                   document.getElementById(idPanel).className = openbgClass;
                }
                var containerNode = document.getElementById(containerId);
                containerNode.className = opencontClass;
                if (cfg.autocenter==true) {
                    centerCfg = {} ;
                    if (cfg.width){
                        centerCfg.width = cfg.width;
                    }
                    if (cfg.height){
                        centerCfg.height = cfg.height;
                    }
                    if (cfg.style){
                        centerCfg.style = cfg.style;
                    }
                    if (cfg.useForm){
                        var frm = mobi.findForm(clientId);
                        if (frm){
                           centerCfg.containerElem = frm.id;
                           // console.log("form id="+frm.id);
                        }
                    }
                    // add scroll listener
                    centerCalculation[clientId] = function () {
                        ice.mobi.panelCenter(containerId, centerCfg);
                    };
                    ice.mobi.addListener(window, scrollEvent, centerCalculation[clientId]);
                    ice.mobi.addListener(window, 'resize', centerCalculation[clientId]);

                    // calculate center for first view
                    ice.mobi.panelCenter(containerId, centerCfg);
                }  else{
                 //   console.log("NO AUTOCENTER");
                    var styleVar = "";
                    if (cfg.width){
                        var wStr = width+"px";
                        styleVar+="width: "+cfg.width+"px;";
                    }
                    if (cfg.height){
                        var hStr = height+"px";
                        styleVar +=" height: "+cfg.height+"px;";
                    }
                    if (cfg.style){
                        styleVar += cfg.style;
                    }
                    containerNode.setAttribute("style", styleVar);
                }
                ice.mobi.panelPopup.visible[clientId] = true;
                updateHidden(clientId, "true");
           },
           closePopup: function(clientId, cfg){
               var containerNode = clientId+"_popup";
               var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
               var greyed = document.getElementById(clientId+"_bg");
               var container = document.getElementById(containerNode);
               if (greyed){
                   greyed.className = closebgClass;
               }
               if (container){
                   document.getElementById(containerNode).className = closecontClass;
               }
               if (cfg.autocenter==true) {
                     if (window.removeEventListener) {
                         window.removeEventListener(scrollEvent, centerCalculation[clientId], false);
                         window.removeEventListener('resize', centerCalculation[clientId], false);
                     } else { // older ie cleanup
                         window.detachEvent(scrollEvent, centerCalculation[clientId], false);
                         window.detachEvent('resize', centerCalculation[clientId], false);
                     }
                     centerCalculation[clientId] = undefined;
               } else {
             //      container.setAttribute("style", "");
               }
               ice.mobi.panelPopup.visible[clientId] = false;
               updateHidden(clientId, "false");
           },
           getId: function(){
               return myId;
           },
           getClientId: function(){
               return cId;
           },
           isClient: function(){
               return client;
           }
        }
    }
    ice.mobi.panelPopup = {
        panels: new Array(),
        visible: {},
        cfg: {},
        centerCalculation:{},
        init: function(clientId, cfgIn) {
          //  console.log("INIT");
            this.cfg[clientId] = cfgIn;
            var thisOne = this.findPanel(clientId, false);
            var i = this.panels.length;
            if (thisOne == null){
                this.panels[i] = PanelPopup(clientId, cfgIn);
            } else {
                var vis =  cfgIn.visible || false;
                if (vis==true){
                   thisOne.openPopup(clientId, cfgIn);
                }else{
                   thisOne.closePopup(clientId, cfgIn);
                }
            }
        },
        openClient: function(popupId){
            var opC = this.findPanel(popupId, true);
            if (!opC){
                var index = this.panels.length;
            }
            if (opC){
                var cId = opC.getClientId();
                var cfgA = this.cfg[cId];
                if (!cfgA.disabled){
                    var chkNode = document.getElementById(cId);
                    if (!chkNode){
                        ice.log.debug(ice.log,"NO ELEMENT CAN BE FOUND FOR ID="+popupId+" clientId="+cId);
                    } else {
                       opC.openPopup(cId, cfgA);
                    }
                }
            }
        },
        closeClient: function(popupId){
            var clA = this.findPanel(popupId,true);
            if (clA){
                var cId =clA.getClientId();
                var cfgA = this.cfg[cId];
                clA.closePopup(cId, cfgA);
            }
        },
        findPanel: function(popupId, isId){
            if (this.panels.length < 1){
                ice.log.debug(ice.log,' no popups available in view to open');
                return;
            }
            var found = false;
            for (var i=0; i < this.panels.length; i++){
                var pane = this.panels[i];
                if (isId){
                    var myId = pane.getId();
                  //  console.log ("id of pane="+myId);
                    if (pane.getId()==popupId){
                        found = true;
                        return pane;
                    }
                }else {
                    if (pane.getClientId()==popupId){
                        found = true;
                        return pane;
                    }
                }
            }
            if (!found){
                ice.log.debug(ice.log, ' Cannot find popup with id='+popupId);
            }
            return null;
        }
    }

  })();