DROP TABLE IF EXISTS BOOK;

CREATE TABLE BOOK (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(250) NOT NULL,
  author varchar(250) NOT NULL,
  genre varchar(250) NOT NULL,
  release_year bigint(5) NOT NULL,
  isbn varchar(17) NOT NULL,
  quantity bigint NOT NULL,
  PRIMARY KEY (id)
  );
