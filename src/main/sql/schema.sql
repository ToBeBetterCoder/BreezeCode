-- 数据库初始化脚本
-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
USE scekill;

-- 创建秒杀库存表
CREATE TABLE `seckill` (
	`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
	`name` VARCHAR (120) NOT NULL COMMENT '商品名称',
	`number` INT NOT NULL COMMENT '库存数量',
	`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
	`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
	`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (seckill_id),
	KEY idx_start_time (start_time),
	KEY idx_end_time (end_time),
	KEY idx_create_time (create_time)
) ENGINE = INNODB AUTO_INCREMENT = 1000 DEFAULT CHARACTER
SET = utf8 COMMENT = '秒杀库存表';

-- 初始化数据
INSERT INTO seckill (
	NAME,
	number,
	start_time,
	end_time,
	create_time
)
VALUES
	(
		'3888元秒杀iPhone6 32G',
		99,
		'2016-06-02 20:45:20',
		'2016-06-05 20:45:51',
		CURRENT_TIMESTAMP
	),
	(
		'2888元秒杀iPad6 64G',
		88,
		'2016-07-02 10:45:20',
		'2016-07-03 10:45:51',
		CURRENT_TIMESTAMP
	),
	(
		'2000元秒杀锤子T2 32G',
		199,
		'2016-08-02 19:45:20',
		'2016-08-05 20:45:51',
		CURRENT_TIMESTAMP
	),
	(
		'1888元秒杀小米note 64G',
		299,
		'2016-05-02 18:45:20',
		'2016-06-05 20:45:51',
		CURRENT_TIMESTAMP
	);

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE `success_killed` (
	`seckill_id` BIGINT NOT NULL COMMENT '秒杀商品名称',
	`user_phone` CHAR (11) NOT NULL COMMENT '用户手机号',
	`state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识：-1：无效 0：成功 1：已付款',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (seckill_id, user_phone),
	-- 联合主键
	KEY idx_create_time (create_time)
) ENGINE = INNODB DEFAULT CHARACTER
SET = utf8 COMMENT = '秒杀成功明细表';

-- 连接数据库控制台
mysql -uroot -p123456
