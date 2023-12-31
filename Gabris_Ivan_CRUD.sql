-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 30, 2023 at 04:03 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

﻿--
-- Database: `_01_CRUD`
--
﻿
-- --------------------------------------------------------

--
-- Table structure for table `Prijmy_Vydaje`
--

CREATE TABLE `Prijmy_Vydaje` (
  `id` int(11) NOT NULL,
  `Datum` date NOT NULL DEFAULT current_timestamp(),
  `Prijem` double NOT NULL DEFAULT 0,
  `Vydaj` double NOT NULL DEFAULT 0,
  `Poznamka` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;
﻿
--
-- Dumping data for table `Prijmy_Vydaje`
--

﻿INSERT INTO `Prijmy_Vydaje` (`id`, `Datum`, `Prijem`, `Vydaj`, `Poznamka`) VALUES
(4, '2023-11-23', 150, 0, '555')﻿,
(7, '1998-11-25', 0, 200, 'wcfwkmfckwpmfcp')﻿,
(8, '2023-11-23', 356, 0, NULL)﻿,
(9, '2023-05-14', 100, 150, '561')﻿,
(11, '2023-11-23', 111111111, 0, '2222222222222222222222222222222222')﻿,
(14, '2023-11-23', 55, 55, 'mockmnkim')﻿;
﻿
--
-- Indexes for dumped tables
--

--
-- Indexes for table `Prijmy_Vydaje`
--
ALTER TABLE `Prijmy_Vydaje`
  ADD PRIMARY KEY (`id`);
﻿
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Prijmy_Vydaje`
--
ALTER TABLE `Prijmy_Vydaje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
﻿COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
