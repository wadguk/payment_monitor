CREATE TABLE payment_requests_paymentrequest (
  id SERIAL PRIMARY KEY ,
  duc_address VARCHAR(66),
  value NUMERIC (78, 0),
  shop VARCHAR(66),
  status VARCHAR(20)
);