package com.mgarcia.qnumberformat.test;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.mgarcia.qnumberformat.api.QNumberFormatNotation;


/*
 * 	Licensed to the Apache Software Foundation (ASF) under one
	or more contributor license agreements.  See the NOTICE file
	distributed with this work for additional information
	regarding copyright ownership.  The ASF licenses this file
	to you under the Apache License, Version 2.0 (the
	"License"); you may not use this file except in compliance
	with the License.  You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	KIND, either express or implied.  See the License for the
	specific language governing permissions and limitations
	under the License.
 */

public class QFormatFixedPointTest {

	public static void main(String[] args) {
		
		/* 
		 * https://en.wikipedia.org/wiki/Q_(number_format)#Q_to_float
		 *  s   3           12
		 * [0]0[01].[0100.0000.0000] = 0x1400
		 * Float 1.25 - s3.12 Format -> 0x1400 -> 16bits 
		 * 
		 *  s   3           12
		 * [1]1[11].[0100.0000.0000] = 0x1400 -> -1.25
		 * Float 1.25 - s3.12 Format -> 0xe400 -> 16bits 
		 * */
		String s16Bits3_12 = "e4:00";
		System.out.println("+-------------------------------------------------------------------------------------+");
		byte[] baS3_12Format = QFormatFixedPointTest.ToByteArray(s16Bits3_12);
		QNumberFormatNotation qtd = new QNumberFormatNotation("s3.12",baS3_12Format,QNumberFormatNotation.BIG_ENDIAN);	
		System.out.println(qtd.toString());
		System.out.println("Double: " + qtd.toDouble());
		System.out.println("Float: " + qtd.toFloat());
		
		
		System.out.println("+-------------------------------------------------------------------------------------+");
		baS3_12Format = QFormatFixedPointTest.ToByteArray(s16Bits3_12);	
		qtd = new QNumberFormatNotation("u3.12",baS3_12Format,QNumberFormatNotation.BIG_ENDIAN);		
		System.out.println(qtd.toString());
		System.out.println("Double: " + qtd.toDouble());
		System.out.println("Float: " + qtd.toFloat());

		
		s16Bits3_12 = "14:00";
		System.out.println("+-------------------------------------------------------------------------------------+");
		baS3_12Format = QFormatFixedPointTest.ToByteArray(s16Bits3_12);
		qtd = new QNumberFormatNotation("s3.12",baS3_12Format,QNumberFormatNotation.BIG_ENDIAN);	
		System.out.println(qtd.toString());
		System.out.println("Double: " + qtd.toDouble());
		System.out.println("Float: " + qtd.toFloat());
		
		
		System.out.println("+-------------------------------------------------------------------------------------+");
		baS3_12Format = QFormatFixedPointTest.ToByteArray(s16Bits3_12);	
		qtd = new QNumberFormatNotation("u3.12",baS3_12Format,QNumberFormatNotation.BIG_ENDIAN);		
		System.out.println(qtd.toString());
		System.out.println("Double: " + qtd.toDouble());
		System.out.println("Float: " + qtd.toFloat());

	}
	
	/**
	 * 
	 * @param sHexString
	 * @return
	 */
	public static byte[] ToByteArray(String sHexString) {
		
		Boolean localDebug = Boolean.FALSE;
		
		if (localDebug)
			System.out.println("HexString.toByteArray(s) " + sHexString);
		
		//Remove HEX Notation
		sHexString = sHexString.replace("0x","");
		
		//Need to add HexString Check	
		sHexString = sHexString	.replaceAll("(\\]|\\)|\\}|\\(|\\[|\\{)", "")
								.replace(":", "")
								//mgarcia 141206 - Added new Replace Check
								.replace(".","");
		
		HexBinaryAdapter hba = new HexBinaryAdapter();	     
	     
		return hba.unmarshal(sHexString.replace(" ", ""));
	}

}
