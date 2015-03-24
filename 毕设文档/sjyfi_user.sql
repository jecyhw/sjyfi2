/*
Navicat MySQL Data Transfer

Source Server         : sijfi_db
Source Server Version : 50173
Source Host           : 159.226.15.215:3306
Source Database       : sjyfi_db

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2014-12-16 09:12:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sjyfi_user
-- ----------------------------
DROP TABLE IF EXISTS `sjyfi_user`;
CREATE TABLE `sjyfi_user` (
  `uid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` char(30) NOT NULL DEFAULT '' COMMENT '用户账号',
  `password` char(32) NOT NULL COMMENT '登录密码',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `gender` tinyint(4) NOT NULL COMMENT '性别（0 保密 1 男 2女）',
  `birthday` datetime NOT NULL COMMENT '生日',
  `organization` varchar(100) NOT NULL COMMENT '工作单位',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `province` varchar(100) DEFAULT NULL COMMENT '省',
  `city` varchar(100) DEFAULT NULL COMMENT '市',
  `county` varchar(100) DEFAULT NULL COMMENT '县',
  `township` varchar(100) DEFAULT NULL COMMENT '乡',
  `add_time` datetime NOT NULL COMMENT '注册时间',
  `login_time` datetime NOT NULL COMMENT '上次登录时间',
  `role` tinyint(4) NOT NULL COMMENT '用户角色 0 管理员 1 超级用户 2 动物调查用户 3 植物调查用户',
  `visit_count` int(11) DEFAULT '0' COMMENT '访问次数',
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjyfi_user
-- ----------------------------
INSERT INTO `sjyfi_user` VALUES ('1', 'admin', '123', '管理员', '1', '2014-01-08 00:00:00', 'CNIC', '中华人民共和国', '海南', '保亭黎族苗族自治县', '保亭黎族苗族自治县', '中关村', '2013-12-11 16:17:50', '2014-11-02 14:15:22', '1', '1936');
INSERT INTO `sjyfi_user` VALUES ('2', 'user', '123456', '用户', '2', '1981-01-01 15:11:52', 'CNIC', 'China', 'Beijing', 'Beijing', null, null, '2013-12-12 15:12:48', '0000-00-00 00:00:00', '0', '0');
INSERT INTO `sjyfi_user` VALUES ('3', 'auser', 'auser', '动物测试', '3', '2014-01-06 13:52:08', 'CNIC', 'China', 'Beijing', 'Beijing', null, null, '2014-01-06 13:52:35', '0000-00-00 00:00:00', '2', '0');
INSERT INTO `sjyfi_user` VALUES ('4', 'vuser', 'vuser', '植物测试', '4', '2014-01-06 13:53:14', 'CNIC', 'China', 'Beijing', 'Beijing', null, null, '2014-01-06 13:53:00', '0000-00-00 00:00:00', '3', '0');
INSERT INTO `sjyfi_user` VALUES ('5', 'qqqq', 'aaaaa', 'aaaaaa', '1', '2014-01-16 00:00:00', 'zxzc', '请选择国籍', '省份', '地级市', '市、县级市、县', '未填写', '2014-01-07 16:58:45', '2014-01-08 13:02:31', '0', '3');
INSERT INTO `sjyfi_user` VALUES ('7', 'uuuu', 'aaaa', 'aaaa', '1', '2014-01-10 00:00:00', 'aaaa', '请选择国籍', '省份', '地级市', '市、县级市、县', '未填写', '2014-01-08 08:43:42', '2014-01-08 13:04:54', '2', '1');
INSERT INTO `sjyfi_user` VALUES ('8', 'liucc', '123', '刘长城', '1', '2014-01-24 00:00:00', 'cnic', '请选择国籍', '省份', '地级市', '市、县级市、县', '未填写', '2014-01-24 15:58:28', '2014-11-27 13:31:04', '3', '699');
INSERT INTO `sjyfi_user` VALUES ('9', 'fhj', '123', '付海静', '2', '2014-03-14 00:00:00', '总体组', '中华人民共和国', '北京', '北京', '北京市', '未填写', '2014-03-14 15:45:55', '2014-12-10 12:54:29', '0', '1338');
INSERT INTO `sjyfi_user` VALUES ('10', 'animalTest', '123', '动物调查员', '1', '2014-04-16 00:00:00', '网络中心', '请选择国籍', '省份', '地级市', '市、县级市、县', '未填写', '2014-04-16 09:16:01', '2014-04-16 09:38:44', '2', '3');
INSERT INTO `sjyfi_user` VALUES ('36', 'qweq', 'qweqwe', 'qwq', '1', '2014-05-06 00:00:00', 'qqqq', 'qqqq', '省份', '地级市', '市、县级市', 'qqqq', '2014-05-06 09:40:45', '2014-05-06 15:23:56', '0', '1');
INSERT INTO `sjyfi_user` VALUES ('39', 'icy', '123', 'icy', '2', '2014-05-20 00:00:00', '网络中心', '', '北京市', '北京市', '海淀区', '', '2014-05-20 09:06:20', '2014-09-14 18:48:08', '2', '9');
INSERT INTO `sjyfi_user` VALUES ('40', 'cnic', '123', '网络中心', '1', '1982-04-26 00:00:00', '中科院网络中心', '中国', '北京市', '北京市', '海淀区', '中关村', '2014-05-20 10:23:59', '2014-05-20 10:23:59', '4', '0');
INSERT INTO `sjyfi_user` VALUES ('41', 'zhangying', '123456', '张营', '2', '2014-05-21 00:00:00', '青海师大', '中国', '山东省', '济南市', '市、县级市', '', '2014-05-21 19:33:43', '2014-09-24 20:22:16', '2', '16');
INSERT INTO `sjyfi_user` VALUES ('35', 'qwe', 'aaa', 'asdf', '1', '2014-05-06 00:00:00', 'zzzqwe', 'zzzqqq', '省份', '地级市', '市、县级市', 'zzz', '2014-05-06 09:39:46', '2014-10-30 10:25:36', '1', '282');
INSERT INTO `sjyfi_user` VALUES ('37', 'gqin', '123', '秦刚', '1', '1978-08-01 00:00:00', '中科院计算机网络信息中心', '中华人民共和国', '北京', '北京', '北京市', '未填写', '2014-05-07 08:36:24', '2014-12-16 08:20:53', '0', '96');
INSERT INTO `sjyfi_user` VALUES ('38', 'lijian', '123', '李健', '1', '2014-05-09 00:00:00', 'cnic', '中华人民共和国', '北京', '北京', '北京市', null, '2014-05-09 00:00:00', '2014-05-20 09:08:17', '1', '3');
INSERT INTO `sjyfi_user` VALUES ('42', 'xiheliu', 'wjf870807', '陈振宁', '1', '1970-04-14 00:00:00', '青海师范大学', '中国', '青海省', '西宁市', '城西区', '青海师大陈振宁', '2014-05-21 20:46:58', '2014-08-19 14:44:58', '1', '10');
INSERT INTO `sjyfi_user` VALUES ('43', 'hkwar', 'yutaolove', '余涛', '1', '1992-01-05 00:00:00', '青海师范大学', '中国', '青海省', '西宁市', '城西区', '', '2014-05-21 21:01:03', '2014-05-21 21:12:13', '3', '4');
INSERT INTO `sjyfi_user` VALUES ('44', 'jingege', '981024', '靳代樱', '1', '1977-08-01 00:00:00', '青海省三江源管理局', '中国', '青海省', '西宁市', '城西区', '', '2014-05-21 21:05:15', '2014-08-19 15:53:03', '0', '1');
INSERT INTO `sjyfi_user` VALUES ('51', 'sjp', 'admin', '史俭朋', '1', '1989-07-07 00:00:00', '中科院网络中心', '中国', '北京市', '北京市', '海淀区', '', '2014-05-30 16:11:14', '2014-07-04 22:57:04', '2', '15');
INSERT INTO `sjyfi_user` VALUES ('47', 'baozi', 'jc.wang', '王洁琛', '2', '1994-04-13 00:00:00', '青海师大', '中国', '河北省', '沧州市', '河间市', '', '2014-05-21 21:07:13', '2014-06-16 00:13:28', '3', '6');
INSERT INTO `sjyfi_user` VALUES ('48', 'shushi', '123456789wjx', '王吉祥', '1', '2014-04-30 00:00:00', '青海师范大学', '艾泽拉斯', '青海省', '海东地区', '乐都县', '', '2014-05-21 21:08:55', '2014-08-31 17:42:07', '2', '11');
INSERT INTO `sjyfi_user` VALUES ('49', 'moxiao', 'wddpwzz1234', '李瑞', '2', '1995-12-08 00:00:00', '青海师大', '中国', '省份', '地级市', '市、县级市', '', '2014-05-21 21:10:14', '2014-06-10 15:35:58', '2', '5');
INSERT INTO `sjyfi_user` VALUES ('50', 'macunxin', 'yingyan1990', '马存新', '1', '2014-04-21 00:00:00', '青海师范大学', '中国', '青海省', '海东地区', '互助土族自治县', '', '2014-05-21 21:12:08', '2014-05-21 21:12:33', '2', '1');
INSERT INTO `sjyfi_user` VALUES ('53', 'oyx', 'ddg167', '欧阳欣', '1', '1976-11-05 00:00:00', '网络中心', '中国', '北京市', '北京市', '海淀区', '', '2014-07-11 23:25:32', '2014-10-20 10:24:23', '4', '14');
INSERT INTO `sjyfi_user` VALUES ('54', 'oyx', 'ddg167', '欧阳欣', '1', '1976-11-05 00:00:00', '网络中心', '中国', '北京市', '北京市', '海淀区', '', '2014-07-11 23:25:34', '2014-10-20 10:24:23', '4', '14');
INSERT INTO `sjyfi_user` VALUES ('56', 'wangj', 'wangjun', '王君', '2', '1982-02-07 00:00:00', '武汉病毒所', '中国', '湖北省', '武汉市', '武昌区', '', '2014-08-29 14:57:24', '2014-09-07 18:55:04', '2', '10');
INSERT INTO `sjyfi_user` VALUES ('57', 'roy_van', '51168van', '范兆军', '1', '1973-07-06 00:00:00', '中国科学院武汉病毒研究所', '中国', '湖北省', '武汉市', '市、县级市', '', '2014-08-29 15:11:41', '2014-09-08 14:50:18', '2', '3');
INSERT INTO `sjyfi_user` VALUES ('58', 'jecyhw', '1029384756', '杨慧伟', '1', '1992-09-29 00:00:00', 'snnu', '中国', '陕西省', '西安市', '长安区', '陕西师范大学长安校区', '2014-09-14 18:52:24', '2014-12-02 19:26:23', '0', '22');
INSERT INTO `sjyfi_user` VALUES ('59', 'zsy6914', '19741223', '赵仕远', '1', '1974-07-13 00:00:00', '云南景东管理局', '中国', '云南省', '思茅市', '景东彝族自治县', '', '2014-11-01 00:09:32', '2014-11-02 14:26:00', '2', '9');
