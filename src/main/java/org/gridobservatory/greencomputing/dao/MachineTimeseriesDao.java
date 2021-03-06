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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.gridobservatory.greencomputing.repository.MachineTimeseriesRepository;
import org.gridobservatory.greencomputing.xml.types.MachineTimeseriesType;
import org.springframework.stereotype.Repository;

@Repository
public class MachineTimeseriesDao extends TimeseriesDao<MachineTimeseriesType>
		implements MachineTimeseriesRepository {

	@Override
	public BigInteger insert(MachineTimeseriesType machineTimeseriesType) {
		final BigInteger id = super.insert(machineTimeseriesType);
		this.getJdbcTemplate()
				.update("insert into machine_time_series (time_series_id, machine_id, sensor_id) values (?,?,?)",
						id, machineTimeseriesType.getMachineID(),
						machineTimeseriesType.getSensorID());
		return id;
	}

	@Override
	public List<BigInteger> insert(Iterable<MachineTimeseriesType> timeseries) {
		List<BigInteger> ids = new ArrayList<>();
		for (MachineTimeseriesType machineTimeseriesType : timeseries) {
			ids.add(this.insert(machineTimeseriesType));
		}
		return ids;
	}
}