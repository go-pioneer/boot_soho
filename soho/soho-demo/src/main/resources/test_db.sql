/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : test_db

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-09 21:02:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `color`
-- ----------------------------
DROP TABLE IF EXISTS `color`;
CREATE TABLE `color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '第三方平台公钥',
  `age` int(4) DEFAULT NULL COMMENT '第三方平台域名',
  `sex` int(4) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL,
  `utime` bigint(20) DEFAULT '0',
  `state` int(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of color
-- ----------------------------
INSERT INTO `color` VALUES ('2', '小狗', '5', '1', '1521983559434', '0', '1');
INSERT INTO `color` VALUES ('3', '小狗', '5', '1', '1522152240128', '0', '1');
INSERT INTO `color` VALUES ('4', '小狗', '5', '1', '1522152258149', '0', '1');
INSERT INTO `color` VALUES ('5', '小狗', '5', '1', '1522152263050', '0', '1');
INSERT INTO `color` VALUES ('6', '小狗', '5', '1', '1522152268259', '0', '1');
INSERT INTO `color` VALUES ('7', '小狗', '5', '1', '1522152270208', '0', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of dog
-- ----------------------------
INSERT INTO `dog` VALUES ('2', '小狗', '5', '1', '1521983559434', '0', '1');
INSERT INTO `dog` VALUES ('3', '小狗', '5', '1', '1522152240128', '0', '1');
INSERT INTO `dog` VALUES ('4', '小狗', '5', '1', '1522152258149', '0', '1');
INSERT INTO `dog` VALUES ('5', '小狗', '5', '1', '1522152263050', '0', '1');
INSERT INTO `dog` VALUES ('6', '小狗', '5', '1', '1522152268259', '0', '1');
INSERT INTO `dog` VALUES ('7', '小狗', '5', '1', '1522152270208', '0', '1');
