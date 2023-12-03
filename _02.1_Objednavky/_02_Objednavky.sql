-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 03, 2023 at 11:42 PM
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
-- Database: `_02_Objednavky`
--

-- --------------------------------------------------------

--
-- Table structure for table `Objednavky`
--

CREATE TABLE `Objednavky` (
  `idObjednavky` int(11) NOT NULL,
  `Datum_objednavky` date NOT NULL DEFAULT current_timestamp(),
  `id_Zakaznikov` int(11) DEFAULT NULL,
  `idTovar` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Objednavky`
--

INSERT INTO `Objednavky` (`idObjednavky`, `Datum_objednavky`, `id_Zakaznikov`, `idTovar`) VALUES
(9, '2023-08-09', 31, 12),
(10, '2023-12-03', 32, 13),
(11, '2023-12-13', 33, 14);

-- --------------------------------------------------------

--
-- Table structure for table `Tovar`
--

CREATE TABLE `Tovar` (
  `idTovar` int(11) NOT NULL,
  `Nazov_tovaru` varchar(50) DEFAULT NULL,
  `Cena_tovaru` double DEFAULT NULL,
  `Hodnotenie` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Tovar`
--

INSERT INTO `Tovar` (`idTovar`, `Nazov_tovaru`, `Cena_tovaru`, `Hodnotenie`) VALUES
(12, 'Kladivo', 5.45, 5),
(13, 'Tehla', 2.45, 0),
(14, 'Roxor', 6, 3),
(18, 'nepouzity', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `Zakaznici`
--

CREATE TABLE `Zakaznici` (
  `id_Zakaznikov` int(11) NOT NULL,
  `Meno_zakaznika` varchar(30) DEFAULT NULL,
  `Priezvisko_zakaznika` varchar(30) DEFAULT NULL,
  `ICO` varchar(15) DEFAULT NULL,
  `Adresa` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Zakaznici`
--

INSERT INTO `Zakaznici` (`id_Zakaznikov`, `Meno_zakaznika`, `Priezvisko_zakaznika`, `ICO`, `Adresa`) VALUES
(31, 'Jozo', 'Mrkvicka', '651651641', 'Nitra'),
(32, 'Peter', 'Maga', '4416', 'Bratislava'),
(33, 'Fero', 'Richtar', '451416841', 'Piestany'),
(36, 'nepouzity', 'nepouzity', 'n', 'n');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Objednavky`
--
ALTER TABLE `Objednavky`
  ADD PRIMARY KEY (`idObjednavky`),
  ADD KEY `idTovar` (`idTovar`),
  ADD KEY `id_Zakaznikov` (`id_Zakaznikov`);

--
-- Indexes for table `Tovar`
--
ALTER TABLE `Tovar`
  ADD PRIMARY KEY (`idTovar`);

--
-- Indexes for table `Zakaznici`
--
ALTER TABLE `Zakaznici`
  ADD PRIMARY KEY (`id_Zakaznikov`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Objednavky`
--
ALTER TABLE `Objednavky`
  MODIFY `idObjednavky` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `Tovar`
--
ALTER TABLE `Tovar`
  MODIFY `idTovar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `Zakaznici`
--
ALTER TABLE `Zakaznici`
  MODIFY `id_Zakaznikov` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Objednavky`
--
ALTER TABLE `Objednavky`
  ADD CONSTRAINT `Objednavky_ibfk_1` FOREIGN KEY (`idTovar`) REFERENCES `Tovar` (`idTovar`),
  ADD CONSTRAINT `Objednavky_ibfk_2` FOREIGN KEY (`id_Zakaznikov`) REFERENCES `Zakaznici` (`id_Zakaznikov`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
