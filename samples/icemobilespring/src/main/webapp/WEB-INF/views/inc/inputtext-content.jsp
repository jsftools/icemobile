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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi"%>
<%@ taglib prefix="push"
	uri="http://www.icepush.org/icepush/jsp/icepush.tld"%>
<form:form id="inputtextform" method="POST"
	modelAttribute="inputTextBean" cssClass="form">

	<c:if test="${viewSize eq 'large'}">
		<h3>HTML5 Input</h3>
	</c:if>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow>
			The HTML5 input tag exposes the new set of HTML5 input
			features on supported browsers.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label>Text:</label>
			<mobi:inputText name="text" type="text" autoCorrect="off"
				placeholder="Text input" value="${inputTextBean.text}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Number:</label>
			<mobi:inputText name="number" type="number" autoCorrect="off"
				placeholder="Number" value="${inputTextBean.number}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Text area:</label>
			<mobi:inputText name="textarea" type="textarea" autoCorrect="off"
				placeholder="Text area" value="${inputTextBean.textarea}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Password:</label>
			<mobi:inputText name="password" type="password" autoCorrect="off"
				placeholder="Password input" value="${inputTextBean.password}" />
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Date: (yyyy-mm-dd)</label>
			<mobi:inputText name="date" type="date" autoCorrect="off"
				placeholder="yyyy-mm-dd" value="${inputTextBean.date}" />
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:commandButton buttonType='important' styleClass="submit"
		value="Submit" type="submit" />

	<h3 style="clear: both; margin-top: 10px;">Input Values</h3>
	<mobi:fieldsetGroup>
		<mobi:fieldsetRow>
			<label>Text:</label>
			<span>${inputTextBean.text}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Number:</label>
			<span>${inputTextBean.number}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Text area:</label>
			<span>${inputTextBean.textarea}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Password:</label>
			<span>${inputTextBean.password}</span>
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			<label>Date</label>
			<span>${inputTextBean.date}</span>
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

	<mobi:fieldsetGroup styleClass="intro">
		<mobi:fieldsetRow group="true">
			Description
		</mobi:fieldsetRow>
		<mobi:fieldsetRow>
			The HTML5 input tag exposes the new set of HTML5 input features on supported browsers. 
			Most notable is the type attribute, which can act as a hint to load a specific keyboard, 
			which matches the specified type. Another notable features is the placeholder attribute, 
			which supports ghost input hints that disappear when the user types content.
		</mobi:fieldsetRow>
	</mobi:fieldsetGroup>

</form:form>

<script type="text/javascript">
	MvcUtil.enhanceForm("#inputtextform");
</script>
