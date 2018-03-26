/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : test_db

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-26 07:53:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dog`
-- ----------------------------
DROP TABLE IF EXISTS `dog`;
CREATE TABLE `dog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '第三方平台公钥',
  `age` int(4) DEFAULT NULL COMMENT '第三方平台域名',
  `sex` int(4) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL,
  `utime` bigint(20) DEFAULT '0',
  `state` int(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of dog
-- ----------------------------
INSERT INTO `dog` VALUES ('2', '小狗', '5', '1', '1521983559434', '0', '1');
