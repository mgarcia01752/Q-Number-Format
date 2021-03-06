# Q-Number-Format
Q is a fixed point number format where the number of fractional bits is specified

[Q-Number-Format](https://en.wikipedia.org/wiki/Q_(number_format))


      s   3           12
      [0][010].[0100.0000.0000] = 0x2400
      Float 1.125 - s3.12 Format -> 0x2400 -> 16bits 
      QNumberFormatNotation(s3.12, 2400): 1.125
      
      s   3           12
      [1][111].[1000.0000.0000] = 0xf800 -> -0.25
      Float -0.25 - s3.12 Format -> 0xf800 -> 16bits
      QNumberFormatNotation(s3.12, f800): -0.25
      
[More Examples](https://github.com/mgarcia01752/Q-Number-Format/blob/master/src/com/mgarcia/qnumberformat/test/QFormatFixedPointTest.java)
		  
[Text Book Citation](https://books.google.com/books?id=ylOMDwAAQBAJ&pg=PA140&lpg=PA140&dq=mgarcia01752&source=bl&ots=MZecy3a5KF&sig=ACfU3U1oll4742LqztFhhTF2pNNimc2Rvw&hl=en&sa=X&ved=2ahUKEwib8PzakqntAhWw2FkKHU1WAYY4ChDoATAEegQIBxAC#v=onepage&q=mgarcia01752&f=false)		  

