-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 03, 2018 at 04:48 PM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `experts2_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `answer`
--

CREATE TABLE `answer` (
  `id` int(11) NOT NULL,
  `answer` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `answer`
--

INSERT INTO `answer` (`id`, `answer`) VALUES
(1, 'YA'),
(2, 'NO');

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
(2, 'Smartphone', 'diagnosa kerusakan pada Smartphone');

-- --------------------------------------------------------

--
-- Table structure for table `premise`
--

CREATE TABLE `premise` (
  `id` int(11) NOT NULL,
  `question` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `premise`
--

INSERT INTO `premise` (`id`, `question`) VALUES
(1, 'lulus test math ?'),
(2, 'lulus test b.ing ?'),
(5, 'tidak memiliki mobil pribadi ?'),
(7, 'pemakaian daya listrik di rekening <= 2200VA'),
(8, 'uang gaji perbulan <= 1jt, dari ortu ?'),
(9, 'ayah/ibu memiliki nama marga asli putra daerah ?'),
(10, 'Akademik'),
(11, 'Finansial'),
(12, 'Putra daerah'),
(13, 'Terdapat garis pada layar ?'),
(14, 'Layar pecah ? '),
(15, 'Layar buram ? '),
(16, 'White screen ? '),
(17, 'Baterai drop ?'),
(18, 'Smartphone panas ?'),
(19, 'blank screen ? '),
(20, 'Tidak dapat memproses Aplikasi ?'),
(21, 'Smartphone reboot tiba-tiba ?'),
(22, 'Tidak berhenti mengisi daya ?'),
(23, 'Baterai cepat panas ?'),
(24, 'Baterai menggelembung ? '),
(25, 'Tidak berhenti mengisi daya ?');

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
(46, 25, 2);

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
  `hierarchy` int(11) NOT NULL
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
(19, 'Baterai', 1, 2, 1);

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
(76, 19, 25, 1);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `experts`
--
ALTER TABLE `experts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `premise`
--
ALTER TABLE `premise`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `premise_answer_list`
--
ALTER TABLE `premise_answer_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `premise_rules`
--
ALTER TABLE `premise_rules`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `rule`
--
ALTER TABLE `rule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `rules_premise`
--
ALTER TABLE `rules_premise`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

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
