USE tiw;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user CASCADE;
DROP TABLE IF EXISTS track CASCADE;
DROP TABLE IF EXISTS user_tracks CASCADE;
DROP TABLE IF EXISTS playlist CASCADE;
DROP TABLE IF EXISTS playlist_tracks CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

# TABLE CREATION

CREATE TABLE user
(
    nickname varchar(32) unique not null,
    password varchar(64)        not null,
    name     varchar(32)        not null,
    surname  varchar(32)        not null,

    primary key (nickname)
);

CREATE TABLE track
(
    track_id       integer      not null,
    title          varchar(16)  not null,
    image_path     varchar(128) not null,
    album_title    varchar(16)  not null,
    artist         varchar(16)  not null,
    year           year         not null,
    genre          varchar(16),
    path           varchar(16),
    image_checksum char(64)     not null default '0000000000000000000000000000000000000000000000000000000000000000' comment "SHA256 checksum",
    path_checksum  char(64)     not null default '0000000000000000000000000000000000000000000000000000000000000000' comment "SHA256 checksum",

    primary key (track_id),
    unique (image_checksum),
    unique (path_checksum)
#     check (genre in ('classical', 'rock', 'edm'))
);

CREATE TABLE user_tracks
(
    nickname varchar(32) not null,
    track_id integer     not null,

    primary key (nickname, track_id),
    foreign key (nickname) references user (nickname)
        ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (track_id) references track (track_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE playlist
(
    playlist_id    integer     not null,
    playlist_title varchar(16) not null,
    creation_date  date        not null,
    nickname       varchar(32) not null,

    primary key (playlist_id),
    unique (playlist_title, nickname),
    foreign key (nickname) references user (nickname)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE playlist_tracks
(
    playlist_id  integer not null,
    track_id     integer not null,
    custom_order integer,

    primary key (playlist_id, track_id),
    foreign key (playlist_id) references playlist (playlist_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (track_id) references track (track_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

# DATA LOADING FROM CSVs
# user -> track -> user_tracks -> playlist  -> playlist_tracks

LOAD DATA LOCAL INFILE 'db_data/user.csv'
    INTO TABLE user
    FIELDS TERMINATED BY ','
    ENCLOSED BY ","
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (@ignore, nickname, password, name, surname)
;

LOAD DATA LOCAL INFILE 'db_data/track.csv'
    INTO TABLE track
    FIELDS TERMINATED BY ','
    ENCLOSED BY ","
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (track_id, title, image_path, album_title, artist, year, genre, path, image_checksum, path_checksum)
;

LOAD DATA LOCAL INFILE 'db_data/user_tracks.csv'
    INTO TABLE user_tracks
    FIELDS TERMINATED BY ','
    ENCLOSED BY ","
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (nickname, track_id)
;

LOAD DATA LOCAL INFILE 'db_data/playlist.csv'
    INTO TABLE playlist
    FIELDS TERMINATED BY ','
    ENCLOSED BY ","
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (playlist_id, playlist_title, creation_date, nickname)
;

LOAD DATA LOCAL INFILE 'db_data/playlist_tracks.csv'
    INTO TABLE playlist_tracks
    FIELDS TERMINATED BY ','
    ENCLOSED BY ","
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (playlist_id, track_id, custom_order)
;