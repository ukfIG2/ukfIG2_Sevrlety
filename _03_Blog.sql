-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 10, 2023 at 03:06 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `_03_Blog`
--

-- --------------------------------------------------------

--
-- Table structure for table `Bans`
--

CREATE TABLE `Bans` (
  `idBan` int(11) NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  `idBanner` int(11) NOT NULL,
  `idBanned` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Bans`
--

INSERT INTO `Bans` (`idBan`, `Date`, `idBanner`, `idBanned`) VALUES
(35, '2023-12-10 14:38:35', 22, 24),
(36, '2023-12-10 14:41:42', 24, 22),
(37, '2023-12-10 14:49:22', 25, 22),
(38, '2023-12-10 14:49:22', 25, 23),
(39, '2023-12-10 14:49:22', 25, 24),
(40, '2023-12-10 14:55:29', 28, 22),
(41, '2023-12-10 15:02:42', 29, 22),
(42, '2023-12-10 15:02:42', 29, 23),
(43, '2023-12-10 15:02:42', 29, 24),
(44, '2023-12-10 15:02:42', 29, 25),
(45, '2023-12-10 15:02:42', 29, 26),
(46, '2023-12-10 15:02:42', 29, 28);

-- --------------------------------------------------------

--
-- Table structure for table `Posts`
--

CREATE TABLE `Posts` (
  `idPosts` int(11) NOT NULL,
  `idUsers` int(11) DEFAULT NULL,
  `Text` text NOT NULL,
  `Datetime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Posts`
--

INSERT INTO `Posts` (`idPosts`, `idUsers`, `Text`, `Datetime`) VALUES
(4, 22, 'ncpanpckadpcampocm', '2023-12-10 09:53:17'),
(5, 23, 'mcpkmc pqmc[oqm', '2023-12-09 09:52:35'),
(6, 22, 'ncpanpckadpcampocm', '2023-12-10 09:53:23'),
(7, 23, 'mcpkmc pqmc[oqm', '2023-12-09 09:52:35'),
(8, 24, 'Nieco nieco\r\n', '2023-12-10 11:34:15'),
(9, 24, 'Nieco nieco\r\n', '2023-12-10 11:34:53'),
(10, 24, 'polk', '2023-12-10 11:58:13'),
(11, 24, 'dmapkdm[mqs[\r\n', '2023-12-10 11:58:19'),
(12, 24, 'wfpmwomf[me2f\r\n', '2023-12-10 12:10:16'),
(13, 22, 'wemef[w', '2023-12-10 12:19:56'),
(14, 22, 'fff', '2023-12-10 12:20:04'),
(15, 22, 'bla', '2023-12-10 12:22:31'),
(16, 23, 'Juro bla bla', '2023-12-10 12:25:19'),
(17, 24, 'aksmd;kam', '2023-12-10 12:57:58'),
(18, 24, 'pfwjfwokj', '2023-12-10 13:39:44'),
(19, 22, 'kkk', '2023-12-10 14:19:50'),
(20, 22, 'iii\r\n', '2023-12-10 14:38:39'),
(21, 22, 'k', '2023-12-10 14:41:18'),
(22, 25, 'bla bla\r\n', '2023-12-10 14:49:12'),
(23, 28, 'nn', '2023-12-10 14:55:17');

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `idUsers` int(11) NOT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Passwords` varchar(64) DEFAULT NULL,
  `Nickname` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`idUsers`, `Email`, `Passwords`, `Nickname`) VALUES
(22, 'ivan@ivan', '3d9f77fa963db003d72494b34359c163d5fbd92b2e8fbaabbeb975a52312b526', 'Ivan'),
(23, 'juro@juro', '86bcea20b2150ff5c9659dc569abc0863fe393a15cf4ba0624edd4eff3bf6ece', 'Juro'),
(24, 'fero@fero', 'f29575d3dcee3499a651e9658c68e76a5c52f2721d87662f4bf3b873bdec992d', 'fero'),
(25, 'palo@palo', 'af5f169dea56411f6174ccbb9df14f172e67a74457ff21188050e2d62ec81b9d', 'Palo1'),
(26, 'bla@bla', '9d1705b04b48829461fa490bf5c3878c27015c9544840e8dcd2cd41583f08cad', 'blaBla'),
(28, 'f@f', 'c5042a48af67ca1a912610c497d05f39aa7c2ce61c4b876f356db9b7ad0b4e65', 'fqpkemfqmf'),
(29, 'll@ll', '74783cbaf13c5f87ea1cdb4559e813806c0544f301f19b5af20d14de4e0cdeda', 'f;qlmfq,mwf[o,m');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Bans`
--
ALTER TABLE `Bans`
  ADD PRIMARY KEY (`idBan`),
  ADD KEY `idBanned` (`idBanned`),
  ADD KEY `Bans_ibfk_2` (`idBanner`);

--
-- Indexes for table `Posts`
--
ALTER TABLE `Posts`
  ADD PRIMARY KEY (`idPosts`),
  ADD KEY `idUsers` (`idUsers`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`idUsers`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Bans`
--
ALTER TABLE `Bans`
  MODIFY `idBan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `Posts`
--
ALTER TABLE `Posts`
  MODIFY `idPosts` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `idUsers` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Bans`
--
ALTER TABLE `Bans`
  ADD CONSTRAINT `Bans_ibfk_1` FOREIGN KEY (`idBanned`) REFERENCES `Users` (`idUsers`),
  ADD CONSTRAINT `Bans_ibfk_2` FOREIGN KEY (`idBanner`) REFERENCES `Users` (`idUsers`);

--
-- Constraints for table `Posts`
--
ALTER TABLE `Posts`
  ADD CONSTRAINT `Posts_ibfk_1` FOREIGN KEY (`idUsers`) REFERENCES `Users` (`idUsers`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
