CREATE TABLE payment_details_eth (
  id SERIAL PRIMARY KEY ,
  rx_address VARCHAR(66),
  value NUMERIC (78, 0),
  shop VARCHAR(66),
  status VARCHAR(20)
);