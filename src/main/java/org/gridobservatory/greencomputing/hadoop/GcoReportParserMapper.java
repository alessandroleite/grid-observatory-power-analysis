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
package org.gridobservatory.greencomputing.hadoop;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import lshw.parser.xml.jaxb.JaxbXmlParser;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.gridobservatory.greencomputing.xml.types.GCOReport;

public class GcoReportParserMapper extends
		Mapper<LongWritable, Text, NullWritable, GCOReport> {
	
	private static final Logger log = Logger
			.getLogger(GcoReportParserMapper.class.getName());

	private final static JaxbXmlParser<GCOReport> JAXB_PARSER;

	static {
		try {
			JAXB_PARSER = new JaxbXmlParser<GCOReport>(GCOReport.class);
		} catch (JAXBException exception) {
			throw new RuntimeException(exception.getMessage(), exception);
		}
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		try {
			context.write(NullWritable.get(),
					JAXB_PARSER.unmarshal(value.toString()));
		} catch (JAXBException exception) {
			log.error(exception.getMessage(), exception);
		}
	}
}