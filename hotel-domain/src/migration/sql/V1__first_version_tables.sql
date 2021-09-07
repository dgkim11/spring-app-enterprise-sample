USE `booking-hotel`;

DROP TABLE IF EXISTS product;
create table product    (
    productId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '상품 id',
    productName varchar(255) NOT NULL COMMENT '상품명',
    price bigint(20) NOT NULL COMMENT '상품가격',
    startSaleDateTime datetime COMMENT '상품 판매 시작 날짜시간',
    endSaleDateTime datetime COMMENT '상품 판매 종료 날짜시간',
    validProduct boolean COMMENT '유효한 상품인지 여부. 유효하지 않은 상품은 판매하지 못한다',
    contents varchar(255) COMMENT '상품 페이지에 나오는 상품 정보 contents',
    outOfStock boolean COMMENT '상품의 품절 여부',
    createdAt datetime COMMENT '해당 레코드가 생성된 날짜',
    primary key (`productId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='호텔의 상품 정보';

drop table if exists productOption;
create table productOption    (
  optionId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '상품 옵션 id',
  productId bigint(20) NOT NULL COMMENT '해당 옵션을 가지는 상품 id',
  optionName varchar(50) NOT NULL COMMENT '옵션 이름',
  description varchar(255) COMMENT '옵션에 대한 설명 메세지',
  price bigint(20) NOT NULL COMMENT '옵션 가격',
  createdAt datetime COMMENT '해당 레코드가 생성된 날짜',

  primary key (`productId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='상품별 옵션 정보';

drop table if exists customer;
create table customer    (
  customerId bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  accountId varchar(12) NOT NULL COMMENT '사용자 계정 id',
  password varchar(50) NOT NULL COMMENT '사용자 계정 비밀번호',
  name varchar(50) NOT NULL COMMENT '사용자 이름',
  emailAddr varchar(50) NOT NULL COMMENT '이메일 주소',
  createdAt datetime COMMENT '해당 레코드가 생성된 날짜',

  primary key (`customerId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='고객 정보';

drop table if exists bookingCartItem;
create table bookingCartItem (
     cartItemId bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     customerId bigint(20) NOT NULL,
     productId bigint(20) NOT NULL,
     bookingDate datetime COMMENT '예약일자',
     primary key (`cartItemId`)

)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='장바구니 아이템';

drop table if exists bookingCartItemOption;
create table bookingCartItemOption  (
    cartItemId bigint(20) NOT NULL,
    productOptionId bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='장바구니에 있는 상품의 옵션 정보';