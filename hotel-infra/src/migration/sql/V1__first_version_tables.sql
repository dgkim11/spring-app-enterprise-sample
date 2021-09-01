USE `booking-hotel`;

DROP TABLE IF EXISTS `booking-hotel`.product;
create table `booking-hotel`.product    (
    productId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '상품 id',
    productName varchar(50) NOT NULL COMMENT '상품명',
    price bigint(20) NOT NULL COMMENT '상품가격',
    categoryId bigint(20) COMMENT '상품 카테고리',
    startSaleDateTime datetime COMMENT '상품 판매 시작 날짜시간',
    endSaleDateTime datetime COMMENT '상품 판매 종료 날짜시간',
    validProduct boolean COMMENT '유효한 상품인지 여부. 유효하지 않은 상품은 판매하지 못한다',
    contents varchar(255) COMMENT '상품 페이지에 나오는 상품 정보 contents',
    createdAt datetime COMMENT '해당 레코드가 생성된 날짜',
    primary key (`productId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='호텔의 상품 정보';

drop table if exists `booking-hotel`.productOption;
create table `booking-hotel`.productOption    (
  optionId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '상품 옵션 id',
  productId bigint(20) NOT NULL COMMENT '해당 옵션을 가지는 상품 id',
  optionName varchar(50) NOT NULL COMMENT '옵션 이름',
  description varchar(255) COMMENT '옵션에 대한 설명 메세지',
  price bigint(20) NOT NULL COMMENT '옵션 가격',
  createdAt datetime COMMENT '해당 레코드가 생성된 날짜',

  primary key (`productId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품별 옵션 정보';
