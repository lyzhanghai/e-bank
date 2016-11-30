<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tmpl" uri="/jsp-templ.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<tmpl:override name="title">个性化菜单规则列表</tmpl:override>

<tmpl:override name="page_css">
</tmpl:override>

<tmpl:override name="rightBox">
    <%-- 标题 --%>
    <span class="title">个性化菜单规则列表</span>
    <div class="greyLine"></div>

    <%-- 条件查询及按钮操作区域 --%>
    <div class="button-wrapper">
        <div class="left button-group-wrapper">
            <sec:authorize url="/weixin/menu/custom/rule/add">
                <div class="button-group">
                    <a href="javascript:CURD.add('${pageContext.request.contextPath}/weixin/menu/custom/rule/add', '新增', '600px', '700px');" class="button">
                        <img src="${pageContext.request.contextPath}/resources/images/btn_add_n.png" height="18" width="18"
                             alt="添加">
                        <span>添加</span>
                    </a>
                </div>
            </sec:authorize>
        </div>
        <div class="right">
            <form class="form-inline" role="form" action="${pageContext.request.contextPath}/weixin/menu/custom/rule/list">
                <div class="form-group">
                    <label>名称</label>
                    <input name="name" type="text" class="form-control" value="${menuCustomRule.name}">
                </div>
                <div class="form-group">
                    <a href="javascript:void(0);" onclick="CURD.search(this)" class="button"><img
                            src="${pageContext.request.contextPath}/resources/images/btn_search_n.png" alt="查询"
                            height="18"
                            width="18">
                        <span>查询</span>
                    </a>
                </div>
                <div class="form-group">
                    <a href="javascript:void(0);" onclick="CURD.reset(this)" class="button"><img
                            src="${pageContext.request.contextPath}/resources/images/btn_Reset_n.png" alt="重置"
                            height="18"
                            width="18">
                        <span>重置</span>
                    </a>
                </div>
            </form>
        </div>
    </div>
    <div class="clearfix"></div>

    <%-- 表格数据 --%>
    <div id="table">
        <table class="table" cellspacing="0">
            <tr>
                <th></th>
                <th>名称</th>
                <th>备注</th>
                <th>菜单包</th>
                <th>同步状态</th>
                <th>同步操作</th>
                <th>操作</th>
            </tr>
            <c:if test="${pagination ne null && pagination.data ne null}">
                <c:forEach var="menuCustomRule" items="${pagination.data}">
                    <tr>
                        <td><input type="checkbox" value="${menuCustomRule.id}"></td>
                        <td>${menuCustomRule.name}</td>
                        <td>${menuCustomRule.remark}</td>
                        <td>${menuCustomRule.menuCustom.name}</td>
                        <td>${menuCustomRule.status.name}</td>
                        <td>
                            <sec:authorize url="/weixin/menu/custom/rule/sync/">
                                <a href="javascript:_sync('${pageContext.request.contextPath}/weixin/menu/custom/rule/sync/${menuCustomRule.id}')" title="同步">
                                    <img src="${pageContext.request.contextPath}/resources/images/reset.png" alt="同步">
                                </a>
                            </sec:authorize>
                            <sec:authorize url="/weixin/menu/custom/rule/delete/">
                                <a href="javascript:CURD.delete('${pageContext.request.contextPath}/weixin/menu/custom/rule/delete/${menuCustomRule.id}')" title="删除">
                                    <img src="${pageContext.request.contextPath}/resources/images/btn_delete_n.png" alt="删除">
                                </a>
                            </sec:authorize>
                        </td>
                        <td class="last-td">
                            <sec:authorize url="/weixin/menu/custom/rule/view/">
                                <a href="javascript:CURD.view('${pageContext.request.contextPath}/weixin/menu/custom/rule/view/${menuCustomRule.id}', '查看', '650px', '700px')" title="查看">
                                    <img src="${pageContext.request.contextPath}/resources/images/eye.png">
                                </a>
                            </sec:authorize>
                            <sec:authorize url="/weixin/menu/custom/rule/edit/">
                                <a href="javascript:CURD.edit('${pageContext.request.contextPath}/weixin/menu/custom/rule/edit/${menuCustomRule.id}', '编辑', '600px', '700px')" title="编辑">
                                    <img src="${pageContext.request.contextPath}/resources/images/edit.png" alt="编辑">
                                </a>
                            </sec:authorize>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </div>

    <%-- 引入分页 --%>
    <jsp:include page="../../shared/pagination.jsp" flush="true">
        <jsp:param name="pagination" value="${pagination}"/>
        <jsp:param name="paginationURL" value="${pageContext.request.contextPath}/weixin/menu/custom/rule/list?name=${menuCustomRule.name}"/>
    </jsp:include>
</tmpl:override>

<tmpl:override name="page_script">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/plugins/jquery.dropkick-min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/pagination.js"></script>
    <script type="text/javascript">
        // 初始化CURD
        CURD.init(window, window);

        <sec:authorize url="/weixin/menu/custom/rule/sync/">
        var _sync = function (url) {
            layer.confirm('确定同步数据至微信?', {icon: 7, title: '警告'}, function (index) {
                var loadIndex = layer.load();
                $.get(url, function (result) {
                    layer.alert(result.message, function () {
                        layer.close(loadIndex);
                        window.location.reload();
                    });
                });
                layer.close(index);
            });
        };
        </sec:authorize>
    </script>
</tmpl:override>

<%@ include file="../../shared/decorator.jsp" %>