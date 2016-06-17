/*
 * 存放主要交互逻辑js
*/
var seckill = {
		// 项目名称
		contextPath: "",
		// 封装秒杀相关ajax的url
		URL: {
			getNowTime: function() {
				return seckill.contextPath + "/seckill/time/now";
			},
			getExposeUrl: function(seckillId) {
				return seckill.contextPath + "/seckill/" + seckillId + "/exposer";
			},
			getSeckillUrl: function(seckillId, md5) {
				return seckill.contextPath + "/seckill/" + seckillId + "/" + md5 + "/execute";
			}
		},
		// 验证手机号
		validate: function(phone) {
			if (phone && $.isNumeric(phone) && 11 == phone.length) {
				return true;
			}
			return false;
		},
		// 显示秒杀按钮
		showSeckillBtn: function(scope, exposer) {
			scope.hide().html('<button id="seckillBtn" type="button" class="btn btn-info">秒 杀</button>');
			$("#seckillBtn").one("click", function() {
				$.post(seckill.URL.getSeckillUrl(exposer.seckillId, exposer.md5), {}, function(data) {
					if (data && data.success) {
						var seckillExecution = data.data;
						if (0 == seckillExecution.state || -1 == seckillExecution.state) {
							// 秒杀成功或者重复秒杀
							scope.html('<button type="button" class="btn btn-success" disabled>' + seckillExecution.stateInfo + '</button>');
							// 记录秒杀产品，防止重复秒杀请求
							$.cookie($.cookie("killPhone") + exposer.seckillId, exposer.seckillId, {expires: 7, path: seckill.contextPath + "/seckill"});
						} else {
							scope.html('<button type="button" class="btn btn-warning" disabled>' + seckillExecution.stateInfo + '</button>');
						}
					}
				});
			});
			scope.show();
		},
		// 秒杀开始
		seckillStart: function(seckillId, scope) {
			$.get(seckill.URL.getExposeUrl(seckillId), {}, function(data) {
				if (data && data.success) {
					var exposer = data.data;
					if (exposer.exposed) {
						seckill.showSeckillBtn(scope, exposer);
					} else {
						// 客户端时间和系统时间有差异，重置倒计时
						seckill.countdown(exposer.seckillId, exposer.startTime, scope);
					}
				}
			});
		},
		// 秒杀倒计时
		countdown: function(seckillId, startTime, scope) {
			scope.countdown(startTime, function(event) {
				scope.html(event.strftime('秒杀倒计时： %D 天 %H:%M:%S'));
			}).on('finish.countdown', function(){
				// 生成倒计时完成后的内容 
				// 秒杀开始
				seckill.seckillStart(seckillId, scope);
			});
		},
		// 秒杀准备
		seckillPrepare: function(seckillId, startTime, endTime, scope) {
			$.get(seckill.URL.getNowTime(), {}, function(data) {
				if (data && data.success) {
					var nowTime = data.data;
					if (nowTime < startTime) {
						// 秒杀未开始，显示倒计时
						seckill.countdown(seckillId, startTime, scope);
					} else if (nowTime > endTime) {
						// 秒杀已结束，显示秒杀结束
						scope.html('<button type="button" class="btn btn-warning" disabled>秒杀已结束</button>');
					} else {
						// 秒杀开始：获取秒杀地址，显示秒杀按钮
						seckill.seckillStart(seckillId, scope);
					}
				}
			});
		},
		// 详情页秒杀逻辑
		detail: {
			// 详情页初始化
			init: function(params) {
				var killPhone = $.cookie("killPhone");
				var seckillId = params.seckillId;
				var startTime = params.startTime;
				var endTime = params.endTime;
				var $seckillState = $('#seckillState');
				seckill.contextPath = params.contextPath;
				// 号码提交（如果没有cookie号码缓存，则弹框要求输入，提交后存入cookie）
				/*if (seckill.submitPhone(killPhone)) {
					// 秒杀准备（判断秒杀是否已经开始，如果还没开始，则显示倒计时，倒计时结束后，向后台请求获取秒杀地址，显示秒杀按钮）
					seckill.seckillPrepare(seckillId, startTime, endTime);
				}*/
				if (!seckill.validate(killPhone)) {
					// 显示输入框
					$("#phoneModal").modal({
						show: true,
						keyboard: false,
						backdrop: 'static'
					});
					$("#phoneSubmit").click(function() {
						var phoneInput = $("#phoneInput").val();
						if (seckill.validate(phoneInput)) {
							$.cookie("killPhone", phoneInput, {expires: 7, path: seckill.contextPath + "/seckill"});
							$('#phoneModal').modal('hide');
							// 秒杀准备（判断秒杀是否已经开始，如果还没开始，则显示倒计时，倒计时结束后，向后台请求获取秒杀地址，显示秒杀按钮）
							seckill.seckillPrepare(seckillId, startTime, endTime, $seckillState);
						} else {
							$("#phoneCheckInfo").hide().text("号码格式错误！").show();
						}
					});
				} else {
					// 如果已经秒杀成功，则不再显示秒杀按钮，提示秒杀成功
					if (seckillId == $.cookie(killPhone + seckillId)) {
						$seckillState.html('<button type="button" class="btn btn-success" disabled>秒杀成功</button>');
						return;
					}
					// 秒杀准备（判断秒杀是否已经开始，如果还没开始，则显示倒计时，倒计时结束后，向后台请求获取秒杀地址，显示秒杀按钮）
					seckill.seckillPrepare(seckillId, startTime, endTime, $seckillState);
				}
			}
		}
};