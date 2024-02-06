-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 06, 2024 at 08:05 PM
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
-- Database: `ais`
--
CREATE DATABASE IF NOT EXISTS `ais` DEFAULT CHARACTER SET utf8 COLLATE utf8_slovak_ci;
USE `ais`;

-- --------------------------------------------------------

--
-- Table structure for table `predmety`
--

CREATE TABLE `predmety` (
  `id` int(11) NOT NULL,
  `predmet` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `predmety`
--

INSERT INTO `predmety` (`id`, `predmet`) VALUES
(1, 'Java'),
(2, 'HTML'),
(3, 'Operacne systemy'),
(4, 'Automatizacia');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `meno` varchar(20) NOT NULL,
  `priezvisko` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `passwd` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `meno`, `priezvisko`, `email`, `passwd`) VALUES
(1, 'Jozef', 'Mak', 'jmak@ukf.sk', '123'),
(2, 'Ivan', 'Hrozny', 'ihrozny@ukf.sk', '123'),
(3, 'Zuzana', 'Mala', 'zmala@ukf.sk', '123'),
(4, 'Michaela', 'Ceresnova', 'mceresnova@ukf.sk', '123');

-- --------------------------------------------------------

--
-- Table structure for table `znamky`
--

CREATE TABLE `znamky` (
  `id` int(11) NOT NULL,
  `datum` date NOT NULL,
  `user_id` int(11) NOT NULL,
  `predmet_id` int(11) NOT NULL,
  `znamka` int(11) NOT NULL,
  `videne` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `znamky`
--

INSERT INTO `znamky` (`id`, `datum`, `user_id`, `predmet_id`, `znamka`, `videne`) VALUES
(1, '2020-10-10', 1, 1, 1, NULL),
(2, '2020-10-11', 1, 2, 1, '2023-12-08'),
(3, '2020-11-02', 2, 1, 3, NULL),
(4, '2020-11-03', 1, 3, 2, '2023-12-08');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `predmety`
--
ALTER TABLE `predmety`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `znamky`
--
ALTER TABLE `znamky`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `predmety`
--
ALTER TABLE `predmety`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `znamky`
--
ALTER TABLE `znamky`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Database: `chat`
--
CREATE DATABASE IF NOT EXISTS `chat` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `chat`;

-- --------------------------------------------------------

--
-- Table structure for table `bans`
--

CREATE TABLE `bans` (
  `users_id` int(11) NOT NULL,
  `blocked_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `bans`
--

INSERT INTO `bans` (`users_id`, `blocked_user`) VALUES
(1, 4),
(1, 1),
(4, 2),
(4, 3),
(4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `users_id` int(11) NOT NULL,
  `text` text DEFAULT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `users_id`, `text`, `datetime`) VALUES
(1, 1, 'Čaute!', '2023-12-09 13:01:09'),
(2, 1, 'Mám nádej, že sa v tomto priestore budete cítiť kľudné.', '2023-12-09 13:01:15'),
(5, 3, 'Čawko', '2023-12-10 11:26:02'),
(6, 2, 'f-word', '2023-12-10 11:26:12'),
(7, 1, 'Ha, funguje)', '2023-12-10 14:11:23'),
(8, 1, 'll', '2023-12-12 22:01:18'),
(9, 4, 'kkk', '2023-12-12 22:01:57'),
(10, 1, 'm', '2023-12-12 22:08:23'),
(11, 1, 'n', '2023-12-12 22:08:41'),
(12, 1, 'm\r\n', '2023-12-12 22:14:09'),
(13, 4, 'n', '2023-12-12 22:14:22'),
(14, 4, 'm\r\n', '2023-12-12 22:16:05');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `color_hex` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nickname`, `password`, `color_hex`) VALUES
(1, 'klementinko', '1', '584c36'),
(2, 'bad_guy666', '2', 'c94e2e'),
(3, 'kittie', '3', '02497d'),
(4, 'ivan', 'ivan', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_message_users_idx` (`users_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_message_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
--
-- Database: `chatAP`
--
CREATE DATABASE IF NOT EXISTS `chatAP` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatAP`;
--
-- Database: `chatJA`
--
CREATE DATABASE IF NOT EXISTS `chatJA` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatJA`;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `text` text NOT NULL,
  `odoslane` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `id_user`, `text`, `odoslane`) VALUES
(2, 1, 'Ahoj, ako sa mas ? ', '2023-12-08 00:00:00'),
(3, 4, 'Ahoj, celkom fajn, TY?', '2023-12-08 14:12:25'),
(4, 1, 'asdadasda', '2023-12-08 15:22:49'),
(5, 2, 'Hello guys', '2023-12-08 15:34:22'),
(6, 2, 'Je to tak ', '2023-12-08 15:34:34'),
(7, 2, 'ahahhah Ahojte \r\n\r\n', '2023-12-08 15:34:46'),
(8, 1, 'Keby sme tak boli vsetci po skuske z OT :(((( ', '2023-12-08 15:35:28'),
(9, 1, 'Dnes je skvely den.\r\n', '2023-12-08 15:37:17'),
(10, 1, 'asdasdasdasdas', '2023-12-08 16:45:20'),
(11, 4, 'asdadsasdsa', '2023-12-08 16:50:06'),
(12, 1, 'Ahahaha\r\n', '2023-12-09 12:28:17'),
(13, 4, 'asdasdad', '2023-12-09 12:28:28'),
(14, 1, 'asdasdasd', '2023-12-09 13:36:23'),
(15, 1, 'hghgfvuuuk', '2023-12-09 13:38:54'),
(16, 1, 'adadada', '2023-12-09 13:45:29'),
(17, 1, 'jhvvhjgvjkj', '2023-12-09 13:57:08'),
(18, 1, 'chhghvhjvg', '2023-12-09 13:57:14'),
(19, 1, 'hkgckuytguj', '2023-12-09 13:57:19'),
(20, 1, 'kklop', '2023-12-12 21:02:20'),
(21, 5, 'epmvpwm\r\n', '2023-12-12 21:03:28'),
(22, 1, 'kklop', '2023-12-12 21:03:30'),
(23, 5, 'mm', '2023-12-12 21:07:22'),
(24, 5, 'n', '2023-12-12 21:07:28'),
(25, 1, 'l', '2023-12-12 21:07:39'),
(26, 1, 'p\r\n', '2023-12-12 21:08:08'),
(27, 1, '123', '2023-12-12 21:08:12'),
(28, 5, 'k', '2023-12-12 21:08:21');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `meno` varchar(20) NOT NULL,
  `passwd` varchar(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `ban` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `meno`, `passwd`, `email`, `ban`) VALUES
(1, 'Jakub', '123', 'jakub@ukf.sk', 0),
(2, 'Adam', '123', 'adam@ukf.sk', 0),
(3, 'Peter', '123', 'peter@ukf.sk', 0),
(4, 'Martin', '123', 'martin@ukf.sk', 0),
(5, 'ivan', 'ivan', 'ivan2ivan', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- Database: `chatMJ`
--
CREATE DATABASE IF NOT EXISTS `chatMJ` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatMJ`;

-- --------------------------------------------------------

--
-- Table structure for table `ban`
--

CREATE TABLE `ban` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `blocked_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ban`
--

INSERT INTO `ban` (`id`, `id_user`, `blocked_by`) VALUES
(17, 1, 5),
(18, 5, 1),
(19, 1, 2),
(20, 2, 1),
(21, 1, 3),
(22, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `content` varchar(50) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`id`, `id_user`, `content`, `date`) VALUES
(1, 1, 'Ahoj toto je moja prva sprava a volam sa Jozef Mak', '2023-12-06 14:33:54'),
(2, 2, 'SERVUS to som ja ferko mrkva', '2023-12-06 14:33:54'),
(3, 2, 'Dlho sme sa nevideli ako sa mas', '2023-12-06 15:12:20'),
(4, 3, 'nech ma nevidi ferko mrkvicak', '2023-12-06 15:45:08'),
(5, 5, 'Ahojte ja som tu novy ', '2023-12-06 16:39:50'),
(6, 3, 'Konecne', '2023-12-06 16:42:23'),
(7, 1, 'fpowfom', '2023-12-12 14:34:34'),
(8, 1, 'pp', '2023-12-12 14:42:32'),
(9, 2, 'lmcúmdqmd,', '2023-08-14 14:45:58'),
(10, 1, 'pp', '2023-12-12 14:46:30'),
(11, 2, 'lopo', '2023-12-12 18:50:00'),
(12, 1, 'lopo', '2023-12-12 18:52:06'),
(13, 6, 'ikik', '2023-12-12 19:27:38'),
(14, 6, 'm', '2023-12-12 19:27:58'),
(15, 1, 'l', '2023-12-12 19:28:53'),
(16, 1, 'p', '2023-12-12 19:36:11'),
(17, 1, 'm', '2023-12-12 19:38:22');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `priezvisko` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `passwd` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `meno`, `priezvisko`, `email`, `passwd`) VALUES
(1, 'Jozef', 'Mak', 'jmak@gmail.com', '123'),
(2, 'Ferko', 'Mrkvicka', 'fm@ukf.sk', '123'),
(3, 'Jergus', 'Banovany', 'jb@ukf.sk', '123'),
(5, 'Marek', 'Jaros', 'mj@ukf.sk', '123'),
(6, 'ivan', 'ivan', 'ivan@ivan', 'ivan');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ban`
--
ALTER TABLE `ban`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ban`
--
ALTER TABLE `ban`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `post`
--
ALTER TABLE `post`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- Database: `chatPV`
--
CREATE DATABASE IF NOT EXISTS `chatPV` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatPV`;

-- --------------------------------------------------------

--
-- Table structure for table `blocks`
--

CREATE TABLE `blocks` (
  `user_id` int(11) NOT NULL,
  `blocked_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blocks`
--

INSERT INTO `blocks` (`user_id`, `blocked_id`) VALUES
(1, 4),
(4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `id_sender` int(11) NOT NULL,
  `msg` text NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`id`, `id_sender`, `msg`, `date`) VALUES
(1, 1, 'Tali > Legion ', '2023-12-10 00:00:00'),
(2, 3, 'I have a nuclear bomb in my backyard', '2023-12-10 08:26:54'),
(10, 4, 'yee', '2023-12-10 10:00:11'),
(11, 3, 'sup', '2023-12-10 10:00:26'),
(12, 2, 'Sometimes i dream about cheese', '2023-12-10 10:00:45'),
(13, 1, 'You know, it kinda sucks that i have to complete this task within 5 days while also having to estimate other people\'s works and having clearly not all the information required to do the task itself. ', '2023-12-10 10:22:36'),
(14, 2, 'Yea man, and we also got other funny subjects like the C and graphics, whilst task at the last one always have less than 2 days deadline with little to no materials given.', '2023-12-10 10:22:42'),
(17, 1, 'Yea, and guess what? i haven\'t slept for more than a whole day so i could finish it in time!', '2023-12-10 12:51:18'),
(18, 4, 'No way!\r\nCan you just send the finished task to the teachers on the next day? ', '2023-12-10 12:54:23'),
(19, 4, 'There is no way they will estimate all  those student works in one day, right?', '2023-12-10 13:09:21'),
(24, 1, 'Hell no, that\'s not happening, cus they will say that you had a deadline, and they will not accept amy more tasks despite the formality of those deadlines. You could\'ve just forgotten to send the homework, but that\'s a no-no', '2023-12-10 13:55:26');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `login` varchar(255) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `login`, `pwd`, `name`, `surname`) VALUES
(1, 'specimen', '1234', 'Test', 'Subject1'),
(2, 'qwerty', '1111', 'Gordon', 'Freeman'),
(3, '1221', '2222', 'Ted', 'Kaczynski'),
(4, 'f3v3r', '1234', 'f4v4r', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blocks`
--
ALTER TABLE `blocks`
  ADD PRIMARY KEY (`user_id`,`blocked_id`),
  ADD KEY `blocked_id` (`blocked_id`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_sender` (`id_sender`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blocks`
--
ALTER TABLE `blocks`
  ADD CONSTRAINT `blocks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `blocks_ibfk_2` FOREIGN KEY (`blocked_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `fk_sender_id` FOREIGN KEY (`id_sender`) REFERENCES `user` (`id`);
--
-- Database: `chatRV`
--
CREATE DATABASE IF NOT EXISTS `chatRV` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatRV`;

-- --------------------------------------------------------

--
-- Table structure for table `banned_users`
--

CREATE TABLE `banned_users` (
  `banning_user_id` int(11) NOT NULL,
  `banned_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `banned_users`
--

INSERT INTO `banned_users` (`banning_user_id`, `banned_user_id`) VALUES
(1, 2),
(1, 3),
(1, 9),
(9, 1),
(9, 2),
(9, 3);

-- --------------------------------------------------------

--
-- Table structure for table `chat_messages`
--

CREATE TABLE `chat_messages` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `message` text NOT NULL,
  `message_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `chat_messages`
--

INSERT INTO `chat_messages` (`id`, `username`, `message`, `message_date`) VALUES
(1, 'user', 'Verify system behavior under conditions that cannot be easily tested with hardware prototypes. Run sets of tests in parallel on a multicore workstation or a cluster.', '2023-12-06 19:42:46'),
(2, 'user2', 'Develop algorithms for state of charge (SOC) and state of health (SOH) estimation, cell balancing, and thermal management. Generate code for use in embedded controllers.', '2023-12-06 20:31:32'),
(3, 'user3', 'Convert your Simscape model to C code to test control algorithms. Run HIL tests on dSPACE®, Speedgoat, OPAL-RT, and other real-time systems before performing physical tests.', '2023-12-06 20:43:11'),
(21, 'ivan', 'lwcmúwokm', '2023-12-12 19:19:59'),
(22, 'ivan', 'kk', '2023-12-12 19:20:02'),
(23, 'user', 'dd', '2023-12-12 19:23:43'),
(24, 'user', 'ds', '2023-12-12 19:23:46');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'user', 'pas'),
(2, 'user2', 'pas2'),
(3, 'user3', 'pas3'),
(9, 'ivan', 'ivan');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `banned_users`
--
ALTER TABLE `banned_users`
  ADD PRIMARY KEY (`banning_user_id`,`banned_user_id`),
  ADD KEY `fk_banned_user` (`banned_user_id`);

--
-- Indexes for table `chat_messages`
--
ALTER TABLE `chat_messages`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chat_messages`
--
ALTER TABLE `chat_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `banned_users`
--
ALTER TABLE `banned_users`
  ADD CONSTRAINT `fk_banned_user` FOREIGN KEY (`banned_user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_banning_user` FOREIGN KEY (`banning_user_id`) REFERENCES `users` (`id`);
--
-- Database: `chatSB`
--
CREATE DATABASE IF NOT EXISTS `chatSB` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `chatSB`;

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE `posts` (
  `id` int(11) NOT NULL,
  `meno` varchar(100) NOT NULL,
  `obsah` text NOT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`id`, `meno`, `obsah`, `user_id`) VALUES
(1, 'jano@pokec.sk', 'skusaisamidam', 1),
(2, 'jano@pokec.sk', 'moyno ano soad', 1),
(3, 'mato@pokec.sk', 'ide ide ide', 2),
(4, 'jano@pokec.sk', 'skuska', 1),
(5, 'jano@pokec.sk', 'skuska\r\n', 1),
(6, 'jano@pokec.sk', 'asksao\r\n', 1),
(7, 'mato@pokec.sk', 'asfaf', 2),
(8, 'jano@pokec.sk', 'skuska\r\n', 1),
(15, 'jano@pokec.sk', 'aaa\r\n', 1),
(16, 'jano@pokec.sk', 'aaa\r\n', 1),
(17, 'jano@pokec.sk', 'aaa\r\n', 1),
(18, 'admin', 'skusam#\r\n', 4),
(19, 'jano@pokec.sk', 'skaklnda\r\n', 1),
(20, 'jano@pokec.sk', 'adidscm', 1),
(21, 'admin', 'assda', 4),
(22, 'sara@pokec.sk', 'acadm\r\n', 3),
(23, 'sara@pokec.sk', 'ascavds', 3),
(24, 'mato@pokec.sk', 'nacna', 2),
(25, 'jano@pokec.sk', 'll\r\n', 1),
(26, 'jano@pokec.sk', 'kk', 1),
(27, 'jano@pokec.sk', 'kk', 1),
(28, 'ivan', 'elke', 5),
(29, 'jano@pokec.sk', 'kk', 1),
(30, 'ivan', 'm', 5),
(31, 'jano@pokec.sk', 'kk', 1),
(32, 'jano@pokec.sk', 'kk', 1),
(33, 'ivan', 'm', 5);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `mail` varchar(100) NOT NULL,
  `heslo` varchar(100) NOT NULL,
  `ban` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `mail`, `heslo`, `ban`) VALUES
(1, 'jano@pokec.sk', 'abcd', 1),
(2, 'mato@pokec.sk', 'abcd', 1),
(3, 'sara@pokec.sk', 'abcd', 0),
(4, 'admin', 'admin', 0),
(5, 'ivan', 'ivan', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `posts`
--
ALTER TABLE `posts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
--
-- Database: `db_app`
--
CREATE DATABASE IF NOT EXISTS `db_app` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `db_app`;

-- --------------------------------------------------------

--
-- Table structure for table `objednavky`
--

CREATE TABLE `objednavky` (
  `id_objednavka` int(11) NOT NULL,
  `id_zakaznici` int(11) NOT NULL,
  `id_tovar` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `objednavky`
--

INSERT INTO `objednavky` (`id_objednavka`, `id_zakaznici`, `id_tovar`, `datum`) VALUES
(5, 3, 5, '2023-11-08'),
(6, 4, 6, '2023-11-10'),
(10, 13, 9, '2022-06-23'),
(11, 13, 4, '2023-11-11'),
(12, 14, 10, '2021-12-12'),
(13, 12, 3, '2022-12-12'),
(15, 3, 9, '2019-01-31'),
(18, 2, 5, '2023-12-22'),
(27, 8, 8, '2023-12-06'),
(28, 21, 18, '2023-12-07');

-- --------------------------------------------------------

--
-- Table structure for table `tovar`
--

CREATE TABLE `tovar` (
  `id_tovar` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL,
  `cena` float NOT NULL,
  `hodnotenie` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `tovar`
--

INSERT INTO `tovar` (`id_tovar`, `nazov`, `cena`, `hodnotenie`) VALUES
(1, 'Chladnička', 450.55, 4.6),
(3, 'Monitor', 350, 4.5),
(4, 'Myš', 35, 4.2),
(5, 'Klávesnica', 133, 4.3),
(8, 'Televizia', 960.75, 4),
(9, 'Monitor', 110.5, 3.9),
(10, 'Okno', 155.5, 3.3),
(12, 'Radiátor ', 130.56, 4.8),
(13, 'Klima', 340, 4.4),
(14, 'Nabíjačka', 6.4, 3.2),
(15, 'Voda', 2.5, 5),
(17, 'Olej ', 35, 125),
(18, 'Voda', 4455, 412);

-- --------------------------------------------------------

--
-- Table structure for table `zakaznici`
--

CREATE TABLE `zakaznici` (
  `id_zakaznici` int(11) NOT NULL,
  `meno` varchar(20) NOT NULL,
  `ico` varchar(20) DEFAULT NULL,
  `adresa` varchar(70) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `zakaznici`
--

INSERT INTO `zakaznici` (`id_zakaznici`, `meno`, `ico`, `adresa`) VALUES
(1, 'Eva M', '678905432', 'Modrá 87 KKK'),
(2, 'Eva E', '', 'Fialová 45 Nitra'),
(3, 'Ivana I', '678905432', 'Nitrianksa 55 Nitra'),
(4, 'Karina K', '678905432', 'Oranžová 56 Malacky'),
(8, 'Ivana I', '[nemá ičo]	', 'Modrá 87 Košice'),
(11, 'Karol K', '', 'Sivá 55 Komjatice'),
(12, 'Alfonz A', '', 'Srechová 65 Semerovo'),
(13, 'Ivan I', '[nemá ičo]	', 'Kaskádová 82 Ivanka pri Nitre'),
(14, 'Mrtin M', '[nemá ičo]', 'Nova 33 Nitra'),
(19, 'Timotej T', '12345654', 'Vodova 55 Komárno'),
(20, 'sdad', 'sadad', 'adad'),
(21, 'sdad', '12345654', 'adad');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `objednavky`
--
ALTER TABLE `objednavky`
  ADD PRIMARY KEY (`id_objednavka`);

--
-- Indexes for table `tovar`
--
ALTER TABLE `tovar`
  ADD PRIMARY KEY (`id_tovar`);

--
-- Indexes for table `zakaznici`
--
ALTER TABLE `zakaznici`
  ADD PRIMARY KEY (`id_zakaznici`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `objednavky`
--
ALTER TABLE `objednavky`
  MODIFY `id_objednavka` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `tovar`
--
ALTER TABLE `tovar`
  MODIFY `id_tovar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `zakaznici`
--
ALTER TABLE `zakaznici`
  MODIFY `id_zakaznici` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- Database: `E_shop_AIS`
--
CREATE DATABASE IF NOT EXISTS `E_shop_AIS` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_shop_AIS`;

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` int(11) NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`) VALUES
(1, 'nohavice', 5, 18),
(2, 'ponožky', 150, 2),
(3, 'tvarohový jogurt', 16, 2),
(4, 'vianočný kapor', 4, 9);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `zlava` int(11) NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `poznamky` text CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `je_admin` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `zlava`, `meno`, `priezvisko`, `poznamky`, `je_admin`) VALUES
(1, 'jskalka@ukf.sk', '123', 'Zeleninova 4, Nitra', 20, 'Jan ', 'Skalka', 'tester', 0),
(2, 'jmrkva@ukf.sk', '123', 'Zahrada 11', 3, 'Jozef', 'Mrkva', 'druhý tester', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Database: `E_SHOP_AK`
--
CREATE DATABASE IF NOT EXISTS `E_SHOP_AK` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_SHOP_AK`;

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`) VALUES
(98, 10, 5, 60, 2),
(97, 10, 6, 160, 2);

-- --------------------------------------------------------

--
-- Table structure for table `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `obj_polozky`
--

INSERT INTO `obj_polozky` (`ID`, `ID_objednavky`, `ID_tovaru`, `cena`, `ks`) VALUES
(55, 35, 3, 40, 1),
(54, 35, 5, 60, 1),
(53, 34, 1, 450, 1),
(52, 1, 1, 450, 1),
(51, 1, 4, 585, 1),
(50, 1, 3, 45, 1),
(56, 36, 2, 20, 1),
(57, 37, 5, 75, 2),
(58, 38, 5, 75, 1),
(59, 44, 2, 20, 1),
(60, 45, 6, 200, 1),
(61, 45, 4, 650, 1),
(67, 46, 1, 500, 1),
(66, 46, 2, 20, 1),
(69, 51, 1, 500, 1),
(68, 49, 5, 75, 1),
(70, 52, 3, 50, 2),
(71, 53, 2, 20, 1),
(72, 54, 1, 500, 1),
(73, 54, 3, 50, 1),
(74, 56, 6, 200, 1);

-- --------------------------------------------------------

--
-- Table structure for table `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(36) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` int(11) NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `obj_zoznam`
--

INSERT INTO `obj_zoznam` (`ID`, `obj_cislo`, `datum_objednavky`, `ID_pouzivatela`, `suma`, `stav`) VALUES
(35, '1d77ba0abced43d38703ce8a2dd589e7', '2023-12-28', 10, 100, 'zaplatená'),
(34, 'c4913fe1f9ce48debd4dac76137f4bbf', '2023-12-28', 9, 450, 'odoslaná'),
(33, '252b975e265c43c58a89d881ffe863f6', '2023-12-28', 9, 1080, 'zaplatená'),
(50, '95ffd2b977d14cd4ae056e3737f27339', '2024-01-04', 11, 0, 'odoslaná'),
(49, 'b416665a278740fa9c09d907edaafe33', '2024-01-04', 11, 75, 'odoslaná'),
(48, '9f625e892efb4e1fa5e21cf3710b5ba3', '2024-01-01', 11, 520, 'odoslaná'),
(45, 'f61f00205cfd45a0a41ec5c60fac3973', '2023-12-31', 11, 850, 'odoslaná'),
(44, '3f18808327834c9d9cd143adf8624e43', '2023-12-31', 11, 20, 'odoslaná'),
(43, '8f4d489427914b91b35ca32322eaa62a', '2023-12-31', 11, 0, 'odoslaná'),
(51, '18a74370eddb4fc9a83b04c336849498', '2024-01-04', 11, 500, 'odoslaná'),
(52, '0a6cfcf5ed5641abafed0f59003f075b', '2024-01-04', 11, 100, 'odoslaná'),
(53, '5c496347055b4708bcb8d0394e194c5a', '2024-01-04', 11, 20, 'odoslaná'),
(54, 'a68c1207d675484c8c5edf5ebc9b4279', '2024-01-04', 11, 550, 'odoslaná'),
(55, '281972c8c4b247f2be5291373fcbcd20', '2024-01-04', 11, 0, 'odoslaná'),
(56, '719947d42df3494b8ac3c0e14152d171', '2024-01-04', 11, 200, 'odoslaná');

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `img` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`, `img`) VALUES
(1, 'Smartphone', 20, 500, 'https://source.unsplash.com/black-iphone-11-LEtrhrME07g'),
(2, 'Myš', 52, 20, 'https://source.unsplash.com/closeup-photo-of-gray-and-black-cordless-mouse-ZtxED1cpB1E'),
(3, 'Bezdrôtové slúchadlá', 3, 50, 'https://source.unsplash.com/apple-airpods-NuOGFo4PudE'),
(4, 'Fotoaparát', 2, 650, 'https://source.unsplash.com/black-nikon-dslr-camera-dcgB3CgidlU'),
(5, 'Slúchadlá', 75, 75, 'https://source.unsplash.com/black-and-silver-headphones-on-white-surface-LSNJ-pltdu8'),
(6, 'Hodinky', 2, 200, 'https://source.unsplash.com/space-gray-apple-watch-with-black-sports-band-hbTKIbuMmBI');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `zlava` int(11) NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `poznamky` text CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `je_admin` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `zlava`, `meno`, `priezvisko`, `poznamky`, `je_admin`) VALUES
(10, 'peter@ukf.sk', '123', 'Levická 9', 20, 'Peter', 'Sivý', '', 0),
(9, 'mikulas@ukf.sk', '123', 'Dlhá 2', 10, 'Mikuláš', 'Modrý', '', 0),
(8, 'admin@ukf.sk', '123', 'Štúrova 1', 0, 'Janko', 'Mrkvička', '', 1),
(11, 'ivan@ivan', 'ivan', 'ivan', 0, 'ivan', 'ivan', '', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- AUTO_INCREMENT for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Database: `E_SHOP_AL`
--
CREATE DATABASE IF NOT EXISTS `E_SHOP_AL` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_SHOP_AL`;

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` decimal(5,2) NOT NULL,
  `ks` int(11) NOT NULL,
  `Status` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`, `Status`) VALUES
(1, 5, 2, 10.00, 3, 'order'),
(25, 3, 2, 9.99, 3, 'payed'),
(26, 3, 5, 4.15, 1, 'payed'),
(23, 3, 3, 6.59, 1, 'payed'),
(22, 3, 5, 4.15, 1, 'payed'),
(41, 1, 1, 3.36, 1, 'order'),
(40, 1, 3, 6.59, 1, 'payed'),
(39, 1, 9, 4.73, 4, 'payed'),
(38, 1, 9, 4.73, 4, 'payed'),
(37, 1, 3, 6.59, 1, 'payed'),
(36, 1, 2, 9.99, 1, 'payed'),
(35, 1, 1, 3.36, 1, 'payed'),
(42, 1, 2, 9.99, 1, 'order'),
(43, 4, 1, 3.36, 1, 'payed'),
(44, 4, 2, 9.99, 4, 'payed'),
(45, 4, 1, 3.36, 1, 'payed'),
(46, 4, 2, 9.99, 1, 'payed'),
(47, 4, 1, 3.36, 20, 'payed'),
(48, 4, 1, 3.36, 20, 'payed');

-- --------------------------------------------------------

--
-- Table structure for table `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` int(11) NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `image` varchar(500) NOT NULL,
  `price` double(3,2) NOT NULL,
  `description` varchar(6000) NOT NULL,
  `discount` double(2,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `image`, `price`, `description`, `discount`) VALUES
(1, 'Ponáhl\'ajte sa a milujte', 'https://s4-goods.ozstatic.by/2000/996/549/10/10549996_0.jpg', 3.36, 'Tiché mestečko Beaufort. Každý rok sem prichádza Landon Carter, aby si pripomenul príbeh svojej prvej lásky... Príbeh vášne a nehy, ktorý pred rokmi spojil jeho, chlapca z bohatej rodiny, a Jamie Sullivanovú, skromnú dcéru miestneho pastora. Príbeh plný radosti a smútku, šťastia a bolesti. Príbeh o pocite, ktorý človek môže zažiť len raz za život - a pamätať si ho navždy.', 0.00),
(2, 'Kalasi pod serpom tvaim (BY)', 'https://s3-page.ozstatic.by/1000/624/556/10/10556624_14.jpg', 9.99, 'Uladzimir Karatkevič je honra a svedomie bieľoruskej literatúry. Jeho dielo \"Kalasy pad kosákom tvaim\" bolo v roku 2016 uznané čitateľmi za najdôležitejšiu knihu, ktorá ovplyvnila rozvoj Bieľorusov ako národa. Román opisuje formovanie bieľoruského národa na príklade šľachtickej rodiny Zahorských. Udalosti sa odohrávajú na Mahiľoŭščyne, Hrodzienščyne, vo Vilniuse, Petrohrade, Moskve, Varšave - vo vidieckom prostredí a v dvoranských palácoch, na študentských zhromaždeniach a vzdelávacích inštitúciách, v salónoch a hostincoch vyšších úradníkov a na uliciach mnohých mestskej oblasti.\r\n\r\nV knihe pôsobia ako literárne postavy vytvorené umeleckou fantáziou, tak aj reálne historické postavy - K. Kalinovský, Z. Serakouský, V. Dunin-Martinkevič, S. Manjuška, T. Šaučenka, U. Syrakomľa.\r\n\r\nV knihe vystupujú literárne postavy zrodené z umeleckej fantázie aj skutočné historické postavy - K. Kalinovskij, Z. Serakovskij, V. Dunin-Martsinkevič, S. Manyushka, T. Shavchenko, U. Syrakomlia.', 0.00),
(3, 'Shach a mat', 'https://s1-goods.ozstatic.by/2000/101/273/101/101273101_0.jpg', 6.59, 'Mellori Greenlif skončila so šachmi. Po tom, čo pred štyrmi rokmi šachy zničili jej rodinu, sa sústredila na mamu, sestry a perspektívne nevyzerajúcu prácu, ktorá jej pomáha vyjsť s koncami. Ale raz Mellori neochotne súhlasí zahrať si v charitatívnom turnaji a náhodou rozdrví \"vraha kráľov\" Nolana Soyera - súčasného majstra sveta a \"zlého chlapca\" v svete šachov.\r\n\r\nSojerov prehra neznámeho nováčika šokuje všetkých. Čo šokuje ešte viac? Jeho túžba sa oplatiť. Pre Mellori by bolo najrozumnejšie akceptovať, že hra skončila, a ísť preč. Ale táto víťazstvo otvára dvere k veľmi potrebnému finančnému víťazstvu, a napriek všetkému Mellori cíti nevysvetliteľnú príťažlivosť k svojmu záhadnému súperovi.\r\n\r\nVstupujúc do veľkej hry, Mellori sa všetkými silami snaží ochrániť svoju rodinu pred tým, čo ju kedysi zničilo. Ale s každým obnovením jej lásky k šachom, ktoré sa snažila nenávidieť, Mellori si uvedomuje, že hra sa odohráva nielen na šachovnici, svietidlá svietia jasnejšie, než si predstavovala, a jej súper nie je len veľmi múdry a diabolsky príťažlivý, ale aj krutý.', 0.00),
(4, 'Portrét Doriana Graye', 'https://s2-goods.ozstatic.by/2000/203/642/10/10642203_0.jpg', 2.78, '\"Portrét Doriana Graye\" je najznámejším dielom Oscara Wilda, jeho jediným románom, ktorý v čase svojho vzniku vyvolal búrlivé negatívne reakcie, no napriek tomu dosiahol neuveriteľný úspech.\r\n\r\nHlavná postava románu, krásaviec Dorian, je dvojznačnou a nejednoznačnou postavou. Jemný estét a romantik sa stáva neľútostným zločincom. Pokus uchovať svoju nezvyčajnú krásu a mladosť končí neúspechom. Namiesto toho, aby starnul hrdina, stárne jeho portrét - no to nemôže trvať večne, a smrť Doriana všetko spraví do poriadku.\r\n\r\nRomán Oscara Wilda zostáva veľmi aktuálny aj dnes - či prenasledovanie večnej mladosti niekedy nevedie k strate svojej pravej tváre?', 0.00),
(5, 'Traja sudruhovia', 'https://s5-goods.ozstatic.by/2000/108/887/10/10887108_0.jpg', 4.15, 'Najkrajší milostný príbeh dvadsiateho storočia...\r\n\r\nNajfascinujúcejší román o priateľstve dvadsiateho storočia...\r\n\r\nNajtragickejší a najpálčivejší román o ľudských vzťahoch v celých dejinách dvadsiateho storočia.', 0.00),
(6, 'Vít\'azný obluk', 'https://s4-goods.ozstatic.by/2000/666/625/10/10625666_0.jpg', 4.24, '„Arc de Triomphe“ je dojímavý príbeh lásky napriek všetkému, lásky, ktorá prináša bolesť, ale aj nekonečnú radosť.\r\n\r\nDejiskom je Paríž v predvečer druhej svetovej vojny. Hrdina je utečenec z Nemecka, bez dokladov, skrývajúci sa pred Francúzmi aj nacistami, chirurg zachraňujúci životy. Hrdinka je talianska herečka, obklopená fanúšikmi, temperamentná, ako všetci umelci, krásna a neodolateľná.\r\n\r\nA čas, keď sa milenci náhodou stretli, a mesto presiaknuté pocitom blížiacej sa katastrofy sa stali hrdinami tohto románu.\r\n\r\n„Arc de Triomphe\" bol sfilmovaný dvakrát a stále uchvacuje čitateľov na celom svete. „Arc de Triomphe\" je dojímavý príbeh lásky proti všetkým prekážkam, lásky, ktorá prináša bolesť, ale aj nekonečnú radosť.\r\n\r\nDejiskom je Paríž v predvečer druhej svetovej vojny. Hrdina je utečenec z Nemecka, bez dokladov, skrývajúci sa pred Francúzmi aj nacistami, chirurg zachraňujúci životy. Hrdinka je talianska herečka, obklopená fanúšikmi, temperamentná, ako všetci umelci, krásna a neodolateľná.\r\n\r\nA čas, keď sa milenci náhodou stretli, a mesto presiaknuté pocitom blížiacej sa katastrofy sa stali hrdinami tohto románu.\r\n\r\nVíťazný oblúk bol sfilmovaný dvakrát a naďalej uchvacuje čitateľov po celom svete.', 0.00),
(7, 'Vo vnutri vraha', 'https://s5-goods.ozstatic.by/2000/139/899/10/10899139_0.jpg', 6.84, 'Profiler... Kriminálny psychológ, doslova založený na niekoľkých malých detailoch, schopný obnoviť vzhľad a spôsob konania toho najprefíkanejšieho zločinca. Títo ľudia vyzerajú zvonku ako čarodejníci, ako superhrdinovia. Najmä ak je profilárkou žena... Na moste v Chicagu, opretá o zábradlie, stojí mladá krásna žena. Veľmi bledý a veľmi smutný. Nehybne hľadí na tmavú vodu a dlaňou si zakrýva plačúce oči. A nikoho nenapadne, že... JE MŔTVA.\r\n\r\nNa moste stojí telo uškrtenej ženy, nabalzamované špeciálnou zmesou, ktorá umožňuje mŕtvole zaujať akúkoľvek pózu. Naozaj diabolská fantázia. Ešte horšie však je, že už boli nájdené tri také telá, ktoré smútia nad vlastnou smrťou. V meste sa objavil SÉRIOVÝ VRAH. Vyšetrovanie vedie chicagská polícia, ale FBI miestnemu profilovačovi neverí, pretože ho považuje za nekompetentného. Na takýto zložitý prípad má Bureau svoju špecialistku – Zoe Bentley. Je najlepšia z najlepších. Predovšetkým preto, že raz, pred mnohými rokmi, som sa osobne stretol so sériovým vrahom... A ZOSTAL NAŽIVO...', 0.00),
(8, 'KARMALOGIC', 'https://s5-goods.ozstatic.by/2000/99/674/10/10674099_0.jpg', 9.99, 'Legendárny konzultant a psychoterapeut Alexey Sitnikov touto knihou spúšťa jedinečný medzinárodný crowdsourcingový projekt na vytvorenie moderného súboru univerzálnych zákonov života, ktoré môžu ovplyvniť osud, varovať pred možnými chybami pri interakcii s prírodným svetom a ľuďmi a zároveň slúžiť na harmonizáciu vnútorný svet človeka.\r\n\r\nČitateľ sa bude môcť nielen zapojiť do diskusie o znení, miere dôležitosti a spôsoboch aplikácie 54 zákonov osudu uvedených v knihe (získaných ako výsledok diskusií na početných seminároch, školeniach, koučingových/psychoterapeutických sedeniach). , internetové fóra), ale aj posielať vlastné životné príbehy potvrdzujúce ich pôsobenie, vytvárať si vlastný zoznam prijatých a dodržiavaných zákonov, ale aj formulovať a navrhovať nové zákony do všeobecnej diskusie a stať sa tak účastníkom pôsobivého sociálneho výskumu, ktorý môže ovplyvniť harmonizácia moderného turbulentného sveta, zlepšenie vzájomného porozumenia medzi ľuďmi rôznych kultúr a náboženstiev, vekových a profesijných sociálnych skupín, svetonázorov a politických systémov.\r\n\r\nKniha je určená širokému okruhu čitateľov a originálna metodika a unikátne výsledky zaujmú odborníkov z oblasti psychológie, sociológie, pedagogiky, politológie a marketingu.', 0.00),
(9, 'Vol\'ba. O slobode a vnútornej sile cloveka', 'https://s4-goods.ozstatic.by/2000/37/122/101/101122037_0.jpg', 4.73, 'Jedna z najsilnejších kníh o vojne a vnútornej sile človeka.\r\n\r\nV roku 1944 bola šestnásťročná baletka Edith Eger poslaná s rodinou do Osvienčimu. Len niekoľko hodín po zabití jej rodičov prinútil nacistický lekár Joseph Mengele Edith tancovať pre svoje vlastné pobavenie a prežitie. Edith a jej sestra prežili všetky hrôzy Osvienčimu, Mauthausenu a Gunskirchenu – tábory smrti. 4. mája 1945 sotva živú Edith vytiahli z kopy mŕtvol.\r\n\r\nMučenie, hlad a neustála hrozba smrti Edith nezlomili a jej vnútorný svet jej pomohol získať silu a duchovnú slobodu. 35 rokov po skončení vojny sa Edith stala slávnou psychologičkou a vrátila sa do Osvienčimu, aby sa zbavila spomienok na minulosť a viny tých, ktorí prežili. Edith strieda udalosti zo svojej osobnej cesty s dojímavými príbehmi tých, ktorým sama pomohla vyzdravieť.\r\n\r\nTáto kniha je nezabudnuteľným príbehom o prežití a uzdravení, príbehom o oslobodení a sile ľudského ducha. Ukazuje, že vždy si môžeme vybrať, čo nás život učí a ako sa k tomu, čo sa deje, postaviť. Toto je kniha, ktorá zmení životy a posilní generácie čitateľov.', 0.00),
(10, 'Boh vzdy cestuje inkognito', 'https://s2-goods.ozstatic.by/2000/592/756/10/10756592_0.jpg', 6.09, 'Kniha „Boh vždy cestuje inkognito“ od Laurenta Gounela, jedného z piatich najpopulárnejších spisovateľov beletrie vo Francúzsku, sa stala celosvetovým bestsellerom, niekoľko rokov bola na popredných priečkach vo Francúzsku a bola preložená do mnohých jazykov.\r\n\r\nPredstavte si: ste na okraji priepasti. A v tejto osudnej chvíli vám istá osoba zachráni život. Na oplátku mu dávate povinnosť dodržiavať všetky jeho pokyny. To by malo zmeniť váš život, urobiť ho radostnejším a šťastnejším. Viac ako román je to sebareflexia, ktorá by vás mala povzbudiť k tomu, aby ste vzali osud do vlastných rúk.', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`) VALUES
(1, 'nohavice', 5, 18),
(2, 'ponožky', 150, 2),
(3, 'tvarohový jogurt', 16, 2),
(4, 'vianočný kapor', 4, 9);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `zlava` int(11) NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `poznamky` text CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `je_admin` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `zlava`, `meno`, `priezvisko`, `poznamky`, `je_admin`) VALUES
(1, 'jskalka@ukf.sk', '123', 'Zeleninova 4, Nitra', 20, 'Jan ', 'Skalka', 'tester', 0),
(2, 'jmrkva@ukf.sk', '123', 'Zahrada 11', 3, 'Jozef', 'Mrkva', 'druhý tester', 0),
(3, 'a@a.a', '111', 'Pupkina,11', 10, 'A', 'A', '', 0),
(4, 'ivan@ivan', 'ivan', 'i', 0, 'ivan', 'ivan', '', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Database: `E_shop_AP`
--
CREATE DATABASE IF NOT EXISTS `E_shop_AP` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_shop_AP`;

-- --------------------------------------------------------

--
-- Table structure for table `ban`
--

CREATE TABLE `ban` (
  `id` int(11) NOT NULL,
  `dovod` varchar(50) NOT NULL,
  `id_usera` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL,
  `link` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id`, `nazov`, `link`) VALUES
(1, 'background', 'https://i.postimg.cc/BQQf57C7/Background.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `kategorie`
--

CREATE TABLE `kategorie` (
  `id` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategorie`
--

INSERT INTO `kategorie` (`id`, `nazov`) VALUES
(1, 'Dobrodružné'),
(2, 'Detektívne'),
(3, 'Fantazy'),
(4, 'Sci-Fi');

-- --------------------------------------------------------

--
-- Table structure for table `knihy`
--

CREATE TABLE `knihy` (
  `id` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `seria` varchar(50) DEFAULT NULL,
  `cena` double NOT NULL,
  `mnozstvo` int(11) NOT NULL,
  `img` varchar(255) NOT NULL,
  `id_kategorie` int(11) NOT NULL,
  `popis` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `knihy`
--

INSERT INTO `knihy` (`id`, `nazov`, `author`, `seria`, `cena`, `mnozstvo`, `img`, `id_kategorie`, `popis`) VALUES
(1, 'Silmarillion', 'JRR Tolkien', '', 25.9, 0, 'https://i.postimg.cc/cJ8SgKcs/Silmarillion-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(2, '20000 Míl pod morom', 'Jules Verne', '', 12, 5, 'https://i.postimg.cc/50LbGHHP/20000-mil-pod-morom-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(3, 'Cesta do stredu zeme', 'Jules Verne', '', 14.5, -1, 'https://i.postimg.cc/J08MW1ZR/cesta-do-stredu-zeme-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(4, 'Karibské Tajomstvo', 'Agatha Christie', '', 10.9, 11, 'https://i.postimg.cc/Vkv1wZr0/Karibske-Tajomstvo-BOOK.jpg', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(5, 'Vražda v Orient Expresse', 'Agatha Christie', '', 18.9, 0, 'https://i.postimg.cc/tC2yLGTH/Murder-on-orient-express-BOOK.jpg', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(6, 'Meč osudu', 'Andrzej Sapkowski', 'The Witcher', 22, 2, 'https://i.postimg.cc/B69Gxqh2/The-Witcher-Sword-of-destiny-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(7, 'Posledné želanie', 'Andrzej Sapkowski', 'The Witcher', 22, 1, 'https://i.postimg.cc/j2R0FMY5/The-Witcher-The-last-wish.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(8, 'Spoločenstvo prsteňa', 'JRR Tolkien', 'Lord of the rings', 18, 0, 'https://i.postimg.cc/hPScytKr/Lotr-tfotr-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(9, 'Dve veže', 'JRR Tolkien', 'Lord of the rings', 18, 0, 'https://i.postimg.cc/Vs9zWCPv/lotr-ttt-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i'),
(10, 'Návrat krála', 'JRR Tolkien', 'Lord of the rings', 18, 1, 'https://i.postimg.cc/5ym14ZSL/lotr-trotk-BOOK.jpg', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor i');

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `id` int(11) NOT NULL,
  `id_polozky` int(11) NOT NULL,
  `id_usera` int(11) NOT NULL,
  `cena` double NOT NULL,
  `mnozstvo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `objednavky`
--

CREATE TABLE `objednavky` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `cena` double NOT NULL,
  `datum` date NOT NULL,
  `stav` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `objednavky`
--

INSERT INTO `objednavky` (`id`, `id_user`, `cena`, `datum`, `stav`) VALUES
(17, 4, 66, '2023-12-25', 'zaplatená'),
(18, 4, 84, '2023-12-25', 'odoslaná'),
(20, 4, 21.8, '2023-12-25', 'zaplatená'),
(21, 5, 10.9, '2023-12-25', 'spracuje sa'),
(22, 4, 30.9, '2023-12-25', 'zaplatená'),
(25, 4, 32.9, '2023-12-26', 'spracuje sa'),
(26, 5, 141.6, '2023-12-26', 'spracuje sa'),
(27, 1, 54, '2023-12-31', 'spracuje sa');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `priezvisko` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `heslo` varchar(50) NOT NULL,
  `adresa` varchar(50) NOT NULL,
  `zlava` int(10) NOT NULL,
  `poznamky` varchar(255) NOT NULL,
  `admin` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `meno`, `priezvisko`, `email`, `heslo`, `adresa`, `zlava`, `poznamky`, `admin`) VALUES
(1, 'Adrian', 'Pazicky', 'adrian.pazicky@student.ukf.sk', '1234', 'Gastanova 653, Chalmova', 80, 'best admin eva', 1),
(4, 'Adko', 'Paz', 'adrian1pazicky@gmail.com', '12345', 'Gastankova', 0, '', 1),
(5, 'Matus', 'Fekete', 'matus.fekete@gmail.com', '123', 'feketeova 6', 0, '', 0),
(6, 'Janko', 'Hrasko', 'j.hrasko@student.ukf.sk', '159', 'Nitrianska, 94', 0, '', 0),
(7, 'Ivan', 'Ivan', 'ivan@ivan', 'Ivan12345*', 'Ivan', 0, '', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ban`
--
ALTER TABLE `ban`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kategorie`
--
ALTER TABLE `kategorie`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `knihy`
--
ALTER TABLE `knihy`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `objednavky`
--
ALTER TABLE `objednavky`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ban`
--
ALTER TABLE `ban`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `kategorie`
--
ALTER TABLE `kategorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `knihy`
--
ALTER TABLE `knihy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `objednavky`
--
ALTER TABLE `objednavky`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- Database: `E_SHOP_MK`
--
CREATE DATABASE IF NOT EXISTS `E_SHOP_MK` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_SHOP_MK`;

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` double NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`) VALUES
(51, 3, 7, 0.16, 1);

-- --------------------------------------------------------

--
-- Table structure for table `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` double NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `obj_polozky`
--

INSERT INTO `obj_polozky` (`ID`, `ID_objednavky`, `ID_tovaru`, `cena`, `ks`) VALUES
(55, 15, 12, 0.49, 3),
(54, 14, 12, 0.49, 3),
(53, 14, 5, 0.13, 1),
(52, 14, 7, 0.16, 1),
(51, 14, 9, 0.39, 1);

-- --------------------------------------------------------

--
-- Table structure for table `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` double NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(100) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` double NOT NULL,
  `img` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`, `img`) VALUES
(5, 'Rozok', 919, 0.13, 'https://secure.ce-tescoassets.com/assets/SK/070/8584126829070/ShotType1_540x540.jpg'),
(7, 'Kajzerka cereálna 60g', 76, 0.16, 'https://secure.ce-tescoassets.com/assets/SK/490/0000000029490/ShotType1_540x540.jpg'),
(9, 'Bageta francúzska 110g', 19, 0.39, 'https://secure.ce-tescoassets.com/assets/SK/434/218434/ShotType1_225x225.jpg'),
(13, 'Donut čokoládový 55g', 0, 0.45, 'https://secure.ce-tescoassets.com/assets/SK/377/218377/ShotType1_225x225.jpg'),
(12, 'Oškvarkový pagáč 71g', 61, 0.49, 'https://secure.ce-tescoassets.com/assets/SK/733/8588007962733/ShotType1_540x540.jpg'),
(14, 'Bageta drevorubačská 150g', 9, 0.99, 'https://secure.ce-tescoassets.com/assets/SK/724/0000000027724/ShotType1_225x225.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `users`
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
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `meno`, `priezvisko`, `admin`) VALUES
(1, 'mkopac@ukf.sk', '123', 'Andovska 1', 'Martin', 'Kopac', 0),
(2, 'admin@ukf.sk', '123', 'Tesco', 'Nick', 'Cage', 1),
(3, 'vstolicka@ukf.sk', '123', 'Stolickova 52', 'Vlastimil', 'Stolicka', 0),
(4, 'ivan@ivan', 'ivan', 'ivan', 'ivan', 'ivan', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- Database: `E_SHOP_TG`
--
CREATE DATABASE IF NOT EXISTS `E_SHOP_TG` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `E_SHOP_TG`;

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`id`, `user_id`, `stock_id`, `price`, `count`) VALUES
(132, 5, 3, 500655, 1);

-- --------------------------------------------------------

--
-- Table structure for table `ordered_items`
--

CREATE TABLE `ordered_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `stock_id` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ordered_items`
--

INSERT INTO `ordered_items` (`id`, `order_id`, `stock_id`, `price`, `count`) VALUES
(26, 32, 1, 10, 1),
(27, 33, 1, 10, 1),
(28, 34, 3, 500655, 1),
(29, 34, 1, 125255, 1),
(30, 36, 1, 125255, 9);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `order_num` varchar(64) NOT NULL,
  `date` date NOT NULL,
  `user_id` int(11) NOT NULL,
  `price_total` int(11) NOT NULL,
  `state` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `order_num`, `date`, `user_id`, `price_total`, `state`) VALUES
(32, 'c68af7a6-c3fd-4f7c-acfe-71bb757dd256', '2023-12-30', 1, 10, 'Delivered'),
(33, 'cdab04b7-20ae-4641-8693-e31377189e92', '2023-12-30', 1, 10, 'Prepairing'),
(34, '5adec38b-580f-4ff5-a9d1-d8ac9141915e', '2023-12-31', 1, 625910, 'Delivered'),
(35, 'b899ff6e-9123-4195-b14b-72e766e89f25', '2024-01-05', 5, 0, 'processing'),
(36, 'dd88294d-ed68-45f5-ba25-26354d31a855', '2024-01-05', 5, 125255, 'processing');

-- --------------------------------------------------------

--
-- Table structure for table `storage`
--

CREATE TABLE `storage` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `count` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `storage`
--

INSERT INTO `storage` (`id`, `name`, `count`, `price`, `image`) VALUES
(1, 'Lambo', 0, 125255, 'https://hips.hearstapps.com/hmg-prod/images/dw-burnett-pcoty22-8260-1671143390.jpg?crop=0.668xw:1.00xh;0.184xw,0&resize=640:*'),
(2, 'Bmw', 17, 32320, 'https://www.topgear.com/sites/default/files/2022/07/13.jpg'),
(3, 'Ferrari testarosa', 24, 500655, 'https://media.evo.co.uk/image/private/s--lxQJSHYz--/v1601996755/evo/2020/10/Ferrari%20Testarossa%20triple%20test-11.jpg'),
(4, 'Audi rs7', 0, 120000, 'https://i.ytimg.com/vi/jwC061bKG2Y/maxresdefault.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `address` varchar(255) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `description` varchar(255) NOT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `address`, `first_name`, `last_name`, `description`, `is_admin`) VALUES
(1, 'jozko@ukf.sk', '123', 'adresa', 'Jožo', 'Mrkva', '', 1),
(2, 'feri@f.sk', '123', 'feriho adresa', 'Fero', 'Malý', '', 0),
(5, 'ivan@ivan', 'Ivan12345*', 'ivan', 'ivan', 'ivan', '', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ordered_items`
--
ALTER TABLE `ordered_items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `storage`
--
ALTER TABLE `storage`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=133;

--
-- AUTO_INCREMENT for table `ordered_items`
--
ALTER TABLE `ordered_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `storage`
--
ALTER TABLE `storage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- Database: `objednavky`
--
CREATE DATABASE IF NOT EXISTS `objednavky` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `objednavky`;

-- --------------------------------------------------------

--
-- Table structure for table `objednavka`
--

CREATE TABLE `objednavka` (
  `id_objednavky` int(11) NOT NULL,
  `id_zakaznika` int(11) NOT NULL,
  `id_tovaru` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `objednavka`
--

INSERT INTO `objednavka` (`id_objednavky`, `id_zakaznika`, `id_tovaru`, `datum`) VALUES
(1, 1, 3, '2023-02-25'),
(2, 1, 2, '2023-04-02'),
(4, 11, 4, '2018-12-22'),
(5, 2, 3, '2023-12-02'),
(6, 3, 1, '2023-12-02'),
(7, 9, 7, '2023-12-02'),
(11, 3, 3, '2023-12-05');

-- --------------------------------------------------------

--
-- Table structure for table `tovar`
--

CREATE TABLE `tovar` (
  `id_tovaru` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL,
  `cena` double NOT NULL,
  `hodnotenie` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tovar`
--

INSERT INTO `tovar` (`id_tovaru`, `nazov`, `cena`, `hodnotenie`) VALUES
(1, 'Monitor Asus', 342, 4.8),
(2, 'PowerBank Xiaomi', 24.6, 4.9),
(3, 'Notebook Lenovo', 860, 4.9),
(4, 'Podlozka Razorcek', 12, 3.8),
(6, 'Sluchadla JBL', 56.4, 4.2),
(7, 'Telefon Redmi', 260, 5),
(8, 'Klavesnica Logitech', 46, 4),
(9, 'Mikrofon Rode', 65, 4.1),
(10, 'kmkm', 468, 6846);

-- --------------------------------------------------------

--
-- Table structure for table `zakaznici`
--

CREATE TABLE `zakaznici` (
  `id_zakaznika` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `adresa` varchar(50) NOT NULL,
  `ico` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `zakaznici`
--

INSERT INTO `zakaznici` (`id_zakaznika`, `meno`, `adresa`, `ico`) VALUES
(1, 'Adrian', 'Gastanova 656', '26518668'),
(2, 'Peder', 'Gastanova 646', '15684987'),
(3, 'Andrej', 'Hlavna 31', '19898458'),
(8, 'Peter', 'Bosakova 5', '64898654'),
(9, 'Frantisek', 'Jankova 34', '16586875'),
(12, '', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `objednavka`
--
ALTER TABLE `objednavka`
  ADD PRIMARY KEY (`id_objednavky`),
  ADD UNIQUE KEY `id` (`id_objednavky`),
  ADD UNIQUE KEY `id_3` (`id_objednavky`),
  ADD KEY `id_2` (`id_objednavky`);

--
-- Indexes for table `tovar`
--
ALTER TABLE `tovar`
  ADD PRIMARY KEY (`id_tovaru`);

--
-- Indexes for table `zakaznici`
--
ALTER TABLE `zakaznici`
  ADD PRIMARY KEY (`id_zakaznika`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `objednavka`
--
ALTER TABLE `objednavka`
  MODIFY `id_objednavky` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tovar`
--
ALTER TABLE `tovar`
  MODIFY `id_tovaru` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `zakaznici`
--
ALTER TABLE `zakaznici`
  MODIFY `id_zakaznika` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- Database: `objednavkyMR`
--
CREATE DATABASE IF NOT EXISTS `objednavkyMR` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `objednavkyMR`;

-- --------------------------------------------------------

--
-- Table structure for table `objednavky`
--

CREATE TABLE `objednavky` (
  `id` int(11) NOT NULL,
  `zakaznici_id` int(11) NOT NULL,
  `tovar_id` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `objednavky`
--

INSERT INTO `objednavky` (`id`, `zakaznici_id`, `tovar_id`, `datum`) VALUES
(11, 2, 4, '2023-12-02'),
(12, 1, 1, '2023-08-16');

-- --------------------------------------------------------

--
-- Table structure for table `tovar`
--

CREATE TABLE `tovar` (
  `id` int(11) NOT NULL,
  `nazov` varchar(50) NOT NULL,
  `cena` int(11) NOT NULL,
  `hodnotenie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tovar`
--

INSERT INTO `tovar` (`id`, `nazov`, `cena`, `hodnotenie`) VALUES
(1, 'voda', 2, 5),
(4, 'kava', 4, 5),
(5, 'chleba', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `zakaznici`
--

CREATE TABLE `zakaznici` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `ico` int(11) NOT NULL,
  `adresa` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `zakaznici`
--

INSERT INTO `zakaznici` (`id`, `meno`, `ico`, `adresa`) VALUES
(1, 'Michal', 1234567890, 'Velke Rovne 55'),
(2, 'Peter', 1234567891, 'Velke Rovne 45');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `objednavky`
--
ALTER TABLE `objednavky`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tovar`
--
ALTER TABLE `tovar`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `zakaznici`
--
ALTER TABLE `zakaznici`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `objednavky`
--
ALTER TABLE `objednavky`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `tovar`
--
ALTER TABLE `tovar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `zakaznici`
--
ALTER TABLE `zakaznici`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- Database: `objektove-technologie`
--
CREATE DATABASE IF NOT EXISTS `objektove-technologie` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `objektove-technologie`;

-- --------------------------------------------------------

--
-- Table structure for table `objednany_tovar`
--

CREATE TABLE `objednany_tovar` (
  `id` int(11) NOT NULL,
  `id_tovar` int(11) NOT NULL,
  `id_zakaznik` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `objednany_tovar`
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
-- Table structure for table `pohyby_uctu`
--

CREATE TABLE `pohyby_uctu` (
  `id` int(11) NOT NULL,
  `typ` enum('prijem','vydavok') NOT NULL,
  `suma` decimal(10,2) NOT NULL,
  `popis` varchar(255) DEFAULT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pohyby_uctu`
--

INSERT INTO `pohyby_uctu` (`id`, `typ`, `suma`, `popis`, `date`) VALUES
(2, 'prijem', 1000.00, 'za kavu', '2023-11-08'),
(7, 'vydavok', 8900.00, 'kupa auta', '2023-11-30'),
(9, 'prijem', 3500.00, 'Vyplata', '2023-11-15');

-- --------------------------------------------------------

--
-- Table structure for table `priestupky`
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
-- Dumping data for table `priestupky`
--

INSERT INTO `priestupky` (`id`, `meno`, `priezvisko`, `popis_priestupku`, `datum`, `suma`) VALUES
(1, 'jozef', 'jaros', 'rychlost', '2023-05-21', 10),
(2, 'samuel', 'Macek', 'svetla', '2023-11-22', 20),
(3, 'simon', 'jerabek', 'svetielka', '2023-11-11', 500),
(6, 'Janko', 'Mrkvicka', 'idk', '2023-11-15', 10),
(7, 'Ferko', 'Mrkva', 'netusim', '2023-10-31', 155);

-- --------------------------------------------------------

--
-- Table structure for table `tovar`
--

CREATE TABLE `tovar` (
  `id` int(11) NOT NULL,
  `nazov` varchar(100) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `hodnotenie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tovar`
--

INSERT INTO `tovar` (`id`, `nazov`, `cena`, `hodnotenie`) VALUES
(1, 'Topankyy', 5555.00, 9),
(2, 'Bunda', 200.00, 10),
(3, 'Tricko', 15.00, 6),
(5, 'testovaciKTORYNEMAVYMAZAT', 99.00, 10),
(7, 'Rukaivce', 20.00, 6);

-- --------------------------------------------------------

--
-- Table structure for table `zakaznici`
--

CREATE TABLE `zakaznici` (
  `id` int(11) NOT NULL,
  `meno` varchar(50) NOT NULL,
  `priezvisko` varchar(50) NOT NULL,
  `ico` varchar(8) NOT NULL,
  `adresa` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `zakaznici`
--

INSERT INTO `zakaznici` (`id`, `meno`, `priezvisko`, `ico`, `adresa`) VALUES
(1, 'Marekk', 'Jaross', '12345678', 'Ziar'),
(2, 'Jozko', 'Mrkvicka', '09445678', 'Bratislava'),
(7, 'Ferko', 'Viler', '12312313', 'Topolcany wpdcnwpinc');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `objednany_tovar`
--
ALTER TABLE `objednany_tovar`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pohyby_uctu`
--
ALTER TABLE `pohyby_uctu`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `priestupky`
--
ALTER TABLE `priestupky`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tovar`
--
ALTER TABLE `tovar`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `zakaznici`
--
ALTER TABLE `zakaznici`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `objednany_tovar`
--
ALTER TABLE `objednany_tovar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `pohyby_uctu`
--
ALTER TABLE `pohyby_uctu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `priestupky`
--
ALTER TABLE `priestupky`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tovar`
--
ALTER TABLE `tovar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `zakaznici`
--
ALTER TABLE `zakaznici`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- Database: `phpmyadmin`
--
CREATE DATABASE IF NOT EXISTS `phpmyadmin` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `phpmyadmin`;

-- --------------------------------------------------------

--
-- Table structure for table `pma__bookmark`
--

CREATE TABLE `pma__bookmark` (
  `id` int(11) NOT NULL,
  `dbase` varchar(255) NOT NULL DEFAULT '',
  `user` varchar(255) NOT NULL DEFAULT '',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `query` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Bookmarks';

-- --------------------------------------------------------

--
-- Table structure for table `pma__central_columns`
--

CREATE TABLE `pma__central_columns` (
  `db_name` varchar(64) NOT NULL,
  `col_name` varchar(64) NOT NULL,
  `col_type` varchar(64) NOT NULL,
  `col_length` text DEFAULT NULL,
  `col_collation` varchar(64) NOT NULL,
  `col_isNull` tinyint(1) NOT NULL,
  `col_extra` varchar(255) DEFAULT '',
  `col_default` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Central list of columns';

-- --------------------------------------------------------

--
-- Table structure for table `pma__column_info`
--

CREATE TABLE `pma__column_info` (
  `id` int(5) UNSIGNED NOT NULL,
  `db_name` varchar(64) NOT NULL DEFAULT '',
  `table_name` varchar(64) NOT NULL DEFAULT '',
  `column_name` varchar(64) NOT NULL DEFAULT '',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `mimetype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `transformation` varchar(255) NOT NULL DEFAULT '',
  `transformation_options` varchar(255) NOT NULL DEFAULT '',
  `input_transformation` varchar(255) NOT NULL DEFAULT '',
  `input_transformation_options` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Column information for phpMyAdmin';

-- --------------------------------------------------------

--
-- Table structure for table `pma__designer_settings`
--

CREATE TABLE `pma__designer_settings` (
  `username` varchar(64) NOT NULL,
  `settings_data` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Settings related to Designer';

--
-- Dumping data for table `pma__designer_settings`
--

INSERT INTO `pma__designer_settings` (`username`, `settings_data`) VALUES
('root', '{\"relation_lines\":\"true\",\"angular_direct\":\"direct\",\"snap_to_grid\":\"off\"}');

-- --------------------------------------------------------

--
-- Table structure for table `pma__export_templates`
--

CREATE TABLE `pma__export_templates` (
  `id` int(5) UNSIGNED NOT NULL,
  `username` varchar(64) NOT NULL,
  `export_type` varchar(10) NOT NULL,
  `template_name` varchar(64) NOT NULL,
  `template_data` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Saved export templates';

-- --------------------------------------------------------

--
-- Table structure for table `pma__favorite`
--

CREATE TABLE `pma__favorite` (
  `username` varchar(64) NOT NULL,
  `tables` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Favorite tables';

-- --------------------------------------------------------

--
-- Table structure for table `pma__history`
--

CREATE TABLE `pma__history` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `username` varchar(64) NOT NULL DEFAULT '',
  `db` varchar(64) NOT NULL DEFAULT '',
  `table` varchar(64) NOT NULL DEFAULT '',
  `timevalue` timestamp NOT NULL DEFAULT current_timestamp(),
  `sqlquery` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='SQL history for phpMyAdmin';

-- --------------------------------------------------------

--
-- Table structure for table `pma__navigationhiding`
--

CREATE TABLE `pma__navigationhiding` (
  `username` varchar(64) NOT NULL,
  `item_name` varchar(64) NOT NULL,
  `item_type` varchar(64) NOT NULL,
  `db_name` varchar(64) NOT NULL,
  `table_name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Hidden items of navigation tree';

-- --------------------------------------------------------

--
-- Table structure for table `pma__pdf_pages`
--

CREATE TABLE `pma__pdf_pages` (
  `db_name` varchar(64) NOT NULL DEFAULT '',
  `page_nr` int(10) UNSIGNED NOT NULL,
  `page_descr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='PDF relation pages for phpMyAdmin';

-- --------------------------------------------------------

--
-- Table structure for table `pma__recent`
--

CREATE TABLE `pma__recent` (
  `username` varchar(64) NOT NULL,
  `tables` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Recently accessed tables';

--
-- Dumping data for table `pma__recent`
--

INSERT INTO `pma__recent` (`username`, `tables`) VALUES
('root', '[{\"db\":\"Skuska_Letecke_Treningy\",\"table\":\"Planes\"},{\"db\":\"Skuska_Letecke_Treningy\",\"table\":\"Users\"},{\"db\":\"Skuska_Letecke_Treningy\",\"table\":\"records\"},{\"db\":\"Skuska_Bankovy_uver\",\"table\":\"Cena_zl_nehnutelnosti\"},{\"db\":\"Skuska_Bankovy_uver\",\"table\":\"BS_Pocet_deti\"},{\"db\":\"Skuska_Bankovy_uver\",\"table\":\"BS_PRIJEM\"},{\"db\":\"Skuska_Bankovy_uver\",\"table\":\"BS_VEK\"},{\"db\":\"Skuska_Bankovy_uver\",\"table\":\"User\"},{\"db\":\"_04_E-SHOP\",\"table\":\"Users\"},{\"db\":\"E_SHOP_MK\",\"table\":\"sklad\"}]');

-- --------------------------------------------------------

--
-- Table structure for table `pma__relation`
--

CREATE TABLE `pma__relation` (
  `master_db` varchar(64) NOT NULL DEFAULT '',
  `master_table` varchar(64) NOT NULL DEFAULT '',
  `master_field` varchar(64) NOT NULL DEFAULT '',
  `foreign_db` varchar(64) NOT NULL DEFAULT '',
  `foreign_table` varchar(64) NOT NULL DEFAULT '',
  `foreign_field` varchar(64) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Relation table';

-- --------------------------------------------------------

--
-- Table structure for table `pma__savedsearches`
--

CREATE TABLE `pma__savedsearches` (
  `id` int(5) UNSIGNED NOT NULL,
  `username` varchar(64) NOT NULL DEFAULT '',
  `db_name` varchar(64) NOT NULL DEFAULT '',
  `search_name` varchar(64) NOT NULL DEFAULT '',
  `search_data` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Saved searches';

-- --------------------------------------------------------

--
-- Table structure for table `pma__table_coords`
--

CREATE TABLE `pma__table_coords` (
  `db_name` varchar(64) NOT NULL DEFAULT '',
  `table_name` varchar(64) NOT NULL DEFAULT '',
  `pdf_page_number` int(11) NOT NULL DEFAULT 0,
  `x` float UNSIGNED NOT NULL DEFAULT 0,
  `y` float UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Table coordinates for phpMyAdmin PDF output';

-- --------------------------------------------------------

--
-- Table structure for table `pma__table_info`
--

CREATE TABLE `pma__table_info` (
  `db_name` varchar(64) NOT NULL DEFAULT '',
  `table_name` varchar(64) NOT NULL DEFAULT '',
  `display_field` varchar(64) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Table information for phpMyAdmin';

--
-- Dumping data for table `pma__table_info`
--

INSERT INTO `pma__table_info` (`db_name`, `table_name`, `display_field`) VALUES
('_03_Blog', 'Posts', 'Text'),
('_04_E-SHOP', 'Polozky_objednavky', 'Cislo_objednavky'),
('_04_E-SHOP', 'Zoznam_objednavok', 'Stav_objednavky');

-- --------------------------------------------------------

--
-- Table structure for table `pma__table_uiprefs`
--

CREATE TABLE `pma__table_uiprefs` (
  `username` varchar(64) NOT NULL,
  `db_name` varchar(64) NOT NULL,
  `table_name` varchar(64) NOT NULL,
  `prefs` text NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Tables'' UI preferences';

--
-- Dumping data for table `pma__table_uiprefs`
--

INSERT INTO `pma__table_uiprefs` (`username`, `db_name`, `table_name`, `prefs`, `last_update`) VALUES
('root', 'E_SHOP_AK', 'obj_zoznam', '{\"sorted_col\":\"`obj_zoznam`.`ID` ASC\"}', '2023-12-31 23:59:06'),
('root', 'Skuska_Bankovy_uver', 'BS_VEK', '{\"sorted_col\":\"`BS_VEK`.`Ved_od` ASC\"}', '2024-01-11 11:39:29'),
('root', 'Skuska_Bankovy_uver', 'Cena_zl_nehnutelnosti', '{\"sorted_col\":\"`Cena_zl_nehnutelnosti`.`Hodnota` ASC\"}', '2024-01-11 12:09:25'),
('root', 'Skuska_Letecke_Treningy', 'Planes', '{\"CREATE_TIME\":\"2024-01-18 14:27:55\",\"col_order\":[0,1,3,2,4],\"col_visib\":[1,1,1,1,1]}', '2024-01-18 15:59:27'),
('root', '_02_Objednavky', 'Tovar', '{\"sorted_col\":\"`Tovar`.`Cena_tovaru` DESC\"}', '2023-12-03 16:43:02'),
('root', '_04_E-SHOP', 'Tovar', '{\"sorted_col\":\"`Modelova_rada` ASC\"}', '2023-12-30 18:22:27'),
('root', '_04_E-SHOP', 'Users', '{\"CREATE_TIME\":\"2023-12-25 15:26:27\"}', '2023-12-30 12:18:39'),
('root', '_04_E-SHOP', 'Zoznam_objednavok', '[]', '2023-12-30 21:47:00');

-- --------------------------------------------------------

--
-- Table structure for table `pma__tracking`
--

CREATE TABLE `pma__tracking` (
  `db_name` varchar(64) NOT NULL,
  `table_name` varchar(64) NOT NULL,
  `version` int(10) UNSIGNED NOT NULL,
  `date_created` datetime NOT NULL,
  `date_updated` datetime NOT NULL,
  `schema_snapshot` text NOT NULL,
  `schema_sql` text DEFAULT NULL,
  `data_sql` longtext DEFAULT NULL,
  `tracking` set('UPDATE','REPLACE','INSERT','DELETE','TRUNCATE','CREATE DATABASE','ALTER DATABASE','DROP DATABASE','CREATE TABLE','ALTER TABLE','RENAME TABLE','DROP TABLE','CREATE INDEX','DROP INDEX','CREATE VIEW','ALTER VIEW','DROP VIEW') DEFAULT NULL,
  `tracking_active` int(1) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Database changes tracking for phpMyAdmin';

-- --------------------------------------------------------

--
-- Table structure for table `pma__userconfig`
--

CREATE TABLE `pma__userconfig` (
  `username` varchar(64) NOT NULL,
  `timevalue` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `config_data` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User preferences storage for phpMyAdmin';

--
-- Dumping data for table `pma__userconfig`
--

INSERT INTO `pma__userconfig` (`username`, `timevalue`, `config_data`) VALUES
('root', '2024-02-06 19:03:40', '{\"Console\\/Mode\":\"show\",\"Console\\/Height\":275.0023299999999153442331589758396148681640625}');

-- --------------------------------------------------------

--
-- Table structure for table `pma__usergroups`
--

CREATE TABLE `pma__usergroups` (
  `usergroup` varchar(64) NOT NULL,
  `tab` varchar(64) NOT NULL,
  `allowed` enum('Y','N') NOT NULL DEFAULT 'N'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User groups with configured menu items';

-- --------------------------------------------------------

--
-- Table structure for table `pma__users`
--

CREATE TABLE `pma__users` (
  `username` varchar(64) NOT NULL,
  `usergroup` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Users and their assignments to user groups';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pma__bookmark`
--
ALTER TABLE `pma__bookmark`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pma__central_columns`
--
ALTER TABLE `pma__central_columns`
  ADD PRIMARY KEY (`db_name`,`col_name`);

--
-- Indexes for table `pma__column_info`
--
ALTER TABLE `pma__column_info`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `db_name` (`db_name`,`table_name`,`column_name`);

--
-- Indexes for table `pma__designer_settings`
--
ALTER TABLE `pma__designer_settings`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `pma__export_templates`
--
ALTER TABLE `pma__export_templates`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `u_user_type_template` (`username`,`export_type`,`template_name`);

--
-- Indexes for table `pma__favorite`
--
ALTER TABLE `pma__favorite`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `pma__history`
--
ALTER TABLE `pma__history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `username` (`username`,`db`,`table`,`timevalue`);

--
-- Indexes for table `pma__navigationhiding`
--
ALTER TABLE `pma__navigationhiding`
  ADD PRIMARY KEY (`username`,`item_name`,`item_type`,`db_name`,`table_name`);

--
-- Indexes for table `pma__pdf_pages`
--
ALTER TABLE `pma__pdf_pages`
  ADD PRIMARY KEY (`page_nr`),
  ADD KEY `db_name` (`db_name`);

--
-- Indexes for table `pma__recent`
--
ALTER TABLE `pma__recent`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `pma__relation`
--
ALTER TABLE `pma__relation`
  ADD PRIMARY KEY (`master_db`,`master_table`,`master_field`),
  ADD KEY `foreign_field` (`foreign_db`,`foreign_table`);

--
-- Indexes for table `pma__savedsearches`
--
ALTER TABLE `pma__savedsearches`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `u_savedsearches_username_dbname` (`username`,`db_name`,`search_name`);

--
-- Indexes for table `pma__table_coords`
--
ALTER TABLE `pma__table_coords`
  ADD PRIMARY KEY (`db_name`,`table_name`,`pdf_page_number`);

--
-- Indexes for table `pma__table_info`
--
ALTER TABLE `pma__table_info`
  ADD PRIMARY KEY (`db_name`,`table_name`);

--
-- Indexes for table `pma__table_uiprefs`
--
ALTER TABLE `pma__table_uiprefs`
  ADD PRIMARY KEY (`username`,`db_name`,`table_name`);

--
-- Indexes for table `pma__tracking`
--
ALTER TABLE `pma__tracking`
  ADD PRIMARY KEY (`db_name`,`table_name`,`version`);

--
-- Indexes for table `pma__userconfig`
--
ALTER TABLE `pma__userconfig`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `pma__usergroups`
--
ALTER TABLE `pma__usergroups`
  ADD PRIMARY KEY (`usergroup`,`tab`,`allowed`);

--
-- Indexes for table `pma__users`
--
ALTER TABLE `pma__users`
  ADD PRIMARY KEY (`username`,`usergroup`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pma__bookmark`
--
ALTER TABLE `pma__bookmark`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pma__column_info`
--
ALTER TABLE `pma__column_info`
  MODIFY `id` int(5) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pma__export_templates`
--
ALTER TABLE `pma__export_templates`
  MODIFY `id` int(5) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pma__history`
--
ALTER TABLE `pma__history`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pma__pdf_pages`
--
ALTER TABLE `pma__pdf_pages`
  MODIFY `page_nr` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pma__savedsearches`
--
ALTER TABLE `pma__savedsearches`
  MODIFY `id` int(5) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- Database: `SKchat`
--
CREATE DATABASE IF NOT EXISTS `SKchat` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `SKchat`;

-- --------------------------------------------------------

--
-- Table structure for table `prispevok`
--

CREATE TABLE `prispevok` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `meno` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prispevok`
--

INSERT INTO `prispevok` (`id`, `id_user`, `meno`, `content`) VALUES
(1, 1, 'jozef', 'Hello'),
(2, 2, 'michal', 'Guten Tag'),
(8, 1, 'jozef', 'Hello 2'),
(9, 3, 'peter', 'Hello 3'),
(10, 1, 'jozef', 'p'),
(11, 1, 'jozef', 'm'),
(12, 3, 'peter', 'fowmúeofmweúomf'),
(13, 1, 'jozef', 'e');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `meno` varchar(255) NOT NULL,
  `heslo` varchar(255) NOT NULL,
  `ban` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `meno`, `heslo`, `ban`) VALUES
(1, 'jozef', '123', 0),
(2, 'michal', '123', 0),
(3, 'peter', '123', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `prispevok`
--
ALTER TABLE `prispevok`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `prispevok`
--
ALTER TABLE `prispevok`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Database: `Skuska_Bankovy_uver`
--
CREATE DATABASE IF NOT EXISTS `Skuska_Bankovy_uver` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `Skuska_Bankovy_uver`;

-- --------------------------------------------------------

--
-- Table structure for table `BS_Pocet_deti`
--

CREATE TABLE `BS_Pocet_deti` (
  `idPocet_deti` int(11) NOT NULL,
  `Pocet_od` int(11) NOT NULL,
  `Pocet_do` int(11) NOT NULL,
  `Skore` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `BS_Pocet_deti`
--

INSERT INTO `BS_Pocet_deti` (`idPocet_deti`, `Pocet_od`, `Pocet_do`, `Skore`) VALUES
(1, 1, 2, 15),
(2, 2, 3, 10),
(3, 3, 10, 5),
(4, 0, 0, 20);

-- --------------------------------------------------------

--
-- Table structure for table `BS_PRIJEM`
--

CREATE TABLE `BS_PRIJEM` (
  `idPrijem` int(11) NOT NULL,
  `Prijem_od` double NOT NULL,
  `Prijem_do` double NOT NULL,
  `Skore` int(11) NOT NULL,
  `Koniec` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `BS_PRIJEM`
--

INSERT INTO `BS_PRIJEM` (`idPrijem`, `Prijem_od`, `Prijem_do`, `Skore`, `Koniec`) VALUES
(1, 650, 999.99, 5, 1),
(2, 1000, 1499.99, 10, 0),
(7, 1500, 2499.99, 15, 0),
(8, 2500, 100000, 25, 0);

-- --------------------------------------------------------

--
-- Table structure for table `BS_VEK`
--

CREATE TABLE `BS_VEK` (
  `idVek` int(11) NOT NULL,
  `Ved_od` int(11) NOT NULL,
  `Vek_do` int(11) NOT NULL,
  `Skore` int(11) NOT NULL,
  `Koniec` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `BS_VEK`
--

INSERT INTO `BS_VEK` (`idVek`, `Ved_od`, `Vek_do`, `Skore`, `Koniec`) VALUES
(1, 18, 24, 10, 0),
(2, 25, 49, 25, 0),
(3, 50, 63, 5, 0),
(4, 0, 17, 0, 1),
(5, 64, 250, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Cena_zl_nehnutelnosti`
--

CREATE TABLE `Cena_zl_nehnutelnosti` (
  `idCena_zl_nehnutelnosti` int(11) NOT NULL,
  `Hodnota` double NOT NULL,
  `Skore` int(11) NOT NULL,
  `Hodnota_od` int(11) NOT NULL,
  `hondota_do` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Dumping data for table `Cena_zl_nehnutelnosti`
--

INSERT INTO `Cena_zl_nehnutelnosti` (`idCena_zl_nehnutelnosti`, `Hodnota`, `Skore`, `Hodnota_od`, `hondota_do`) VALUES
(1, 500, 30, 401, 500),
(2, 400, 25, 301, 400),
(3, 300, 20, 201, 300),
(4, 200, 15, 101, 200),
(5, 100, 10, 51, 100),
(6, 50, 5, 0, 50);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `idUser` int(11) NOT NULL,
  `Meno` varchar(25) DEFAULT NULL,
  `Priezvisko` varchar(25) DEFAULT NULL,
  `Vek` int(11) DEFAULT NULL,
  `Prijem` double DEFAULT NULL,
  `Pocet_deti` int(11) DEFAULT NULL,
  `Cena _zalozenej_nehnutelnosti` double DEFAULT NULL,
  `Heslo` varchar(64) DEFAULT NULL,
  `Bankar/zakaznik` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovak_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `BS_Pocet_deti`
--
ALTER TABLE `BS_Pocet_deti`
  ADD PRIMARY KEY (`idPocet_deti`);

--
-- Indexes for table `BS_PRIJEM`
--
ALTER TABLE `BS_PRIJEM`
  ADD PRIMARY KEY (`idPrijem`);

--
-- Indexes for table `BS_VEK`
--
ALTER TABLE `BS_VEK`
  ADD PRIMARY KEY (`idVek`);

--
-- Indexes for table `Cena_zl_nehnutelnosti`
--
ALTER TABLE `Cena_zl_nehnutelnosti`
  ADD PRIMARY KEY (`idCena_zl_nehnutelnosti`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `BS_Pocet_deti`
--
ALTER TABLE `BS_Pocet_deti`
  MODIFY `idPocet_deti` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `BS_PRIJEM`
--
ALTER TABLE `BS_PRIJEM`
  MODIFY `idPrijem` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `BS_VEK`
--
ALTER TABLE `BS_VEK`
  MODIFY `idVek` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `Cena_zl_nehnutelnosti`
--
ALTER TABLE `Cena_zl_nehnutelnosti`
  MODIFY `idCena_zl_nehnutelnosti` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT;
--
-- Database: `Skuska_Letecke_Treningy`
--
CREATE DATABASE IF NOT EXISTS `Skuska_Letecke_Treningy` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `Skuska_Letecke_Treningy`;

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
--
-- Database: `test`
--
CREATE DATABASE IF NOT EXISTS `test` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `test`;
--
-- Database: `vasilik_Eshop`
--
CREATE DATABASE IF NOT EXISTS `vasilik_Eshop` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `vasilik_Eshop`;

-- --------------------------------------------------------

--
-- Table structure for table `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`) VALUES
(1, 4, 2, 69, 1);

-- --------------------------------------------------------

--
-- Table structure for table `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `obj_polozky`
--

INSERT INTO `obj_polozky` (`ID`, `ID_objednavky`, `ID_tovaru`, `cena`, `ks`) VALUES
(2, 2, 2, 69, 1),
(3, 2, 1, 36, 1),
(4, 3, 3, 77, 1),
(5, 3, 1, 36, 2),
(6, 4, 4, 56, 1),
(7, 5, 3, 77, 1),
(8, 6, 1, 36, 2),
(9, 7, 1, 36, 3),
(11, 9, 2, 69, 1),
(12, 11, 5, 10, 1),
(15, 14, 2, 69, 1),
(19, 18, 2, 34, 1),
(20, 19, 5, 5, 2),
(21, 19, 2, 34, 3),
(22, 20, 2, 69, 1),
(23, 21, 5, 5, 1),
(24, 21, 3, 38, 1),
(25, 21, 4, 28, 1),
(26, 21, 2, 34, 1),
(27, 21, 7, 23, 1),
(28, 21, 8, 9, 1),
(29, 21, 9, 9, 1),
(30, 21, 10, 39, 1),
(31, 21, 11, 13, 1),
(34, 24, 2, 34, 5),
(35, 25, 2, 34, 1),
(36, 26, 2, 34, 1),
(37, 27, 2, 34, 1);

-- --------------------------------------------------------

--
-- Table structure for table `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` int(11) NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `obj_zoznam`
--

INSERT INTO `obj_zoznam` (`ID`, `obj_cislo`, `datum_objednavky`, `ID_pouzivatela`, `suma`, `stav`) VALUES
(2, 'ORDER1702678138734', '2023-12-15', 3, 105, 'Shipped'),
(3, 'ORDER1702678452390', '2023-12-15', 3, 149, 'Shipped'),
(4, 'ORDER1702678664424', '2023-12-15', 3, 56, 'Shipped'),
(5, 'ORDER1702761962616', '2023-12-16', 3, 77, 'Shipped'),
(6, 'ORDER1702762697280', '2023-12-16', 4, 72, 'Shipped'),
(7, 'ORDER1702762714648', '2023-12-16', 4, 108, 'Shipped'),
(9, 'ORDER1702815597845', '2023-12-17', 3, 69, 'Shipped'),
(21, 'ORDER1702934653492', '2023-12-18', 3, 201, 'Shipped'),
(11, 'ORDER1702826006209', '2023-12-17', 3, 10, 'Processed'),
(14, 'ORDER1702828300352', '2023-12-17', 3, 69, 'Processed'),
(23, 'ORDER1703018573018', '2023-12-19', 5, 69, 'Processed'),
(18, 'ORDER1702829107237', '2023-12-17', 3, 34, 'Paid'),
(19, 'ORDER1702846351613', '2023-12-17', 3, 113, 'Paid'),
(20, 'ORDER1702846801424', '2023-12-17', 4, 69, 'Paid');

-- --------------------------------------------------------

--
-- Table structure for table `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `popis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `photo_url` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`, `popis`, `photo_url`) VALUES
(1, 'WHEY PROTEIN', 0, 36, 'Quickly absorbable instant whey protein with high BCAA content and excellent taste.\r\nQuickly absorbable instant whey protein with high BCAA content and excellent taste. Whey protein contains all important amino acids, including a valuable dose of BCAA. This makes it an ideal choice for all athletes and people who want to increase their daily protein intake, thus supporting muscle growth, their protection and accelerating regeneration after training.', 'https://www.bodyworld.eu/media/product/4631/original.png?nc=1697785782'),
(2, 'WHEY GOLD STANDARD 100%', 126, 69, 'Delicious whey protein with high biological value enriched with a mixture of digestive enzymes.\r\nDelicious whey protein with high biological value enriched with a mixture of digestive enzymes. It is among the best-selling proteins in the world. Its main ingredient is high-quality whey isolate supplemented with whey concentrate and hydrolyzed isolate. This unique mixture ensures the intake of quickly absorbable proteins, which are essential for muscle growth, to improve regeneration after demanding training, but also during reduction diets to protect muscle mass from its loss.', 'https://www.bodyworld.eu/media/product/231/original.png?nc=1630323144'),
(3, 'WHEY PROTEIN', 15, 77, 'Quickly absorbable instant whey protein with high BCAA content and excellent taste.\r\nQuickly absorbable instant whey protein with high BCAA content and excellent taste. Whey protein contains all important amino acids, including a valuable dose of BCAA. This makes it an ideal choice for all athletes and people who want to increase their daily protein intake, thus supporting muscle growth, their protection and accelerating regeneration after training.', 'https://www.bodyworld.eu/media/product/4801/original.png?nc=1683096100'),
(4, 'PROTEIN POWER', 3, 56, 'Excellent flavored multi-component protein enriched with micronized creatine.\r\nExcellent flavored multi-component protein enriched with micronized creatine intended for athletes who want to increase performance, improve muscle growth and accelerate regeneration. It contains three types of protein: soy protein isolate, calcium caseinate and whey protein concentrate.', 'https://www.bodyworld.eu/media/product/1417/original.png?nc=1635241728'),
(5, 'VEGAN PROTEIN 500G', 61, 10, 'Delicious multi-ingredient vegetable protein with no added sugar.\r\nDelicious multi-ingredient vegetable protein with no added sugar. The basis of Vegan Protein is soy isolate, which is enriched with another 6 types of protein from pea, cranberry, rice, hemp, sunflower and almond protein. Soy isolate is a valuable source of healthy plant proteins with a low carbohydrate and fat content. The proteins contained in Vegan Protein contribute to the growth and maintenance of muscle mass and to the maintenance of healthy bones.', 'https://www.bodyworld.eu/media/product/5891/original.png?nc=1578496356'),
(7, 'WPC 80 1000 g', 11, 46, 'Quality and tasty whey protein made in Slovakia.\r\nTasty and easily soluble whey concentrate made in Slovakia from the highest quality domestic raw materials. It is characterized by a proportion of proteins approaching 80%, a high biological value and a rich content of important BCAA amino acids. WPC 80 is intended for all athletes and people who want to increase their daily protein intake, start muscle growth, support their protection and speed up the regeneration process after hard training.', 'https://www.bodyworld.eu/media/product/4055/original.png?nc=1678259911'),
(6, 'ISOLATE PROTEIN 908 G', 1, 33, 'Instant whey isolate with up to 85% of rapidly absorbable proteins.\r\nFirst-class instant whey isolate with up to 85% of quickly absorbable and easily digestible proteins. Isolate Protein starts muscle growth without fat, prevents catabolism and accelerates post-workout regeneration. It is characterized by an excellent amino acid profile rich in BCAA branched amino acids and an extremely high biological value of BV 159.', 'https://www.bodyworld.eu/media/product/4935/original.png?nc=1662461845'),
(8, 'NUTLOVE PROTEIN SHAKE', 30, 19, 'Delicious creamy protein based on milk concentrates with added buttermilk and nuts.\r\nDelicious creamy protein based on milk concentrates with added buttermilk and nuts. This excellent protein primarily supplements our diet with proteins that are necessary for muscle growth and protection. Thanks to the three flavors and the creamy consistency, supplementing the daily protein intake turns into a perfect pleasure and successfully replaces any dessert.', 'https://www.bodyworld.eu/media/product/7070/original.png?nc=1643186566'),
(9, 'PRO CASEIN', 10, 19, 'Delicious micellar casein enriched with LactoSpore® and DigeZyme®.\r\nDelicious micellar casein enriched with LactoSpore® probiotic complex and DigeZyme® enzyme complex. Micellar casein is characterized by slow absorption, which guarantees a gradual release of amino acids. It is especially useful at night while sleeping or when you have been without food for a long time. Pro Casein contains a full spectrum of essential and non-essential amino acids, has a naturally high content of BCAA and glutamic acid, and all this with a minimal content of fat and carbohydrates.', 'https://www.bodyworld.eu/media/product/7581/original.png?nc=1671470345'),
(10, 'PRO WHEY', 5, 79, 'Tasty whey protein with added amino acids enriched with digestive enzymes.\r\nTasty protein made from high-quality whey concentrate and whey isolate. In addition, extra amino acids and digestive enzymes were added to the protein. Whey protein supports the growth of muscles, their protection and accelerates regeneration after demanding training. The added digestive enzymes papain and bromelain ensure trouble-free digestion of proteins and maximum utilization of nutrients.', 'https://www.bodyworld.eu/media/product/16/original.png?nc=1694425246'),
(11, 'ISO WHEY ZERO', 2, 27, 'Premium extremely quickly absorbable whey isolate enriched with amino acids BCAA and glutamine.\r\nExtremely quickly and easily absorbed whey isolate enriched with amino acids BCAA and glutamine. The gentle production process through the process of micro and ultrafiltration is a guarantee of high purity of the resulting protein, which is cleaned of all unwanted substances. Iso Whey does not contain added sugar, it is lactose and gluten free. Proteins support muscle growth, improve regeneration and protect muscles during reduction diets.', 'https://www.bodyworld.eu/media/product/1449/original.png?nc=1650968867');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `zlava` int(11) NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `poznamky` text CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `je_admin` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `zlava`, `meno`, `priezvisko`, `poznamky`, `je_admin`) VALUES
(1, 'jskalka@ukf.sk', '123', 'Zeleninova 4, Nitra', 20, 'Jan ', 'Skalka', 'tester', 1),
(2, 'jmrkva@ukf.sk', '123', 'Zahrada 11', 3, 'Jozef', 'Mrkva', 'druhý tester', 0),
(3, '1@gmail.com', '1', 'Bratislavská 1, 949 01 Nitra', 50, '1', '11', '11', 0),
(4, '2@gmail.com', '2', 'Piaristická 1379/2, 949 01 Nitra', 0, '2', '2', '2', 1),
(5, 'lbenko@ukf.sk', '123', 'Cajkovskeho 434/40, 949 11 Nitra', 0, 'Lubomir', 'Benko', 'tester', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Database: `_01_CRUD`
--
CREATE DATABASE IF NOT EXISTS `_01_CRUD` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `_01_CRUD`;

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

--
-- Dumping data for table `Prijmy_Vydaje`
--

INSERT INTO `Prijmy_Vydaje` (`id`, `Datum`, `Prijem`, `Vydaj`, `Poznamka`) VALUES
(4, '2023-11-23', 150, 0, '555'),
(8, '2023-11-23', 356, 0, NULL),
(9, '2023-05-14', 100, 150, '561'),
(11, '2023-11-23', 111111111, 0, '2222222222222222222222222222222222');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Prijmy_Vydaje`
--
ALTER TABLE `Prijmy_Vydaje`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Prijmy_Vydaje`
--
ALTER TABLE `Prijmy_Vydaje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- Database: `_02_Objednavky`
--
CREATE DATABASE IF NOT EXISTS `_02_Objednavky` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `_02_Objednavky`;

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
--
-- Database: `_03_Blog`
--
CREATE DATABASE IF NOT EXISTS `_03_Blog` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `_03_Blog`;

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
(23, 28, 'nn', '2023-12-10 14:55:17'),
(24, 22, 'Kontrolna otayka ', '2023-12-18 21:27:29'),
(25, 22, 'kkkk', '2023-12-18 21:36:18'),
(26, 22, 'kkkk', '2023-12-18 21:36:24'),
(27, 22, 'lll\r\n', '2023-12-18 21:41:56'),
(28, 22, 'lll\r\n', '2023-12-18 21:42:02');

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
  MODIFY `idBan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `Posts`
--
ALTER TABLE `Posts`
  MODIFY `idPosts` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

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
--
-- Database: `_04_E-SHOP`
--
CREATE DATABASE IF NOT EXISTS `_04_E-SHOP` DEFAULT CHARACTER SET utf16 COLLATE utf16_slovak_ci;
USE `_04_E-SHOP`;

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
