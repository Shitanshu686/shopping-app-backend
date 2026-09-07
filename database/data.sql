-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 06, 2026 at 09:22 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopping_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`id`, `user_id`) VALUES
(2, 8),
(1, 9),
(3, 10),
(4, 11);

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`id`, `quantity`, `cart_id`, `product_id`) VALUES
(20, 1, 1, 3),
(39, 1, 2, 12),
(40, 1, 3, 2),
(41, 1, 3, 56),
(43, 1, 4, 7);

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`id`, `comment`, `created_at`, `product_id`, `user_id`) VALUES
(3, 'Samsung Product are just awesome', '2026-08-21 22:45:45.000000', 1, 11),
(4, 'Nice product', '2026-08-22 00:19:37.000000', 1, 8);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `pincode` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `status` enum('CANCELLED','CONFIRMED','DELIVERED','PENDING','SHIPPED') NOT NULL,
  `total_amount` double NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `address`, `city`, `created_at`, `full_name`, `phone`, `pincode`, `state`, `status`, `total_amount`, `user_id`) VALUES
(1, 'BAKOLI', 'Delhi', '2026-08-17 13:57:49.000000', 'Demo User', '9582005020', '110036', 'Delhi', 'DELIVERED', 11497, 9),
(2, 'vjjjhbhj', 'gvghvj', '2026-08-18 14:15:59.000000', 'vhjvvj', '9582005020', '999999', 'bnhjhjhj', 'DELIVERED', 13996, 9),
(3, 'vhvjhjv', 'chvhhg', '2026-08-18 14:26:07.000000', 'Neeraj Sharma', '9582005020', '111111', 'yufg', 'CANCELLED', 10999, 9),
(4, 'fdsfsefs', 'fsdfdsfs', '2026-08-18 14:38:58.000000', 'Tarun', '8920423974', '828282', 'sfdsfds', 'PENDING', 3999, 10),
(5, 'zxxooi', 'xxzjhbhbhb', '2026-08-18 15:18:53.000000', 'xzxz', '7678335622', '110036', 'zzxbjhbhhhhjk', 'SHIPPED', 8999, 9),
(6, 'gvgkvgkgvk', 'gvvghuy', '2026-08-18 15:23:31.000000', 'hjbhjhj', '9999999999', '999999', 'ggyvtg', 'PENDING', 4299, 9),
(7, 'hbcdchjb', 'hjbdhjds', '2026-08-18 15:26:20.000000', 'hbcdchjbd', '9873421065', '999999', 'chbcdshcbj', 'PENDING', 3499, 10),
(8, 'vssvsdvds', 'vsvdsv', '2026-08-18 15:27:51.000000', 'kndsfjkd', '6288841161', '111111', 'sdsvsdvsd', 'PENDING', 99999, 10),
(9, 'West Bengal', 'Kolkata', '2026-08-23 14:43:05.000000', 'Amit Rajput', '9994367589', '722836', 'West Bengal', 'PENDING', 6998, 8),
(10, '12, MG Road, Near Central Mall', 'Mumbai', '2026-08-23 15:02:04.000000', 'Rohan Sharma', '9876543210', '400001', 'Maharashtra', 'PENDING', 3999, 8),
(11, 'Plot No. 45, Jubilee Hills', 'Hyderabad', '2026-08-23 15:28:31.000000', 'Vikram Singh', '8888877777', '500033', 'Telangana', 'PENDING', 8499, 8),
(12, '24, Indiranagar 100 Feet Road', 'Bengaluru', '2026-08-23 15:50:04.000000', 'Deepika Rao', '9444012345', '560038', 'Karnataka', 'PENDING', 3499, 8),
(13, '57/A, Salt Lake City, Sector 2', 'Kolkata', '2026-08-23 15:52:29.000000', 'Amit Choudhury', '9333344444', '700091', 'West Bengal', 'PENDING', 60, 8),
(14, 'Skyline Apartments, Flat 3B, MG Road', 'Kochi', '2026-08-23 15:58:40.000000', 'Siddharth Nair', '9555666777', '682011', 'Kerala', 'CONFIRMED', 4299, 8),
(15, '402, Venus Elegance, CG Road', 'Ahmedabad', '2026-08-23 16:02:03.000000', 'Megha Desai', '9777888999', '380009', 'Gujarat', 'PENDING', 3999, 8),
(16, 'Prabhat Road, Lane No. 5, Near Deccan', 'Pune', '2026-08-23 16:07:43.000000', 'Aditya Joshi', '9222333444', '411004', 'Maharashtra', 'PENDING', 7499, 8),
(17, 'Mahanagar Extension, Near Gomti Nagar', 'Lucknow', '2026-08-23 16:15:42.000000', 'Pooja Mishra', '9666555444', '226006', 'Uttar Pradesh', 'PENDING', 3499, 8),
(18, 'House No. 124, Sector 22-B', 'Chandigarh', '2026-08-23 16:41:21.000000', 'Jaspreet Singh', '9111222333', '160022', 'Punjab', 'PENDING', 8999, 8),
(19, 'Block C, Flat 104, Silver Oak Residency', 'Bhubaneswar', '2026-08-23 16:44:41.000000', 'Rohan Das', '9830012345', '751001', 'Odisha', 'PENDING', 20, 8),
(20, 'vusiivsv', 'ffssdfs', '2026-08-23 17:07:11.000000', 'dff', '9999999332', '222222', 'fsdfsf', 'CONFIRMED', 3499, 8),
(21, 'jskdjsdf', 'dfkjdskj', '2026-08-23 17:08:39.000000', 'jbkdkj', '9222222222', '222333', 'sdfjkds', 'PENDING', 62999, 8),
(22, 'Marine Drive,Mubai', 'Mumbai', '2026-09-02 07:39:37.000000', 'Naveen', '9377789274', '889234', 'Maharashtra', 'DELIVERED', 3499, 11);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `price` double NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `subtotal` double NOT NULL,
  `order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `price`, `product_id`, `product_name`, `quantity`, `subtotal`, `order_id`) VALUES
(1, 3499, 3, 'Noise ColorFit Pro 5', 1, 3499, 1),
(2, 3999, 7, 'American Tourister Pink Cabin Suitcase', 2, 7998, 1),
(3, 3499, 5, 'Safari Marble Print Cabin Trolley', 1, 3499, 2),
(4, 3499, 3, 'Noise ColorFit Pro 5', 3, 10497, 2),
(5, 10999, 8, 'VIP Heritage Trolley Set', 1, 10999, 3),
(6, 3999, 4, 'Nike Revolution 7', 1, 3999, 4),
(7, 8999, 15, 'MRF Genius Grand Edition', 1, 8999, 5),
(8, 4299, 6, 'Aristocrat Printed Travel Trolley', 1, 4299, 6),
(9, 3499, 3, 'Noise ColorFit Pro 5', 1, 3499, 7),
(10, 99999, 1, 'Samsung Galaxy S25 Ultra', 1, 99999, 8),
(11, 3499, 3, 'Noise ColorFit Pro 5', 2, 6998, 9),
(12, 3999, 4, 'Nike Revolution 7', 1, 3999, 10),
(13, 8499, 9, 'Samsonite Executive Cabin Trolley', 1, 8499, 11),
(14, 3499, 5, 'Safari Marble Print Cabin Trolley', 1, 3499, 12),
(15, 60, 29, 'Coca-Cola Original', 1, 60, 13),
(16, 4299, 6, 'Aristocrat Printed Travel Trolley', 1, 4299, 14),
(17, 3999, 4, 'Nike Revolution 7', 1, 3999, 15),
(18, 7499, 16, 'CEAT Hitman English Willow Cricket Bat', 1, 7499, 16),
(19, 3499, 3, 'Noise ColorFit Pro 5', 1, 3499, 17),
(20, 8999, 15, 'MRF Genius Grand Edition', 1, 8999, 18),
(21, 20, 47, 'Uncle Chipps Spicy Treat', 1, 20, 19),
(22, 3499, 3, 'Noise ColorFit Pro 5', 1, 3499, 20),
(23, 62999, 12, 'OnePlus 15', 1, 62999, 21),
(24, 3499, 3, 'Noise ColorFit Pro 5', 1, 3499, 22);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `razorpay_order_id` varchar(255) NOT NULL,
  `razorpay_payment_id` varchar(255) DEFAULT NULL,
  `status` enum('CREATED','FAILED','SUCCESS') NOT NULL,
  `order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `amount`, `created_at`, `razorpay_order_id`, `razorpay_payment_id`, `status`, `order_id`) VALUES
(1, 11497, '2026-08-23 14:06:00.000000', 'order_TT9CFlQkHkyXwR', NULL, 'CREATED', 1),
(2, 6998, '2026-08-23 14:43:06.000000', 'order_TT9pRfg21y2f6b', NULL, 'CREATED', 9),
(3, 3999, '2026-08-23 15:02:04.000000', 'order_TTA9TwZXb2UfmS', 'pay_TTALxe4JWmfewZ', 'SUCCESS', 10),
(4, 8499, '2026-08-23 15:28:32.000000', 'order_TTAbRA4m15zGpP', NULL, 'CREATED', 11),
(5, 4299, '2026-08-23 15:58:41.000000', 'order_TTB7HvJFehMkvO', 'pay_TTB7feDAary0uv', 'SUCCESS', 14),
(6, 3999, '2026-08-23 16:02:04.000000', 'order_TTBAqpx82AH76j', NULL, 'CREATED', 15),
(7, 7499, '2026-08-23 16:07:43.000000', 'order_TTBGpUSSHHO901', NULL, 'CREATED', 16),
(8, 3499, '2026-08-23 16:15:43.000000', 'order_TTBPH0Tg3DuOXb', NULL, 'CREATED', 17),
(9, 8999, '2026-08-23 16:41:22.000000', 'order_TTBqN0bZluQAbK', NULL, 'FAILED', 18),
(10, 20, '2026-08-23 16:44:42.000000', 'order_TTBttSK5fxpiX3', NULL, 'FAILED', 19),
(11, 3499, '2026-08-23 17:07:12.000000', 'order_TTCHfZePfXCtAy', 'pay_TTCHsR5aq2WcXn', 'SUCCESS', 20),
(12, 62999, '2026-08-23 17:08:39.000000', 'order_TTCJCMUFs2knnk', NULL, 'FAILED', 21),
(13, 3499, '2026-09-02 07:39:38.000000', 'order_TWzxHVOt4gr1dg', 'pay_TWzxSlBkM6hDd7', 'SUCCESS', 22);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `old_price` double DEFAULT NULL,
  `rating` double NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `stock` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `brand`, `description`, `price`, `old_price`, `rating`, `image`, `category`, `stock`) VALUES
(1, 'Samsung Galaxy S25 Ultra', 'Samsung', 'Updated flagship phone', 99999, 109999, 4.9, 'images/s25-ultra.jpg', 'Mobiles', 30),
(2, 'Allen Solly Men\'s Casual Shirt', 'Allen Solly', '100% Cotton | Regular Fit | Full Sleeve | Blue', 1499, 2199, 4.5, 'images/shirt.jpg', 'Fashion', 35),
(3, 'Noise ColorFit Pro 5', 'Noise', 'AMOLED Display | Bluetooth Calling | IP68 Water Resistant', 3499, 4999, 4.4, 'images/watch.jpg', 'Watches', 18),
(4, 'Nike Revolution 7', 'Nike', 'Running Shoes | Lightweight | Black', 3999, 5499, 4.7, 'images/shoes.jpg', 'Shoes', 24),
(5, 'Safari Marble Print Cabin Trolley', 'Safari', '55 cm Cabin Luggage | Polycarbonate Hard Shell | 360° Spinner Wheels', 3499, 4999, 4.5, 'images/products/bags/BAG_1.jpg', 'Luggage', 1),
(6, 'Aristocrat Printed Travel Trolley', 'Aristocrat', 'Medium Check-in Suitcase | Lightweight | TSA Lock', 4299, 5899, 4.4, 'images/products/bags/BAG_2.jpg', 'Luggage', 14),
(7, 'American Tourister Pink Cabin Suitcase', 'American Tourister', 'Cabin Size Hard Luggage | Scratch Resistant | Spinner Wheels', 3999, 5599, 4.7, 'images/products/bags/BAG_3.jpg', 'Luggage', 20),
(8, 'VIP Heritage Trolley Set', 'VIP', '3 Piece Premium Luggage Set | Durable Polycarbonate | Spinner Wheels', 10999, 13999, 4.8, 'images/products/bags/BAG_4.jpg', 'Luggage', 8),
(9, 'Samsonite Executive Cabin Trolley', 'Samsonite', 'Business Cabin Suitcase | Premium Leather Finish | 360° Spinner Wheels', 8499, 10999, 4.9, 'images/products/bags/BAG_5.jpg', 'Luggage', 10),
(10, 'OnePlus Ace 5 Pro', 'OnePlus', '12GB RAM | 256GB Storage | Snapdragon 8 Elite | AMOLED Display', 49999, 54999, 4.8, 'images/products/smartphones/SmartPhone_1.jpg', 'Smartphones', 18),
(11, 'Xiaomi 15', 'Xiaomi', '12GB RAM | 256GB Storage | Leica Camera | Snapdragon 8 Elite', 52999, 57999, 4.7, 'images/products/smartphones/SmartPhone_2.jpg', 'Smartphones', 15),
(12, 'OnePlus 15', 'OnePlus', '16GB RAM | 512GB Storage | 120Hz AMOLED | 5G', 62999, 67999, 4.9, 'images/products/smartphones/SmartPhone_3.jpg', 'Smartphones', 10),
(13, 'Google Pixel 10', 'Google', '12GB RAM | 256GB Storage | Tensor G5 Processor', 69999, 74999, 4.8, 'images/products/smartphones/SmartPhone_4.jpg', 'Smartphones', 12),
(14, 'i17 Pro Max', 'i17', '8GB RAM | 256GB Storage | Android 15 | 8000mAh Battery', 18999, 22999, 4.3, 'images/products/smartphones/SmartPhone_5.jpg', 'Smartphones', 25),
(15, 'MRF Genius Grand Edition', 'MRF', 'English Willow Cricket Bat | Short Handle', 8999, 9999, 4.8, 'images/products/BAT/bat_2.jpg', 'BAT', 20),
(16, 'CEAT Hitman English Willow Cricket Bat', 'CEAT', 'English Willow Cricket Bat | Premium Grip | Short Handle', 7499, 8999, 4.7, 'images/products/BAT/bat_3.jpg', 'BAT', 18),
(17, 'Puma EvoPower Cricket Bat', 'Puma', 'Kashmir Willow Cricket Bat | Lightweight | Short Handle', 4299, 5299, 4.5, 'images/products/BAT/bat_4.jpg', 'BAT', 22),
(18, 'Spartan MSD Edition Cricket Bat', 'Spartan', 'English Willow Cricket Bat | Professional Grade | Short Handle', 8999, 10499, 4.8, 'images/products/BAT/bat_5.jpg', 'BAT', 15),
(19, 'Hero Splendor Plus', 'Hero', '97.2cc Engine | 4-Speed Manual | 70 kmpl Mileage | BS6', 85000, 92000, 4.7, 'images/products/BIKE/BIKE_1.jpg', 'BIKE', 12),
(20, 'Royal Enfield Hunter 350', 'Royal Enfield', '349cc Engine | 5-Speed Manual | Retro Roadster', 175000, 189000, 4.8, 'images/products/BIKE/BIKE_2.jpg', 'BIKE', 8),
(21, 'Bajaj Pulsar N160', 'Bajaj', '164.82cc Engine | Dual Channel ABS | LED Projector Headlamp', 145000, 152000, 4.7, 'images/products/BIKE/BIKE_3.jpg', 'BIKE', 10),
(22, 'Aprilia RS 457', 'Aprilia', '457cc Parallel Twin Engine | Quick Shifter | Sport Bike', 420000, 445000, 4.9, 'images/products/BIKE/BIKE_4.jpg', 'BIKE', 5),
(23, 'Hero Passion Pro', 'Hero', '113.2cc Engine | i3S Technology | Excellent Fuel Efficiency', 82000, 89000, 4.5, 'images/products/BIKE/BIKE_5.jpg', 'BIKE', 15),
(24, 'Jaguar F-Pace', 'Jaguar', 'Luxury SUV', 7200000, NULL, 0, 'images/products/CAR/CAR_1.jpg', 'CAR', 5),
(25, 'Lamborghini Temerario', 'Lamborghini', 'Super Sports Car', 65000000, NULL, 0, 'images/products/CAR/CAR_2.jpg', 'CAR', 2),
(26, 'BMW X3', 'BMW', 'Premium SUV', 7500000, NULL, 0, 'images/products/CAR/CAR_3.jpg', 'CAR', 4),
(27, 'Mahindra XEV 9e', 'Mahindra', 'Electric SUV', 3200000, NULL, 0, 'images/products/CAR/CAR_4.jpg', 'CAR', 8),
(28, 'Ford Endeavour', 'Ford', 'Premium SUV', 4500000, NULL, 0, 'images/products/CAR/CAR_5.jpg', 'CAR', 3),
(29, 'Coca-Cola Original', 'Coca-Cola', 'Refreshing soft drink', 60, NULL, 0, 'images/products/COLD_DRINK/cold_Drink_1.jpg', 'COLD_DRINK', 100),
(30, 'Sprite', 'Sprite', 'Lemon-Lime Soft Drink', 60, NULL, 0, 'images/products/COLD_DRINK/cold_Drink_2.jpg', 'COLD_DRINK', 100),
(31, 'Fanta Orange', 'Fanta', 'Orange Flavoured Soft Drink', 60, NULL, 0, 'images/products/COLD_DRINK/cold_Drink_3.jpg', 'COLD_DRINK', 100),
(32, 'Mirinda Orange', 'Mirinda', 'Orange Soft Drink', 60, NULL, 0, 'images/products/COLD_DRINK/cold_Drink_4.jpg', 'COLD_DRINK', 100),
(33, 'Real Mixed Fruit Juice', 'Real', 'Mixed Fruit Juice', 120, NULL, 0, 'images/products/COLD_DRINK/cold_Drink_5.jpg', 'COLD_DRINK', 80),
(34, 'Bournvita', 'Cadbury', 'Health Nutrition Drink', 420, NULL, 0, 'images/products/HEALTHY_DRINK/DRINK_1.jpg', 'HEALTHY_DRINK', 50),
(35, 'Horlicks Classic Malt', 'Horlicks', 'Nutrition Health Drink', 450, NULL, 0, 'images/products/HEALTHY_DRINK/DRINK_2.jpg', 'HEALTHY_DRINK', 50),
(36, 'PowerVita Plus', 'Patanjali', 'Herbal Nutrition Drink', 380, NULL, 0, 'images/products/HEALTHY_DRINK/DRINK_3.jpg', 'HEALTHY_DRINK', 40),
(37, 'Complan', 'Complan', 'Nutrition Drink', 480, NULL, 0, 'images/products/HEALTHY_DRINK/DRINK_4.jpg', 'HEALTHY_DRINK', 45),
(38, 'Horlicks Chocolate Delight', 'Horlicks', 'Chocolate Health Drink', 460, NULL, 0, 'images/products/HEALTHY_DRINK/DRINK_5.jpg', 'HEALTHY_DRINK', 50),
(39, 'The Blue', 'Penshoppe', 'Long Lasting Perfume', 899, NULL, 0, 'images/products/PERFUME/perfume_1.jpg', 'PERFUME', 35),
(40, 'Amalfi Bleu', 'SKINN', 'Premium Eau De Toilette', 1499, NULL, 0, 'images/products/PERFUME/perfume_2.jpg', 'PERFUME', 25),
(41, 'Ocean Breeze Body Mist', 'Aqualogica', 'Refreshing Body Mist', 699, NULL, 0, 'images/products/PERFUME/perfume_3.jpg', 'PERFUME', 30),
(42, 'Blu Eau De Parfum', 'Ajmal', 'Luxury Perfume', 1799, NULL, 0, 'images/products/PERFUME/perfume_4.jpg', 'PERFUME', 20),
(43, 'Vanilla Eau De Parfum', 'Ajmal', 'Sweet Vanilla Fragrance', 1599, NULL, 0, 'images/products/PERFUME/perfume_5.jpg', 'PERFUME', 20),
(44, 'Kurkure Masala Munch', 'Kurkure', 'Crunchy Masala Snack', 20, NULL, 0, 'images/products/SNACK/Snack_1.jpg', 'SNACK', 200),
(45, 'Bikaji Bikaneri Bhujia', 'Bikaji', 'Traditional Bhujia Snack', 120, NULL, 0, 'images/products/SNACK/Snack_2.jpg', 'SNACK', 80),
(46, 'Lay\'s Sizzlin Hot', 'Lay\'s', 'Spicy Potato Chips', 25, NULL, 0, 'images/products/SNACK/Snack_3.jpg', 'SNACK', 200),
(47, 'Uncle Chipps Spicy Treat', 'Uncle Chipps', 'Crispy Potato Chips', 20, NULL, 0, 'images/products/SNACK/Snack_4.jpg', 'SNACK', 150),
(48, 'Too Yumm! Spanish Tomato Chips', 'Too Yumm!', 'Spanish Tomato Flavour Chips', 30, NULL, 0, 'images/products/SNACK/Snack_5.jpg', 'SNACK', 120),
(49, 'Surf Excel Easy Wash', 'Surf Excel', 'Detergent Powder', 120, NULL, 0, 'images/products/SURF/SURF_1.jpg', 'SURF', 70),
(50, 'Surf Excel Easy Wash Powder', 'Surf Excel', 'Powerful Stain Removal', 180, NULL, 0, 'images/products/SURF/SURF_2.jpg', 'SURF', 60),
(51, 'Rin Detergent Powder', 'Rin', 'Bright Wash Detergent', 90, NULL, 0, 'images/products/SURF/SURF_3.jpg', 'SURF', 80),
(52, 'Fena Detergent Powder', 'Fena', 'Laundry Washing Powder', 110, NULL, 0, 'images/products/SURF/SURF_4.jpg', 'SURF', 70),
(53, 'Tide Double Power', 'Tide', 'Advanced Cleaning Detergent', 220, NULL, 0, 'images/products/SURF/SURF_5.jpg', 'SURF', 60),
(56, 'Samsung Galaxy S25', 'Samsung', 'Latest flagship smartphone', 79999, 89999, 4.8, 'images/s25.jpg', 'Mobiles', 25),
(57, 'iPhone 17', 'Apple', 'Latest iPhone', 99999, 109999, 4.9, 'images/iphone17.jpg', 'Mobiles', 10),
(58, 'Boat Airdopes 999', 'Boat', 'Wireless Earbuds', 2999, 3999, 4.6, 'images/boat999.jpg', 'Audio', 40),
(61, 'Sony WH-1000XM6', 'Sony', 'Wireless Noise Cancelling Headphones', 34999, 39999, 4.8, 'images/sony-xm6.jpg', 'Audio', 15),
(62, 'DSC Intense Rage Cricket Bat', 'DSC', 'English Willow Cricket Bat | Premium Grade | Short Handle', 7999, 9499, 4.7, 'images/products/BAT/BAT_1.jpg', 'BAT', 20),
(65, 'Modern Comfort Sofa', 'Urban Living', 'Comfortable modern sofa for living room', 14999, 17999, 4.5, '/uploads/products/70fd1fbb-091b-4c64-a7b2-9e4c944cf699.jpg', 'Furniture', 15),
(66, 'Stainless Steel Kadai', 'Hawkins', 'Durable stainless steel kadai suitable for everyday cooking.', 1299, 1599, 4.5, '/uploads/products/1c295fcc-67f5-47b2-801d-f5a7e5be60d3.jpg', 'Kitchen', 15);

-- --------------------------------------------------------

--
-- Table structure for table `product_ratings`
--

CREATE TABLE `product_ratings` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_ratings`
--

INSERT INTO `product_ratings` (`id`, `product_id`, `rating`, `user_id`) VALUES
(1, 3, 4, 9),
(2, 3, 2, 11);

-- --------------------------------------------------------

--
-- Table structure for table `product_specifications`
--

CREATE TABLE `product_specifications` (
  `id` int(11) NOT NULL,
  `specification_name` varchar(255) DEFAULT NULL,
  `specification_value` varchar(255) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_specifications`
--

INSERT INTO `product_specifications` (`id`, `specification_name`, `specification_value`, `product_id`) VALUES
(1, 'Display', 'AMOLED', 3),
(4, 'Material', 'Polycarbonate', 8),
(5, 'Material', 'PolyCarbonate', 2),
(6, 'ds', 'dad', 5),
(7, 'Battery', '6000mah', 12),
(8, 'Material', 'English Willow Pure', 15),
(9, 'dds', 'ds', 15),
(10, 'Durabilty', '1 Year', 15),
(11, 'Brand Ambassador', 'Virat Kohli', 15),
(12, 'Weight', 'Light', 15),
(13, 'Color', 'Pink', 7),
(14, 'Material', 'Synthetic', 6);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `dark_mode` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `name`, `password`, `role`, `dark_mode`) VALUES
(1, 'shitanshu686@gmail.com', 'Shitanshu Jha', '123456', 'USER', 0),
(4, 'rohit@gmail.com', 'Rohit', '$2a$10$ltXEmaiqY1XiN2m096Mu5uozoeBCe8fiY4ye1B.Iz7l27j0qQiNJu', 'USER', 0),
(5, 'testuser@gmail.com', 'Test User', '$2a$10$5D9Q3wfyw60Xs/gW0MCUTOgt1kfxdfYHuyJ2EEBZOKzg6MDHLUW7i', 'USER', 0),
(6, 'test2@gmail.com', 'Shitanshu Test', '$2a$10$0UjtDdeoult4u0maa0syD.T/hNqxOwsOjURe66idGj27IO.0bwcm6', 'USER', 0),
(7, 'testthree@gmail.com', 'Test Three', '$2a$10$eV/YhPhY/AriiEdo3rveneTDjj61s/B7sukbkNTfcbWAfqQ0cbLSK', 'USER', 0),
(8, 'shopease.test@gmail.com', 'ShopEase Test', '$2a$10$ZFIvSHmGldCNdmz.dTpdjuDo84mUrBfiLwiRwEYBeDCAAczegCzGC', 'USER', 0),
(9, 'usertest@gmail.com', 'User Test', '$2a$10$IZy/q0pvLk4MLt6pZkjqhe8GK.C2.mtEA6IBWeHAkM4azqmA9SmEm', 'USER', 0),
(10, 'neeraj5847585@gmail.com', 'Neeraj', '$2a$10$WtdoCwF5TnwAWzX6c/NyzOCyB5IOFonMFZ2ae0kYLCoTzDH6pDvm2', 'ADMIN', 0),
(11, 'newuser@gmail.com', 'Naman', '$2a$10$QuoFr7Yy8nSzk1TIbJYLH.PkidXkPYZOcgUNonn4jD.Vevk.j0oLm', 'USER', 0);

-- --------------------------------------------------------

--
-- Table structure for table `wishlists`
--

CREATE TABLE `wishlists` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlists`
--

INSERT INTO `wishlists` (`id`, `user_id`) VALUES
(2, 8),
(1, 9),
(3, 10),
(4, 11);

-- --------------------------------------------------------

--
-- Table structure for table `wishlist_items`
--

CREATE TABLE `wishlist_items` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `wishlist_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlist_items`
--

INSERT INTO `wishlist_items` (`id`, `product_id`, `wishlist_id`) VALUES
(10, 3, 1),
(14, 56, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  ADD KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK3t4epkrvwdosm1pvjlkfx33xh` (`user_id`,`product_id`),
  ADD KEY `FKc3p4lovbwrtqqkd3ci5t0g84u` (`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKc3w49re3w3eiexjdnm9khcsd8` (`razorpay_order_id`),
  ADD UNIQUE KEY `UK8vo36cen604as7etdfwmyjsxt` (`order_id`),
  ADD UNIQUE KEY `UK3h326otx9ko45mitb1ptj38bi` (`razorpay_payment_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_ratings`
--
ALTER TABLE `product_ratings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_specifications`
--
ALTER TABLE `product_specifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbets5sov4bn9d2wy8vqathw6d` (`product_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wishlists`
--
ALTER TABLE `wishlists`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKobh8c909a28dx3aqh4cbdhh25` (`user_id`);

--
-- Indexes for table `wishlist_items`
--
ALTER TABLE `wishlist_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK1tt7y773rvi7jkh499ipw7r8w` (`wishlist_id`,`product_id`),
  ADD KEY `FKqxj7lncd242b59fb78rqegyxj` (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `product_ratings`
--
ALTER TABLE `product_ratings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `product_specifications`
--
ALTER TABLE `product_specifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `wishlists`
--
ALTER TABLE `wishlists`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `wishlist_items`
--
ALTER TABLE `wishlist_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`);

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `FKc3p4lovbwrtqqkd3ci5t0g84u` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `FKpwwmhguqianghvi1wohmtsm8l` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Constraints for table `product_specifications`
--
ALTER TABLE `product_specifications`
  ADD CONSTRAINT `FKbets5sov4bn9d2wy8vqathw6d` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Constraints for table `wishlists`
--
ALTER TABLE `wishlists`
  ADD CONSTRAINT `FK330pyw2el06fn5g28ypyljt16` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `wishlist_items`
--
ALTER TABLE `wishlist_items`
  ADD CONSTRAINT `FKkem9l8vd14pk3cc4elnpl0n00` FOREIGN KEY (`wishlist_id`) REFERENCES `wishlists` (`id`),
  ADD CONSTRAINT `FKqxj7lncd242b59fb78rqegyxj` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
