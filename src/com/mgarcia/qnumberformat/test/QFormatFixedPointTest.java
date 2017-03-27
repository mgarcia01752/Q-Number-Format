package com.mgarcia.qnumberformat.test;

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
	
	Author: Maurice Garcia
	email:	maurice.garcia.2015@gmail.com
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

		QNumberFormatNotation qnfn = null;
		byte[] baData; 
		
		byte[] baReadMePostive = new byte[] {(byte)0x14, (byte)0x00};
		qnfn = new QNumberFormatNotation("s3.12", baReadMePostive);
		System.out.println("QNumberFormatNotation(s3.12, 1400): " + qnfn.toDouble());
		
		byte[] baReadMeNegative = new byte[] {(byte)0xF4, (byte)0x00};
		qnfn = new QNumberFormatNotation("s3.12", baReadMeNegative);
		System.out.println("QNumberFormatNotation(s3.12, f400): " + qnfn.toDouble());
		
		baData = new byte[] {(byte)0xFF, (byte)0xF0};
		
		qnfn = new QNumberFormatNotation("s3.12", baData);
		System.out.println("QNumberFormatNotation(s3.12, fff0): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s2.13", baData);
		System.out.println("QNumberFormatNotation(s2.13, fff0): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s1.14", baData);
		System.out.println("QNumberFormatNotation(s1.14, fff0): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s15.0", baData);
		System.out.println("QNumberFormatNotation(s15.0, fff0): " + qnfn.toDouble());
		
		baData = new byte[] {(byte)0x24, (byte)0x00};
		qnfn = new QNumberFormatNotation("s1.14", baData);
		System.out.println("QNumberFormatNotation(s1.14, 2400): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s2.13", baData);
		System.out.println("QNumberFormatNotation(s2.13, 2400): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s3.12", baData);
		System.out.println("QNumberFormatNotation(s3.12, 2400): " + qnfn.toDouble());
		
		baData = new byte[] {(byte)0xf8, (byte)0x00};
		qnfn = new QNumberFormatNotation("s1.14", baData);
		System.out.println("QNumberFormatNotation(s1.14, f800): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s2.13", baData);
		System.out.println("QNumberFormatNotation(s2.13, f800): " + qnfn.toDouble());
		
		qnfn = new QNumberFormatNotation("s3.12", baData);
		System.out.println("QNumberFormatNotation(s3.12, f800): " + qnfn.toDouble());

		baData = new byte[] {(byte)0xB8, (byte)0x00};
		qnfn = new QNumberFormatNotation("s2.13", baData);
		System.out.println("QNumberFormatNotation(s3.12, b800): " + qnfn.toDouble());
	
				
		
		/* 							Output
		 * 
					QNumberFormatNotation(s3.12, 1400): 1.25
					QNumberFormatNotation(s3.12, f400): -0.75
					QNumberFormatNotation(s3.12, fff0): -0.00390625
					QNumberFormatNotation(s2.13, fff0): -0.001953125
					QNumberFormatNotation(s1.14, fff0): -9.765625E-4
					QNumberFormatNotation(s15.0, fff0): -16.0
					QNumberFormatNotation(s1.14, 2400): 0.5625
					QNumberFormatNotation(s2.13, 2400): 1.125
					QNumberFormatNotation(s3.12, 2400): 2.25
					QNumberFormatNotation(s1.14, f800): -0.125
					QNumberFormatNotation(s2.13, f800): -0.25
					QNumberFormatNotation(s3.12, f800): -0.5
					QNumberFormatNotation(s3.12, B800): -2.25
			
		*/

	}

}
