INSERT INTO Bank (bank_name, bank_address)
VALUES ('Belarusbank', 'Nezavisimosti Ave 95, Minsk'),
       ('Belagroprombank', 'Kirova St 49, Minsk'),
       ('BPS-Sberbank', 'Surganova St 43, Minsk'),
       ('Belinvestbank', 'Pobediteley Ave 49, Minsk'),
       ('Priorbank', 'Dzerzhinsky Ave 57, Minsk'),
       ('MTBank', 'Komsomolskaya St 10, Minsk'),
       ('Alfa-Bank', 'Pobediteley Ave 84, Minsk'),
       ('BelVEB', 'Pobediteley Ave 29, Minsk'),
       ('VTB Bank', 'Nezavisimosti Ave 11, Minsk'),
       ('Home Credit Bank', 'Nezavisimosti Ave 58, Minsk');
	   
INSERT INTO "User" (user_name, user_email, user_phone)
VALUES ('Alexey Ivanov', 'alexey.ivanov@gmail.com', '+375291234567'),
       ('Maria Petrova', 'maria.petrova@yahoo.com', '+375292345678'),
       ('Dmitry Sidorov', 'dmitry.sidorov@outlook.com', '+375293456789'),
       ('Anna Kuznetsova', 'anna.kuznetsova@mail.ru', '+375294567890'),
       ('Sergey Smirnov', 'sergey.smirnov@yandex.ru', '+375295678901'),
       ('Elena Popova', 'elena.popova@icloud.com', '+375296789012'),
       ('Andrey Sokolov', 'andrey.sokolov@protonmail.com', '+375297890123'),
       ('Irina Pavlova', 'irina.pavlova@zoho.com', '+375298901234'),
       ('Nikolay Vasiliev', 'nikolay.vasiliev@bk.ru', '+375299012345'),
       ('Olga Morozova', 'olga.morozova@rambler.ru', '+375291112233'),
	   ('Vladimir Petrov', 'vladimir.petrov@gmail.com', '+375291223344'),
       ('Natalia Ivanova', 'natalia.ivanova@yahoo.com', '+375292334455'),
       ('Mikhail Sidorov', 'mikhail.sidorov@outlook.com', '+375293445566'),
       ('Ekaterina Smirnova', 'ekaterina.smirnova@mail.ru', '+375294556677'),
       ('Igor Kuznetsov', 'igor.kuznetsov@yandex.ru', '+375295667788'),
       ('Tatiana Popova', 'tatiana.popova@icloud.com', '+375296778899'),
       ('Roman Sokolov', 'roman.sokolov@protonmail.com', '+375297889900'),
       ('Oksana Pavlova', 'oksana.pavlova@zoho.com', '+375298990011'),
       ('Denis Vasiliev', 'denis.vasiliev@bk.ru', '+375299100122'),
       ('Yulia Morozova', 'yulia.morozova@rambler.ru', '+375291213141'),
       ('Maxim Smirnov', 'maxim.smirnov@gmail.com', '+375291324252'),
       ('Veronika Ivanova', 'veronika.ivanova@yahoo.com', '+375292435363'),
       ('Anton Sidorov', 'anton.sidorov@outlook.com', '+375293546474'),
       ('Anastasia Smirnova', 'anastasia.smirnova@mail.ru', '+375294657585'),
       ('Evgeny Kuznetsov', 'evgeny.kuznetsov@yandex.ru', '+375295768696'),
       ('Svetlana Popova', 'svetlana.popova@icloud.com', '+375296879707'),
       ('Alexandr Sokolov', 'alexandr.sokolov@protonmail.com', '+375297980818'),
       ('Galina Pavlova', 'galina.pavlova@zoho.com', '+375298091929'),
       ('Kirill Vasiliev', 'kirill.vasiliev@bk.ru', '+375299102030'),
       ('Polina Morozova', 'polina.morozova@rambler.ru', '+375291414242');

INSERT INTO Account (account_number, account_balance, account_type, user_id, bank_id)
VALUES ('BY01BLBB3012000000010000', 1000.00, 'savings', 1, 1),
       ('BY02BLBB3012000000020000', 500.00, 'checking', 1, 1),
       ('BY03BLBB3012000000030000', 1500.00, 'savings', 2, 1),
       ('BY04BLBB3012000000040000', 2000.00, 'checking', 3, 1),
       ('BY05BLBB3012000000050000', 2500.00, 'savings', 4, 1),
       ('BY06BLBB3012000000060000', 3000.00, 'checking', 5, 1),
       ('BY07BLBB3012000000070000', 3500.00, 'savings', 6, 1),
       ('BY08BLBB3012000000080000', 4000.00, 'checking', 7, 1),
       ('BY09BLBB3012000000090000', 4500.00, 'savings', 8, 1),
       ('BY10BLBB3012000000100000', 5000.00, 'checking', 9, 1),
	   ('BY11BLBB3012000000110000', 5500.00, 'savings', 10, 1),
       ('BY12BLBB3012000000120000', 6000.00, 'checking', 11, 1),
       ('BY13BLBB3012000000130000', 6500.00, 'savings', 12, 1),
       ('BY14BLBB3012000000140000', 7000.00, 'checking', 13, 1),
       ('BY15BLBB3012000000150000', 7500.00, 'savings', 14, 1),
       ('BY16BLBB3012000000160000', 8000.00, 'checking', 15, 1),
       ('BY17BLBB3012000000170000', 8500.00, 'savings', 16, 1),
       ('BY18BLBB3012000000180000', 9000.00, 'checking', 17, 1),
       ('BY19BLBB3012000000190000', 9500.00, 'savings', 18, 1),
       ('BY20BLBB3012000000200000', 10000.00, 'checking' ,19 ,1),
       ('BY21BGPK3012000000210000' ,11000.00 ,'savings' ,1 ,2),
       ('BY22BPSB3012000000220000' ,12000.00 ,'checking' ,2 ,3),
       ('BY23BIBB3012000000230000' ,13000.00 ,'savings' ,3 ,4),
       ('BY24PRRB3012000000240000' ,14000.00 ,'checking' ,4 ,5),
       ('BY25MTBK3012000000250000' ,15000.00 ,'savings' ,5 ,6),
       ('BY26ALFA3012000000260000' ,16000.00 ,'checking' ,6 ,7),
       ('BY27BELB3012000000270000' ,17000.00 ,'savings' ,7 ,8),
       ('BY28AKBB3012000000280000' ,18000.00 ,'checking' ,8 ,9),
       ('BY29HOMC3012000000290000' ,19000.00 ,'savings' ,9 ,10),
       ('BY30BLBB3012000000300000' ,20000.00 ,'checking' ,10 ,1),
       ('BY31BGPK3012000000310000' ,21000.00 ,'savings' ,11 ,2),
       ('BY32BPSB3012000000320000' ,22000.00 ,'checking' ,12 ,3),
       ('BY33BIBB3012000000330000' ,23000.00 ,'savings' ,13 ,4),
       ('BY34PRRB3012000000340000' ,24000.00 ,'checking' ,14 ,5),
       ('BY35MTBK3012000000350000' ,25000.00 ,'savings' ,15 ,6),
       ('BY36ALFA3012000000360000' ,26000.00 ,'checking' ,16 ,7),
       ('BY37BELB3012000000370000' ,27000.00 ,'savings' ,17 ,8),
       ('BY38AKBB3012000000380000' ,28000.00 ,'checking' ,18 ,9),
       ('BY39HOMC3012000000390000' ,29000.00 ,'savings' ,19,10),
       ('BY40BLBB3012000000400000', 30000.00, 'checking', 20, 1);

INSERT INTO "Transaction" (transaction_date, transaction_amount, transaction_type, account_id, recipient_account_id)
VALUES ('2023-08-01', 100.00, 'replenishment', 1, 1),
       ('2023-08-02', 200.00, 'withdrawal', 2, 2),
       ('2023-08-03', 300.00, 'transfer', 3, 4),
       ('2023-08-04', 400.00, 'replenishment', 5, 5),
       ('2023-08-05', 500.00, 'withdrawal', 6, 6),
       ('2023-08-06', 600.00, 'transfer', 7, 8),
       ('2023-08-07', 700.00, 'replenishment', 9, 9),
       ('2023-08-08', 800.00, 'withdrawal', 10, 10),
       ('2023-08-09', 900.00, 'transfer', 1, 2),
       ('2023-08-10', 1000.00, 'replenishment', 3, 3);