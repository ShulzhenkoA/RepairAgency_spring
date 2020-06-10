<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.javaexternal_shulzhenko.car_repair_agency.constants.CRAPaths" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="cust" %>
<%@ taglib uri="/WEB-INF/cust_tags.tld" prefix="cust_formatter" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<fmt:setLocale value="${pageContext.response.locale}"/>
<fmt:setBundle basename="cra_language"/>

<div class="col-md-12">

    <security:authorize access="!isAnonymous() and !hasAuthority('CUSTOMER')">
        <div class="col-md-8 red-mess"><fmt:message key="cra.reviews.only_cus"/></div>
    </security:authorize>

    <security:authorize access="isAnonymous()">
        <div class="col-md-8 red-mess"><fmt:message key="cra.reviews.log_in"/></div>
    </security:authorize>

    <security:authorize access="hasAuthority('CUSTOMER')">
        <c:if test="${success eq null}">
            <c:if test="${bindingResult.hasFieldErrors('reviewContent')}">
                <p class="formError">
                    <fmt:message key="cra.reviews.inconsistencies.you_can't"/>
                </p>
            </c:if>
            <form action="${pageContext.request.contextPath}${CRAPaths.REVIEWS}" method="post">
                <textarea id="review-textarea" class="form-control" name="reviewContent" required
                          placeholder="<fmt:message key="cra.reviews.comment_area_pl"/>"></textarea>
                <div class="col-md-4 offset-md-8">
                    <button type="submit" class="btn btn-block">
                        <fmt:message key="cra.reviews.leave_review_btn"/></button>
                </div>
            </form>
        </c:if>
        <c:if test="${success ne null}">
            <fmt:message key="cra.reviews.success"/>
        </c:if>
    </security:authorize>
    <hr>
</div>

<div class="col-md-12" id="review">
    <c:forEach var="review" items="${reviews}">
        <h5>${review.customer.firstName}</h5>
        <p>${review.reviewContent}</p>
        <p id="reviewDate">
            <small><cust_formatter:date-formatter localDateTime="${review.dateTime}" pattern="yyyy-MM-dd"/></small></p>
        <hr>
    </c:forEach>
</div>

<cust:pagination pagination_model="${pgModel}"/>