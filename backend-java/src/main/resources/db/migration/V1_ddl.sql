create table if not exists tb_address (
	postal_code varchar(9) not null,
	street varchar(100) not null,
	house_number varchar(10) null,
	complement varchar(50) null,
	district varchar(50) not null,
	city varchar(50) not null,
	state varchar(2) not null,
	created_at timestamptz(6) not null,
	updated_at timestamptz(6) not null,
	constraint tb_address_pk primary key (postal_code)
);

create table if not exists tb_api_exception (
	id uuid not null,
	created_at timestamptz(6) not null,
	status int4 not null,
	message varchar(255) not null,
	request_path varchar(255) not null,
	constraint tb_api_exception_pk primary key (id)
);

create table if not exists tb_product (
	id uuid not null,
	title varchar(100) not null,
	description varchar(255) not null,
	quantity_in_stock int4 not null,
	price numeric(38,2) not null,
	validity_number_of_days int4 not null,
	images_url _varchar null,
	created_at timestamptz(6) not null,
	updated_at timestamptz(6) not null,
	constraint tb_product_pk primary key (id),
	constraint tb_product_title_uk unique (title)
);

create table if not exists tb_user (
	id uuid not null,
	full_name varchar(50) not null,
	cpf varchar(11) not null,
	date_of_birth date not null,
	email varchar(50) not null,
	mobile varchar(11) not null,
	address_fk varchar(9) not null,
	image_url varchar(255) null,
	created_at timestamptz(6) not null,
	password_hash varchar(255) not null,
	role_name varchar(255) not null,
	updated_at timestamptz(6) not null,
	constraint tb_user_cpf_uk unique (cpf),
	constraint tb_user_email_uk unique (email),
	constraint tb_user_mobile_uk unique (mobile),
	constraint tb_user_pk primary key (id),
	constraint tb_user_role_name_check check (((role_name)::text = any ((array['ADMIN'::character varying,
	'USER'::character varying])::text[]))),
	constraint tb_user_address_fk foreign key (address_fk) references tb_address(postal_code)
);

create table if not exists tb_leasing_cart (
	id uuid not null,
	total_price numeric(38,2) not null,
	first_expiration_date timestamptz(6) not null,
	last_expiration_date timestamptz(6) not null,
	user_fk uuid not null,
	created_at timestamptz(6) not null,
	updated_at timestamptz(6) not null,
	constraint tb_leasing_cart_pk primary key (id),
	constraint tb_leasing_cart_user_fk_uk unique (user_fk),
	constraint tb_leasing_cart_user_fk foreign key (user_fk) references tb_user(id)
);

create table if not exists tb_item_leasing (
	id uuid not null,
	quantity_to_leased int4 not null,
	subtotal_price numeric(38,2) not null,
	expiration_date timestamptz(6) not null,
	leasing_fk uuid not null,
	product_fk uuid not null,
	created_at timestamptz(6) not null,
	updated_at timestamptz(6) not null,
	constraint tb_item_leasing_pk primary key (id),
	constraint tb_item_leasing_leasing_fk foreign key (leasing_fk) references tb_leasing(id),
	constraint tb_item_leasing_product_fk foreign key (product_fk) references tb_product(id)
);