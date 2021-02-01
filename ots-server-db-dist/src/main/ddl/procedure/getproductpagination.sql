# Host: 156.67.222.106  (Version 5.5.5-10.4.15-MariaDB)
# Date: 2021-02-01 14:14:29
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Procedure "getproductpagination"
#

DROP PROCEDURE IF EXISTS `getproductpagination`;
CREATE PROCEDURE `getproductpagination`(
  IN starton int, 
  IN bsize int, 
  IN productLevel int, 
  IN productcategoryid int, 
  IN productstatus varchar(20)
)
BEGIN
IF productstatus = 'ALL' THEN
		SELECT 
		  * 
		FROM 
		  ots_product p 
		WHERE 
		  p.ots_product_level_id = productLevel 
		ORDER BY 
		  p.ots_product_id 
		limit 
		  starton, 
		  bsize;
ELSEIF(productstatus = 'subCategory') THEN 
		SELECT 
		  * 
		FROM 
		 ots_product p, 
		 ots_product_category_mapping pcm 
		WHERE 
		 p.ots_product_level_id = productLevel 
	  AND
		 pcm.ots_product_category_id = productcategoryid 
	  AND 
			pcm.ots_product_id = p.ots_product_id 
		ORDER BY 
			p.ots_product_id ;
ELSEIF(productstatus = 'category') THEN 
		SELECT 
			DISTINCT	(p.ots_product_id),
			p.ots_product_name,
			p.ots_product_description,
			p.ots_product_price,
			p.ots_product_status,
			p.ots_product_image,
			p.ots_product_type,
			p.ots_product_gst,
			p.ots_product_threshold_day,
			p.ots_product_base_price		
		FROM 
			ots_product p, 
			ots_product_category_mapping pcm 
		WHERE 
		 p.ots_product_level_id = productLevel
			AND
		  p.ots_product_id IN (
		SELECT 
		 DISTINCT  p.ots_product_id
		FROM
			ots_product p,
			ots_product_category_mapping pcm 
		WHERE
			pcm.ots_product_category_id = productcategoryid
		)	
		ORDER BY 
			p.ots_product_id
		LIMIT
			starton, 
		  bsize;
		END IF;
END;
