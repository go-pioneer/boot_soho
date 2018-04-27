/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : test_db

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-27 15:51:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for color
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of color
-- ----------------------------
INSERT INTO `color` VALUES ('2', 'test', '1', '1', '0', '0', '1');

-- ----------------------------
-- Table structure for dog
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of dog
-- ----------------------------
INSERT INTO `dog` VALUES ('2', '小狗', '5', '1', '1521983559434', '0', '1');
INSERT INTO `dog` VALUES ('3', '??', '5', '1', '1522137045894', '0', '1');
INSERT INTO `dog` VALUES ('5', '??', '5', '1', '1522140691214', '0', '1');
INSERT INTO `dog` VALUES ('6', '??', '5', '1', '1522141027839', '0', '1');
INSERT INTO `dog` VALUES ('7', '??', '5', '1', '1522141124201', '0', '1');
INSERT INTO `dog` VALUES ('8', '??', '5', '1', '1522142760166', '0', '1');

-- ----------------------------
-- Table structure for oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client`;
CREATE TABLE `oauth_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(100) DEFAULT NULL COMMENT '第三方平台公钥',
  `client_secret` varchar(100) DEFAULT NULL COMMENT '第三方授权密钥',
  `company` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `mobile` varchar(100) DEFAULT NULL COMMENT '联系号码',
  `address` varchar(100) DEFAULT NULL COMMENT '公司地址',
  `domain_uri` varchar(255) DEFAULT NULL COMMENT '第三方平台域名',
  `bindIp` varchar(100) DEFAULT NULL COMMENT '第三方服务器IP',
  `usestate` int(4) DEFAULT '1' COMMENT '1.正常 2.冻结 3.禁用',
  `ctime` bigint(20) DEFAULT NULL,
  `utime` bigint(20) DEFAULT '0',
  `state` int(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_appid` (`client_id`),
  KEY `idx_appkey` (`client_secret`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='第三方平台';

-- ----------------------------
-- Records of oauth_client
-- ----------------------------
INSERT INTO `oauth_client` VALUES ('2', 'c522f0c158d4c9d5be2f1032c38a8148', 'dedadc9d4d3525033047d12150ab3b065405ab7eb0ac4e9f5a2dcedb1653f3eeb6cf1a742a8dcca814f668cb1ada781f', '皮皮公司', '13823912345', '中山', 'http://localhost', '192.168.1.176', '1', '0', '0', '1');

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(100) DEFAULT NULL COMMENT '第三方平台公钥',
  `uid` varchar(100) DEFAULT NULL,
  `access_token` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `code_state` int(4) DEFAULT '1' COMMENT '1.未使用 2.已使用',
  `code_expire` bigint(20) DEFAULT NULL,
  `token_state` int(4) DEFAULT '1' COMMENT '1.有效 2.无效',
  `token_expire` bigint(20) DEFAULT NULL,
  `access_time` bigint(20) DEFAULT NULL,
  `refresh_token` varchar(100) DEFAULT NULL,
  `refresh_time` bigint(20) DEFAULT NULL,
  `logout_time` bigint(20) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL,
  `utime` bigint(20) DEFAULT '0',
  `state` int(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`),
  UNIQUE KEY `idx_token` (`access_token`),
  UNIQUE KEY `idx_refreshtoken` (`refresh_token`),
  KEY `idx_appid` (`client_id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_token_state` (`token_state`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='平台授权TOKEN';

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------
INSERT INTO `oauth_client_token` VALUES ('44', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '3d6fcd979c02f93599f31ea9494fcca3', '2ef4b1cf34bc701cc0db146aaf9b136f', '1', '1524753942956', '1', '1525962942956', '1524753342956', '61a88f0bb92799fe4b9f117136157612', '1524753342956', '0', '1524753342956', '0', '1');
INSERT INTO `oauth_client_token` VALUES ('45', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '99001b1e7fff05dae9bec53536260592', '9297ae241c371255357dfcfb0d2c956d', '1', '1524755852498', '1', '1525964852498', '1524755252498', 'b92185390d3b5138a607a607737c7ecb', '1524755252498', '0', '1524755252498', '0', '1');
INSERT INTO `oauth_client_token` VALUES ('46', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '7ed06c27fbdde37256c389a1117dda7b', 'f0758b6196a79283db0dbb82160d7ea2', '1', '1524757521871', '1', '1525966521871', '1524756921871', '73e9846087960a5da51936f23b9b30d0', '1524756921871', '0', '1524756921871', '0', '1');
INSERT INTO `oauth_client_token` VALUES ('47', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', 'eeda4d33f61f3b994c5b111cd9fa4b48', 'd1c02045fdb7438b2026c8e40aa9734f', '2', '1524793295201', '1', '1526002295201', '1524792695201', '13958526bace25f69f28b11e6309a4c7', '1524792695201', '0', '1524792695201', '1524792761308', '1');
INSERT INTO `oauth_client_token` VALUES ('48', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '954f588b38a9c71acf0565ed9a1ce5c1', 'ff09cdf7c205488f7a54930a1d10d493', '2', '1524793929282', '1', '1526002929282', '1524793329282', '03416115a0b9d62ae99f4063e7d25c23', '1524793329282', '0', '1524793329282', '1524793368283', '1');
INSERT INTO `oauth_client_token` VALUES ('49', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', 'fbd29555334e9857172e988246a7a7f3', '781b59a499dbf44c58a8d5340320e6bb', '2', '1524794979475', '1', '1526003979475', '1524794379475', '8c19c250f4f3f75ec27fdbf9f67facec', '1524794379475', '0', '1524794379475', '1524794437188', '1');
INSERT INTO `oauth_client_token` VALUES ('50', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', 'bbff8a5c8e4d42bfeb0ff1a3b7b17378', 'df6b8b6f07623e3788fc8b570b30f26f', '2', '1524795722135', '1', '1526012063482', '1524795122135', '8bf6d6b77d831ae4c1ce5da2b2e38f58', '1524802463482', '1524795673729', '1524795122135', '1524802463482', '1');
INSERT INTO `oauth_client_token` VALUES ('51', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '5261baf8a809f431e84b5d4e120549fb', 'a7a2d1bd51ddcb6d54bf95f1eab6a1f8', '2', '1524811932503', '1', '1526020932503', '1524811332503', 'e6050538878cb9242a4f93ba808dcd86', '1524811332503', '0', '1524811332503', '1524811631913', '1');
INSERT INTO `oauth_client_token` VALUES ('52', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '2b73354e3f3653dbf08806ab0921b9cd', '28712cdf817e33daad469f98fda894ac', '2', '1524813451884', '1', '1526022451884', '1524812851884', '5bf3a814d2b134d30ef62662d93e1025', '1524812851884', '0', '1524812851884', '1524813044816', '1');
INSERT INTO `oauth_client_token` VALUES ('53', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', '4d276f917e36d9b17dfa5f4c5e10cf2f', 'd33dac82e54f60cce4f9901d92c14a52', '1', '1524815089082', '1', '1526024089082', '1524814489082', 'fc48779be93c470f5c73d16ea7ac26da', '1524814489082', '0', '1524814489082', '0', '1');
INSERT INTO `oauth_client_token` VALUES ('54', 'c522f0c158d4c9d5be2f1032c38a8148', '7930040530a4744d77504bbbff7e7cb3', 'e53bce814680028e3e4c08bccb1539ef', '65c3c9f4325fefca9012d7be37e767d4', '2', '1524815117530', '1', '1526024117530', '1524814517530', '42b9d013e83614f23b409b46de25a8f5', '1524814517530', '0', '1524814517530', '1524814783176', '1');

-- ----------------------------
-- Table structure for oauth_user
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user`;
CREATE TABLE `oauth_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `company` varchar(100) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_user
-- ----------------------------
INSERT INTO `oauth_user` VALUES ('6', '7930040530a4744d77504bbbff7e7cb3', 'zhangsan', '7930040530a4744d77504bbbff7e7cb3', '张三', '皮皮公司', '0');
