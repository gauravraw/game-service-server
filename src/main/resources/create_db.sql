-- Scheme creation query
CREATE SCHEMA if not exists `game` ;

-- Player details table
Create table if not exists game.player_details (
id BIGINT AUTO_INCREMENT NOT NULL,
name VARCHAR(100) NOT NULL,
team VARCHAR(100) ,
status VARCHAR(20) NOT NULL Default 'ACTIVE',
created_at TIMESTAMP NOT NULL Default CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
Index STATUS_IDX(status)
);

-- Score details table
Create table if not exists game.score_details (
id BIGINT AUTO_INCREMENT NOT NULL,
player_id BIGINT NOT NULL,
score BIGINT NOT NULL default 0,
created_at TIMESTAMP NOT NULL Default CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
Index PLAYER_ID_IDX(player_id)
);