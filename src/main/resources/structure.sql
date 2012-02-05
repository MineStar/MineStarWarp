CREATE TABLE IF NOT EXISTS `warps` (
	`id` INTEGER PRIMARY KEY,
	`name` varchar(32) NOT NULL,
	`creator` varchar(32) NOT NULL,
	`world` varchar(32) NOT NULL,
	`x` DOUBLE NOT NULL,
	`y` DOUBLE NOT NULL,
	`z` DOUBLE NOT NULL,
	`yaw` smallint NOT NULL,
	`pitch` smallint NOT NULL,
	`permissions` text
);

CREATE TABLE IF NOT EXISTS `homes` (
	`player` varchar(32) PRIMARY KEY,
	`world` varchar(32) NOT NULL,
	`x` DOUBLE NOT NULL,
	`y` DOUBLE NOT NULL,
	`z` DOUBLE NOT NULL,
	`yaw` smallint NOT NULL,
	`pitch` smallint NOT NULL
);

CREATE TABLE IF NOT EXISTS `banks` (
	`player` varchar(32) PRIMARY KEY,
	`world` varchar(32) NOT NULL,
	`x` DOUBLE NOT NULL,
	`y` DOUBLE NOT NULL,
	`z` DOUBLE NOT NULL,
	`yaw` smallint NOT NULL,
	`pitch` smallint NOT NULL 
);