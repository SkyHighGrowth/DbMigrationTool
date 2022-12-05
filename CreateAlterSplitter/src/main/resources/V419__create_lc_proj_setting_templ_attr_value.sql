USE `anon`;
 
/* SQLINES DEMO *** le [dbo].[lc_proj_setting_templ_attr_value]    Script Date: 12/2/2022 10:41:34 AM ******/
/* SET ANSI_NULLS ON */
 
/* SET QUOTED_IDENTIFIER ON */
 
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE `lc_proj_setting_templ_attr_value`(
	`ID` int AUTO_INCREMENT NOT NULL,
	`ATTRIBUTE_GUID` varchar(255) NOT NULL,
	`ATTRIBUTE_NAME` Longtext NOT NULL,
	`REQUIRED` Tinyint NOT NULL DEFAULT 0,
	`VALUE_GUID` varchar(255) NOT NULL,
	`VALUE` varchar(4096) NOT NULL,
	`IS_DEFAULT` Tinyint NOT NULL DEFAULT 0,
	`SETTING_ID` int NOT NULL,
PRIMARY KEY 
(
	`ID` ASC
)  
)  ;
/* Moved to CREATE TABLE
ALTER TABLE `lc_proj_setting_templ_attr_value` ADD  DEFAULT ((0)) FOR `REQUIRED`
GO */
/* Moved to CREATE TABLE
ALTER TABLE `lc_proj_setting_templ_attr_value` ADD  DEFAULT ((0)) FOR `IS_DEFAULT`
GO */
ALTER TABLE `lc_proj_setting_templ_attr_value` ADD  CONSTRAINT `FK_LC_SETTING_TO_ATTR` FOREIGN KEY(`SETTING_ID`)
REFERENCES `lc_proj_setting_templ` (`ID`)
ON DELETE CASCADE;
 
ALTER TABLE `lc_proj_setting_templ_attr_value` CHECK CONSTRAINT `FK_LC_SETTING_TO_ATTR`;
 
