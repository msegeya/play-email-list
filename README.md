Play-Email-List
=================================
Written by Logan Esch

To setup mySQL:
---------------------------------
    CREATE DATABASE play;
    CREATE USER 'user' IDENTIFIED BY 'password';
    GRANT ALL PRIVILEGES ON play.* TO 'play';
    USE play;
    CREATE TABLE tlds (tld VARCHAR(32));
    LOAD DATA INFILE '/path/to/file/tld.txt' INTO TABLE tlds;
