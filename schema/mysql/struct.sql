#
# Source for table eg_sequence
#

DROP TABLE IF EXISTS `nsc_meap_sequence`;
CREATE TABLE `nsc_meap_sequence` (
  `id` VARCHAR(128) NOT NULL,
  `current` BIGINT(16) NOT NULL DEFAULT '0',
  `step` INT(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_schedule`;
CREATE TABLE `wx_schedule` (
  `ID` VARCHAR(32) NOT NULL,
  `SECOND` INT(11) NOT NULL DEFAULT '-1',
  `MINUTE` INT(11) NOT NULL DEFAULT '-1',
  `HOUR` INT(11) NOT NULL DEFAULT '-1',
  `WEEKDAY` INT(11) NOT NULL DEFAULT '-1',
  `DAYOFMONTH` INT(11) NOT NULL DEFAULT '-1',
  `MONTH` INT(2) NOT NULL DEFAULT '-1',
  `TASK` VARCHAR(99) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_app_token`;
CREATE TABLE `nsc_meap_app_token` (
  `id` VARCHAR(64) NOT NULL,
  `userToken` VARCHAR(64) NOT NULL DEFAULT "Anonymous",
  `artifactId` VARCHAR(64) NOT NULL DEFAULT "GLOBLE",
  `device` VARCHAR(16) NOT NULL,
  `deviceId` VARCHAR(64) NOT NULL,
  `salt` VARCHAR(128) NOT NULL,
  `saltKey` VARCHAR(64) NOT NULL,
  `publicKey` VARCHAR(1024) NOT NULL,
  `pushToken` VARCHAR(128) NOT NULL,
  `createdTime` BIGINT(16) NOT NULL DEFAULT '0',
  `verify` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE(`artifactId`,`device`,`deviceId`),
  INDEX(`verify`),
  INDEX(`userToken`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_user_token`;
CREATE TABLE `nsc_meap_user_token` (
  `id` VARCHAR(64) NOT NULL,
  `userId` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`userId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_file_token`;
CREATE TABLE `nsc_meap_file_token` (
  `id` VARCHAR(32) NOT NULL,
  `userToken` VARCHAR(64),
  `appToken` VARCHAR(64) NOT NULL,
  `fileName` VARCHAR(64) NOT NULL,
  `digest` VARCHAR(64) NOT NULL,
  `algorithm` VARCHAR(32) NOT NULL,
  `fileSize` BIGINT(16) NOT NULL,
  `filePos` BIGINT(16) NOT NULL,
  `fileType` VARCHAR(32),
  `fileStatus` VARCHAR(32),
  `createdTime` BIGINT(16) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX(`userToken`),
  INDEX(`appToken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_queue_push`;
CREATE TABLE `nsc_meap_queue_push` (
  `id` VARCHAR(32) NOT NULL,
  `priority` INT(1) NOT NULL,
  `type` VARCHAR(32) NOT NULL,
  `device` VARCHAR(64) NOT NULL,
  `pushToken` VARCHAR(64) NOT NULL,
  `messageId` VARCHAR(40) NOT NULL,
  `title` VARCHAR(64),
  `message` VARCHAR(256) NOT NULL,
  `callback` INT(1) NOT NULL,
  `createdTime` BIGINT(16) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX(`priority`),
  INDEX(`device`),
  INDEX(`createdTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `nsc_meap_queue_outter`;
CREATE TABLE `nsc_meap_queue_outter` (
  `id` VARCHAR(32) NOT NULL,
  `subject` VARCHAR(32),
  `command` VARCHAR(512) NOT NULL,
  `createdTime` BIGINT(16) NOT NULL,
  `expiredTime` BIGINT(16) NOT NULL,
  `needPrompt` TINYINT(1) NOT NULL,
  `pushPromptText` VARCHAR(128) NOT NULL,
  `hasPromptTimes` INT(2) NOT NULL DEFAULT '0',
  `promptTimes` INT(2) NOT NULL DEFAULT '0',
  `promptMinutePeriod` INT(4) NOT NULL DEFAULT '0',
  `nextPromptTime` BIGINT(16) NOT NULL,
  `tokenType` VARCHAR(16) NOT NULL,
  `token` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY(`tokenType`,`token`),
  INDEX(`createdTime`),
  INDEX(`expiredTime`),
  INDEX(`needPrompt`),
  INDEX(`nextPromptTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
