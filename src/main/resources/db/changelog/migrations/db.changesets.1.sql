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
    id   bigserial primary key,
    code text not null
);

create table customer_order_dish
(
    id                bigserial primary key,
    customer_order_id bigint references customer_order not null,
    dish_id           bigint references dish           not null
);

insert into dish (code)
values ('PIZZA'),
       ('SALAD'),
       ('ZEAMA'),
       ('SCALLOP_SASHIMI_MEYER_LEMON'),
       ('ISLAND_DUCK_MUSTARD'),
       ('WAFFLES'),
       ('AUBERGINE'),
       ('LASAGNA'),
       ('BURGER'),
       ('GYROS'),
       ('KEBAB'),
       ('UNAGI_MAKI'),
       ('TOBACCO_CHICKEN');

insert into restaurant_table_status(code)
values ('FREE'),
       ('WAITING_FOR_WAITER'),
       ('WAITING_FOR_ORDER'),
       ('ORDER_SERVED');
