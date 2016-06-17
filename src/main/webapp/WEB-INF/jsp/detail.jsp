<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>秒杀列表</title>
<%@include file="common/head.jsp"%>
</head>
<body>
	<!-- phone modal -->
	<div id="phoneModal" class="modal fade" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">秒杀电话</h4>
				</div>
				<div class="modal-body">
					<div class="input-group">
					 	<span class="input-group-addon">
					 		<span class="glyphicon glyphicon-earphone" aria-hidden="true"></span>
					 	</span>
						<input id="phoneInput" type="text" class="form-control" placeholder="请输入手机号^o^">
					</div>
				</div>
				<div class="modal-footer">
					<span id="phoneCheckInfo" class="text-danger"></span>
					<button id="phoneSubmit" type="button" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading text-center">${seckill.name}</div>
					<div id="seckillState" class="panel-body text-center"></div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="common/foot.jsp"%>
	<script src="//cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
	<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/seckill.js"></script>
	<script type="text/javascript">
		$(function() {
			var contextPath = "${pageContext.request.contextPath}";
			seckill.detail.init({
				seckillId: ${seckill.seckillId},
				startTime: ${seckill.startTime.time},
				endTime: ${seckill.endTime.time},
				contextPath: contextPath
			});
		});
	</script>
</body>
</html>