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
<mobi:outputList inset="false" id="galleryList">
    <c:forEach var="m" items="${mediaService.mediaListSortedByTime}">
        <mobi:outputListItem>
            <div id="${m.id}" data-created="${m.created}">
                <c:if test="${layout eq 'm'}">
                    <a class="mediaLink" href='<c:url value="/app?p=viewer&l=${layout}&photoId=${m.id}"/>'>
                        <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                    </a>
                </c:if>
                <c:if test="${layout ne 'm'}">
                    <a class="mediaLink" href="#" onclick="updateViewerPanel('${m.id}');">
                        <img src='resources/uploads/${m.smallPhoto.name}' class="p"/>
                    </a>
                </c:if>
                
                <span class="desc"><c:out value="${m.description}"/></span>
             </div>
        </mobi:outputListItem>
    </c:forEach>
</mobi:outputList>
<push:register group="photos" callback="function(){getGalleryUpdate();}"/>
