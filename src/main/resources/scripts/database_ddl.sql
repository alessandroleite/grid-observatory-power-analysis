--
-- Copyright (C) 2013 Alessandro <alessandro dot leite at alessandro dot cc>
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

create database grid_observatory_grif_lal_power;

use grid_observatory_grif_lal_power;

create table sensor(
  sensor_id bigint unsigned not null primary key,
  sensor_name varchar(120) not null,
  acquisition_tool_type varchar(60) not null,
  unit varchar(30) not null
);
-- insert into sensor (sensor_id, sensor_name, acquisition_tool_type, unit) values (?,?,?,?)

create table motherboard (
 motherboard_id bigint unsigned not null primary key,
 product_manufacturer varchar(150),
 product_name varchar(150),
 port_number varchar(20),
 board_serial varchar(60),
 board_manufacturing varchar(120),
 board_product varchar(150),
 manufacturing_date varchar(60),
 date_from timestamp not null
);

-- insert into motherboard (motherboard_id, product_manufacturer, product_name, port_number, board_serial, board_manufacturing, board_product, manufacturing_date, date_from) values (?,?,?,?,?,?,?,?,?)

create table middleware (
	middleware_id bigint unsigned not null primary key,
	middleware_type varchar(30) not null,
	product_name varchar(200) not null,
	product_version varchar(120),
	kernel_name varchar(120),
	kernel_version varchar(120),
	arch varchar(120)
);

-- insert into middleware (middleware_id,middleware_type,product_name,product_version,kernel_name,kernel_version,arch) values (?,?,?,?,?,?)

create table room (
  room_id bigint unsigned not null primary key,
  date_created timestamp not null
);

-- insert into room (room_id, date_created) values (?,?)

create table machine (
  machine_id bigint unsigned not null primary key,
  room_id bigint unsigned not null references room(room_id),
  motherboard_id bigint unsigned not null references motherboard(motherboard_id),
  middleware_id bigint unsigned not null references middleware(middleware_id),
  date_created timestamp not null,
  date_retired timestamp  
);

-- insert into machine (machine_id, room_id, motherboard_id, middleware_id, date_created, date_retired) values (?,?,?,?,?,?)

create table time_series (
  time_series_id bigint unsigned not null auto_increment primary key,
  constant_value varchar(60), 
  start_date timestamp not null,
  end_date timestamp not null,
  acquisition_count bigint unsigned not null
 );
 
 -- insert into time_series (time_series_id, constant_value,start_date,end_date,acquisition_count ) values (?,?,?,?,?)
 
 create table time_series_acquisition (
  time_series_acquisition bigint unsigned not null auto_increment primary key,
  time_series_id bigint unsigned not null references time_series (time_series_id),
  ts bigint not null, 
  value varchar(160) not null
 );

 
 -- insert into time_series_acquisition (time_series_id, ts, value) values (?,?,?)
 
 create table machine_time_series (
   time_series_id bigint unsigned not null primary key references time_series (time_series_id),
   machine_id bigint unsigned not null references machine (machine_id),
   sensor_id bigint unsigned not null references sensor (sensor_id)
 );
 
 -- insert into machine_time_series (time_series_id, machine_id, sensor_id) values (?,?, ?)
 
 create table room_time_series (
   time_series_id bigint unsigned not null primary key references time_series (time_series_id),
   room_id bigint unsigned not null references room (room_id),
   sensor_id bigint unsigned not null references sensor (sensor_id)
 );
 
 -- insert into room_time_series (time_series_id, room_id, sensor_id) values (?, ?, ?)
 
 -----------------------------
 --         Views			--
 -----------------------------
 create or replace view vw_machine as 
 	select 	
 			m.machine_id, 
    	   	m.room_id, 
       		m.motherboard_id, mb.product_manufacturer, mb.product_name,
       		m.middleware_id, mw.middleware_type, mw.product_name as middleware_name, mw.product_version, mw.kernel_name, mw.kernel_version, mw.arch,
       		m.date_created, m.date_retired
	from machine m
  		join room r on r.room_id = m.room_id
  		join motherboard mb on mb.motherboard_id = m.motherboard_id
  		join middleware mw on mw.middleware_id = m.middleware_id
  	order by m.machine_id
;  	

create or replace view vw_machine_time_series as
  	select 
  		mts.time_series_id, mts.machine_id, mts.sensor_id,
  		ts.constant_value, ts.start_date, ts.end_date, ts.acquisition_count,  			
  		s.sensor_name, s.acquisition_tool_type, s.unit
  	from machine_time_series mts
  		join time_series ts on ts.time_series_id = mts.time_series_id
  		join sensor s on s.sensor_id = mts.sensor_id
  	order by machine_id
;

create or replace view vw_room_time_series as
  	select 
  		rts.time_series_id, rts.room_id, rts.sensor_id,
  		ts.constant_value, ts.start_date, ts.end_date, ts.acquisition_count,  			
  		s.sensor_name, s.acquisition_tool_type, s.unit
  	from room_time_series rts
  		join time_series ts on ts.time_series_id = rts.time_series_id
  		join sensor s on s.sensor_id = rts.sensor_id
  	order by room_id  	
 ;	