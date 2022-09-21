--liquibase formatted sql

--changeset Stephania:database-initial-setup
create table waiter
(
    id   bigserial primary key,
    code text unique not null
);

create table restaurant_table_status
(
    id   bigserial primary key,
    code text not null
);

create table restaurant_table
(
    id             bigserial primary key,
    code           text unique                               not null,
    status_id      bigint references restaurant_table_status not null,
    waiter_lock_id bigint references waiter
);

create table customer_order
(
    id           bigserial primary key,
    code         text unique                        not null,
    table_id     bigint references restaurant_table not null,
    waiter_id    bigint references waiter           not null,
    priority     numeric                            not null,
    max_wait     numeric                            not null,
    pick_up_time timestamptz                        not null
);

create table dish
(
    id        bigserial primary key,
    code      text    not null,
    wait_time numeric not null
);

create table customer_order_dish
(
    id                bigserial primary key,
    customer_order_id bigint references customer_order not null,
    dish_id           bigint references dish           not null
);

insert into dish (code, wait_time)
values ('PIZZA', 20),
       ('SALAD', 10),
       ('ZEAMA', 7),
       ('SCALLOP_SASHIMI_MEYER_LEMON', 32),
       ('ISLAND_DUCK_MUSTARD', 35),
       ('WAFFLES', 10),
       ('AUBERGINE', 20),
       ('LASAGNA', 30),
       ('BURGER', 15),
       ('GYROS', 15),
       ('KEBAB', 15),
       ('UNAGI_MAKI', 20),
       ('TOBACCO_CHICKEN', 30);

insert into restaurant_table_status(code)
values ('FREE'),
       ('WAITING_FOR_WAITER'),
       ('WAITING_FOR_ORDER'),
       ('ORDER_SERVED');
