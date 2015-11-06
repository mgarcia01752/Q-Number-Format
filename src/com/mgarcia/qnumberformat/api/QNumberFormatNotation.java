package com.mgarcia.qnumberformat.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
	
	private int iFixedPointInteger = 0;
	private double dFractionalValue = 0;
	private String sFixedPointConvertedOutput;
	
	//Format: s3.12
	private final Pattern FIXED_POINT_NOTATION = Pattern.compile(""
			+ "^(s|u)(\\d+)\\.(\\d+)", Pattern.CASE_INSENSITIVE);
	
	private final String SIGNED = "s";
	@SuppressWarnings("unused")
	private final String UNSIGNED = "u";
	
	public static int BIG_ENDIAN = 1;
	public static int LITTLE_ENDIAN = 0;
	
	public boolean DEBUG = Boolean.TRUE;

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
		return new Double(sFixedPointConvertedOutput);
	}
	
	/**
	 * 
	 * @return float of the FixedPoint Data
	 */
	public float toFloat() {
		return new Float(sFixedPointConvertedOutput);
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
				"Input: " + toHexString(this.baFixedPointData)+ " -> Output: " + this.sFixedPointConvertedOutput;
		
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
		
		/*Convert to 4 Byte Unsigned Integer*/
		int iFixedPointFraction = toUnsignedInt(baFixedPointData);
		if (boolDebug) System.out.println("Integer: " + iFixedPointFraction + " \t\t\tHex: " + toHexString(baFixedPointData));
		
		if (sSignUnsignFormat.equalsIgnoreCase(SIGNED)) {
			/*Convert to 4Byte Signed Integer */
			this.iFixedPointInteger = toSignedInt(baFixedPointData);
		} else {
			/*Convert to 4Byte UnSigned Integer */
			this.iFixedPointInteger = toUnsignedInt(baFixedPointData);				
		}
		if (boolDebug) System.out.println("Integer: " + iFixedPointInteger + " U/S-Bit: " + sSignUnsignFormat + " \tHex: " + toHexString(baFixedPointData));
		
		/*Bit Shift to determine Fractional Value */
		iFixedPointFraction = iFixedPointFraction << (MAX_BIT_SIZE - iFractionalBits);
		if (boolDebug) System.out.println("Shift << : " + iFixedPointFraction + " \t\tHex: " + toHexString(iFixedPointFraction) + " NumberToShift: " + (MAX_BIT_SIZE - iFractionalBits));
		
		/*Bit Shift to determine Integer */
		if (iFractionalBits > 0) {
			this.iFixedPointInteger = this.iFixedPointInteger >> iFractionalBits;
		
			if (boolDebug) System.out.println("Shift >> : " + iFixedPointInteger + " \t\tHex: " + toHexString(iFixedPointInteger) + " NumberToShift: " + iFractionalBits);
	
			/*Figure out Base2 Fractional Value 2^-(32)*/
			this.dFractionalValue = iFixedPointFraction * Math.pow(2,(-MAX_BIT_SIZE));
			if (boolDebug) System.out.println("Fraction: " + dFractionalValue + " \t\t\tHex: " + toHexString(dFractionalValue));
		}
		
		/*Final String Output - Total Hack*/
		String sDecimal = ""+dFractionalValue;
		this.sFixedPointConvertedOutput = iFixedPointInteger + sDecimal.replaceAll("0\\.",".");
		if (boolDebug) System.out.println("Input: " + toHexString(this.baFixedPointData) + " -> Output: " + this.sFixedPointConvertedOutput);		
	}
	
	/**
	 * 
	 * @param baUnsigedInt
	 * @return
	 */
	private static int toUnsignedInt(byte[] baUnsigedInt) {
		ByteArrayOutputStream  baosByteToInt = new ByteArrayOutputStream();
		
		while ((baosByteToInt.size()+baUnsigedInt.length) < 4) {
			baosByteToInt.write(0x00);
		}

		try {
			baosByteToInt.write(baUnsigedInt);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		byte[] bByteToInt = baosByteToInt.toByteArray();
		
		return 	bByteToInt[0] << 24 | 
				(bByteToInt[1] & 0xFF) << 16 | 
				(bByteToInt[2] & 0xFF) << 8 | 
				(bByteToInt[3] & 0xFF);	
	}
	
	/**
	 * 
	 * @param baUnsignedInt
	 * @return
	 */
	private static int toSignedInt(byte[] baSignedInt) {
		return new BigInteger(baSignedInt).intValue();
	}
	
	/**
	 * 
	 * @param ba
	 * @return
	 */
	private static String toHexString(Integer iValue) {
		if (isModulus(Integer.toHexString(iValue).toUpperCase())) {
			return Integer.toHexString(iValue).toUpperCase();
		} else {
			return "0" + Integer.toHexString(iValue).toUpperCase();	
		}
	}
	
	/**
	 * 
	 * @param dValue
	 * @return
	 */
	private static String toHexString(Double dValue) {
		if (isModulus(Double.toHexString(dValue).toUpperCase())) {
			return Double.toHexString(dValue).toUpperCase();
		} else {
			return "0" + Double.toHexString(dValue).toUpperCase();	
		}
	}
	
	/**
	 * 
	 * @param ba
	 * @return
	 */
	private static String toHexString(byte[] ba) {
		
		StringBuilder sb = new StringBuilder();
		
		for (byte bByte : ba) {
			sb.append(String.format("%02X ", bByte));
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param sHexString
	 * @return
	 */
	private static boolean isModulus(String sHexString) {

		//Some Clean up in case it is separated by spaces and colons
		String regex = "//s+|/:";
		String replacement = "";

		sHexString.replaceAll(regex, replacement);

		//Check modulus for even pair
		if ((sHexString.length() % 2) == 0) {
			return true;
		} else  {
			return false;
		}
	}
}
