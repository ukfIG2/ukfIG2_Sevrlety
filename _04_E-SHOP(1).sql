-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 30, 2023 at 11:52 PM
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
-- Database: `_04_E-SHOP`
--

-- --------------------------------------------------------

--
-- Table structure for table `Kosik`
--

CREATE TABLE `Kosik` (
  `idKosika` int(11) NOT NULL,
  `ID_Users` int(11) NOT NULL,
  `ID_Tovaru` int(11) NOT NULL,
  `Cena` float NOT NULL,
  `Pocet_kusov` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Polozky_objednavky`
--

CREATE TABLE `Polozky_objednavky` (
  `id_Polozky_objednavky` int(11) NOT NULL,
  `Cena_za_kus` double NOT NULL COMMENT 'Uz realna suma',
  `Pocet_kusov` int(11) NOT NULL,
  `Cislo_objednavky` varchar(20) NOT NULL,
  `idTovaru` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Polozky_objednavky`
--

INSERT INTO `Polozky_objednavky` (`id_Polozky_objednavky`, `Cena_za_kus`, `Pocet_kusov`, `Cislo_objednavky`, `idTovaru`) VALUES
(16, 1460.75, 5, '2023/12/30/1', 3),
(17, 1782.8999999999999, 5, '2023/12/30/1', 6),
(18, 1792.8999999999999, 5, '2023/12/30/1', 7),
(19, 1460.75, 5, '2023/12/30/2', 3),
(20, 1782.8999999999999, 5, '2023/12/30/2', 6),
(21, 1792.8999999999999, 5, '2023/12/30/2', 7),
(22, 1461, 5, '2023/12/30/3', 3),
(23, 1783, 5, '2023/12/30/3', 6),
(24, 1793, 5, '2023/12/30/3', 7),
(25, 1460, 5, '2023/12/30/4', 3),
(26, 1782, 5, '2023/12/30/4', 6),
(27, 1792, 5, '2023/12/30/4', 7),
(28, 1460.75, 5, '2023/12/30/5', 3),
(29, 1782.9, 5, '2023/12/30/5', 6),
(30, 1792.9, 5, '2023/12/30/5', 7),
(31, 1460.75, 5, '2023/12/30/6', 3),
(32, 2139.48, 6, '2023/12/30/6', 6),
(33, 2868.64, 8, '2023/12/30/6', 7),
(34, 4820.53, 11, '2023/12/30/7', 3),
(35, 3744.02, 7, '2023/12/30/7', 6),
(36, 1613.58, 3, '2023/12/30/7', 7);

-- --------------------------------------------------------

--
-- Table structure for table `Tovar`
--

CREATE TABLE `Tovar` (
  `idTovaru` int(11) NOT NULL,
  `Znacka` varchar(20) DEFAULT NULL,
  `Modelova_rada` varchar(20) DEFAULT NULL,
  `Nazov` varchar(20) DEFAULT NULL,
  `Procesor` varchar(20) DEFAULT NULL,
  `Velkost_operacnej_pamate` int(11) DEFAULT NULL,
  `Uhlopriecka_displeja` double DEFAULT NULL,
  `Fotka` text NOT NULL,
  `Pocet_kusov` int(11) DEFAULT NULL COMMENT 'Pocet kusov za cenu=Cena',
  `Cena` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Tovar`
--

INSERT INTO `Tovar` (`idTovaru`, `Znacka`, `Modelova_rada`, `Nazov`, `Procesor`, `Velkost_operacnej_pamate`, `Uhlopriecka_displeja`, `Fotka`, `Pocet_kusov`, `Cena`) VALUES
(3, 'Lenovo', 'ThinkPad', 'E14 Gen3', 'AMD Ryzen 5 5500U', 8, 14, 'https://img.lenovo-shop.sk/S/2020/09/91571-profesionalny-notebook-lenovo-thinkpad-t490s-gen2-cierny-02.webp', 24, 584.3),
(6, 'Lenovo', 'ThinkPad', 'E15 gen4', 'AMD Ryzen 5 5625U', 8, 15.6, 'https://img.lenovo-shop.sk/S/2022/06/113328-profesionalny-notebook-lenovo-thinkpad-e15-gen4-cierny-01.webp', 27, 713.15),
(7, 'Lenovo', 'ThinkPad', 'E16 Gen1', 'AMD Ryzen 5 7530U', 8, 16, 'https://img.lenovo-shop.sk/S/2023/06/126311-notebook-lenovo-thinkpad-e16-gen1-graphite-black-01.webp', 29, 717.15);

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `idUsers` int(11) NOT NULL,
  `Meno` varchar(20) DEFAULT NULL,
  `Priezvisko` varchar(20) DEFAULT NULL,
  `E_mail` varchar(50) DEFAULT NULL,
  `Adresa` varchar(50) DEFAULT NULL,
  `Admin` tinyint(4) DEFAULT 0,
  `Password` varchar(64) DEFAULT NULL,
  `Zlava` double NOT NULL DEFAULT 0 COMMENT 'Je to percento. Ned8vaj to pod 0.01 a  nad 100.',
  `Poznamky_k_pouzivatelovi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`idUsers`, `Meno`, `Priezvisko`, `E_mail`, `Adresa`, `Admin`, `Password`, `Zlava`, `Poznamky_k_pouzivatelovi`) VALUES
(1, 'Ivan', 'Gabris', 'ivan@ivan', 'M. Benku 2423', 0, '3d9f77fa963db003d72494b34359c163d5fbd92b2e8fbaabbeb975a52312b526', 25, 'Je to ....'),
(6, 'Juro', 'Gabris', 'juro@juro', 'M. Benku 5555', 1, '86bcea20b2150ff5c9659dc569abc0863fe393a15cf4ba0624edd4eff3bf6ece', 0, NULL),
(10, 'fero', 'fero', 'fero@fero', 'fero', 0, 'f29575d3dcee3499a651e9658c68e76a5c52f2721d87662f4bf3b873bdec992d', 50, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Zoznam_objednavok`
--

CREATE TABLE `Zoznam_objednavok` (
  `Cislo_objednavky` varchar(20) NOT NULL,
  `idUsers` int(11) NOT NULL,
  `Datum_objednavky` datetime DEFAULT current_timestamp(),
  `suma` double NOT NULL COMMENT 'Vypocitana z Polozky objednavky',
  `Stav_objednavky` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Zoznam_objednavok`
--

INSERT INTO `Zoznam_objednavok` (`Cislo_objednavky`, `idUsers`, `Datum_objednavky`, `suma`, `Stav_objednavky`) VALUES
('2023/12/30/1', 10, '2023-12-30 22:04:30', 1007.31, 'Spracuváva SA'),
('2023/12/30/2', 10, '2023-12-30 22:06:28', 5036.549999999999, 'Spracuváva SA'),
('2023/12/30/3', 10, '2023-12-30 22:08:11', 5036.549999999999, 'Spracuváva SA'),
('2023/12/30/4', 10, '2023-12-30 22:10:35', 5036.549999999999, 'Spracuváva SA'),
('2023/12/30/5', 10, '2023-12-30 22:12:08', 5036.549999999999, 'Spracuváva SA'),
('2023/12/30/6', 10, '2023-12-30 22:12:53', 6468.87, 'Spracuváva SA'),
('2023/12/30/7', 1, '2023-12-30 23:52:32', 10178.13, 'Spracuváva SA');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Kosik`
--
ALTER TABLE `Kosik`
  ADD PRIMARY KEY (`idKosika`),
  ADD KEY `ID_Tovaru` (`ID_Tovaru`),
  ADD KEY `ID_Users` (`ID_Users`);

--
-- Indexes for table `Polozky_objednavky`
--
ALTER TABLE `Polozky_objednavky`
  ADD PRIMARY KEY (`id_Polozky_objednavky`),
  ADD KEY `Cislo_objednavky` (`Cislo_objednavky`),
  ADD KEY `idTovaru` (`idTovaru`);

--
-- Indexes for table `Tovar`
--
ALTER TABLE `Tovar`
  ADD PRIMARY KEY (`idTovaru`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`idUsers`),
  ADD UNIQUE KEY `E-mail` (`E_mail`);

--
-- Indexes for table `Zoznam_objednavok`
--
ALTER TABLE `Zoznam_objednavok`
  ADD PRIMARY KEY (`Cislo_objednavky`),
  ADD KEY `idUsers` (`idUsers`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Kosik`
--
ALTER TABLE `Kosik`
  MODIFY `idKosika` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `Polozky_objednavky`
--
ALTER TABLE `Polozky_objednavky`
  MODIFY `id_Polozky_objednavky` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `Tovar`
--
ALTER TABLE `Tovar`
  MODIFY `idTovaru` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `idUsers` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Kosik`
--
ALTER TABLE `Kosik`
  ADD CONSTRAINT `Kosik_ibfk_1` FOREIGN KEY (`ID_Tovaru`) REFERENCES `Tovar` (`idTovaru`),
  ADD CONSTRAINT `Kosik_ibfk_2` FOREIGN KEY (`ID_Users`) REFERENCES `Users` (`idUsers`);

--
-- Constraints for table `Polozky_objednavky`
--
ALTER TABLE `Polozky_objednavky`
  ADD CONSTRAINT `Polozky_objednavky_ibfk_1` FOREIGN KEY (`Cislo_objednavky`) REFERENCES `Zoznam_objednavok` (`Cislo_objednavky`),
  ADD CONSTRAINT `Polozky_objednavky_ibfk_2` FOREIGN KEY (`idTovaru`) REFERENCES `Tovar` (`idTovaru`);

--
-- Constraints for table `Zoznam_objednavok`
--
ALTER TABLE `Zoznam_objednavok`
  ADD CONSTRAINT `Zoznam_objednavok_ibfk_1` FOREIGN KEY (`idUsers`) REFERENCES `Users` (`idUsers`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
