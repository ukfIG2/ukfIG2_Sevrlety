-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hostiteľ: 127.0.0.1
-- Čas generovania: So 30.Dec 2023, 14:16
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
-- Databáza: `obchod`
--

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` double NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Sťahujem dáta pre tabuľku `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`) VALUES
(51, 3, 7, 0.16, 1);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` double NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Sťahujem dáta pre tabuľku `obj_polozky`
--

INSERT INTO `obj_polozky` (`ID`, `ID_objednavky`, `ID_tovaru`, `cena`, `ks`) VALUES
(4, 2, 8, 0.39, 2),
(5, 2, 7, 0.16, 3),
(6, 2, 5, 0.13, 4),
(7, 2, 5, 0.13, 14),
(8, 3, 5, 0.13, 14),
(9, 2, 5, 0.13, 5),
(10, 3, 5, 0.13, 5),
(11, 4, 5, 0.13, 5),
(12, 2, 5, 0.13, 54),
(13, 2, 8, 0.39, 7),
(14, 2, 7, 0.16, 16),
(15, 3, 5, 0.13, 54),
(16, 3, 8, 0.39, 7),
(17, 3, 7, 0.16, 16),
(18, 4, 5, 0.13, 54),
(19, 4, 8, 0.39, 7),
(20, 4, 7, 0.16, 16),
(21, 5, 5, 0.13, 54),
(22, 5, 8, 0.39, 7),
(23, 5, 7, 0.16, 16),
(24, 2, 5, 0.13, 3),
(25, 3, 5, 0.13, 3),
(26, 4, 5, 0.13, 3),
(27, 5, 5, 0.13, 3),
(28, 6, 5, 0.13, 3),
(29, 7, 10, 1, 2),
(30, 2, 5, 0.13, 1),
(31, 3, 5, 0.13, 1),
(32, 4, 5, 0.13, 1),
(33, 8, 5, 0.13, 1),
(34, 6, 5, 0.13, 1),
(35, 9, 5, 0.13, 5),
(36, 3, 5, 0.13, 5),
(37, 4, 5, 0.13, 5),
(38, 8, 5, 0.13, 5),
(39, 6, 5, 0.13, 5);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` double NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Sťahujem dáta pre tabuľku `obj_zoznam`
--

INSERT INTO `obj_zoznam` (`ID`, `obj_cislo`, `datum_objednavky`, `ID_pouzivatela`, `suma`, `stav`) VALUES
(9, '00000007', '2023-12-30', 1, 0.65, 'nová'),
(3, '00000002', '2023-12-28', 1, 1.82, 'expedicia'),
(4, '00000003', '2023-12-28', 1, 0.65, 'expedicia'),
(8, '00000006', '2023-12-30', 1, 0.13, 'nová'),
(6, '00000005', '2023-12-28', 1, 0.39, 'nová');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(100) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` double NOT NULL,
  `img` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Sťahujem dáta pre tabuľku `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`, `img`) VALUES
(5, 'Rozok', 925, 0.13, 'https://secure.ce-tescoassets.com/assets/SK/070/8584126829070/ShotType1_540x540.jpg'),
(7, 'Kajzerka cereálna 60g', 81, 0.16, 'https://secure.ce-tescoassets.com/assets/SK/490/0000000029490/ShotType1_540x540.jpg'),
(9, 'Bageta francúzska 110g', 25, 0.39, 'https://secure.ce-tescoassets.com/assets/SK/434/218434/ShotType1_225x225.jpg'),
(13, 'Donut čokoládový 55g', 45, 0.45, 'https://secure.ce-tescoassets.com/assets/SK/377/218377/ShotType1_225x225.jpg'),
(12, 'Oškvarkový pagáč 71g', 64, 0.49, 'https://secure.ce-tescoassets.com/assets/SK/733/8588007962733/ShotType1_540x540.jpg'),
(14, 'Bageta drevorubačská 150g', 12, 0.99, 'https://secure.ce-tescoassets.com/assets/SK/724/0000000027724/ShotType1_225x225.jpg');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `admin` tinyint(1) DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Sťahujem dáta pre tabuľku `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `meno`, `priezvisko`, `admin`) VALUES
(1, 'mkopac@ukf.sk', '123', 'Andovska 1', 'Martin', 'Kopac', 0),
(2, 'admin@ukf.sk', '123', 'Tesco', 'Nick', 'Cage', 1),
(3, 'vstolicka@ukf.sk', '123', 'Stolickova 52', 'Vlastimil', 'Stolicka', 0);

--
-- Kľúče pre exportované tabuľky
--

--
-- Indexy pre tabuľku `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexy pre tabuľku `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexy pre tabuľku `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexy pre tabuľku `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexy pre tabuľku `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pre exportované tabuľky
--

--
-- AUTO_INCREMENT pre tabuľku `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT pre tabuľku `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT pre tabuľku `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pre tabuľku `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pre tabuľku `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
