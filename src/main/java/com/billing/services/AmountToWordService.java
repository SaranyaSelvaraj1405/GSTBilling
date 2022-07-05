package com.billing.services;

public class AmountToWordService {

	

	public static String convert(Double d){
		int num=(int) Math.round(d);
		String s="";
		String number = String.valueOf(num);
				
		
		String[] words= {"","One","Two","Three","Four","Five","Six","Seven","Eight","Nine"};
		
		String[] tens= {"","Ten","Twenty","Thirty","Fourty","Fifty","Sixty","Seventy","Eighty","Ninenty"};
		
		String[] elevens= {"","Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen",
				"Nineteen"};

		
		
		for(int i=number.length()-1;i>=0;i--) {
			
			//Ones
			if(i==number.length()-1) {
				boolean flag=true;
				if((i-1>=0)) 
				{
					if(number.charAt(i-1)=='1') {
						flag = false;
					}
				}
				if(flag)
					s=words[Character.getNumericValue(number.charAt(i))];
				
			}
			
			
			//Tens
			if(i==number.length()-2) {
				if(number.charAt(i+1)=='0') {
					s+=tens[Character.getNumericValue(number.charAt(i))];
				}else if(number.charAt(i)=='1'){
					s+=elevens[Character.getNumericValue(number.charAt(i+1))];
				}else {
					s=tens[Character.getNumericValue(number.charAt(i))]+" "+s;
				}
			}
			
			//Hundered
			if(i==number.length()-3) {
				if(number.charAt(i)!='0')
				s=words[Character.getNumericValue(number.charAt(i))]+" Hundered "+s;
			}
			
			//Thousands
			if(i==number.length()-4) {
				if(number.charAt(i)!='0') {
					boolean flag=true;
					if((i-1>=0)) 
					{
						if(number.charAt(i-1)=='1') {
							flag = false;
						}
					}
					if(flag)
						s=words[Character.getNumericValue(number.charAt(i))]+" Thousand "+s;
				}
			}
			
			//Ten Thousands
			if(i==number.length()-5) {
				if(number.charAt(i+1)=='0') {
					if(number.charAt(i)!='0')
						s=tens[Character.getNumericValue(number.charAt(i))]+" Thousand "+s;
				}else 
					if(number.charAt(i)=='1'){
					s=elevens[Character.getNumericValue(number.charAt(i+1))]+" Thousand "+s;
				}else {
					s=tens[Character.getNumericValue(number.charAt(i))]+" "+s;
				}
			}
			
			//Lakhs
			if(i==number.length()-6) {
				if(number.charAt(i)!='0') {
					boolean flag=true;
					if((i-1>=0)) 
					{
						if(number.charAt(i-1)=='1') {
							flag = false;
						}
					}
					if(flag)
						s=words[Character.getNumericValue(number.charAt(i))]+" Lakhs "+s;
				}
			}
			
			//TenLakhs
			if(i==number.length()-7) {
				if(number.charAt(i+1)=='0') {
					s=tens[Character.getNumericValue(number.charAt(i))]+" Lakhs "+s;
				}else 
					if(number.charAt(i)=='1'){
					s=elevens[Character.getNumericValue(number.charAt(i+1))]+" Lakhs "+s;
				}else {
					s=tens[Character.getNumericValue(number.charAt(i))]+" "+s;
				}
			}
			
			//Crores
			if(i==number.length()-8) {
				if(number.charAt(i)!='0') {
					boolean flag=true;
					if((i-1>=0)) 
					{
						if(number.charAt(i-1)=='1') {
							flag = false;
						}
					}
					if(flag)
						s=words[Character.getNumericValue(number.charAt(i))]+" Crore "+s;
				}
			}
		}
		
		
		return s+" Only";
	}
}