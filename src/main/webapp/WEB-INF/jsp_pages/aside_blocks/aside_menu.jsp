<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<fmt:setLocale value="${pageContext.response.locale}"/>
<fmt:setBundle basename="cra_language"/>

<div class="list-group sticky-top">

    <security:authorize access="hasAuthority('CUSTOMER')">
        <a href="${pageContext.request.contextPath}${CRAPaths.CUSTOMER_HOME}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.customer.personal_page"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.CUSTOMER_ORDER_HISTORY}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.customer.order_history"/></a>
    </security:authorize>

    <security:authorize access="hasAuthority('MASTER')">
        <a href="${pageContext.request.contextPath}${CRAPaths.MASTER_HOME}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.master.active_orders"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.MASTER_COMPLETED_ORDERS}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.master.completed_order"/></a>
    </security:authorize>

    <security:authorize access="hasAuthority('MANAGER')">
        <a href="${pageContext.request.contextPath}${CRAPaths.MANAGER_HOME}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.manager.pending_orders"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.ACTIVE_ORDERS}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.manager.active_orders"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.ORDER_HISTORY}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.manager.order_history"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.CUSTOMERS}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.manager.cra_customers"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.MASTERS}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.manager.cra_masters"/></a>
    </security:authorize>

    <security:authorize access="hasAuthority('ADMIN')">
        <a href="${pageContext.request.contextPath}${CRAPaths.ADMIN_HOME}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.admin.sys_info"/></a>
        <a href="${pageContext.request.contextPath}${CRAPaths.MAN_MAS_REGISTRATION}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_personal_menu.admin.reg_man/mas"/></a>
    </security:authorize>

    <security:authorize access="isAnonymous() or hasAuthority('CUSTOMER')">
        <a href="${pageContext.request.contextPath}${CRAPaths.CREATE_ORDER}"
           class="list-group-item list-group-item-action">
            <fmt:message key="cra.aside_menu.create_order"/></a>
    </security:authorize>

    <a href="${requestScope['javax.servlet.forward.servlet_path'] eq CRAPaths.HOME ?
                '#reviewsCards' : pageContext.request.contextPath.concat(CRAPaths.REVIEWS)}"
                         class="list-group-item list-group-item-action">
                            <fmt:message key="cra.aside_menu.reviews"/></a>

    <a href="${requestScope['javax.servlet.forward.servlet_path'] eq CRAPaths.HOME ?
                '#contacts' : pageContext.request.contextPath.concat(CRAPaths.HOME.concat('#contacts'))}"
                        class="list-group-item list-group-item-action">
                            <fmt:message key="cra.aside_menu.contacts"/></a>
</div>