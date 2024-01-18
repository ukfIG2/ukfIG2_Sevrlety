-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 18, 2024 at 10:14 PM
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
-- Database: `Skuska_Letecke_Treningy`
--

-- --------------------------------------------------------

--
-- Table structure for table `Planes`
--

CREATE TABLE `Planes` (
  `idPlanes` int(11) NOT NULL,
  `Designation` varchar(10) DEFAULT NULL,
  `Manufacturer` varchar(50) DEFAULT NULL,
  `Model` varchar(25) DEFAULT NULL,
  `Active` tinyint(4) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Planes`
--

INSERT INTO `Planes` (`idPlanes`, `Designation`, `Manufacturer`, `Model`, `Active`) VALUES
(3, 'KVM-55', 'BOING', '22', 1),
(4, 'LL-55', 'DELTA', '50', 0),
(5, 'POL-55-L', 'Nejake', 'M55', 1),
(6, '123', '123', '12', 0);

-- --------------------------------------------------------

--
-- Table structure for table `records`
--

CREATE TABLE `records` (
  `idRecord` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Time_From` time DEFAULT NULL,
  `Time_TO` time DEFAULT NULL,
  `Customer` int(11) DEFAULT NULL,
  `Instructor` int(11) DEFAULT NULL,
  `Lietadlo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `records`
--

INSERT INTO `records` (`idRecord`, `Date`, `Time_From`, `Time_TO`, `Customer`, `Instructor`, `Lietadlo`) VALUES
(2, '2024-01-19', '01:54:08', '16:40:08', 2, 3, 4),
(6, '2024-01-18', '14:20:00', '18:30:00', NULL, 3, 3),
(7, '2024-01-08', '17:03:00', '11:12:00', NULL, 3, 3),
(9, '2024-01-24', '11:22:00', '22:11:00', NULL, 3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `idUsers` int(11) NOT NULL,
  `Name` varchar(25) DEFAULT NULL,
  `Surname` varchar(25) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Position` varchar(20) DEFAULT 'zakaznik',
  `Note` text DEFAULT NULL,
  `Active` tinyint(4) DEFAULT NULL,
  `Password` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`idUsers`, `Name`, `Surname`, `Email`, `Position`, `Note`, `Active`, `Password`) VALUES
(1, 'Admin', 'Ivan', 'ivan@ivan', 'Admin', '', NULL, 'ivan'),
(2, 'Customer', 'Ivan', 'fero@fero', 'Customer', NULL, NULL, 'fero'),
(3, 'Instructor', 'juro', 'juro@juro', 'Instructor', 'tractor', 1, 'juro'),
(5, 'Jozo', 'Nejaaky', 'jozo@jozo', 'Instructor', '', 1, NULL),
(7, '123', '123', '123@123', 'Customer', NULL, NULL, '123'),
(8, 'poli', 'poli', 'polli@Poli', 'Customer', NULL, NULL, 'poli');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Planes`
--
ALTER TABLE `Planes`
  ADD PRIMARY KEY (`idPlanes`);

--
-- Indexes for table `records`
--
ALTER TABLE `records`
  ADD PRIMARY KEY (`idRecord`),
  ADD KEY `Customer` (`Customer`),
  ADD KEY `Instructor` (`Instructor`),
  ADD KEY `Lietadlo` (`Lietadlo`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`idUsers`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Planes`
--
ALTER TABLE `Planes`
  MODIFY `idPlanes` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `records`
--
ALTER TABLE `records`
  MODIFY `idRecord` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `idUsers` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `records`
--
ALTER TABLE `records`
  ADD CONSTRAINT `Customer` FOREIGN KEY (`Customer`) REFERENCES `Users` (`idUsers`),
  ADD CONSTRAINT `Instructor` FOREIGN KEY (`Instructor`) REFERENCES `Users` (`idUsers`),
  ADD CONSTRAINT `Lietadlo` FOREIGN KEY (`Lietadlo`) REFERENCES `Planes` (`idPlanes`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
