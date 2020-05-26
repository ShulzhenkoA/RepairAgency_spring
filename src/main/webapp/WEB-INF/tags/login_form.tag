<%@ tag pageEncoding="UTF-8" %>
<%@ tag import="ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<fmt:setLocale value="${pageContext.response.locale}"/>
<fmt:setBundle basename="cra_language"/>

<div class="modal-dialog${requestScope['javax.servlet.forward.servlet_path'] eq CRAPaths.LOGIN ? '' : ' modal-dialog-centered'}">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title"><fmt:message key="cra.login_form.header"/></h4>
        </div>
        <div class="modal-body">
            <p class="formError">
                <c:choose>
                    <c:when test="${email ne null}">
                        <fmt:message key="cra.login_form.error_msg.email"/>
                    </c:when>
                    <c:when test="${pass ne null}">
                        <fmt:message key="cra.login_form.error_msg.pass"/>
                    </c:when>
                </c:choose>
            </p>
            <form action="${pageContext.request.contextPath}${CRAPaths.LOGIN}" method="post">
                <div class="form-group">
                    <input type="text" class="form-control" required placeholder="E-mail" name="email"
                           value="${prevEmail}">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" required
                           placeholder="<fmt:message key="cra.login_form.pass_pl"/>" name="password">
                </div>
                <div class="col-sm-6 offset-sm-3 submit-button">
                    <button type="submit" class="btn btn-block">
                        <fmt:message key="cra.login_form.login_btn"/></button>
                </div>
            </form>
        </div>
        <div class=" modal-footer">
            <div class="col-sm-12"><fmt:message key="cra.login_form.not_reg"/>
                <a <c:choose>
                    <c:when test="${requestScope['javax.servlet.forward.servlet_path'] eq CRAPaths.LOGIN}">
                        href="${pageContext.request.contextPath}${CRAPaths.REGISTRATION}"
                    </c:when>
                    <c:otherwise>
                        href="" data-toggle="modal" data-target="#registerForm" data-dismiss="modal"
                    </c:otherwise>
                </c:choose>> <fmt:message key="cra.login_form.reg_btn"/></a>
            </div>
        </div>
    </div>
</div>