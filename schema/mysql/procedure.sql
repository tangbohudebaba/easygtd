DROP PROCEDURE IF EXISTS gen_next_sequence_pk;
DELIMITER $$

CREATE
    /**
    *	获得下一个序列值,不过不存在创建一个新对象
    *	param_1: 对应序列Key值
    *	param_2: 返回下一个值
    */
    PROCEDURE gen_next_sequence_pk(IN i_name VARCHAR(128) , OUT o_value BIGINT)
    /*LANGUAGE SQL
    | [NOT] DETERMINISTIC
    | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
    | SQL SECURITY { DEFINER | INVOKER }
    | COMMENT 'string'*/
    BEGIN

	DECLARE t_current BIGINT DEFAULT -1;

	INSERT INTO nsc_meap_sequence(id,current,step) VALUES(i_name,100,1) ON DUPLICATE KEY UPDATE current = current + step;

	SELECT current INTO t_current FROM nsc_meap_sequence WHERE id = i_name;	
	
	SET o_value = t_current;

    END$$

DELIMITER ;