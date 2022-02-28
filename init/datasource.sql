## skeleton
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id` bigint NOT NULL COMMENT '主键',
`name` varchar(30) DEFAULT NULL COMMENT '姓名',
`age` int DEFAULT NULL COMMENT '年龄',
`email` varchar(50) DEFAULT NULL COMMENT '邮箱',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '大BOSS', '40', 'boss@baomidou.com', '2021-03-22 09:48:00');
INSERT INTO `user` VALUES ('2', '李经理', '40', 'boss@baomidou.com', '2021-01-22 09:48:00');
INSERT INTO `user` VALUES ('3', '黄主管', '40', 'boss@baomidou.com', '2021-01-22 09:48:00');
INSERT INTO `user` VALUES ('4', '吴组长', '40', 'boss@baomidou.com', '2021-02-22 09:48:00');
INSERT INTO `user` VALUES ('5', '小菜', '40', 'boss@baomidou.com', '2021-02-22 09:48:00');
INSERT INTO `user` VALUES ('6', 'gg', '27', '110@qq.com', '2022-02-27 12:00:48');


## event
DROP TABLE IF EXISTS event_info;
CREATE TABLE event_info (
id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
event_name VARCHAR(30) DEFAULT NULL COMMENT '时间名称',
event_type INT(11) DEFAULT NULL COMMENT '年龄',
create_time DATETIME DEFAULT NULL COMMENT '创建时间'
) ENGINE=INNODB CHARSET=UTF8;
-- ----------------------------
-- Records of event_info
-- ----------------------------
INSERT INTO event_info (id, event_name, event_type, create_time) VALUES
(1, '事件-1', 1, '2021-03-22 09:48:00'),
(2, '事件-2', 1, '2021-01-22 09:48:00'),
(3, '事件-3', 2, '2021-01-22 09:48:00'),
(4, '事件-4', 2, '2021-02-22 09:48:00'),
(5, '事件-5', 3, '2021-02-22 09:48:00');


CREATE TABLE `file` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`filePath` varchar(255) DEFAULT NULL,
`fileName` varchar(255) DEFAULT NULL,
`fileSuffix` varchar(255) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
