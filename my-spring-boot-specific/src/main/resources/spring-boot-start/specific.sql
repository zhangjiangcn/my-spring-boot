/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : 127.0.0.1:3306
 Source Schema         : specific

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 05/02/2019 20:37:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for specific_interface_param
-- ----------------------------
DROP TABLE IF EXISTS `specific_interface_param`;
CREATE TABLE `specific_interface_param`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `param_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数名称',
  `param_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数值，所有值都为字符串类型，其他类型需要在sql中自行转换。',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0.未删除；1.已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for specific_interface_sql
-- ----------------------------
DROP TABLE IF EXISTS `specific_interface_sql`;
CREATE TABLE `specific_interface_sql`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `data_space` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据空间',
  `data_sql` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据sql',
  `data_param_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据参数Id',
  `data_source_id` int(11) NULL DEFAULT NULL COMMENT '数据源Id',
  `sql_template_engine` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'mybatis' COMMENT 'sql模板引擎 :\r\n  null || mybatis : mybatis模板引擎；\r\n  beetl : beetl模板引擎。',
  `result_data_format` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'array' COMMENT '结果数据格式 \r\n  array：数组；\r\n  object：对象。',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0.未删除；1.已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
