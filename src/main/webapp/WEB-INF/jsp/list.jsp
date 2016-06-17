<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 引入标签 -->
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>商品详情</title>
<%@include file="common/head.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-xs-12 table-responsive">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="sk" items="${list}">
							<tr>
								<td>${sk.name}</td>
								<td>${sk.number}</td>
								<td>
									<fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td>
									<fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td>
									<fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td>
									<a class="btn btn-info btn-sm" href="${sk.seckillId}/detail" target="_blank">详情</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<%@include file="common/foot.jsp"%>
</body>
</html>