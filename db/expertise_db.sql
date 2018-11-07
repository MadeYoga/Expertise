-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 07, 2018 at 12:02 PM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `expert`
--

-- --------------------------------------------------------

--
-- Table structure for table `answer`
--

CREATE TABLE `answer` (
  `id` int(11) NOT NULL,
  `answer` varchar(32) NOT NULL,
  `expertid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `answer`
--

INSERT INTO `answer` (`id`, `answer`, `expertid`) VALUES
(1, 'YA', NULL),
(2, 'NO', NULL),
(3, 'YA', 6),
(4, 'TIDAK', 6),
(5, 'TERIMA', 6),
(6, 'TOLAK', 6),
(7, 'TYA', 7),
(8, 'TIDAK', 7),
(9, 'TOLAK', 7),
(10, 'TERIMA', 7),
(11, 'YA', 8),
(12, 'TIDAK', 8),
(13, 'TOLAK', 8),
(14, 'TERIMA', 8),
(15, 'YA', 9),
(16, 'TIDAK', 9),
(17, 'YA', 10),
(18, 'TIDAK', 10),
(19, 'YA', 11),
(20, 'TIDAK', 11),
(21, 'YA', 12),
(22, 'TIDAK', 12),
(23, 'YA', 13),
(24, 'TIDAK', 13),
(25, 'YA', 16),
(26, 'TIDAK', 16),
(27, 'YA', 17),
(28, 'NO', 17);

-- --------------------------------------------------------

--
-- Table structure for table `experts`
--

CREATE TABLE `experts` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `description` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `experts`
--

INSERT INTO `experts` (`id`, `name`, `description`) VALUES
(1, 'beasiswa', 'uts '),
(2, 'Smartphone', 'diagnosa kerusakan pada Smartphone'),
(6, 'TESTING 1', 'TESTING 1'),
(7, 'TESTING 3', 'TESTING 3'),
(8, 'TESTING 4', 'TESTING 4'),
(9, 'TESTING 5', 'TESTING 5'),
(10, 'TESTING 6', 'TESTING 6'),
(11, 'TESTING 7', 'TESTING 7'),
(12, 'TESTING 8', 'TESTING 8'),
(13, 'TESTING 9', 'TESTING 9'),
(14, 'TESTING 10', 'TESTING 10'),
(15, 'TESTING 11', 'TESTING 11'),
(16, 'TETING 12', 'TESTING 12'),
(17, 'TESTING 13', 'TESTING 13');

-- --------------------------------------------------------

--
-- Table structure for table `premise`
--

CREATE TABLE `premise` (
  `id` int(11) NOT NULL,
  `question` varchar(256) NOT NULL,
  `expertid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `premise`
--

INSERT INTO `premise` (`id`, `question`, `expertid`, `name`) VALUES
(1, 'lulus test math ?', NULL, NULL),
(2, 'lulus test b.ing ?', NULL, NULL),
(5, 'tidak memiliki mobil pribadi ?', NULL, NULL),
(7, 'pemakaian daya listrik di rekening <= 2200VA', NULL, NULL),
(8, 'uang gaji perbulan <= 1jt, dari ortu ?', NULL, NULL),
(9, 'ayah/ibu memiliki nama marga asli putra daerah ?', NULL, NULL),
(10, 'Akademik', NULL, NULL),
(11, 'Finansial', NULL, NULL),
(12, 'Putra daerah', NULL, NULL),
(13, 'Terdapat garis pada layar ?', NULL, NULL),
(14, 'Layar pecah ? ', NULL, NULL),
(15, 'Layar buram ? ', NULL, NULL),
(16, 'White screen ? ', NULL, NULL),
(17, 'Baterai drop ?', NULL, NULL),
(18, 'Smartphone panas ?', NULL, NULL),
(19, 'blank screen ? ', NULL, NULL),
(20, 'Tidak dapat memproses Aplikasi ?', NULL, NULL),
(21, 'Smartphone reboot tiba-tiba ?', NULL, NULL),
(22, 'Tidak berhenti mengisi daya ?', NULL, NULL),
(23, 'Baterai cepat panas ?', NULL, NULL),
(24, 'Baterai menggelembung ? ', NULL, NULL),
(25, 'Tidak berhenti mengisi daya ?', NULL, NULL),
(26, 'AKADEMIK TERPENUHI ?', 6, 'AKADEMIK'),
(27, 'AKADEMIK TERPENUHI ?', 7, 'AKADEMIK'),
(28, 'FINANSIAL TERPENUHI ?', 7, 'FINANSIAL '),
(29, 'AKADEMIK TERPENUHI ?', 8, 'AKADEMIK'),
(30, 'FINANSIAL TERPENUHI ?', 8, 'FINANSIAL'),
(31, 'AKADEMIK TERPENUHI ?', 9, 'AKADEMIK'),
(32, 'FIANSIAL TERPENUHI ?', 9, 'FINANSIAL'),
(33, 'AKADEMIK TERPENUHI ?', 10, 'AKADEMIK'),
(34, 'FINANSIAL TERPENUHI ?', 10, 'FINANSIAL '),
(35, 'AKADEMIK TERPENUHI', 11, 'AKADEMIK'),
(36, 'AKADEMIK TERPENUHI ?', 12, 'AKADEMIK'),
(37, 'AKADEMIK TERPENUHI ?', 13, 'AKADEMIK '),
(38, 'AKADEMIK TERPENUHI ?', 16, 'AKADEMIK'),
(39, 'FINANSIAL TERPENUHI ?', 16, 'FINANSIAL'),
(40, 'PUTRA DAERAH TERPENUHI ?', 16, 'PUTRA DAERAH'),
(41, 'RACUN ADA ?', 17, 'RACUN'),
(42, 'ROTI BERJAMUR ? ', 17, 'BERJAMUR '),
(43, 'ROTI BAU ? ', 17, 'BAU');

-- --------------------------------------------------------

--
-- Table structure for table `premise_answer_list`
--

CREATE TABLE `premise_answer_list` (
  `id` int(11) NOT NULL,
  `premise_id` int(11) NOT NULL,
  `answer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `premise_answer_list`
--

INSERT INTO `premise_answer_list` (`id`, `premise_id`, `answer_id`) VALUES
(1, 10, 1),
(2, 10, 2),
(3, 9, 1),
(4, 9, 2),
(5, 11, 1),
(6, 11, 2),
(7, 2, 1),
(8, 2, 2),
(9, 1, 1),
(10, 1, 2),
(11, 7, 1),
(12, 7, 2),
(13, 5, 1),
(14, 5, 2),
(15, 12, 1),
(16, 12, 2),
(17, 8, 1),
(18, 8, 2),
(19, 13, 1),
(20, 13, 2),
(23, 14, 1),
(24, 14, 2),
(25, 15, 1),
(26, 15, 2),
(27, 16, 1),
(28, 16, 2),
(29, 17, 1),
(30, 17, 2),
(31, 18, 1),
(32, 18, 2),
(33, 19, 1),
(34, 19, 2),
(35, 20, 1),
(36, 20, 2),
(37, 21, 1),
(38, 21, 2),
(39, 22, 1),
(40, 22, 2),
(41, 23, 1),
(42, 23, 2),
(43, 24, 1),
(44, 24, 2),
(45, 25, 1),
(46, 25, 2),
(47, 27, 7),
(48, 27, 8),
(49, 28, 8),
(50, 28, 7),
(51, 29, 11),
(52, 29, 12),
(53, 30, 12),
(54, 30, 11),
(55, 31, 15),
(56, 31, 16),
(57, 32, 16),
(58, 32, 15),
(59, 33, 17),
(60, 33, 18),
(61, 34, 18),
(62, 34, 17),
(63, 35, 19),
(64, 35, 20),
(65, 36, 21),
(66, 36, 22),
(67, 37, 23),
(68, 37, 24),
(69, 38, 25),
(70, 38, 26),
(71, 39, 25),
(72, 39, 26),
(73, 40, 25),
(74, 40, 26),
(75, 41, 27),
(76, 41, 28),
(77, 42, 27),
(78, 42, 28),
(79, 43, 27),
(80, 43, 28);

-- --------------------------------------------------------

--
-- Table structure for table `premise_rules`
--

CREATE TABLE `premise_rules` (
  `id` int(11) NOT NULL,
  `premise_id` int(11) NOT NULL,
  `rule_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `premise_rules`
--

INSERT INTO `premise_rules` (`id`, `premise_id`, `rule_id`) VALUES
(1, 10, 7),
(2, 10, 8),
(3, 10, 9),
(4, 11, 10),
(5, 11, 11),
(6, 11, 12),
(7, 12, 14),
(8, 12, 15),
(9, 11, 13);

-- --------------------------------------------------------

--
-- Table structure for table `rule`
--

CREATE TABLE `rule` (
  `id` int(11) NOT NULL,
  `conclusion` varchar(32) NOT NULL,
  `conclusion_value` int(11) NOT NULL,
  `expert_id` int(11) NOT NULL,
  `hierarchy` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rule`
--

INSERT INTO `rule` (`id`, `conclusion`, `conclusion_value`, `expert_id`, `hierarchy`) VALUES
(1, 'Terima', 1, 1, 1),
(2, 'Tolak', 1, 1, 1),
(3, 'Tolak', 1, 1, 1),
(4, 'Tolak', 1, 1, 1),
(7, 'Akademik', 1, 1, 2),
(8, 'Akademik', 2, 1, 2),
(9, 'Akademik', 2, 1, 2),
(10, 'Finansial', 1, 1, 2),
(11, 'Finansial', 2, 1, 2),
(12, 'Finansial', 2, 1, 2),
(13, 'Finansial', 2, 1, 2),
(14, 'Putra daerah', 1, 1, 2),
(15, 'Putra daerah', 2, 1, 2),
(16, 'LCD', 1, 2, 1),
(17, 'IC, PowerSupply', 1, 2, 1),
(18, 'Software', 1, 2, 1),
(19, 'Baterai', 1, 2, 1),
(20, 'BEASISWA', 17, 10, 1),
(21, 'BEASISWA', 19, 11, 1),
(22, 'BEASISWA', 21, 12, 1),
(23, 'BEASISWA', 23, 13, 1),
(24, 'BEASISWA', 25, 16, 1),
(25, 'BEASISWA', 26, 16, 1),
(26, 'BEASISWA', 26, 16, 1),
(27, 'MAKAN', 28, 17, 1),
(28, 'MAKAN', 27, 17, 1),
(29, 'MAKAN', 28, 17, 1),
(30, 'MAKAN', 28, 17, 1);

-- --------------------------------------------------------

--
-- Table structure for table `rules_premise`
--

CREATE TABLE `rules_premise` (
  `id` int(11) NOT NULL,
  `rule_id` int(11) NOT NULL,
  `premise_id` int(11) NOT NULL,
  `premise_val` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rules_premise`
--

INSERT INTO `rules_premise` (`id`, `rule_id`, `premise_id`, `premise_val`) VALUES
(19, 1, 10, 1),
(20, 1, 11, 1),
(21, 1, 12, 1),
(22, 2, 10, 2),
(40, 7, 2, 1),
(41, 7, 1, 1),
(42, 8, 2, 1),
(43, 8, 1, 2),
(44, 9, 2, 2),
(45, 10, 5, 1),
(46, 10, 8, 1),
(47, 10, 7, 1),
(48, 11, 5, 1),
(49, 11, 8, 2),
(50, 12, 5, 1),
(51, 12, 8, 1),
(52, 12, 7, 2),
(53, 13, 5, 2),
(54, 14, 9, 1),
(55, 15, 9, 2),
(56, 3, 10, 1),
(57, 3, 11, 2),
(58, 4, 10, 1),
(59, 4, 11, 1),
(60, 4, 12, 2),
(61, 16, 13, 1),
(62, 16, 14, 1),
(63, 16, 15, 1),
(64, 16, 16, 1),
(65, 17, 17, 1),
(66, 17, 18, 1),
(67, 17, 19, 1),
(68, 17, 16, 1),
(69, 18, 20, 1),
(70, 18, 19, 1),
(71, 18, 21, 1),
(72, 18, 22, 1),
(73, 19, 17, 1),
(74, 19, 23, 1),
(75, 19, 24, 1),
(76, 19, 25, 1),
(77, 23, 37, 23),
(78, 24, 38, 25),
(79, 24, 39, 25),
(80, 24, 40, 25),
(81, 25, 38, 25),
(82, 25, 39, 26),
(83, 26, 38, 26),
(84, 27, 41, 27),
(85, 27, 42, 27),
(86, 27, 43, 27),
(87, 28, 41, 28),
(88, 28, 42, 28),
(89, 28, 43, 28),
(90, 29, 41, 27),
(91, 29, 42, 27),
(92, 29, 43, 28),
(93, 30, 41, 27),
(94, 30, 42, 28);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `answer`
--
ALTER TABLE `answer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `experts`
--
ALTER TABLE `experts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `premise`
--
ALTER TABLE `premise`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `premise_answer_list`
--
ALTER TABLE `premise_answer_list`
  ADD PRIMARY KEY (`id`),
  ADD KEY `answer_id` (`answer_id`),
  ADD KEY `question_id` (`premise_id`) USING BTREE;

--
-- Indexes for table `premise_rules`
--
ALTER TABLE `premise_rules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `premise_id` (`premise_id`),
  ADD KEY `rule_id` (`rule_id`);

--
-- Indexes for table `rule`
--
ALTER TABLE `rule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `expert_id` (`expert_id`),
  ADD KEY `conclusion_value` (`conclusion_value`);

--
-- Indexes for table `rules_premise`
--
ALTER TABLE `rules_premise`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rule_id` (`rule_id`),
  ADD KEY `question_id` (`premise_id`) USING BTREE,
  ADD KEY `premise_val` (`premise_val`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `answer`
--
ALTER TABLE `answer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `experts`
--
ALTER TABLE `experts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `premise`
--
ALTER TABLE `premise`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `premise_answer_list`
--
ALTER TABLE `premise_answer_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT for table `premise_rules`
--
ALTER TABLE `premise_rules`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `rule`
--
ALTER TABLE `rule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `rules_premise`
--
ALTER TABLE `rules_premise`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=95;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `premise_answer_list`
--
ALTER TABLE `premise_answer_list`
  ADD CONSTRAINT `premise_answer_list_ibfk_1` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`),
  ADD CONSTRAINT `premise_answer_list_ibfk_2` FOREIGN KEY (`premise_id`) REFERENCES `premise` (`id`);

--
-- Constraints for table `premise_rules`
--
ALTER TABLE `premise_rules`
  ADD CONSTRAINT `premise_rules_ibfk_1` FOREIGN KEY (`premise_id`) REFERENCES `premise` (`id`),
  ADD CONSTRAINT `premise_rules_ibfk_2` FOREIGN KEY (`rule_id`) REFERENCES `rule` (`id`);

--
-- Constraints for table `rule`
--
ALTER TABLE `rule`
  ADD CONSTRAINT `rule_ibfk_1` FOREIGN KEY (`expert_id`) REFERENCES `experts` (`id`),
  ADD CONSTRAINT `rule_ibfk_2` FOREIGN KEY (`conclusion_value`) REFERENCES `answer` (`id`);

--
-- Constraints for table `rules_premise`
--
ALTER TABLE `rules_premise`
  ADD CONSTRAINT `rules_premise_ibfk_1` FOREIGN KEY (`premise_id`) REFERENCES `premise` (`id`),
  ADD CONSTRAINT `rules_premise_ibfk_2` FOREIGN KEY (`rule_id`) REFERENCES `rule` (`id`),
  ADD CONSTRAINT `rules_premise_ibfk_4` FOREIGN KEY (`premise_val`) REFERENCES `answer` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
