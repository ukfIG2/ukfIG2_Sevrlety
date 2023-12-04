-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hostiteľ: 127.0.0.1
-- Čas generovania: Št 30.Nov 2023, 15:18
-- Verzia serveru: 10.4.27-MariaDB
-- Verzia PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáza: `objektove-technologie`
--

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `objednany_tovar`
--

CREATE TABLE `objednany_tovar` (
  `id` int(11) NOT NULL,
  `id_tovar` int(11) NOT NULL,
  `id_zakaznik` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Sťahujem dáta pre tabuľku `objednany_tovar`
--

INSERT INTO `objednany_tovar` (`id`, `id_tovar`, `id_zakaznik`, `datum`) VALUES
(1, 1, 1, '2023-11-08'),
(2, 2, 1, '2023-11-28'),
(3, 3, 2, '2023-11-14'),
(4, 2, 2, '2023-11-14'),
(5, 5, 1, '2023-11-01'),
(8, 7, 1, '2022-06-29'),
(9, 1, 2, '2001-01-28'),
(10, 7, 7, '2023-11-29'),
(12, 7, 7, '2023-12-08');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `pohyby_uctu`
--

CREATE TABLE `pohyby_uctu` (
  `id` int(11) NOT NULL,
  `typ` enum('prijem','vydavok') NOT NULL,
  `suma` decimal(10,2) NOT NULL,
  `popis` varchar(255) DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Sťahujem dáta pre tabuľku `pohyby_uctu`
--

INSERT INTO `pohyby_uctu` (`id`, `typ`, `suma`, `popis`, `date`) VALUES
(2, 'prijem', '1000.00', 'za kavu', '2023-11-08'),
(7, 'vydavok', '8900.00', 'kupa auta', '2023-11-30'),
(9, 'prijem', '3500.00', 'Vyplata', '2023-11-15');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `priestupky`
--

CREATE TABLE `priestupky` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `priezvisko` varchar(50) NOT NULL,
  `popis_priestupku` varchar(100) NOT NULL,
  `datum` date NOT NULL,
  `suma` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Sťahujem dáta pre tabuľku `priestupky`
--

INSERT INTO `priestupky` (`id`, `meno`, `priezvisko`, `popis_priestupku`, `datum`, `suma`) VALUES
(1, 'jozef', 'jaros', 'rychlost', '2023-05-21', 10),
(2, 'samuel', 'Macek', 'svetla', '2023-11-22', 20),
(3, 'simon', 'jerabek', 'svetielka', '2023-11-11', 500),
(6, 'Janko', 'Mrkvicka', 'idk', '2023-11-15', 10),
(7, 'Ferko', 'Mrkva', 'netusim', '2023-10-31', 155);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `tovar`
--

CREATE TABLE `tovar` (
  `id` int(11) NOT NULL,
  `nazov` varchar(100) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `hodnotenie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Sťahujem dáta pre tabuľku `tovar`
--

INSERT INTO `tovar` (`id`, `nazov`, `cena`, `hodnotenie`) VALUES
(1, 'Topankyy', '999999.00', 1),
(2, 'Bunda', '200.00', 10),
(3, 'Tricko', '15.00', 6),
(5, 'testovaciKTORYNEMAVYMAZAT', '99.00', 10),
(7, 'Rukaivce', '20.00', 6);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `zakaznici`
--

CREATE TABLE `zakaznici` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `priezvisko` varchar(50) NOT NULL,
  `ico` varchar(8) NOT NULL,
  `adresa` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Sťahujem dáta pre tabuľku `zakaznici`
--

INSERT INTO `zakaznici` (`id`, `meno`, `priezvisko`, `ico`, `adresa`) VALUES
(1, 'Marekk', 'Jaross', '12345678', 'Ziar'),
(2, 'Jozko', 'Mrkvicka', '09445678', 'Bratislava'),
(7, 'Ferko', 'Viler', '12312313', 'Topolcany');

--
-- Kľúče pre exportované tabuľky
--

--
-- Indexy pre tabuľku `objednany_tovar`
--
ALTER TABLE `objednany_tovar`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pre tabuľku `pohyby_uctu`
--
ALTER TABLE `pohyby_uctu`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pre tabuľku `priestupky`
--
ALTER TABLE `priestupky`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pre tabuľku `tovar`
--
ALTER TABLE `tovar`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pre tabuľku `zakaznici`
--
ALTER TABLE `zakaznici`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pre exportované tabuľky
--

--
-- AUTO_INCREMENT pre tabuľku `objednany_tovar`
--
ALTER TABLE `objednany_tovar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT pre tabuľku `pohyby_uctu`
--
ALTER TABLE `pohyby_uctu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pre tabuľku `priestupky`
--
ALTER TABLE `priestupky`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pre tabuľku `tovar`
--
ALTER TABLE `tovar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT pre tabuľku `zakaznici`
--
ALTER TABLE `zakaznici`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
