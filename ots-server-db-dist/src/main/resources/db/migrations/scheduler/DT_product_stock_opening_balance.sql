# Host: 13.232.112.42  (Version 5.7.24-log)
# Date: 2019-02-19 13:28:46
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Event "DT_product_stock_opening_balance"
#

DROP EVENT IF EXISTS `DT_product_stock_opening_balance`;
CREATE EVENT `DT_product_stock_opening_balance` ON SCHEDULE EVERY 1 DAY STARTS '2019-02-19 13:00:00' ON COMPLETION NOT PRESERVE ENABLE DO INSERT INTO ots_stock_dist_ob (ots_users_id, ots_product_id, ots_stock_dist_opening_balance,ots_stock_dist_ob_stockdt)
	SELECT ots_users_id, ots_product_id, ots_prodcut_stock_act_qty,CURDATE()
	FROM   ots_product_stock;
