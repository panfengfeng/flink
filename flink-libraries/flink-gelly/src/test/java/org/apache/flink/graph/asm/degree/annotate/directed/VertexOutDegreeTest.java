/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.graph.asm.degree.annotate.directed;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.Utils.ChecksumHashCode;
import org.apache.flink.api.java.utils.DataSetUtils;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.asm.AsmTestBase;
import org.apache.flink.test.util.TestBaseUtils;
import org.apache.flink.types.IntValue;
import org.apache.flink.types.LongValue;
import org.apache.flink.types.NullValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VertexOutDegreeTest
extends AsmTestBase {

	@Test
	public void testWithSimpleGraph()
			throws Exception {
		DataSet<Vertex<IntValue, LongValue>> vertexDegrees = directedSimpleGraph
			.run(new VertexOutDegree<IntValue, NullValue, NullValue>()
				.setIncludeZeroDegreeVertices(true));

		String expectedResult =
			"(0,2)\n" +
			"(1,2)\n" +
			"(2,1)\n" +
			"(3,2)\n" +
			"(4,0)\n" +
			"(5,0)";

		TestBaseUtils.compareResultAsText(vertexDegrees.collect(), expectedResult);
	}

	@Test
	public void testWithEmptyGraph()
			throws Exception {
		DataSet<Vertex<LongValue, LongValue>> vertexDegrees;

		vertexDegrees = emptyGraph
			.run(new VertexOutDegree<LongValue, NullValue, NullValue>()
				.setIncludeZeroDegreeVertices(false));

		assertEquals(0, vertexDegrees.collect().size());

		vertexDegrees = emptyGraph
			.run(new VertexOutDegree<LongValue, NullValue, NullValue>()
				.setIncludeZeroDegreeVertices(true));

		String expectedResult =
			"(0,0)\n" +
			"(1,0)\n" +
			"(2,0)";

		TestBaseUtils.compareResultAsText(vertexDegrees.collect(), expectedResult);
	}

	@Test
	public void testWithRMatGraph()
			throws Exception {
		ChecksumHashCode outDegreeChecksum = DataSetUtils.checksumHashCode(directedRMatGraph
			.run(new VertexOutDegree<LongValue, NullValue, NullValue>()
				.setIncludeZeroDegreeVertices(true)));

		assertEquals(902, outDegreeChecksum.getCount());
		assertEquals(0x0000000000e1d885L, outDegreeChecksum.getChecksum());
	}
}
