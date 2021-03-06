/**
 * Copyright (C) 2013 Alessandro <alessandro dot leite at alessandro dot cc>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gridobservatory.greencomputing.dao;

import java.util.Collections;

import org.gridobservatory.greencomputing.adapters.Motherboard;
import org.gridobservatory.greencomputing.repository.MotherboardRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MotherboardDao extends DaoSupport implements MotherboardRepository {

	@Override
	public void insert(Motherboard motherboard) {
		this.insert(Collections.singleton(motherboard));
	}

	public void insert(Iterable<Motherboard> motherboards) {
		for (Motherboard motherboard : motherboards) {
			try{
			this.getJdbcTemplate()
					.update("insert into motherboard (motherboard_id, product_manufacturer, product_name, port_number, "
							+ " board_serial, board_manufacturing, board_product, manufacturing_date, date_from) "
							+ " values (?,?,?,?,?,?,?,?,?)",

							motherboard.getMotherboardID(),
							motherboard.getProductManufacturer(),
							motherboard.getProductName(),
							motherboard.getPartNumber(),
							motherboard.getBoardSerial(),
							motherboard.getBoardManufacturing(),
							motherboard.getBoardProduct(),
							motherboard.getManufacturingDate(),
							motherboard.getDateFrom() != null ? motherboard.getDateFrom().toDate() : null);
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
}