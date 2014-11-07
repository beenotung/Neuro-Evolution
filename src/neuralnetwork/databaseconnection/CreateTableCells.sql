CREATE TABLE `cells` (
  `cell_id` int(11) NOT NULL AUTO_INCREMENT,
  `layer_id` int(11) DEFAULT NULL,
  `bias` double DEFAULT '0',
  `activation` double DEFAULT '0',
  `delta_bias` double DEFAULT '0',
  `delta_error` double DEFAULT '0',
  PRIMARY KEY (`cell_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
