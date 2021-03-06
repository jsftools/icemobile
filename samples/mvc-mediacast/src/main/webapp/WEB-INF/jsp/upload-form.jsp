<%--
  ~ Copyright 2004-2012 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div id="uploadFormContainer">
     
    <form:form id="uploadForm" method="POST" enctype="multipart/form-data" 
        htmlEscape="true" cssClass="form">
        <input type="hidden" name="layout" value="${layout}"/>
        <input type="hidden" name="fullPost" id="fullPost" value="false"/>
        <input type="hidden" name="operation" id="operation"/>
        <form:errors path="*" cssClass="errorblock" element="div" />
        
        <mobi:getEnhanced/>
        
        <mobi:fieldsetGroup inset="true">
           <mobi:fieldsetRow style="font-weight: bold;color: #326ADB">
                <span>
                    <c:choose>
                        <c:when test="${desktop}">
                            Share a photo, video clip, or audio recording with your mobile device.
                        </c:when>
                        <c:otherwise>
                            Upload a photo, video clip, or audio recording.
                        </c:otherwise>
                    </c:choose>
                </span>
           </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
        
        <div id="msg" style="clear: both;margin:10px;font-size:12px;color:#326ADB;font-weight:bold">${msg}</div>
        
        <c:if test="${sessionScope['sxRegistered'] or enhanced}">
            <mobi:fieldsetGroup inset="true">
                 <mobi:fieldsetRow>
                    <c:if test="${!desktop and empty sessionScope['sxPhotoUpload']}">
                        <mobi:camera id="camera"/>
                    </c:if>
                    <c:if test="${!desktop and empty sessionScope['sxVideoUpload']}">
                        <mobi:camcorder id="camcorder"/>
                    </c:if>
                    <c:if test="${!desktop and empty sessionScope['sxAudioUpload']}">
                        <mobi:microphone id="microphone" buttonLabel="Audio"/>
                    </c:if>
                 </mobi:fieldsetRow>  
                 <mobi:fieldsetRow>
                     <c:if test="${!sessionScope['sxRegistered']}">
                        <mobi:thumbnail for="camera"/>
                    </c:if>
                    <c:if test="${not empty sxThumbnail and not empty sessionScope['sxPhotoUpload']}">
                        <img src='resources/uploads/${sxThumbnail.name}'/>
                    </c:if>
                    <c:if test="${not empty sxVideoUpload}">
                        <img src='resources/images/movieIcon.png' style="height:85px;width:81px"/>
                    </c:if>
                    <c:if test="${not empty sxAudioUpload}">
                        <img src='resources/images/soundIcon.png' style="height:85px;width:81px"/>
                    </c:if>
                 </mobi:fieldsetRow>     
                 <mobi:fieldsetRow>
                     <mobi:inputText name="description" 
                        styleClass="input"
                        type="textarea"
                        autoCorrect="off"
                        placeholder="Description"/>
                 </mobi:fieldsetRow>
                 <mobi:fieldsetRow style="text-align:center;">
                    <input type="submit" class="mobi-button"
                           value="Cancel" style="width:100px;margin-top:0;margin-bottom:0"
                           onclick="$('#operation').val('cancel')"/>
                    <input type="submit" class="mobi-button mobi-button-important"
                           value="Share" style="float:none;width:100px;margin-top:0;margin-bottom:0;"
                           onclick="$('#fullPost').val(true)" ${desktop ? "disabled='disabled'" : ""}/>
                </mobi:fieldsetRow>
            </mobi:fieldsetGroup>
        </c:if>
        <mobi:fieldsetGroup inset="true" style="padding:10px;">
            <mobi:fieldsetRow style="text-align:center;padding:0;">
            Learn more about ICEmobile video, audio, QR code, augmented reality
            and rich UI features at <a href="http://www.icesoft.org/projects/ICEmobile/overview.jsf">www.icesoft.org/projects/ICEmobile/</a>.
            </mobi:fieldsetRow>
        </mobi:fieldsetGroup>
    </form:form>
    <script type="text/javascript">
        enhanceForm("#uploadForm","#root");
        window.onhashchange = function()  {
            if ("#icemobilesx" === window.location.hash)  {
                window.location.hash = "";
                window.location.reload();
            }
        }
        window.scrollTo(0, 0);
    </script>
</div>