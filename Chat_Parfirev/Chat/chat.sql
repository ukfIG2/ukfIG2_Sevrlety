-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hostiteľ: 127.0.0.1
-- Čas generovania: Sun 10.Dec 2023, 14:13
-- Verzia serveru: 10.4.28-MariaDB
-- Verzia PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáza: `chat`
--
CREATE DATABASE IF NOT EXISTS `chat` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `chat`;

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `bans`
--

DROP TABLE IF EXISTS `bans`;
CREATE TABLE `bans` (
  `users_id` int(11) NOT NULL,
  `blocked_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Sťahujem dáta pre tabuľku `bans`
--

INSERT INTO `bans` (`users_id`, `blocked_user`) VALUES
(1, 2);

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `users_id` int(11) NOT NULL,
  `text` text DEFAULT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Sťahujem dáta pre tabuľku `messages`
--

INSERT INTO `messages` (`id`, `users_id`, `text`, `datetime`) VALUES
(1, 1, 'Čaute!', '2023-12-09 13:01:09'),
(2, 1, 'Mám nádej, že sa v tomto priestore budete cítiť kľudné.', '2023-12-09 13:01:15'),
(5, 3, 'Čawko', '2023-12-10 11:26:02'),
(6, 2, 'f-word', '2023-12-10 11:26:12'),
(7, 1, 'Ha, funguje)', '2023-12-10 14:11:23');

-- --------------------------------------------------------

--
-- Štruktúra tabuľky pre tabuľku `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `color_hex` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Sťahujem dáta pre tabuľku `users`
--

INSERT INTO `users` (`id`, `nickname`, `password`, `color_hex`) VALUES
(1, 'klementinko', '1', '584c36'),
(2, 'bad_guy666', '2', 'c94e2e'),
(3, 'kittie', '3', '02497d');

--
-- Kľúče pre exportované tabuľky
--

--
-- Indexy pre tabuľku `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_message_users_idx` (`users_id`);

--
-- Indexy pre tabuľku `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pre exportované tabuľky
--

--
-- AUTO_INCREMENT pre tabuľku `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pre tabuľku `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Obmedzenie pre exportované tabuľky
--

--
-- Obmedzenie pre tabuľku `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_message_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
