# Q-Number-Format
Q is a fixed point number format where the number of fractional bits is specified

[Q-Number-Format](https://en.wikipedia.org/wiki/Q_(number_format))


      s   3           12
      [0][001].[0100.0000.0000] = 0x1400
      Float 1.25 - s3.12 Format -> 0x1400 -> 16bits 
      QNumberFormatNotation(s3.12, 1400): 1.25
      
      s   3           12
      [1][111].[0100.0000.0000] = 0x1400 -> -1.25
      Float 1.25 - s3.12 Format -> 0xe400 -> 16bits
      QNumberFormatNotation(s3.12, f400): -1.25
      
[More Examples](https://github.com/mgarcia01752/Q-Number-Format/blob/master/src/com/mgarcia/qnumberformat/test/QFormatFixedPointTest.java)
		  
		  

