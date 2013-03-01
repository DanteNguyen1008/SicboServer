-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 28, 2013 at 08:47 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `kb_sicbo`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `USER_INSERT`(IN `username` VARCHAR(50) CHARSET utf8, IN `password` TEXT CHARSET utf8, IN `email` VARCHAR(50) CHARSET utf8)
    NO SQL
BEGIN
	#Routine body goes here...
	INSERT INTO 
	kb_user(`username`,
		`password`,
		`email`)
	VALUES(
		username, 
		MD5(password),
                email);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `kb_bet_spot`
--

CREATE TABLE IF NOT EXISTS `kb_bet_spot` (
  `bet_spot_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pattern_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`bet_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `kb_history`
--

CREATE TABLE IF NOT EXISTS `kb_history` (
  `history_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `is_win` tinyint(1) DEFAULT '0',
  `bet_date` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bet_spots_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `balance` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `kb_pattern`
--

CREATE TABLE IF NOT EXISTS `kb_pattern` (
  `pattern_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `probability` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `odds` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `house_edge` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rtp` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`pattern_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `kb_user`
--

CREATE TABLE IF NOT EXISTS `kb_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` text COLLATE utf8_unicode_ci,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bitcoin_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `current_balance` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `kb_user`
--

INSERT INTO `kb_user` (`user_id`, `username`, `password`, `email`, `bitcoin_id`, `current_balance`) VALUES
(1, 'vuongngocnam', '4e7e9270dc88cbd961ff5e12bad98fab', 'vuongngocnam@gmail.com', NULL, NULL),
(3, 'vuongngocnam', '4e7e9270dc88cbd961ff5e12bad98fab', 'vuongngocnam@gmail.com', NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
