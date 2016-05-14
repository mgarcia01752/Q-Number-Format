package com.mgarcia.qnumberformat.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class QNumberFormatNotation {
	
	private byte[] baFixedPointData;
	private String sFixedPointNotation;
	@SuppressWarnings("unused")
	private int iEndianness = BIG_ENDIAN;;
	
	private String sSignUnsignFormat;
	private int iIntegerBits = 0;
	private int iFractionalBits = 0;
	
	private final int MAX_BIT_SIZE = 32;
		
	private double dQNumber;
	
	//Format: s3.12
 	private final Pattern FIXED_POINT_NOTATION = Pattern.compile(""
			+ "^(s|u)(\\d+)\\.(\\d+)", Pattern.CASE_INSENSITIVE);
	
	@SuppressWarnings("unused")
	private final String SIGNED = "s";
	@SuppressWarnings("unused")
	private final String UNSIGNED = "u";
	
	public static int BIG_ENDIAN = 1;
	public static int LITTLE_ENDIAN = 0;
	
	public boolean DEBUG = Boolean.FALSE;

	/**
	 * 
	 * @param sFixedPoint complex data using sm.n notation
	 * 	
	 * 	s|u = Signed or Unsigned
	 *  m = Integer bit(s)
	 *  n = Fractional bits
	 *  
	 * @param iEndianness BIG_ENDIAN =1 ,LITTLE_ENDIAN = 0 
	 * @param baFixedPointData
	 */
	public QNumberFormatNotation(String sFixedPointNotation, byte[] baFixedPointData, int iEndianness) {
		
		this.sFixedPointNotation = sFixedPointNotation;
		this.baFixedPointData = baFixedPointData;
		this.iEndianness = iEndianness;
		
		checkUpdateQNumberFormat();
		
		isByteArrayLengthCorrect();
		
		calculateFixPointToDouble();

	}
	
	/**
	 *  Big-Endian is Assumed
	 * 
	 * @param sFixedPointNotation
	 * @param baFixedPointData
	 */
	public QNumberFormatNotation(String sFixedPointNotation, byte[] baFixedPointData) {
		
		this.sFixedPointNotation = sFixedPointNotation;
		this.baFixedPointData = baFixedPointData;
		
		checkUpdateQNumberFormat();
		
		isByteArrayLengthCorrect();
		
		calculateFixPointToDouble();

	}
	
	/**
	 * 
	 * @return Double of the FixedPoint Data
	 */
	public double toDouble() {
		return this.dQNumber;
	}
	
	/**
	 * 
	 * @return float of the FixedPoint Data
	 */
	public float toFloat() {
		return (float)this.dQNumber;
	}

	/**
	 * Stats of Data
	 */
	public String toString() {
		
		return	"MaxInputSizeBits: " + MAX_BIT_SIZE + "\n" +
				"Fixed-Point-Format: " + sFixedPointNotation + "\n" +
				"Signed|Unsigned: " + sSignUnsignFormat + "\n" +
				"Integer-Fixed-Point-Bits: " + iIntegerBits + "\n" +
				"Fractional-Fixed-Point-Bits: " + iFractionalBits + "\n" +
				"Output: " + dQNumber;
		
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 * @throws java.lang.Exception
	 */
	public static Integer twosComp(String str) throws java.lang.Exception {
	    
		Integer iInt = null;
		
		//System.out.println("twosCompNibble(String str) : " + str);
		
		if(str.length() <= 4) {
			Short num = Short.parseShort(str, 2);
			iInt = (int) ((num > Math.pow(2, str.length()-1)) ? num - Math.pow(2, str.length()) : num);			
		} else if ((str.length() <= 16) && (str.length() > 4)) {
			Integer num = Integer.parseInt(str, 2);
			iInt = (int) ((num > Math.pow(2, str.length()-1)) ? num - Math.pow(2, str.length()) : num);	
		}
		
		return iInt;
	 }
		
	/**
	 * Verifies that byte array is of proper length against Q number Format
	 * @return True if correct, False is not correct
	 */
	private boolean isByteArrayLengthCorrect() {
		
		boolean boolCorrectLength = false;
		
		if (baFixedPointData.length > (iIntegerBits + iFractionalBits)) {
			boolCorrectLength = true;
		}
		
		return boolCorrectLength;
	}
	
	/**
	 * 
	 * @return True if Format is correct
	 */
	private boolean checkUpdateQNumberFormat() {
		
		boolean boolCorrectFormat = false;
		
		Matcher mFixedPointNotation = FIXED_POINT_NOTATION.matcher(sFixedPointNotation);
		
		if (mFixedPointNotation.find()) {
			
			sSignUnsignFormat = mFixedPointNotation.group(1);
			iIntegerBits = Integer.parseInt(mFixedPointNotation.group(2));
			iFractionalBits = Integer.parseInt(mFixedPointNotation.group(3));
			
			boolCorrectFormat = true;
		}
		
		return boolCorrectFormat;
	}
	
	/**
	 * 	Algorithm Q-Number to (Float|Double) 
	 *	1. Convert the number to floating point as if it were an integer, in other words remove the binary point
	 * 	2. Multiply by 2^−n
	 */
	private void calculateFixPointToDouble() {
		
		boolean boolDebug = (DEBUG|Boolean.FALSE);
		
		if (boolDebug) System.out.println("Fixed-Point-Notation: (" + sFixedPointNotation + ")");
		
		String sBin = "";
		
		for (byte b : baFixedPointData) {
		    sBin += Integer.toBinaryString(b & 255 | 256).substring(1);
		}
		
		StringBuilder sb = new StringBuilder(sBin);
		
		/*Incase that there are no fractional Bits */
		
		Double dFractionConvert = 0.0;
		
		if (this.iFractionalBits != 0) {
			
			CharSequence csFractionalBits = sb.subSequence((sb.length() - this.iFractionalBits),sb.length());
			//System.out.println("FractionalBits: " + csFractionalBits);
			
			int iFraction = Integer.parseInt((String) csFractionalBits, 2);
			dFractionConvert = iFraction/Math.pow(2, this.iFractionalBits);
			//System.out.println("Fraction: " + dFractionConvert);
		
		}
		
		
		CharSequence csInteger = sb.subSequence(0,iIntegerBits+1 );
		Integer iSINT = null;
		
		try {
			iSINT = twosComp((String)csInteger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("SignInteger: " + iSINT);
		
		this.dQNumber = Double.parseDouble(iSINT.toString()+dFractionConvert.toString().replaceFirst("^0", ""));
		
	}

}